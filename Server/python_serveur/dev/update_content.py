from write_data import *
#from insert_in_db import insert_all
import platform
import os

if platform.system() == "Windows":
    path_manga = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\manga.csv"
    path_chapter = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\chapters.csv"
    path_page = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\pages.csv"
    input_file_path = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\input_file.txt"

if platform.system() == "Darwin":
    path_manga = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout/manga.csv"
    path_chapter = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout/chapters.csv"
    path_page = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout/pages.csv"
    input_file_path = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/input_file.txt"

if platform.system() == "Linux":
    path_manga = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout/manga.csv"
    path_chapter = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout/chapters.csv"
    path_page = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout/pages.csv"
    input_file_path = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/input_file.txt"

# for rewriting manga in case of new links
if os.path.exists(path_manga):
     os.remove(path_manga)


#read the input file
file = open(input_file_path,'r').readlines()
input_file_as_list_of_dict = []

#add lines as dctionnaries in input_file_as_list_of_dict
for line in file :
    if "---NEW---" in line :
        manga = ""
        cover = ""
        list_of_link = []
    if "--MANGA--" in line :
        manga = line[10:-1]
    if "--COVER--" in line :
        cover = line[10:-1]
    if "--LINK--" in line:
        list_of_link.append(line[10:-1])
    if "--FIN NEW--" in line:
        input_file_as_list_of_dict.append({"manga_name": manga,
                                           "cover_link": cover,
                                           "list_of_link": list_of_link})

# rewritting manga in case of a new link
id_book = 0
list_of_dic_to_rewrite = []
chapter_for_page = []
list_of_dic_to_rewrite_for_page = []


for item in input_file_as_list_of_dict:
    print(item)
    write_manga(item, id_book)
    list_of_link = item['list_of_link']
    list_of_dic_to_rewrite_for_page.append({"id_book":id_book , "list_of_link":list_of_link})

    with open(path_chapter) as chapter_file :
        csv_reader = csv.reader(chapter_file, delimiter=',')
        for line in csv_reader:
            id = int(line[0])
            chapters = line[1][1:-1].split(',')
            our_last_chapter = int(chapters[-1])
            last_chapter = int(chapters[-1])

            if id_book == id :
                l_chapter = []
                last_chapter = update_last_chapter(last_chapter, list_of_link)
                print("last_chapter = " + str(last_chapter))
                for it in range(1, last_chapter+1):
                    l_chapter.append(it)
                    if it > our_last_chapter:
                        chapter_for_page.append(it)

                list_of_dic_to_rewrite.append({"id_book" : id_book, "liste_chapitre" : l_chapter})

    id_book = id_book + 1

if os.path.exists(path_chapter):
     os.remove(path_chapter)

# rewrite chapters
with open(path_chapter,'w') as chapter_file:
    chapter_csv_fieldnames = ['id_book', 'liste_chapitre']
    chapter_csv_writer = csv.DictWriter(chapter_file, fieldnames=chapter_csv_fieldnames, lineterminator='\n', quoting=csv.QUOTE_ALL)

    for item in list_of_dic_to_rewrite :
        chapter_csv_writer.writerow(item)
chapter_file.close()


# rewrite page with new page
page_file_content = []

id_book = 0

if len(chapter_for_page)>0 :
    with open(path_page,'r') as page_file:
        csv_reader = csv.reader(page_file, delimiter=',')
        for new_chapitre in chapter_for_page :
            for line in csv_reader:
                id_book_file = int(line[0])
                chapitre = int(line[1])
                links = line[2][1:-1].replace("'", '').split(',')

                if id_book == id_book_file and chapitre<new_chapitre:
                    page_file_content.append({'id_book': id_book, 'chapitre':  chapitre, 'list_page': links})

            list_base_link = []
            for item in list_of_dic_to_rewrite_for_page:
                if item['id_book'] == id_book :
                    list_base_link = item['list_of_link']
            page_file_content.append({'id_book': id_book, 'chapitre': new_chapitre, 'list_page': get_page(new_chapitre, list_base_link)})
        id_book = id_book + 1


with open(path_page , 'w') as page_file :
    page_csv_fieldnames = ['id_book', 'chapitre', 'list_page']
    page_csv_writer = csv.DictWriter(page_file, fieldnames=page_csv_fieldnames, lineterminator='\n', quoting=csv.QUOTE_ALL)

    for item in page_file_content:
        page_csv_writer.writerow(item)



#insert_all()


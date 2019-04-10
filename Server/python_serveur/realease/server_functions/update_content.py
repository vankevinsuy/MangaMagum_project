from server_functions.write_data import *
from server_functions.insert_in_db import insert_all
import platform
import os

print("########### UPDATE CONTENT ##########")

if platform.system() == "Windows":
    path_manga = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\outpout\\manga.csv"
    path_chapter = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\outpout\\chapters.csv"
    path_page = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\outpout\\pages.csv"
    input_file_path = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\input_file.txt"

if platform.system() == "Darwin":
    path_manga = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv"
    path_chapter = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv"
    path_page = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv"
    input_file_path = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/input_file.txt"

if platform.system() == "Linux":
    path_manga = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv"
    path_chapter = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv"
    path_page = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv"
    input_file_path = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/input_file.txt"

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
        book_description =""
    if "--MANGA--" in line :
        manga = line[10:-1]
    if "--COVER--" in line :
        cover = line[10:-1]
    if "--LINK--" in line:
        list_of_link.append(line[10:-1])
    if "--FIN NEW--" in line:
        input_file_as_list_of_dict.append({"manga_name": manga,
                                           "cover_link": cover,
                                           "list_of_link": list_of_link,
                                           "description": book_description})

# rewritting manga in case of a new link
id_book = 0
list_of_dic_to_rewrite = []
chapter_for_page = []
list_of_dic_to_rewrite_for_page = []


for item in input_file_as_list_of_dict:
    print(item)
    write_manga(item, id_book)
    list_of_link = item['list_of_link']

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

                        list_of_dic_to_rewrite_for_page.append({"id_book":id_book ,
                                                                "list_of_link":list_of_link,
                                                                'new_chapter' : chapter_for_page})
                        chapter_for_page = []
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

print("")


# write new pages
with open(path_page, 'a') as csv_page :
    page_csv_fieldnames = ['id_book', 'chapitre', 'list_page']
    page_csv_writer = csv.DictWriter(csv_page, fieldnames=page_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)

    if len(list_of_dic_to_rewrite_for_page) >0:
        for item in list_of_dic_to_rewrite_for_page :
            print(item)
            id_book = item['id_book']
            list_of_link = item['list_of_link']
            new_chapter = item['new_chapter']

            for chapitre in new_chapter:
                pages = get_page(chapitre, list_of_link)
                page_csv_writer.writerow({'id_book': id_book , 'chapitre':  chapitre , 'list_page': pages })

            print(" ")

    else:
        print("everything is up to date")
        print(" ")





insert_all()

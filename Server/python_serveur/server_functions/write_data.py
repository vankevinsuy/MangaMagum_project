import platform
os = platform.system()
import csv
import numpy as np
from server_functions.URL_Checker import *
from server_functions.insert_in_db import insert_all
import time
import gc

path_manga = "outpout/manga.csv"
path_chapter = "outpout/chapters.csv"
path_page = "outpout/pages.csv"
input_file_path = "input_file.txt"


#function for manga
def write_manga(dictionnary,id_book):
    with open(path_manga, "a") as manga_csv:
        manga_name = dictionnary["manga_name"]
        cover_link = dictionnary["cover_link"]
        description = dictionnary["description"]

        # manga_csv.write('\n')
        #writing manga.csv manga's name, cover_link ,id_book
        manga_csv_fieldnames = ['manga_name', 'cover_link', 'id_book','description']
        manga_csv_writer = csv.DictWriter(manga_csv, fieldnames=manga_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)

        manga_csv_writer.writerow({"manga_name": manga_name, "cover_link": cover_link, "id_book": id_book, "description": description})
    manga_csv.close()


#functions for chapter
def write_chapter(dictionnary, id_book, from_chap=0):
    list_of__base_link = dictionnary["list_of_link"]

    if(from_chap == 0):
        chapitre_max = max_chapter_dicoto(list_of__base_link, 1, 1000, 1000)
        print(dictionnary["manga_name"] + " chapitre max = " + str(chapitre_max))
        print('')
    else:
        chapitre_max = max_chapter_dicoto(list_of__base_link, from_chap, 1000, 1000)
        print(dictionnary["manga_name"] + " chapitre max = " + str(chapitre_max))
        print('')

    list_chapitre = []

    for it in range(from_chap,chapitre_max+1):
        list_chapitre.append(it)

    with open(path_chapter,'a') as chapter_csv:
        chapter_csv_fieldnames = ['id_book', 'liste_chapitre']
        chapter_csv_writer = csv.DictWriter(chapter_csv, fieldnames=chapter_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)
        chapter_csv_writer.writerow({'id_book': id_book , 'liste_chapitre':  list_chapitre })
    chapter_csv.close()

def max_chapter_dicoto(list_link, min_chapitre, max_chapitre, init_max):
    chapitre_existe = False

    for link in list_link:
        url1 = link.format(str(max_chapitre), "1")
        url2 = link.format(str(max_chapitre), "01")

        if check_url(url1) or check_url(url2):
            chapitre_existe = True

        print("min : " + str(min_chapitre) + "  max : " + str(max_chapitre) + "    " + url1)

    if min_chapitre == max_chapitre:
        return max_chapitre

    else:

        if not chapitre_existe : #si on est faux on divise par 2
            val_moyenne = int(round(np.mean(np.arange(min_chapitre,max_chapitre,1))))
            if val_moyenne >= min_chapitre : #si on ne depasse pas l'intervalle on divise
                return max_chapter_dicoto(list_link, min_chapitre, val_moyenne, init_max)


        else: # si on est vrai on avance
            min_chapitre = max_chapitre
            max_chapitre = init_max
            val_moyenne = int(round(np.mean(np.arange(min_chapitre,max_chapitre,1))))
            return max_chapter_dicoto(list_link, min_chapitre, val_moyenne, init_max)

def update_last_chapter(last_chapter,list_of_link):
    new_chap = []
    for chap in range(last_chapter+1, last_chapter+5):
        for base in list_of_link:

            url1 = base.format(str(chap), "1")
            url2 = base.format(str(chap), "01")

            if check_url(url1) or check_url(url2):
                new_chap.append(chap)

    if len(new_chap) != 0 :
        return new_chap[-1]
    else:
        return last_chapter

#functions for page
def write_page(dictionnary, id_book):
    with open(path_chapter, 'r') as csv_file:
        with open(path_page, 'a') as page_csv:
            csv_reader = csv.reader(csv_file)
            page_csv_fieldnames = ['id_book', 'chapitre', 'list_page']
            page_csv_writer = csv.DictWriter(page_csv, fieldnames=page_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)

            list_chapitre = None
            list_of__base_link = dictionnary["list_of_link"]

            page_csv.write('\n')
            for line in csv_reader:
                if int(line[0]) == id_book:
                    list_chapitre = line[1][1:-1].split(',')
                    for i in range(len(list_chapitre)):
                        if '[' in list_chapitre[i]:
                            list_chapitre[i] = list_chapitre[i][1:]
                        if ']' in list_chapitre[i] :
                            list_chapitre[i] = list_chapitre[i][:-1]
                        else:
                            list_chapitre[i] = int(list_chapitre[i])
                else:
                    pass


            for chapitre in list_chapitre:
                urls_to_test = []
                for i in range(len(list_of__base_link)):
                    urls_to_test.append(list_of__base_link[i].format(str(chapitre), str(1)))
                    urls_to_test.append(list_of__base_link[i].format(str(chapitre), "01"))

                for i in range(len(urls_to_test)) :
                    if check_url(urls_to_test[i]):
                        pages = get_page(chapitre, list_of__base_link)
                        with open(path_page, 'a') as page_csv2:
                            page_csv_writer.writerow({'id_book': id_book , 'chapitre':  chapitre , 'list_page': pages })
                            pages = []
                        page_csv2.close()

def get_page(chapitre, list_of__base_link):
# loop for finding all pages by chapter
    num_page = 1
    list_page = []
    while (True):
        #mettre en forme les urls
        url_on_test = []
        for i in range(len(list_of__base_link)):
            if num_page < 10 :
                url = list_of__base_link[i].format(str(chapitre), "0"+str(num_page))
                url_on_test.append({'url':url, 'base url': list_of__base_link[i]})

                url = list_of__base_link[i].format(str(chapitre), str(num_page))
                url_on_test.append({'url':url, 'base url': list_of__base_link[i]})
            else:
                url = list_of__base_link[i].format(str(chapitre), str(num_page))
                url_on_test.append({'url':url, 'base url': list_of__base_link[i]})

        #verifier leurs états
        url_res = []
        for i in range(len(url_on_test)):
            if check_url(url_on_test[i]['url']):
                url_res.append(True)
                break
            else:
                url_res.append(False)

        #récupérer la bonne url et l'ajouter à la liste
        for i in range(len(url_res)):
            if url_res[i] == True:
                good_url = url_on_test[i]['url']
                list_page.append(good_url)
#---------------------------------------------------
        #vérifier si la prochaine page existe
        url_on_test = []
        for i in range(len(list_of__base_link)):
            if num_page <10:
                url = list_of__base_link[i].format(str(chapitre), "0"+str(num_page + 1))
                url_on_test.append(url)
                url = list_of__base_link[i].format(str(chapitre), str(num_page+1))
                url_on_test.append(url)
            else:
                url = list_of__base_link[i].format(str(chapitre), str(num_page+1))
                url_on_test.append(url)


        #verifier les états
        url_res = []
        for i in range(len(url_on_test)):
            if check_url(url_on_test[i]):
                url_res.append(True)
            else:
                url_res.append(False)

        #si il y a un true dans url_res on continue sinon break
        if True in url_res:
            num_page = num_page +1
        else:
            return list_page

def get_last_id():
    with open(path_manga) as manga_file :
        csv_reader = csv.reader(manga_file)

        last_id = 0
        for line in csv_reader :
            last_id = int(line[-2])
    return last_id

def add_new_manga() :
    path_new_manga = "new_manga.txt"
    # read the file which have the new mangas to add
    file = open(path_new_manga, 'r').readlines()
    input_file_as_list_of_dict = []

    # add lines as dctionnaries in input_file_as_list_of_dict
    for line in file:
        if "---NEW---" in line:
            manga = ""
            cover = ""
            list_of_link = []
            book_description =""
            from_chap = 0
        if "--MANGA--" in line:
            manga = line[10:-1]
        if "--COVER--" in line:
            cover = line[10:-1]
        if "--FROM--" in line:
            from_chap = int(line[10:-1])
        if "--LINK--" in line:
            list_of_link.append(line[10:-1])
        if "--FIN NEW--" in line:
            input_file_as_list_of_dict.append({"manga_name":   manga,
                                               "cover_link":   cover,
                                               "list_of_link": list_of_link,
                                               "description": book_description})
            with open(input_file_path, 'a') as input_file:
                for item in input_file_as_list_of_dict:
                    input_file.write("\n")

                    manga = item['manga_name']
                    cover = item['cover_link']
                    link = item['list_of_link']
                    desc = item['description']

                    # adding data in input file
                    input_file.write("\n---NEW---\n")
                    input_file.write("--MANGA--:" + manga + "\n")
                    input_file.write("--COVER--:" + cover + "\n")
                    input_file.write("--DESC-- :" + desc + "\n")
                    for i in range(len(link)):
                        input_file.write("--LINK-- :" + link[i] + "\n")
                    input_file.write("\n--FIN NEW--")

                    id_book = get_last_id() + 1
                    write_manga(item, id_book)
                    write_chapter(item, id_book, from_chap)
                    write_page(item, id_book)
                    gc.collect()

    # with open(input_file_path , 'a') as input_file :
    #     for item in input_file_as_list_of_dict :
    #         input_file.write("\n")
    #
    #         manga = item['manga_name']
    #         cover = item['cover_link']
    #         link = item['list_of_link']
    #         desc = item['description']
    #
    #         # adding data in input file
    #         input_file.write("\n---NEW---\n")
    #         input_file.write("--MANGA--:" + manga +"\n")
    #         input_file.write("--COVER--:" + cover +"\n")
    #         input_file.write("--DESC-- :" + desc +"\n")
    #         for i in range(len(link)):
    #             input_file.write("--LINK-- :" + link[i] +"\n")
    #         input_file.write("\n--FIN NEW--")
    #
    #         id_book = get_last_id()+1
    #         write_manga(item, id_book)
    #         write_chapter(item, id_book, from_chap)
    #         write_page(item, id_book)
    #         gc.collect()

    # effacer le contenu du fichier
    new_manga = open(path_new_manga,'w')
    new_manga.close()
    insert_all()

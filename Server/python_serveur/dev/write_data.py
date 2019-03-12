import csv
import numpy as np
from URL_Checker import *
import platform
os = platform.system()

path_manga = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\windows\\outpout\\manga.csv"
path_chapter = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\windows\\outpout\\chapters.csv"
path_page = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\windows\\outpout\\pages.csv"

#function for manga
def write_manga(dictionnary,id_book):
    with open(path_manga,'a') as manga_csv:
        manga_name = dictionnary["manga_name"]
        cover_link = dictionnary["cover_link"]

        #writing manga.csv manga's name, cover_link ,id_book
        manga_csv_fieldnames = ['manga_name', 'cover_link', 'id_book']
        manga_csv_writer = csv.DictWriter(manga_csv, fieldnames=manga_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)

        manga_csv_writer.writerow({"manga_name": manga_name, "cover_link": cover_link, "id_book": id_book})
        manga_csv.close()




#function for chapter
def write_chapter(dictionnary, id_book):
    #list_of__base_link = link_of_list_converter(dictionnary["list_of_link"])
    list_of__base_link = dictionnary["list_of_link"]
    #chapter_recursif(id_book, list_of__base_link, 1, 0, [])
    chapitre_max = max_chapter_dicoto(list_of__base_link,1,10000,10000)
    print(dictionnary["manga_name"]+" chapitre max = " + str(chapitre_max))
    print('')

# check each chapter with the first page
    list_chapitre = []
    for chapitre in range(1, chapitre_max+1):
        for link_base in list_of__base_link :
            url1 = link_base.format(str(chapitre), "1")
            url2 = link_base.format(str(chapitre), "01")
            if check_url(url1) or check_url(url2):
                list_chapitre.append(chapitre)

    with open(path_chapter,'a') as chapter_csv:
        chapter_csv_fieldnames = ['id_book', 'liste_chapitre']
        chapter_csv_writer = csv.DictWriter(chapter_csv, fieldnames=chapter_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)
        chapter_csv_writer.writerow({'id_book': id_book , 'liste_chapitre':  list_chapitre })
    chapter_csv.close()

    return None

def link_of_list_converter(links):
    links = links[1:-1]

    if ',' not in links:
        return [links]
    else:
        links = links.split(',')
        return links

def max_chapter_dicoto(list_link, min_chapitre, max_chapitre, init_max):
    chapitre_existe = False

    for link in list_link:
        url1 = link.format(str(max_chapitre), "1")
        url2 = link.format(str(max_chapitre), "01")

        if check_url(url1):
            chapitre_existe = True

        if check_url(url2):
            chapitre_existe = True

        print("min : " + str(min_chapitre) + "  max : " + str(max_chapitre))

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
            # print("nouvel interval :  [" + str(min_chapitre) + " , " + str(max_chapitre) + ']')
            return max_chapter_dicoto(list_link, min_chapitre, val_moyenne, init_max)

def chapter_recursif(id_book, urls_base,chapitre, indice_liste, list_chapitre):
    with open(path_chapter,'a') as chapter_csv:
        chapter_csv_fieldnames = ['id_book', 'liste_chapitre']
        chapter_csv_writer = csv.DictWriter(chapter_csv, fieldnames=chapter_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)

        if indice_liste > len(urls_base)-1:
            indice_liste = 0


        if check_url(urls_base[indice_liste].format(str(chapitre), str(1))) or check_url(urls_base[indice_liste].format(str(chapitre), "01")):
            if check_url(urls_base[indice_liste].format(str(chapitre), str(1))):
                print(urls_base[indice_liste].format(str(chapitre), str(1)))
            else:
                print(urls_base[indice_liste].format(str(chapitre), "01"))

            future = []
            for i in range(len(urls_base)):
                if check_last_chapter(urls_base[i].format(str(chapitre+1), str(1))) or check_last_chapter(urls_base[i].format(str(chapitre+1), "01")):
                    future.append(True)
                    list_chapitre.append(chapitre)
                else:
                    future.append(False)
            if True in future:
                return chapter_recursif(id_book,urls_base, chapitre+1, indice_liste, list_chapitre)
            else:
                list_chapitre.append(chapitre)
                chapter_csv_writer.writerow({'id_book': id_book , 'liste_chapitre':  list_chapitre })
                chapter_csv.close()
                return None

        else:
            return chapter_recursif(id_book,urls_base, chapitre, indice_liste+1,list_chapitre)  #forget this function




#function for page
def write_page(dictionnary, id_book):
    with open(path_chapter, 'r') as csv_file:
        with open(path_page, 'a') as page_csv:
            csv_reader = csv.reader(csv_file)
            page_csv_fieldnames = ['id_book', 'chapitre', 'list_page']
            page_csv_writer = csv.DictWriter(page_csv, fieldnames=page_csv_fieldnames, lineterminator='\n',quoting=csv.QUOTE_ALL)

            list_chapitre = None
            list_of__base_link = link_of_list_converter(dictionnary["list_of_link"])

            for line in csv_reader :
                if int(line[0]) == id_book:
                    list_chapitre = line[1][1:-1].split(',')
                    for i in range(len(list_chapitre)):
                        if '[' in list_chapitre[i] :
                            list_chapitre[i] = list_chapitre[i][1:]
                        if ']' in list_chapitre[i] :
                            list_chapitre[i] = list_chapitre[i][:-1]
                        else:
                            list_chapitre[i] = int(list_chapitre[i])
                else:
                    pass


            for chapitre in list_chapitre:
                urls_to_test = []
                good_url_base = None
                for i in range(len(list_of__base_link)):
                    urls_to_test.append(list_of__base_link[i].format(str(chapitre), str(1)))
                    urls_to_test.append(list_of__base_link[i].format(str(chapitre), "01"))

                for i in range(len(urls_to_test)) :
                    if check_url(urls_to_test[i]):
                        # good_url_base = list_of__base_link[i]
                        pages = get_page(chapitre, list_of__base_link)
                        # to_write = []
                        # for j in range(len(pages)):
                        #     to_write.append(good_url_base.format(str(chapitre), str(pages[j])))
                        page_csv_writer.writerow({'id_book': id_book , 'chapitre':  chapitre , 'list_page': pages })

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
                # good_url = list_of__base_link[i].format(str(chapitre), str(num_page))
                good_url = url_on_test[i]['url']
                print(good_url)
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

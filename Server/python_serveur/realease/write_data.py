import csv
from URL_Checker import *

def write_manga(dictionnary,id_book):
    with open("/home/van-kevin/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv",'a') as manga_csv:
        manga_name = dictionnary["manga_name"]
        cover_link = dictionnary["cover_link"]

        #writing manga.csv manga's name, cover_link ,id_book
        manga_csv_fieldnames = ['manga_name', 'cover_link', 'id_book']
        manga_csv_writer = csv.DictWriter(manga_csv, fieldnames=manga_csv_fieldnames, lineterminator='\n')

        manga_csv_writer.writerow({"manga_name": manga_name, "cover_link": cover_link, "id_book": id_book})
        manga_csv.close()




def write_chapter(dictionnary, id_book):
    list_of__base_link = link_of_list_converter(dictionnary["list_of_link"])

    chapter_recursif(id_book, list_of__base_link, 1, 0, [])
    return None


def link_of_list_converter(links):
    links = links[1:-1]

    if ',' not in links:
        return [links]
    else:
        links = links.split(',')
        return links

def chapter_recursif(id_book, urls_base,chapitre, indice_liste, list_chapitre):
    with open("/home/van-kevin/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv",'a') as chapter_csv:
        chapter_csv_fieldnames = ['id_book', 'liste_chapitre']
        chapter_csv_writer = csv.DictWriter(chapter_csv, fieldnames=chapter_csv_fieldnames, lineterminator='\n')

        if indice_liste > len(urls_base)-1:
            indice_liste = 0

        if check_url(urls_base[indice_liste].format(str(chapitre), str(1))):
            print(urls_base[indice_liste].format(str(chapitre), str(1)))

            future = []
            for i in range(len(urls_base)):
                if check_last_chapter(urls_base[i].format(str(chapitre+1), str(1))):
                    future.append(True)
                    list_chapitre.append(chapitre)
                else:
                    future.append(False)
            if True in future:
                return chapter_recursif(id_book,urls_base, chapitre+1, indice_liste, list_chapitre)
            else:
                list_chapitre.append(chapitre)
                chapter_csv_writer.writerow({'id_book': id_book, 'liste_chapitre': list_chapitre})
                return None

        else:
            return chapter_recursif(id_book,urls_base, chapitre, indice_liste+1,list_chapitre)


def write_page(dictionnary, id_book):
    with open('/home/van-kevin/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv', 'r') as csv_file:
        with open('/home/van-kevin/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv', 'a') as page_csv:
            csv_reader = csv.reader(csv_file)
            page_csv_fieldnames = ['id_book', 'chapitre', 'list_page']
            page_csv_writer = csv.DictWriter(page_csv, fieldnames=page_csv_fieldnames, lineterminator='\n')

            list_chapitre = None
            list_of__base_link = link_of_list_converter(dictionnary["list_of_link"])

            for line in csv_reader :
                if int(line[0]) == id_book:
                    list_chapitre = line[1][1:-1].split(',')
                    for i in range(len(list_chapitre)):
                        list_chapitre[i] = int(list_chapitre[i])
                else:
                    pass


            for chapitre in list_chapitre:
                urls_to_test = []
                good_url_base = None
                for i in range(len(list_of__base_link)):
                    urls_to_test.append(list_of__base_link[i].format(str(chapitre), str(1)))

                for i in range(len(urls_to_test)) :
                    if check_url(urls_to_test[i]):
                        good_url_base = list_of__base_link[i]
                        pages = get_page(chapitre, good_url_base)
                        page_csv_writer.writerow({'id_book': id_book, 'chapitre': chapitre, 'list_page': pages})




def get_page(chapitre,good_url_base):
# loop for finding all pages by chapter
    num_page = 1
    list_page = []
    while (True):
        url = good_url_base.format(str(chapitre), str(num_page))

        if check_url(url):
            print(url)
            list_page.append(num_page)
            num_page = num_page + 1
        else:
            break
    return list_page
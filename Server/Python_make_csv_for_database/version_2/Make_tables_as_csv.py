import csv
from version_2.URL_Checker import *


#using the list of links we will ping all urls we can find by changing chapter and page number
def make_tables(dictionnary, id_book):
    with open("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\manga.csv",'a') as manga_csv:
        with open("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\chapters.csv", 'a') as chapter_csv :
            with open("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\pages.csv", 'a') as page_csv:
                manga_name = dictionnary["manga_name"]
                cover_link = dictionnary["cover_link"]
                list_of__base_link = link_of_list_converter(dictionnary["list_of_link"])

                #writing manga.csv manga's name, cover_link ,id_book
                manga_csv_fieldnames = ['manga_name', 'cover_link', 'id_book']
                manga_csv_writer = csv.DictWriter(manga_csv, fieldnames=manga_csv_fieldnames, lineterminator='\n')
                manga_csv_writer.writerow({"manga_name": manga_name, "cover_link": cover_link, "id_book": id_book})

                #writing chapter.csv  id_book, liste_chapitre && writing pages.csv  id_book, num_chapitre, list_page
                chapter_csv_fieldnames = ['id_book', 'liste_chapitre']
                chapter_csv_writer = csv.DictWriter(chapter_csv, fieldnames=chapter_csv_fieldnames, lineterminator='\n')
                page_csv_fieldnames = ['id_book', 'num_chapitre','liste_page']
                page_csv_writer = csv.DictWriter(page_csv, fieldnames=page_csv_fieldnames, lineterminator='\n')

                # loop for finding all available chapters
                chapitre_num = 1
                list_chapitre = []
                list_page = None
                list_link_to_test = link_of_list_converter(dictionnary["list_of_link"])

                while(True):
                    for i in range(len(list_link_to_test)):
                        list_link_to_test[i] = list_link_to_test[i].format(str(chapitre_num), str(1))

                    # if the first page of chapter exist,find all page or if chapter doesn't exist break
                    res = check_urls(list_link_to_test, list_of__base_link)
                    state = res[0]
                    good_url_base = res[1]

                    if state:
                        list_chapitre.append(chapitre_num)
                        list_page = get_page(chapitre_num,good_url_base)
                        chapter_csv_writer.writerow({'id_book':id_book,'liste_chapitre': list_chapitre})
                        page_csv_writer.writerow({'id_book':id_book, 'num_chapitre': chapitre_num, 'liste_page': list_page})
                        chapitre_num = chapitre_num + 1

                    else:
                        break





def link_of_list_converter(links):
    links = links[1:-1]

    if ',' not in links:
        return [links]
    else:
        links = links.split(',')
        return links



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

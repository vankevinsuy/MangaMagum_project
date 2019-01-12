import csv
try :
    from python_code.Chapter_list_maker import *
    from python_code.Url_page_checker import get_list_of_url_for_page
except:
    from  Chapter_list_maker import *
    from Url_page_checker import get_list_of_url_for_page


input_file = open("D:\MangaMagum\Python_make_csv_for_database\input_file.txt").readlines()

# ecriture de manga.csv
with open("D:/MangaMagum/Python_make_csv_for_database/out/manga.csv", 'w') as manga_csv:
    fieldnames = ['manga_name', 'cover_link', 'id_book']
    csv_writer = csv.DictWriter(manga_csv, fieldnames=fieldnames, lineterminator='\n')
    i = -1

    for line in input_file :
        i = i +1
        element = line.split(',')
        element[1] = element[1][:-2]
        # print(element)
        csv_writer.writerow({'manga_name':element[0], 'cover_link': element[1], 'id_book':i})

manga_csv.close()

# ecriture de chapitre.csv
with open("D:/MangaMagum/Python_make_csv_for_database/out/manga.csv",'r') as csvfile:
    with open("D:/MangaMagum/Python_make_csv_for_database/out/chapitre.csv", 'w') as chapitre_csv :
        reader = csv.reader(csvfile)
        fieldnames = ['id_book', 'liste_chapitre']
        writer = csv.DictWriter(chapitre_csv, fieldnames=fieldnames, lineterminator='\n')

        for row in reader:
            id_book = row[-1]
            # print(id_book)
            liste_chapitre = get_list_chapter(id_book)
            writer.writerow({'id_book':id_book,'liste_chapitre': liste_chapitre})
            

csvfile.close()
chapitre_csv.close()


# ecriture de page.csv
with open("D:/MangaMagum/Python_make_csv_for_database/out/chapitre.csv", 'r') as chapitre:
    with open("D:/MangaMagum/Python_make_csv_for_database/out/pages.csv", 'w') as page:
        chapitre_reader = csv.reader(chapitre)
        fieldnames = ['id_book', 'num_chapitre', 'liste_page']
        writer = csv.DictWriter(page, fieldnames=fieldnames, lineterminator='\n')


        for line in chapitre_reader:
            id_manga = int(line[0])
            liste_chapter = line[1][1:-1].split(',')

            for it in range(len(liste_chapter)):
                liste_chapter[it] = int(liste_chapter[it])


            for num_chapiter in liste_chapter :
                liste_url_page = get_list_of_url_for_page(id_manga,num_chapiter)

                writer.writerow({'id_book':id_manga, 'num_chapitre':num_chapiter, 'liste_page':liste_url_page})
                liste_url_page = []


chapitre.close()
page.close()
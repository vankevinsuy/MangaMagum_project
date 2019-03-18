# rewrite page with new page
from write_data import get_page
import csv

path_page = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\pages.csv"
chapter_for_page = [31,32]
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

            page_file_content.append({'id_book': id_book, 'chapitre': new_chapitre, 'list_page': get_page(new_chapitre, list_of_dic_to_rewrite_for_page)})
            id_book = id_book + 1

for item in page_file_content:
    print(item)
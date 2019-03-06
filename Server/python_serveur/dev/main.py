import os
from write_data import *
#from insert_in_db import insert_all

path_manga = "D:\MangaMagum_project\Server\python_serveur\dev\outpout\manga.csv"
path_chapter = "D:\MangaMagum_project\Server\python_serveur\dev\outpout\chapters.csv"
path_page = "D:\MangaMagum_project\Server\python_serveur\dev\outpout\pages.csv"

if os.path.exists(path_manga):
 os.remove(path_manga)

if os.path.exists(path_chapter):
 os.remove(path_chapter)

if os.path.exists(path_page):
 os.remove(path_page)


#read the input file
with open("D:\MangaMagum_project\Server\python_serveur\dev\input_file.txt", "r") as input_file:
    lines_in_input_file = input_file.readlines()
    input_file_as_list_of_dict = []

input_file.close()
#add lines as dctionnaries in input_file_as_list_of_dict
for line in lines_in_input_file:
    line = line.split(';')
    line[-1] = line[-1].rstrip()
    input_file_as_list_of_dict.append({"manga_name": line[0], "cover_link": line[1], "list_of_link": line[2]})

# treatment for each line (dictionnary)
id_book = 0
for item in input_file_as_list_of_dict:
    write_manga(item, id_book)
    write_chapter(item, id_book)
    #write_page(item, id_book)
    id_book = id_book + 1


#insert_all()

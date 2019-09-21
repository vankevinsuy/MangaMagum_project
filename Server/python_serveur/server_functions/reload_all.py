import platform
from server_functions.write_data import *
from server_functions.insert_in_db import insert_all
import os
import gc
from server_functions.Mail import Mail

print("######### RELOADING ALL ############")

path_manga = "outpout/manga.csv"
path_chapter = "outpout/chapters.csv"
path_page = "outpout/pages.csv"
input_file_path = "input_file.txt"


if os.path.exists(path_manga):
     os.remove(path_manga)

if os.path.exists(path_chapter):
     os.remove(path_chapter)

if os.path.exists(path_page):
     os.remove(path_page)


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
    if "--DESC--" in line:
        book_description = line[10:-1]
    if "--FIN NEW--" in line:
        input_file_as_list_of_dict.append({"manga_name": manga,
                                           "cover_link": cover,
                                           "list_of_link": list_of_link,
                                           "description": book_description})

# # treatment for each line (dictionnary)
id_book = 0
for item in input_file_as_list_of_dict:
    print(item)
    print("")
    write_manga(item, id_book)
    id_book = id_book + 1
    gc.collect()

id_book = 0
for item in input_file_as_list_of_dict:
    print(item)
    write_chapter(item, id_book)
    id_book = id_book + 1
    gc.collect()

print("")
id_book = 0
for item in input_file_as_list_of_dict:
    print(item)
    write_page(item, id_book)
    id_book = id_book + 1
    gc.collect()


insert_all()

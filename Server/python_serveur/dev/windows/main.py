import os
from write_data import *
#from insert_in_db import insert_all

path_manga = "D:\MangaMagum_project\Server\python_serveur\dev\windows\outpout\manga.csv"
path_chapter = "D:\MangaMagum_project\Server\python_serveur\dev\windows\outpout\chapters.csv"
path_page = "D:\MangaMagum_project\Server\python_serveur\dev\windows\outpout\pages.csv"

if os.path.exists(path_manga):
 os.remove(path_manga)

if os.path.exists(path_chapter):
 os.remove(path_chapter)

if os.path.exists(path_page):
 os.remove(path_page)


#read the input file
file = open('D:\\MangaMagum_project\\Server\\python_serveur\\dev\\windows\\input_file.txt','r').readlines()
input_file_as_list_of_dict = []

#add lines as dctionnaries in input_file_as_list_of_dict
for line in file :
    if "---NEW---" in line :
        manga = ""
        cover = ""
        list_of_link = []
    if "--MANGA--" in line :
        manga = line[11:-1]
    if "--COVER--" in line :
        cover = line[11:-1]
    if "--LINK--" in line:
        list_of_link.append(line[10:-1])
    if "--FIN NEW--" in line:
        input_file_as_list_of_dict.append({"manga_name": manga,
                                           "cover_link": cover,
                                           "list_of_link": list_of_link})

# treatment for each line (dictionnary)
id_book = 0
for item in input_file_as_list_of_dict:
    write_manga(item, id_book)
    write_chapter(item, id_book)
    #write_page(item, id_book)
    id_book = id_book + 1


#insert_all()

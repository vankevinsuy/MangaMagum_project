import os
from version_2.Make_tables_as_csv import *


if os.path.exists("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\manga.csv"):
  os.remove("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\manga.csv")

if os.path.exists("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\chapters.csv"):
  os.remove("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\chapters.csv")

if os.path.exists("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\pages.csv"):
  os.remove("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\outpout\\pages.csv")


#read the input file
with open("D:\\MangaMagum_project\\Server\\Python_make_csv_for_database\\version_2\\input_file.txt", "r") as input_file:
    lines_in_input_file = input_file.readlines()
    input_file_as_list_of_dict = []

input_file.close()
#add lines as dctionnaries in input_file_as_list_of_dict
for line in lines_in_input_file:
    line = line.split(';')
    line[-1] = line[-1].rstrip()

    input_file_as_list_of_dict.append({"manga_name": line[0], "cover_link": line[1], "list_of_link": line[2]})

#treatment for each line (dictionnary)
id_book = 0
for item in input_file_as_list_of_dict:
        # add_new_manga(item)
        make_tables(item, id_book)
        id_book = id_book + 1

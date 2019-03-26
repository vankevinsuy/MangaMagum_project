import time
start = time.time()
import platform
import os
import sys
from server_functions.write_data import *
from server_functions.insert_in_db import insert_all


def launch():
    what_to_do = input("what to do ? ")

    if what_to_do == "reload" :
        from  server_functions import reload_all
        reload_all
        return 1

    if what_to_do == "update" :
        if len(os.listdir(path_output)) == 0 :
            print("output folder is empty, you have to fill the database first \n")

            while(True):
                re = str(input("do you want to fill the database ? Y | N :  "))
                if re == "Y" or re == 'y':
                    from  server_functions import reload_all
                    from server_functions import update_content
                    reload_all
                    update_content
                    return 1

                if re == "N" or re == 'n':
                    return launch()

        else :
            from Server.python_serveur.dev.server_functions import update_content
            update_content
            return 1

    if what_to_do == "add" :
        add_new_manga()

    if what_to_do == "finsert":
        insert_all()

    if what_to_do == "quit" :
        sys.exit()


if platform.system() == "Windows":
    path_output = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout"

if platform.system() == "Darwin":
    path_output = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout"

if platform.system() == "Linux":
    path_output = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout"


print("rewrite the data base : reload")
print("update the database : update")
print("add new manga in database : add")
print("force insertion of datas in databases : finsert")
print("quit program : quit")

launch()

end = time.time()
print(end - start)

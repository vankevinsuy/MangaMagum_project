import time

start = time.time()

import sys
import subprocess
from server_functions.write_data import *
from server_functions.insert_in_db import insert_all
from server_functions.Mail import Mail

def launch():
    import os
    Mail().clear()
    path_output = "outpout"


    what_to_do = input("what to do ? ")

    if what_to_do == "reload":
        from server_functions import reload_all
        reload_all
        Mail().add("ACTION : reload_all")
        return 1

    if what_to_do == "update":
        if len(os.listdir(path_output)) == 0:
            print("output folder is empty, you have to fill the database first \n")

            while (True):
                re = str(input("do you want to fill the database ? Y | N :  "))
                if re == "Y" or re == 'y':
                    from server_functions import reload_all
                    from server_functions import update_content
                    reload_all
                    update_content
                    Mail().add("ACTION : reload_all + update_content")
                    return 1

                if re == "N" or re == 'n':
                    return launch()

        else:
            from server_functions import update_content
            Mail().add("ACTION : update")
            for it in range(10):
                update_content
            return 1

    if what_to_do == "add":
        add_new_manga()
        open("new_manga.txt", 'w')
        Mail().add("ACTION : add manga")

    if what_to_do == "finsert":
        insert_all()

    if what_to_do == "quit":
        sys.exit()




print("rewrite the data base : reload")
print("update the database : update")
print("add new manga in database : add")
print("force insertion of datas in databases : finsert")
print("quit program : quit")

launch()

end = time.time()
print(end - start)
# Mail().add("EXECUTION TIME  : " + str(end - start))
# Mail().send_report_to_admin("Server has finished to run")
#
# if platform.system() == "Linux":
#     #turn off the server
#     cmdCommand = "sudo shutdown -h now"
#     process = subprocess.Popen(cmdCommand.split(), stdout=subprocess.PIPE)

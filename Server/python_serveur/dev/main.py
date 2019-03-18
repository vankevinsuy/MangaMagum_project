import time
start = time.time()
import platform
import os
import sys

if platform.system() == "Windows":
    path_output = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout"

if platform.system() == "Darwin":
    path_output = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout"

if platform.system() == "Linux":
    path_output = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout"


print("What to do ? \n")
print("rewrite the data base : reload")
print("update the database : update")
print("quit program : quit")


while(True):
    what_to_do = str(input("what to do ?"))

    if what_to_do == "reload" :
        import reload_all
        reload_all
        break

    if what_to_do == "update" :
        if len(os.listdir(path_output)) == 0 :
            print("output folder is empty, you have to fill the database first \n")

            while(True):
                re = str(input("do you want to fill the database ? Y | N :  "))
                if re == "Y" or re == 'y':
                    import reload_all
                    import update_content
                    reload_all
                    update_content
                    break
                if re == "N" or re == 'n':
                    break

        else :
            import update_content
            update_content
            break

    if what_to_do == "quit" :
        break
        sys.exit()

end = time.time()
print(end - start)

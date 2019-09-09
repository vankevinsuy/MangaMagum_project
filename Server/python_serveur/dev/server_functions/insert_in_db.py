def insert_all():
    import csv
    import platform
    from firebase import firebase
    from server_functions.Mail import Mail

    if platform.system() == "Windows":
        path_manga = "outpout\\manga.csv"
        path_chapter = "outpout\\chapters.csv"
        path_page = "outpout\\pages.csv"
        input_file_path = "input_file.txt"


    if platform.system() == "Darwin":
        path_manga = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv"
        path_chapter = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv"
        path_page = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv"


    if platform.system() == "Linux":
        path_manga = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv"
        path_chapter = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv"
        path_page = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv"

    try:
        print("######### FIREBASE DATABASE INSERTION #########")
        root = {}
        mail = Mail()
        mail.add("ADDED MANGA : ")

        with open(path_manga, 'r') as manga_csv :
            with open(path_chapter, 'r') as chapter_csv:
                with open(path_page, 'r') as page_csv:

                    csv_reader_manga = csv.reader(manga_csv)
                    csv_reader_manga = list(csv_reader_manga)

                    csv_reader_chapter = csv.reader(chapter_csv)
                    csv_reader_chapter = list(csv_reader_chapter)

                    csv_reader_page = csv.reader(page_csv)
                    csv_reader_page = list(csv_reader_page)

                    list_page = {}

                    for line in csv_reader_manga :
                        # print(line)

                        manga_name = line[0]
                        cover = line[1]
                        id_book = int(line[2])
                        last_chapter = 0
                        description = line[3]

                        for line2 in csv_reader_chapter:
                            id_book_line2 = int(line2[0])
                            if id_book == id_book_line2 :
                                last_chapter = int(line2[1][1:-1].split(',')[-1])
                        # print(id_book)
                        # print(last_chapter)
                        for chapitre in range(1,last_chapter+1):
                            for line3 in csv_reader_page :
                                if len(line3) != 0:
                                    id_book_line3 = int(line3[0])
                                    chapitre_line3 = int(line3[1])
                                    list_link_page = line3[2][1:-1].replace("'","").split(',')
                                    if id_book_line3 == id_book and chapitre_line3 == chapitre:
                                        list_page["chapitre__" + str(chapitre)] = list_link_page

                    # print(list_page)

                        ## add manga in root
                        root[manga_name] = \
                        {
                            "name": manga_name,
                            "cover" : cover,
                            "id" : id_book,
                            "last_chapitre" : last_chapter,
                            "list_page" : list_page,
                            "description" : description
                        }
                        mail = Mail()
                        mail.add("      " + manga_name + " id : " + str(id_book) + "   last chapter : " + str(last_chapter))
                        list_page = {}
        # print(root)


        firebase = firebase.FirebaseApplication("https://manga-time-7a6bf.firebaseio.com/", None)
        firebase.delete('/',"manga")
        firebase.put('/',"manga" ,root)
        print("DATAS INSERT IN FIREBASE DATABASE")

    except:
        print("INSERTION IN FIREBASE DATABASE FAILED")

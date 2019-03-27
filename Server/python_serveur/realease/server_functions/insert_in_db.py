def insert_all():
    import mysql.connector
    import csv
    import platform
    from firebase import firebase

    try:
        print("######### SQL DATABASE INSERTION #########")

        if platform.system() == "Windows":
            path_manga = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\outpout\\manga.csv"
            path_chapter = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\outpout\\chapters.csv"
            path_page = "D:\\MangaMagum_project\\Server\\python_serveur\\realease\\outpout\\pages.csv"
            a_host="localhost"
            a_user="root"
            a_passwd=""
            a_database="mangamagum"

        if platform.system() == "Darwin":
            path_manga = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv"
            path_chapter = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv"
            path_page = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv"
            a_host="localhost"
            a_user="root"
            a_passwd="root"
            a_database="mangamagum"

        if platform.system() == "Linux":
            path_manga = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/manga.csv"
            path_chapter = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/chapters.csv"
            path_page = "/home/vankevin/MangaMagum_project/Server/python_serveur/realease/outpout/pages.csv"
            a_host="vankevin_server"
            a_user="server"
            a_passwd="deadoralive"
            a_database="mangamagum"

        mydb = mysql.connector.connect(
            host=a_host,
            user=a_user,
            passwd=a_passwd,
            database=a_database
        )

        mycursor = mydb.cursor()


        # clear the database
        Delete_all_query = """truncate table manga """
        mycursor.execute(Delete_all_query)
        mydb.commit()

        Delete_all_query = """truncate table chapters """
        mycursor.execute(Delete_all_query)
        mydb.commit()

        Delete_all_query = """truncate table pages """
        mycursor.execute(Delete_all_query)
        mydb.commit()

        to_insert = []


        #fill manga table
        with open(path_manga) as manga_file:
            csv_reader = csv.reader(manga_file, delimiter=',')

            for data in csv_reader:
                # print(data)
                manga_name = data[0]
                cover_link = data[1]
                id_manga = data[2]

                to_insert.append((manga_name, cover_link, id_manga))



            sql = "INSERT INTO manga (manga_name, cover_link, id_manga) VALUES (%s, %s, %s)"
            mycursor.executemany(sql, to_insert)
            mydb.commit()
            print(mycursor.rowcount, "mangas was inserted.")
            to_insert.clear()
            manga_file.close()
            to_insert.clear()
        #------------------------------------------------------------------------------------------------

        #fill chapter table
        with open(path_chapter) as chapter_file:
            csv_reader = csv.reader(chapter_file, delimiter=',')

            for data in csv_reader:
                # print(data)
                id_manga = data[0]
                list_of_chapter = data[1]

                to_insert.append((id_manga, list_of_chapter))



            sql = "INSERT INTO chapters (id_book, list_of_chapters) VALUES (%s, %s)"
            mycursor.executemany(sql, to_insert)
            mydb.commit()
            print(mycursor.rowcount, " list chapters was inserted for id book = " + str(id_manga))
            to_insert.clear()
            chapter_file.close()
            to_insert.clear()
        #----------------------------------------------------------------------------------------------------

        # fill page table
        with open(path_page) as page_file:
            csv_reader = csv.reader(page_file, delimiter=',')

            for data in csv_reader:
                # print(data)
                id_book = data[0]
                chapitre = data[1]
                list_page = data[2]

                to_insert.append((id_book, chapitre, list_page))

            for row in to_insert:
                sql = "INSERT INTO pages (id_book, chapitre, list_page) VALUES (%s, %s, %s)"
                mycursor.execute(sql, row)
                mydb.commit()
                print(mycursor.rowcount, " list pages was inserted for id book = " + str(id_book))
                manga_file.close()
            to_insert.clear()
        print("DATAS INSERT IN MYSQL DATABASE")

    except :
        print("INSERTION IN MYSQL DATABASE FAILED")


    try:
        print("######### FIREBASE DATABASE INSERTION #########")
        root = {}

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
                            "list_page" : list_page
                        }

                        list_page = {}
        # print(root)


        firebase = firebase.FirebaseApplication('https://fireapp-f2062.firebaseio.com/', None)
        firebase.delete('/',"manga")
        firebase.put('/',"manga" ,root)
        print("DATAS INSERT IN FIREBASE DATABASE")

    except:
        print("INSERTION IN FIREBASE DATABASE FAILED")

def insert_all():
    import mysql.connector
    import csv
    import platform

    if platform.system() == "Windows":
        path_manga = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\manga.csv"
        path_chapter = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\chapters.csv"
        path_page = "D:\\MangaMagum_project\\Server\\python_serveur\\dev\\outpout\\pages.csv"

    if platform.system() == "Darwin":
        path_manga = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout/manga.csv"
        path_chapter = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout/chapters.csv"
        path_page = "/Users/vankevinsuy/Documents/MangaMagum_project/Server/python_serveur/dev/outpout/pages.csv"

    if platform.system() == "Linux":
        path_manga = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout/manga.csv"
        path_chapter = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout/chapters.csv"
        path_page = "/home/vankevin/MangaMagum_project/Server/python_serveur/dev/outpout/pages.csv"

    mydb = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="",
        database="mangamagum"
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

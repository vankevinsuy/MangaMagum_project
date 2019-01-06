def get_list_chapter(id_book) :

    # Bleach
    if id_book == '0':
        nb_chapter = 686
        list_chapter = []
        for i in range(1,nb_chapter+1):
            list_chapter.append(i)
        return list_chapter
        # print(list_chapter)
file = open('D:\\MangaMagum_project\\Server\\python_serveur\\dev\\version2\\link_one_piece.txt','r').readlines()
input_file_as_list_of_dict = []

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

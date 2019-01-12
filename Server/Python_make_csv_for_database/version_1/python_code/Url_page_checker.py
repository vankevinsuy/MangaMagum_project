import urllib3
urllib3.disable_warnings()

# def check_URL(url):
#     return True

def get_list_of_url_for_page(id_book, num_chapitre):

    # Bleach
    if id_book == '0' or id_book == 0:
        http = urllib3.PoolManager()
        num_page = 1

        list_page_to_return = []
        while(True):
            base_url = "https://cdn.mangahub.io/file/imghub/bleach/{}/{}.jpg".format(num_chapitre,num_page)
            r = http.request('GET', base_url)

            if r.status == 200 :
                list_page_to_return.append(base_url)
                print(base_url)
                num_page = num_page+1
            else:
                return list_page_to_return
# import urllib3
# urllib3.disable_warnings()
#
# def check_url(url):
#     http = urllib3.PoolManager()
#     r = http.request('GET', url)
#     if r.status == 200:
#         print(url)
#         http.clear()
#         return True
#     else:
#         http.clear()
#         return False
#
#
# def check_last_chapter(url):
#     http = urllib3.PoolManager()
#     r = http.request('GET', url)
#     if r.status == 200:
#         http.clear()
#         return True
#     else:
#         http.clear()
#         return False

import requests

def check_url(url):
    r = requests.get(url)
    if r.status_code == 200:
        print(url)
        return True
    else:
        return False

def check_last_chapter(url):
    r = requests.get(url)
    if r.status_code == 200:
        return True
    else:
        return False
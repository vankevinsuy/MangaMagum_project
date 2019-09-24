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
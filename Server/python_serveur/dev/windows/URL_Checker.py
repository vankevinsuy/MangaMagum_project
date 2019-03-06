import urllib3
urllib3.disable_warnings()

def check_url(url):
    http = urllib3.PoolManager()
    r = http.request('GET', url)
    if r.status == 200:
        # print(url)
        http.clear()
        return True
    else:
        http.clear()
        return False


def check_last_chapter(url):
    http = urllib3.PoolManager()
    r = http.request('GET', url)
    if r.status == 200:
        http.clear()
        return True
    else:
        http.clear()
        return False
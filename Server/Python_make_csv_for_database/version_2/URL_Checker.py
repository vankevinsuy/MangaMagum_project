import urllib3
urllib3.disable_warnings()

def check_urls(list_of_url, base_urls):
    http = urllib3.PoolManager()
    good_urls = []
    good_base_url = []

    for i in range(len(list_of_url)):
        r = http.request('GET', list_of_url[i])

        if r.status == 200:
            good_urls.append(list_of_url[i])
            good_base_url.append(base_urls[i])
        else:
            pass

    # if there is at least 1 good url return true
    if len(good_urls)>0:
        return True, good_base_url[0]
    else:
        return False


def check_url(url):
    http = urllib3.PoolManager()
    r = http.request('GET', url)
    if r.status == 200:
        return True
    else:
        return False
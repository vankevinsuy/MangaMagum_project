from urllib.request import Request, urlopen
url="https://mangahub.io/chapter/onepunch-man_148/chapter-102"
req = Request(url, headers={'User-Agent': 'Mozilla/5.0'})

web_byte = urlopen(req).read()

webpage = web_byte.decode('utf-8')

for i in range(len(webpage)):
    if webpage[i] == 'i' and webpage[i+1] == 'm' and webpage[i+2] == 'g' :
        print(webpage[i : i+90])
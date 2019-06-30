from firebase import firebase
firebase = firebase.FirebaseApplication('https://manga-time-7a6bf.firebaseio.com/', None)
result = firebase.get('/manga/Boruto', None)
print(result["cover"])
<?php
try {
  //connection to mangamagum db
 $bdd = new PDO('mysql:host=localhost;dbname=mangamagum;charset=utf8', 'root', '');
    if ($bdd) {
      //if connection succeed get manga's names
        $reponse = $bdd->query('SELECT * FROM `manga` ORDER BY `id_manga`');

        //complete the list of mangamagum
           while ($donnees = $reponse->fetch()){
              echo $donnees['manga_name'].";";
              echo $donnees['cover_link'].";";
              echo $donnees['id_manga']."\n";
           }
        }
}

catch (\Exception $e) {
    die('Erreur : ' . $e->getMessage());
}


 ?>

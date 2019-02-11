<?php
try {
  //connection to mangamagum db
 $bdd = new PDO('mysql:host=localhost;dbname=mangamagum;charset=utf8', 'root', '');
    if ($bdd) {

          $reponse2 = $bdd->query("SELECT * FROM `chapters` ORDER BY `id_book`");
          while ($donnees = $reponse2->fetch()){
              echo $donnees['id_book'].";";
              echo $donnees['list_of_chapters']."\n";
          }
        }
}

catch (\Exception $e) {
    die('Erreur : ' . $e->getMessage());
}


 ?>

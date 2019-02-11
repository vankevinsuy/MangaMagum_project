<?php
try {
  //connection to mangamagum db
 $bdd = new PDO('mysql:host=localhost;dbname=mangamagum;charset=utf8', 'root', '');
    if ($bdd) {

          $reponse2 = $bdd->query("SELECT * FROM `pages` ORDER BY `id_book`");
          while ($donnees = $reponse2->fetch()){
              echo $donnees['id_book'].";";
              echo $donnees['chapitre'].";";
              echo $donnees['list_page']."\n";
          }
        }
}

catch (\Exception $e) {
    die('Erreur : ' . $e->getMessage());
}


 ?>

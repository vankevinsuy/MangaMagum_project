package com.example.mangamagum.Model;

<<<<<<< HEAD
import java.util.ArrayList;

public class Chapitre {
    private String num_chapitre;
    private ArrayList<String>pages;

    public Chapitre(String num_chapitre, ArrayList<String> pages) {
        this.num_chapitre = num_chapitre;
        this.pages = pages;
    }

    public String getNum_chapitre() {
        return num_chapitre;
    }

    public ArrayList<String> getPages() {
        return pages;
    }

    public String getPages_as_list(){
        String res ="[";


        for(String link : getPages()){
            res = res + '"' + link + '"' +',';
        }
        res = res.substring(0,res.length()-1);
        res = res + ']';

        return res;
    }
}



=======
public class Chapitre {
}
>>>>>>> 04a6cdb183b29988422db6cab3d7c106ca096341

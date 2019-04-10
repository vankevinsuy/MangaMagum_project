package com.wallaby.mangamagum.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String name ;
    private String cover_link ;
    private String description ;
    private String id_book ;
    private String last_chapitre;
    private ArrayList<Chapitre> list_chapitre;

    public Book(String name, String cover_link, String id_book) {
        this.name = name;
        this.cover_link = cover_link;
        this.id_book = id_book;
    }

    public Book(String name, String cover_link, String id_book, String last_chapitre, ArrayList<Chapitre> list_chapitre, String description) {
        this.name = name;
        this.cover_link = cover_link;
        this.id_book = id_book;
        this.last_chapitre = last_chapitre;
        this.list_chapitre = list_chapitre;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCover_link() {
        return cover_link;
    }

    public String getId_book() {
        return id_book;
    }

    public String getDescription() {
        return description;
    }

    public String getLast_chapitre() {
        return last_chapitre;
    }

    public ArrayList<Chapitre> getList_chapitre() {
        return list_chapitre;
    }

    public String getList_Chapitre_from_one_to_end_as_list(){
        String res = "[";
        int last_chap = Integer.parseInt(getLast_chapitre());
        for (int i = 1; i < last_chap+1; i++) {
            res = res + '"' +Integer.toString(i) +'"'+',' ;
        }
        res = res.substring(0,res.length()-1);

        res = res + ']';

        return res;
    }


}

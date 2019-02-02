package com.example.mangamagum.model;

public class Book {
    private String name ;
    private String cover_link ;
    private String description ;
    private String id_book ;

    public Book(String name, String cover_link, String id_book) {
        this.name = name;
        this.cover_link = cover_link;
        this.id_book = id_book;
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

}

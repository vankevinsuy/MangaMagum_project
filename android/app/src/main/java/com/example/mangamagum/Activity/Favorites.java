package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.mangamagum.Adapter.Favorites_Adapter;
import com.example.mangamagum.Adapter.Library_Adapter;
import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Favorites extends AppCompatActivity {

    private ImageButton go_to_library;

    private DataBase dataBase;

    private RecyclerView favorites_recycler_view;
    private RecyclerView.Adapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ArrayList<Book> arrayList_book = new ArrayList<>();

        favorites_recycler_view = findViewById(R.id.favorites_recycler_view);
        favorites_recycler_view.setHasFixedSize(false);
        favorites_recycler_view.setLayoutManager(new GridLayoutManager(this,3));

        go_to_library = findViewById(R.id.go_to_library_button);
        go_to_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent library = new Intent(getApplicationContext(), Library.class);
                startActivity(library);
            }
        });

        dataBase = new DataBase(this);
        fill_library(getApplicationContext());

    }


    public ArrayList<Book> fill_library(Context context){
        final ArrayList<Book> arrayList_book = new ArrayList<>();
        dataBase = new DataBase(context);
        Cursor res= dataBase.get_all_manga();

        for(Integer id_fav : dataBase.get_all_favorites() ){
            arrayList_book.add(dataBase.get_book_from_id(id_fav));
        }

        mAdapter = new Library_Adapter(arrayList_book, context);
        ((Library_Adapter) mAdapter).setOnItemClickListener(new Library_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id_book = arrayList_book.get(position).getId_book();
                String manga_name = arrayList_book.get(position).getName();
                String cover_link = arrayList_book.get(position).getCover_link();

                Intent chosen_manga_activity = new Intent(getApplicationContext() , Chosen_manga.class);

                chosen_manga_activity.putExtra("id_book" , id_book);
                chosen_manga_activity.putExtra("manga_name" , manga_name);
                chosen_manga_activity.putExtra("cover_link" , cover_link);

                startActivity(chosen_manga_activity);
            }
        });
        favorites_recycler_view.setAdapter(mAdapter);
        return arrayList_book;
    }

    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

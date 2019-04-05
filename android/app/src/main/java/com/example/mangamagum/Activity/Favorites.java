package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.mangamagum.Adapter.Favorites_Adapter;
import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    private ImageButton go_to_library;
    private ImageButton go_to_search;


    private DataBase dataBase;

    private RecyclerView favorites_recycler_view;
    private RecyclerView.Adapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        go_to_library = findViewById(R.id.go_to_library_button);
        go_to_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent library = new Intent(getApplicationContext(), Library.class);
                startActivityForResult(library,0);
                overridePendingTransition(0,0);
                finish();
            }
        });

        go_to_search = findViewById(R.id.go_to_search_button);
        go_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_layout = new Intent(getApplicationContext(), Research.class);
                startActivityForResult(search_layout,0);
                overridePendingTransition(0,0);
                finish();
            }
        });

        favorites_recycler_view = findViewById(R.id.favorites_recycler_view);
        favorites_recycler_view.setHasFixedSize(false);
        favorites_recycler_view.setLayoutManager(new GridLayoutManager(this,3));



        dataBase = new DataBase(this);
        fill_library(getApplicationContext());

    }


    public void fill_library(Context context){    //ArrayList<Book>
        final ArrayList<Book> arrayList_book = new ArrayList<>();
        dataBase = new DataBase(context);

        for(Integer id_fav : dataBase.get_all_favorites() ){
            arrayList_book.add(dataBase.get_book_from_id(id_fav));

        }

        mAdapter = new Favorites_Adapter(arrayList_book, context);
        ((Favorites_Adapter) mAdapter).setOnItemClickListener(new Favorites_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id_book = arrayList_book.get(position).getId_book();
                String manga_name = arrayList_book.get(position).getName();
                String cover_link = arrayList_book.get(position).getCover_link();

                Intent chosen_manga_activity = new Intent(getApplicationContext() , Chosen_manga.class);

                chosen_manga_activity.putExtra("id_book" , id_book);
                chosen_manga_activity.putExtra("manga_name" , manga_name);
                chosen_manga_activity.putExtra("cover_link" , cover_link);

                startActivityForResult(chosen_manga_activity,0);
                overridePendingTransition(0,0);
            }
        });
        favorites_recycler_view.setAdapter(mAdapter);
    }

    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent library = new Intent(getApplicationContext() , Library.class);
        startActivityForResult(library,0);
        overridePendingTransition(0,0);
        finish();
    }
}

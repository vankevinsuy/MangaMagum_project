package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mangamagum.Adapter.Library_Adapter;
import com.example.mangamagum.Adapter.Research_adapter;
import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.util.ArrayList;

public class Research extends AppCompatActivity {

    private ImageButton go_to_favorites;
    private ImageButton go_to_library;
    private EditText search_bar;

    private RecyclerView result_recycler_view;
    private RecyclerView.Adapter mAdapter;

    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        dataBase = new DataBase(this);


        go_to_favorites = findViewById(R.id.go_to_favorite);
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorite = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorite);
                finish();
            }
        });

        go_to_library = findViewById(R.id.go_to_library_button);
        go_to_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent library = new Intent(getApplicationContext(), Library.class);
                startActivity(library);
                finish();
            }
        });
        //remove focus on edit text
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        result_recycler_view = findViewById(R.id.result_recycler_view);
        result_recycler_view.setHasFixedSize(false);
        result_recycler_view.setLayoutManager(new GridLayoutManager(this,3));




        search_bar = findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                init_fill_library(getApplicationContext());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fill_research(getApplicationContext(), dataBase.search_bar_database(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });


    }



    public ArrayList<Book> init_fill_library(Context context){
        final ArrayList<Book> arrayList_book = dataBase.get_all_manga();

        mAdapter = new Research_adapter(arrayList_book, context);
        ((Research_adapter) mAdapter).setOnItemClickListener(new Research_adapter.OnItemClickListener() {
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
                finish();
            }
        });
        result_recycler_view.setAdapter(mAdapter);
        return arrayList_book;
    }

    public ArrayList<Book> fill_research(Context context, ArrayList<Book> books){
        final ArrayList<Book> arrayList_book = books;

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
                finish();
            }
        });
        result_recycler_view.setAdapter(mAdapter);
        return arrayList_book;
    }


    @Override
    public void onBackPressed() {
        Intent library = new Intent(getApplicationContext() , Library.class);

        startActivity(library);
        finish();
    }
}

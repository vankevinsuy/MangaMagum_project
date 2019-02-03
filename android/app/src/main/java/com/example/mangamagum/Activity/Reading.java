package com.example.mangamagum.Activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.util.ArrayList;

public class Reading extends AppCompatActivity {

    private String chapter;
    private String id_book;
    private DataBase dataBase;
    private ArrayList<String> list_urls;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager Manager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        this.dataBase = new DataBase(getApplicationContext());
        this.chapter = getIntent().getExtras().getString("chapter");
        this.id_book = getIntent().getExtras().getString("id_book");
        this.list_urls = this.dataBase.get_page_by_chapitre(Integer.parseInt(this.id_book), Integer.parseInt(this.chapter));

    }



    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

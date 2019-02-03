package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;

import com.example.mangamagum.Adapter.Page_adapter;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Reading extends AppCompatActivity {

    private String chapter;
    private String id_book;
    private DataBase dataBase;
    private ArrayList<String> list_urls;

    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        this.dataBase = new DataBase(getApplicationContext());
        this.chapter = getIntent().getExtras().getString("chapter");
        this.id_book = getIntent().getExtras().getString("id_book");
        this.list_urls = this.dataBase.get_page_by_chapitre(Integer.parseInt(this.id_book), Integer.parseInt(this.chapter));
//        ShowMessage("list url size" , Integer.toString(this.list_urls.size()));
//        ShowMessage("list url index 0 content" , this.list_urls.get(0));

        mRecyclerView = findViewById(R.id.page_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        Page_adapter page_adapter = new Page_adapter(this.list_urls, this.getApplicationContext(), width,height);
        mRecyclerView.setAdapter(page_adapter);

    }



    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
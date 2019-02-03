package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        Page_adapter page_adapter = new Page_adapter(this.list_urls, this.getApplicationContext());
        mRecyclerView.setAdapter(page_adapter);

//        Displayer displayer = new Displayer(this.chapter, this.id_book,this.dataBase, this.getApplicationContext(), this);
//        displayer.execute();
    }



    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

//class Displayer extends AsyncTask<Void,Void,Void>{
//
//    String chapter;
//    String id_book;
//    DataBase dataBase;
//    ArrayList<String> list_urls;
//    Context context;
//    WeakReference<Reading> activityReference;
//
//    public Displayer(String chapter, String id_book, DataBase dataBase, Context context, Reading reading_activity) {
//        this.chapter = chapter;
//        this.id_book = id_book;
//        this.dataBase = dataBase;
//        this.context = context;
//        this.activityReference = new WeakReference<Reading>(reading_activity);
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        this.list_urls = this.dataBase.get_page_by_chapitre(Integer.parseInt(this.id_book), Integer.parseInt(this.chapter));
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//        Reading activity = this.activityReference.get();
//
//        activity.mRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        activity.mRecyclerView.setLayoutManager(manager);
//        Page_adapter page_adapter = new Page_adapter(this.list_urls, context);
//        activity.mRecyclerView.setAdapter(page_adapter);
//        return null;
//    }
//}
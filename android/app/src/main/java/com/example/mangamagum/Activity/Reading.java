package com.example.mangamagum.Activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.widget.Toast;

import com.example.mangamagum.Adapter.Page_adapter;
import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.Chapitre;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.util.ArrayList;

public class Reading extends AppCompatActivity {

    public int chapter;
    public Book selected_book;


    private DataBase dataBase;
    private ArrayList<String> list_urls;

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);


// get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        this.dataBase = new DataBase(getApplicationContext());

        Intent i = getIntent();
        selected_book = (Book)i.getSerializableExtra("selected_book");
        chapter = getIntent().getExtras().getInt("chapter_target");

        for(Chapitre chapitre : selected_book.getList_chapitre()){
            if(Integer.parseInt(chapitre.getNum_chapitre()) == chapter){
                this.list_urls = chapitre.getPages();
            }
        }

        ArrayList<String> corected_list = new ArrayList<>();

        for(String link : this.list_urls){
            corected_list.add(link.replaceAll("\\s+",""));
        }

//        Toast.makeText(this, this.list_urls.get(1), Toast.LENGTH_SHORT).show();



        mRecyclerView = findViewById(R.id.page_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new Page_adapter(corected_list, this.getApplicationContext(), width,height);
        mRecyclerView.setAdapter(mAdapter);

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
        Intent chosen_manga_activity = new Intent(getApplicationContext() , Chosen_manga.class);

        chosen_manga_activity.putExtra("selected_book" , selected_book);

        startActivity(chosen_manga_activity);
    }

}
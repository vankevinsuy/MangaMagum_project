package com.wallaby.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wallaby.mangamagum.Adapter.Page_adapter;
import com.wallaby.mangamagum.Model.Book;
import com.wallaby.mangamagum.Model.Chapitre;
import com.wallaby.mangamagum.Model.DataBase;
import com.wallaby.mangamagum.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Reading extends AppCompatActivity {

    public int chapter;
    public Book selected_book;
    public DatabaseReference reference;


    private DataBase dataBase;


    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;

    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);


// get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;


        this.dataBase = new DataBase(getApplicationContext());

        Intent i = getIntent();
        selected_book = (Book)i.getSerializableExtra("selected_book");
        chapter = getIntent().getExtras().getInt("chapter_target");

        Loading_data_reading loading_data = new Loading_data_reading(this , getApplicationContext());
        loading_data.execute();


//        for(Chapitre chapitre : selected_book.getList_chapitre()){
//            if(Integer.parseInt(chapitre.getNum_chapitre()) == chapter){
//                this.list_urls = chapitre.getPages(selected_book.getId_book(), chapter, getApplicationContext());
//            }
//        }
//
//        ArrayList<String> corected_list = new ArrayList<>();
//        for(String link : this.list_urls){
//            corected_list.add(link.replaceAll("\\s+",""));
//        }
//
//
//
//        mRecyclerView = findViewById(R.id.page_recycler_view);
//        mRecyclerView.setHasFixedSize(false);
//        LinearLayoutManager manager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(manager);
//        mAdapter = new Page_adapter(corected_list, this.getApplicationContext(), width,height);
//        mRecyclerView.setAdapter(mAdapter);

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




class Loading_data_reading extends AsyncTask<Void,Void,Void>{
    private WeakReference<Reading> activityReference;
    Context context;
    ArrayList<String> list_urls = new ArrayList<>();

    public Loading_data_reading(Reading activity, Context context) {
        this.activityReference = new WeakReference<Reading>(activity);
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference reference;
        final Reading activity = activityReference.get();
        final String idBook = activity.selected_book.getId_book();
        final int chapterTargeted = activity.chapter;

        reference = FirebaseDatabase.getInstance().getReference().child("manga");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> book = dataSnapshot.getChildren();


                while (book.iterator().hasNext()){
                    DataSnapshot iterator = book.iterator().next();


                    if (iterator.child("id").getValue().toString().equals(idBook)){
//                        Toast.makeText(context, idBook +"amamam", Toast.LENGTH_SHORT).show();

                        Iterable<DataSnapshot> chapitres = iterator.child("list_page").getChildren();
                        while (chapitres.iterator().hasNext()){

                            DataSnapshot iterator2 = chapitres.iterator().next();
                            String n = iterator2.child(iterator2.getKey()).getKey().toString();
                            String[]m = n.split("__");
                            String num_chapitre = m[1];

                            if(num_chapitre.equals(Integer.toString(chapterTargeted))){
                                Iterable<DataSnapshot> pages = iterator.child("list_page").child(n).getChildren();
                                while (pages.iterator().hasNext()){
                                    DataSnapshot iterator3 = pages.iterator().next();
                                    list_urls.add(iterator3.getValue().toString());
                                }

                                ArrayList<String> corected_list = new ArrayList<>();
                                for(String link : list_urls){
                                    corected_list.add(link.replaceAll("\\s+",""));
                                }

                                Log.i("url", Integer.toString(list_urls.size()));

                                activity.mRecyclerView = activity.findViewById(R.id.page_recycler_view);
                                activity.mRecyclerView.setHasFixedSize(false);
                                LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                activity.mRecyclerView.setLayoutManager(manager);
                                activity.mAdapter = new Page_adapter(corected_list, context, activity.width,activity.height);
                                activity.mRecyclerView.setAdapter(activity.mAdapter);


                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return null;
    }
}
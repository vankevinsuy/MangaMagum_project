package com.example.mangamagum.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangamagum.Adapter.Library_Adapter;
import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.Chapitre;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Library extends AppCompatActivity {

    private ImageButton update_button;
    private ImageButton go_to_favorites;
    private ImageButton go_to_search;


    private RecyclerView manga_recycler_view;
    private RecyclerView.Adapter mAdapter;

    private Context context;
    private Library activity;
    private DataBase dataBase;
    private Boolean internet_check;
    private Boolean network_check;

    public ArrayList<String> manga_table_data_list;
    public ArrayList<String> chapter_table_data_list;
    public ArrayList<String> page_table_data_list;
    public ArrayList<Book> list_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);


        update_button = findViewById(R.id.update_database);
        go_to_favorites = findViewById(R.id.go_to_favorite);
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorite = new Intent(getApplicationContext(), Favorites.class);
                favorite.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(favorite);
                startActivityForResult(favorite,0);
                overridePendingTransition(0,0);
                finish();
            }
        });

        go_to_search = findViewById(R.id.go_to_search_button);
        go_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_layout = new Intent(getApplicationContext(), Research.class);
//                startActivity(search_layout);
                startActivityForResult(search_layout,0);
                overridePendingTransition(0,0);
                finish();
            }
        });

        manga_recycler_view = findViewById(R.id.manga_recycler_view);
        manga_recycler_view.setHasFixedSize(false);
        manga_recycler_view.setLayoutManager(new GridLayoutManager(this,3));


        manga_table_data_list = new ArrayList<>();
        chapter_table_data_list = new ArrayList<>();
        page_table_data_list = new ArrayList<>();
        list_book = new ArrayList<>();

        context = this.getApplicationContext();
        activity = this;
        dataBase = new DataBase(this);

        first_use(context);



//        internet_check = internetIsConnected();
//        network_check = isNetworkConnected(context);
//
//        ShowMessage("internet" , internet_check.toString());
//        ShowMessage("network" , network_check.toString());


        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill_library(context);

                update_button.animate().rotation(update_button.getRotation()-360).setDuration(1000).start();

                update_button.setEnabled(false);
                Server server = new Server(context , activity, dataBase);
                server.execute();
                dataBase.close();

                update_button.setEnabled(true);
                fill_library(context);

            }
        });

    }


    public void fill_library(Context context){
        final ArrayList<Book> arrayList_book = dataBase.get_all_manga();

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

                startActivityForResult(chosen_manga_activity,0);
                overridePendingTransition(0,0);
//                startActivity(chosen_manga_activity);
                finish();
            }
        });
        manga_recycler_view.setAdapter(mAdapter);
//        return arrayList_book;
    }
    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!= null;
    }
    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    private void first_use(Context context){

        int use = -1;
        dataBase = new DataBase(context);



        Cursor res= dataBase.get_first_use();

        //if it's the first use
        if (res.getCount() == 0){
            dataBase.initiate_first_use();
            ShowMessage("Hello", "It's your first time on this app, wait a little bit, we are updating your database with all we have");
            update_button.setEnabled(false);
            Server server = new Server(context , activity, dataBase);
            server.execute();
            dataBase.close();
            fill_library(context);
            update_button.setEnabled(true);
            fill_library(context);
            fill_library(context);
        }


        //if we have already used
        while (res.moveToNext()){
            use = res.getInt(0);
            fill_library(context);
        }
    }


    // method for closing the app
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Library.super.onBackPressed();
                        quit();
                        System.exit(0);
                    }
                }).create().show();
    }
    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }

}



class Server{
    Context context;
    private WeakReference<Library> activityReference;

    public String name ;
    public String cover_link ;
    public String id_book ;
    public String last_chapitre;



    private DataBase db;

    public DatabaseReference reference;

    public Server(Context context, Library activity, DataBase dataBase) {
        this.context = context;
        activityReference = new WeakReference<Library>(activity);
        this.db = dataBase;
    }

    public void execute(){
        Library activity = this.activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        //vider la base de données interne
        this.db.clear_manga_table();
        this.db.clear_chapter_table();
        this.db.clear_page_table();


        //remplir la liste avant de remplir la base de données
        reference = FirebaseDatabase.getInstance().getReference().child("manga");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Library activity = activityReference.get();

                activity.list_book.clear();
                Iterable<DataSnapshot> book = dataSnapshot.getChildren();

                ArrayList<Chapitre> list_chapitres;
                list_chapitres = new ArrayList<>();

                while (book.iterator().hasNext()){
                    DataSnapshot iterator = book.iterator().next();
                    list_chapitres.clear();

                    name = iterator.child("name").getValue().toString();
                    cover_link = iterator.child("cover").getValue().toString();
                    id_book = iterator.child("id").getValue().toString();
                    last_chapitre = iterator.child("last_chapitre").getValue().toString();

                    Iterable<DataSnapshot> chapitres = iterator.child("list_page").getChildren();
                    while (chapitres.iterator().hasNext()){

                        DataSnapshot iterator2 = chapitres.iterator().next();
                        String n = iterator2.child(iterator2.getKey()).getKey().toString();
                        String[]m = n.split("__");
                        String num_chapitre = m[1];

                        ArrayList<String> list_pages = new ArrayList<>();

                        Iterable<DataSnapshot> pages = iterator.child("list_page").child(n).getChildren();
                        while (pages.iterator().hasNext()){

                            DataSnapshot iterator3 = pages.iterator().next();
                            list_pages.add(iterator3.getValue().toString());
                        }
                        list_chapitres.add(new Chapitre(num_chapitre, list_pages));

                    }
                    activity.list_book.add(new Book(name,cover_link,id_book,last_chapitre,list_chapitres));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //remplir la base de données
        for (Book item: activity.list_book) {
            String manga_name = item.getName();
            String cover_link = item.getCover_link();
            String id_manga = item.getId_book();
            this.db.insertManga(manga_name,cover_link,id_manga);
        }

        for (Book item: activity.list_book) {
            String id_book = item.getId_book();
            String list_of_chapter = item.getList_Chapitre_from_one_to_end_as_list();
            this.db.insertChapter(Integer.parseInt(id_book), list_of_chapter);
        }

        for (Book item: activity.list_book) {
            String id_book = item.getId_book();

            for (Chapitre chapitre : activity.list_book.get(Integer.parseInt(id_book)).getList_chapitre()) {
                String num_chapitre = chapitre.getNum_chapitre();
                String list_page = chapitre.getPages_as_list();
                this.db.insertPages(Integer.parseInt(id_book), Integer.parseInt(num_chapitre), list_page);

            }
        }
        Toast.makeText(context, "Update done", Toast.LENGTH_SHORT).show();
    }
}









//// class for updating the manga database
//class Server extends AsyncTask<Void,Void, ArrayList<Book>> {
//    Context context;
//    private WeakReference<Library> activityReference;
//
//    public String name ;
//    public String cover_link ;
//    public String id_book ;
//    public String last_chapitre;
//
//
//
//    private DataBase db;
//
//    public DatabaseReference reference;
//
//    public Server(Context context, Library activity, DataBase dataBase) {
//        this.context = context;
//        activityReference = new WeakReference<Library>(activity);
//        this.db = dataBase;
//    }
//
////    @Override
////    protected void onPreExecute() {
////        super.onPreExecute();
////        Library activity = this.activityReference.get();
////        if (activity == null || activity.isFinishing()) {
////            return;
////        }
////        this.db.clear_manga_table();
////        this.db.clear_chapter_table();
////        this.db.clear_page_table();
////        activity.fill_library(context);
////
////    }
//
//    //**** filling list with server's datas
//    @Override
//    protected ArrayList<Book> doInBackground(Void... voids) {
//        Library activity = activityReference.get();
//
//        reference = FirebaseDatabase.getInstance().getReference().child("manga");
//        reference.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Library activity = activityReference.get();
//
//                Iterable<DataSnapshot> book = dataSnapshot.getChildren();
//
//                ArrayList<Chapitre> list_chapitres;
//                list_chapitres = new ArrayList<>();
//
//                while (book.iterator().hasNext()){
//                    DataSnapshot iterator = book.iterator().next();
//                    list_chapitres.clear();
//
//                    name = iterator.child("name").getValue().toString();
//                    cover_link = iterator.child("cover").getValue().toString();
//                    id_book = iterator.child("id").getValue().toString();
//                    last_chapitre = iterator.child("last_chapitre").getValue().toString();
//
//                    Iterable<DataSnapshot> chapitres = iterator.child("list_page").getChildren();
//                    while (chapitres.iterator().hasNext()){
//
//                        DataSnapshot iterator2 = chapitres.iterator().next();
//                        String n = iterator2.child(iterator2.getKey()).getKey().toString();
//                        String[]m = n.split("__");
//                        String num_chapitre = m[1];
//
//                        ArrayList<String> list_pages = new ArrayList<>();
//
//                        Iterable<DataSnapshot> pages = iterator.child("list_page").child(n).getChildren();
//                        while (pages.iterator().hasNext()){
//
//                            DataSnapshot iterator3 = pages.iterator().next();
//                            list_pages.add(iterator3.getValue().toString());
//                        }
//                        list_chapitres.add(new Chapitre(num_chapitre, list_pages));
//
//                    }
//                    activity.list_book.add(new Book(name,cover_link,id_book,last_chapitre,list_chapitres));
//                }
//
////                Toast.makeText(context, "size of list_manga "+Integer.toString(list_manga.size()), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
////        Toast.makeText(context, "size of list_manga "+Integer.toString(), Toast.LENGTH_SHORT).show();
//
//        return activity.list_book;
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<Book> returned_list_manga) {
//
//        Library activity = this.activityReference.get();
//        if (activity == null || activity.isFinishing()) {
//            return;
//        }
//////------------------------------------
//        this.db.clear_manga_table();
//
//////**** Add manga's data in database
//        for (Book item: returned_list_manga) {
//            String manga_name = item.getName();
//            String cover_link = item.getCover_link();
//            String id_manga = item.getId_book();
//            this.db.insertManga(manga_name,cover_link,id_manga);
//        }
//////---------------------------------------------
////        activity.chapter_table_data_list.clear();
//        this.db.clear_chapter_table();
//
//        for (Book item: returned_list_manga) {
//            String id_book = item.getId_book();
//            String list_of_chapter = item.getList_Chapitre_from_one_to_end_as_list();
//            this.db.insertChapter(Integer.parseInt(id_book), list_of_chapter);
//        }
//
//////---------------------------------------------
//        this.db.clear_page_table();
//
//        for (Book item: returned_list_manga) {
//            String id_book = item.getId_book();
//
//            for (Chapitre chapitre: returned_list_manga.get(Integer.parseInt(id_book)).getList_chapitre()) {
//                String num_chapitre = chapitre.getNum_chapitre();
//                String list_page = chapitre.getPages_as_list();
//                this.db.insertPages(Integer.parseInt(id_book), Integer.parseInt(num_chapitre), list_page);
//
//            }
//
//        }
//
//
//        this.db.close();
//        Toast.makeText(context, "update done", Toast.LENGTH_SHORT).show();
//        activity.fill_library(context);
//        this.db.initiate_resume_table();
//        activity.list_book.clear();
//    }
//
//
//}
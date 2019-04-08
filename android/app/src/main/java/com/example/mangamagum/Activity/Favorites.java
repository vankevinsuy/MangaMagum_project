package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mangamagum.Adapter.Favorites_Adapter;
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
import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    private ImageButton go_to_library;
    private ImageButton go_to_search;


    private DataBase dataBase;
    public ArrayList<Book> list_book;

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

        Server_firebase_favorite server_firebase_favorite = new Server_firebase_favorite(getApplicationContext(),this);
        server_firebase_favorite.execute();
    }


    public void fill_favorite(Context context){


        mAdapter = new Favorites_Adapter(list_book, context);
        ((Favorites_Adapter) mAdapter).setOnItemClickListener(new Favorites_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent chosen_manga_activity = new Intent(getApplicationContext() , Chosen_manga.class);

                chosen_manga_activity.putExtra("selected_book" , list_book.get(position));

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


class Server_firebase_favorite {
    Context context;
    private WeakReference<Favorites> activityReference;

    public String name ;
    public String cover_link ;
    public String id_book ;
    public String last_chapitre;
    public String description;
    public DataBase dataBase;




    public DatabaseReference reference;

    public Server_firebase_favorite(Context context, Favorites activity) {
        this.context = context;
        activityReference = new WeakReference<Favorites>(activity);
    }

    public void execute(){

        //remplir la liste
        reference = FirebaseDatabase.getInstance().getReference().child("manga");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Favorites activity = activityReference.get();
                dataBase = new DataBase(context);

                Iterable<DataSnapshot> book = dataSnapshot.getChildren();
                activity.list_book = new ArrayList<>();

                while (book.iterator().hasNext()){
                    DataSnapshot iterator = book.iterator().next();

                    ArrayList<Chapitre> list_chapitres;
                    list_chapitres = new ArrayList<>();

                    name = iterator.child("name").getValue().toString();
                    cover_link = iterator.child("cover").getValue().toString();
                    id_book = iterator.child("id").getValue().toString();
                    last_chapitre = iterator.child("last_chapitre").getValue().toString();
                    description = iterator.child("description").getValue().toString();

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
                    activity.list_book.add(new Book(name,cover_link,id_book,last_chapitre,list_chapitres, description));
                }

                //ne garder que les favoris
                ArrayList<Book> my_fav = new ArrayList<>();

                for(Integer id_fav : dataBase.get_all_favorites()){
                    for(Book book1 : activity.list_book){
                        if (id_fav == Integer.parseInt(book1.getId_book())){
                            my_fav.add(book1);
                        }
                    }
                }

                activity.list_book = my_fav;
                activity.fill_favorite(context);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
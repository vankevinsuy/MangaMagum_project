package com.wallaby.mangamagum.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wallaby.mangamagum.Adapter.Library_Adapter;
import com.wallaby.mangamagum.Model.Book;
import com.wallaby.mangamagum.Model.Chapitre;
import com.wallaby.mangamagum.Model.DataBase;
import com.wallaby.mangamagum.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Library extends AppCompatActivity {

    public ArrayList<Book> list_book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isNetworkConnected(getApplicationContext()) || internetIsConnected()){
            setContentView(R.layout.splash_screen);

            this.list_book = new ArrayList<>();
            Loading_data loading_data = new Loading_data(this,getApplicationContext());
            loading_data.execute();
        }
        else {
            setContentView(R.layout.no_internet_layout);
        }

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


class Loading_data extends AsyncTask<Void,Void,Void>{
    public RecyclerView manga_recycler_view;
    public RecyclerView.Adapter mAdapter;
    Context context;
    private WeakReference<Library> activityReference;
    private ImageButton go_to_favorites;
    private ImageButton go_to_search;




    public Loading_data(Library activity, Context context) {
        this.activityReference = new WeakReference<Library>(activity);
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference reference;

        //remplir la liste
        reference = FirebaseDatabase.getInstance().getReference().child("manga");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name ;
                String cover_link ;
                String id_book ;
                String last_chapitre;
                String description;
                final Library activity = activityReference.get();


                activity.list_book.clear();
                Iterable<DataSnapshot> book = dataSnapshot.getChildren();

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

                        list_chapitres.add(new Chapitre(num_chapitre));
                    }
                    activity.list_book.add(new Book(name,cover_link,id_book,last_chapitre,list_chapitres, description));
                }
                activity.setContentView(R.layout.library);


                go_to_favorites = activity.findViewById(R.id.go_to_favorite);
                go_to_favorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Library activity = activityReference.get();
                        Intent favorite = new Intent(context, Favorites.class);
                        activity.startActivity(favorite);
                    }
                });

                go_to_search = activity.findViewById(R.id.go_to_search_button);
                go_to_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Library activity = activityReference.get();
                        Intent search_layout = new Intent(context, Research.class);
                        activity.startActivity(search_layout);

                    }
                });

                manga_recycler_view = activity.findViewById(R.id.manga_recycler_view);
                manga_recycler_view.setHasFixedSize(false);
                manga_recycler_view.setLayoutManager(new GridLayoutManager(context,3));




                mAdapter = new Library_Adapter(activity.list_book, context);
                ((Library_Adapter) mAdapter).setOnItemClickListener(new Library_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Library activity = activityReference.get();
                        Intent chosen_manga_activity = new Intent(context , Chosen_manga.class);

                        chosen_manga_activity.putExtra("selected_book" , activity.list_book.get(position));
                        activity.startActivity(chosen_manga_activity);
                    }
                });
                manga_recycler_view.setAdapter(mAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return null;
    }
}
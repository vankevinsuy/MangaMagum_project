package com.wallaby.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wallaby.mangamagum.Adapter.Research_adapter;
import com.wallaby.mangamagum.Model.Book;
import com.wallaby.mangamagum.Model.Chapitre;
import com.wallaby.mangamagum.Model.DataBase;
import com.wallaby.mangamagum.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Research extends AppCompatActivity {

    private ImageButton go_to_favorites;
    private ImageButton go_to_library;
    private EditText search_bar;

    private RecyclerView result_recycler_view;
    private RecyclerView.Adapter mAdapter;

    private DataBase dataBase;

    public ArrayList<Book> list_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);


        go_to_favorites = findViewById(R.id.go_to_favorite);
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorite = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorite);
            }
        });

        go_to_library = findViewById(R.id.go_to_library_button);
        go_to_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent library = new Intent(getApplicationContext(), Library.class);
                startActivity(library);
            }
        });

        //remove focus on edit text
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        result_recycler_view = findViewById(R.id.result_recycler_view);
        result_recycler_view.setHasFixedSize(false);
        result_recycler_view.setLayoutManager(new GridLayoutManager(this,3));

        Loading_data_research loading_data_research = new Loading_data_research(this,getApplicationContext());
        loading_data_research.execute();

        search_bar = findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(new TextWatcher() {

            ArrayList<Book>to_display = new ArrayList<>();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                to_display.clear();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                to_display.clear();
                for(Book book_item : list_book){
                    String item = book_item.getName().replaceAll("\\s+","").toUpperCase();
                    charSequence = charSequence.toString().replaceAll("\\s+","").toUpperCase();
                    if(item.contains(charSequence)){
                        to_display.add(book_item);
                    }
                }
                fill_research(getApplicationContext(), to_display);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });


    }




    public void fill_research(Context context, final ArrayList<Book> books){
        final ArrayList<Book> arrayList_book = books;

        mAdapter = new Research_adapter(arrayList_book, context);
        ((Research_adapter) mAdapter).setOnItemClickListener(new Research_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent chosen_manga_activity = new Intent(getApplicationContext() , Chosen_manga.class);

                chosen_manga_activity.putExtra("selected_book" , books.get(position));


                startActivity(chosen_manga_activity);
            }
        });
        result_recycler_view.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        Intent library = new Intent(getApplicationContext() , Library.class);
        startActivity(library);
    }
}





class Loading_data_research extends AsyncTask<Void,Void,Void> {
    private WeakReference<Research> activityReference;
    Context context;

    private ImageButton go_to_favorites;
    private ImageButton go_to_search;


    public RecyclerView manga_recycler_view;
    public RecyclerView.Adapter mAdapter;

    private Boolean internet_check;
    private Boolean network_check;



    public Loading_data_research(Research activity, Context context) {
        this.activityReference = new WeakReference<Research>(activity);
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
                final Research activity = activityReference.get();


                activity.list_book = new ArrayList<>();
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

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return null;
    }
}

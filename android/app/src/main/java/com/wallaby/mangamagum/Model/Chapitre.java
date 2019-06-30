package com.wallaby.mangamagum.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wallaby.mangamagum.Activity.Favorites;

import java.io.Serializable;
import java.util.ArrayList;

public class Chapitre implements Serializable {
    private String num_chapitre;
    public DatabaseReference reference;


    public Chapitre(String num_chapitre) {
        this.num_chapitre = num_chapitre;

    }

    public String getNum_chapitre() {
        return num_chapitre;
    }

    public ArrayList<String> getPages(final String idBook, final int chapterTargeted, final Context context) {

        final ArrayList<String> list_pages = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference().child("manga");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> book = dataSnapshot.getChildren();

                while (book.iterator().hasNext()){
                    DataSnapshot iterator = book.iterator().next();


                    if (iterator.child("id").getValue().toString().equals(idBook)){
                        Toast.makeText(context, idBook +"amamam", Toast.LENGTH_SHORT).show();

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
                                    list_pages.add(iterator3.getValue().toString());
                                }

                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return list_pages;
    }
}

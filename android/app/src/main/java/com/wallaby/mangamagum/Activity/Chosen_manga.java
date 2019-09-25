package com.wallaby.mangamagum.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wallaby.mangamagum.Model.Book;
import com.wallaby.mangamagum.Model.DataBase;
import com.wallaby.mangamagum.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.TreeSet;

public class Chosen_manga extends AppCompatActivity {

    public String selected_manga_id;
    public String selected_manga_name;
    public String selected_manga_cover_link;
    public String selected_manga_last_chapter;
    public String selected_manga_description;

    public Book selected_book;
    public DataBase dataBase;

    public TextView num_chapter;
    public ImageButton go_to_library;
    public ImageButton go_to_favorites;
    public ImageButton go_to_search;


    public int last_chapter;
    public int chapter_target;

    public ImageButton add_remove_favorite;

    public int firstChapitre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyFireBaseAPI myFireBaseAPI = new MyFireBaseAPI(this, getApplicationContext());
        myFireBaseAPI.execute();
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





class MyFireBaseAPI extends AsyncTask<Void,Void,Void> {
    private DatabaseReference reference;
    Context context;
    private WeakReference<Chosen_manga> activityReference;

    public MyFireBaseAPI(Chosen_manga activity, Context context) {
        this.activityReference = new WeakReference<Chosen_manga>(activity);
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        final TreeSet<Integer> listChapitre = new TreeSet<>();
        final Chosen_manga activity = activityReference.get();

        this.reference = FirebaseDatabase.getInstance().getReference().child("manga");

        this.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> book = dataSnapshot.getChildren();


                while (book.iterator().hasNext()) {
                    DataSnapshot iterator = book.iterator().next();

                    Intent i = activity.getIntent();
                    activity.selected_book = (Book)i.getSerializableExtra("selected_book");

                    activity.selected_manga_id = activity.selected_book.getId_book();

                    if (iterator.child("id").getValue().toString().equals(activity.selected_manga_id)) {


                        Iterable<DataSnapshot> chapitres = iterator.child("list_page").getChildren();
                        while (chapitres.iterator().hasNext()) {

                            DataSnapshot iterator2 = chapitres.iterator().next();
                            String n = iterator2.child(iterator2.getKey()).getKey().toString();
                            String[] m = n.split("__");
                            String num_chapitre = m[1];
                            listChapitre.add(Integer.parseInt(num_chapitre));
                        }
                        activity.firstChapitre = listChapitre.first();
                    }

                }

                activity.setContentView(R.layout.activity_chosen_manga);

                activity.go_to_library = activity.findViewById(R.id.go_to_library_button);
                activity.go_to_library.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent library = new Intent(activity.getApplicationContext(), Library.class);
                        activity.startActivity(library);
                    }
                });

                activity.go_to_favorites = activity.findViewById(R.id.go_to_favorite);
                activity.go_to_favorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent favorites = new Intent(activity.getApplicationContext(), Favorites.class);
                        activity.startActivity(favorites);
                    }
                });

                activity.go_to_search = activity.findViewById(R.id.go_to_search_button);
                activity.go_to_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent search_layout = new Intent(activity.getApplicationContext(), Research.class);
                        activity.startActivity(search_layout);
                    }
                });


                Intent i = activity.getIntent();
                activity.selected_book = (Book)i.getSerializableExtra("selected_book");

                activity.selected_manga_id = activity.selected_book.getId_book();
                activity.selected_manga_name = activity.selected_book.getName();
                activity.selected_manga_cover_link = activity.selected_book.getCover_link();
                activity.selected_manga_last_chapter = activity.selected_book.getLast_chapitre();
                activity.selected_manga_description = activity.selected_book.getDescription();

                activity.dataBase = new DataBase(activity.getApplicationContext());
                activity.last_chapter = Integer.parseInt(activity.selected_manga_last_chapter);


                int my_chapter = activity.dataBase.get_chapter_to_resume(Integer.parseInt(activity.selected_manga_id));
                if(my_chapter == 1){
                    my_chapter = activity.firstChapitre;
                }
                activity.chapter_target = my_chapter;

                TextView header_text = activity.findViewById(R.id.header_manga_name);
                ImageView imageView = activity.findViewById(R.id.manga_cover);
                ImageButton plus_button = activity.findViewById(R.id.plus_button);
                plus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.chapter_target = activity.chapter_target + 1;
                        if(activity.chapter_target>=activity.last_chapter){
                            activity.chapter_target = activity.last_chapter;
                        }
                        activity.num_chapter.setText(Integer.toString(activity.chapter_target) + "/" + activity.last_chapter);
                    }
                });
                ImageButton minus_button = activity.findViewById(R.id.minus_button);
                minus_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.chapter_target = activity.chapter_target - 1;
                        if (activity.chapter_target <= activity.firstChapitre){
                            activity.chapter_target = activity.firstChapitre;
                        }
                        activity.num_chapter.setText(Integer.toString(activity.chapter_target) + "/" + activity.last_chapter);
                    }
                });


                TextView description = activity.findViewById(R.id.description);
                TextView resume_from = activity.findViewById(R.id.resume_text);
                resume_from.setText("Resume from chapter : " + Integer.toString(my_chapter));
                SeekBar seekBar = activity.findViewById(R.id.seekBar);
                activity.num_chapter = activity.findViewById(R.id.chapter_num);
                activity.num_chapter.setText(Integer.toString(my_chapter) + "/" + Integer.toString(activity.last_chapter));

                seekBar.setMin(activity.firstChapitre);
                seekBar.setMax(activity.last_chapter);


                //seekBar.setProgress(my_chapter);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                        int progress = i;
                        activity.chapter_target = progress;


                        if (activity.chapter_target <= activity.firstChapitre){
                            activity.chapter_target = activity.firstChapitre;
                        }
                        activity.num_chapter.setText(Integer.toString(activity.chapter_target) + "/" + activity.last_chapter);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                header_text.setText(activity.selected_manga_name);
                Picasso.with(activity.getApplicationContext()).load(activity.selected_manga_cover_link).into(imageView);
                description.setText(activity.selected_manga_description);
                description.setMovementMethod(new ScrollingMovementMethod());

                //click for reading
                ImageButton play_button = activity.findViewById(R.id.play_button);
                play_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.dataBase.update_my_chapter(activity.chapter_target, activity.selected_manga_id);
                        Intent reading_activity = new Intent(activity.getApplicationContext(), Reading.class);

                        reading_activity.putExtra("selected_book" , activity.selected_book);
                        reading_activity.putExtra("chapter_target" , activity.chapter_target);

                        activity.startActivity(reading_activity);
                    }
                });

                activity.add_remove_favorite = activity.findViewById(R.id.add_remove_favorite);

                if (activity.dataBase.is_one_of_favorite(Integer.parseInt(activity.selected_manga_id))){
                    activity.add_remove_favorite.setBackgroundResource(R.drawable.favorite_selected);
                }
                else {
                    activity.add_remove_favorite.setBackgroundResource(R.drawable.favorite_not_selected);
                }
                activity.add_remove_favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (activity.dataBase.is_one_of_favorite(Integer.parseInt(activity.selected_manga_id)) == false){
                            activity.dataBase.add_favorite(Integer.parseInt(activity.selected_manga_id));
                            activity.add_remove_favorite.setBackgroundResource(R.drawable.favorite_selected);
                            activity.dataBase.close();
                        }
                        else {
                            activity.dataBase.remove_favorite(Integer.parseInt(activity.selected_manga_id));
                            activity.add_remove_favorite.setBackgroundResource(R.drawable.favorite_not_selected);
                            activity.dataBase.close();
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }
}





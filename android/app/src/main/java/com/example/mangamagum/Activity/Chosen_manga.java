package com.example.mangamagum.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;
import com.squareup.picasso.Picasso;

public class Chosen_manga extends AppCompatActivity {

    public String selected_manga_id;
    public String selected_manga_name;
    public String selected_manga_cover_link;
    public String selected_manga_last_chapter;
    public String selected_manga_description;

    public Book selected_book;
    private DataBase dataBase;

    private TextView num_chapter;
    private ImageButton go_to_library;
    private ImageButton go_to_favorites;
    private ImageButton go_to_search;


    private int last_chapter;
    private int chapter_target;

    private ImageButton add_remove_favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_manga);

        go_to_library = findViewById(R.id.go_to_library_button);
        go_to_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent library = new Intent(getApplicationContext(), Library.class);
                startActivity(library);
            }
        });

        go_to_favorites = findViewById(R.id.go_to_favorite);
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
            }
        });

        go_to_search = findViewById(R.id.go_to_search_button);
        go_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_layout = new Intent(getApplicationContext(), Research.class);
                startActivity(search_layout);
            }
        });


        Intent i = getIntent();
        selected_book = (Book)i.getSerializableExtra("selected_book");

        selected_manga_id = selected_book.getId_book();
        selected_manga_name = selected_book.getName();
        selected_manga_cover_link = selected_book.getCover_link();
        selected_manga_last_chapter = selected_book.getLast_chapitre();
        selected_manga_description = selected_book.getDescription();

        this.dataBase = new DataBase(getApplicationContext());
        last_chapter = Integer.parseInt(selected_manga_last_chapter);


        int my_chapter = dataBase.get_chapter_to_resume(Integer.parseInt(selected_manga_id));

        chapter_target = my_chapter;

        TextView header_text = findViewById(R.id.header_manga_name);
        ImageView imageView = findViewById(R.id.manga_cover);
        ImageButton plus_button = findViewById(R.id.plus_button);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapter_target = chapter_target + 1;
                if(chapter_target>=last_chapter){
                    chapter_target = last_chapter;
                }
                num_chapter.setText(Integer.toString(chapter_target) + "/" + last_chapter);
            }
        });
        ImageButton minus_button = findViewById(R.id.minus_button);
        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapter_target = chapter_target - 1;
                if (chapter_target <=0){
                    chapter_target = 1;
                }
                num_chapter.setText(Integer.toString(chapter_target) + "/" + last_chapter);
            }
        });


        TextView description = findViewById(R.id.description);
        TextView resume_from = findViewById(R.id.resume_text);
        resume_from.setText("Resume from chapter : " + Integer.toString(my_chapter));
        SeekBar seekBar = findViewById(R.id.seekBar);
        num_chapter = findViewById(R.id.chapter_num);
        num_chapter.setText(Integer.toString(my_chapter) + "/" + Integer.toString(last_chapter));

        seekBar.setMax(last_chapter);
        seekBar.setProgress(my_chapter);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                int progress = i;
                chapter_target = progress;

                if (chapter_target <= 0){
                    chapter_target = 1;
                }
                num_chapter.setText(Integer.toString(chapter_target) + "/" + last_chapter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        header_text.setText(selected_manga_name);
        Picasso.with(this).load(selected_manga_cover_link).into(imageView);
        description.setText(selected_manga_description);
        description.setMovementMethod(new ScrollingMovementMethod());

//click for reading
        ImageButton play_button = findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase.update_my_chapter(chapter_target, selected_manga_id);
                Intent reading_activity = new Intent(getApplicationContext(), Reading.class);

                reading_activity.putExtra("selected_book" , selected_book);
                reading_activity.putExtra("chapter_target" , chapter_target);

                startActivity(reading_activity);
            }
        });

        add_remove_favorite = findViewById(R.id.add_remove_favorite);

        if (dataBase.is_one_of_favorite(Integer.parseInt(selected_manga_id))){
            add_remove_favorite.setBackgroundResource(R.drawable.favorite_selected);
        }
        else {
            add_remove_favorite.setBackgroundResource(R.drawable.favorite_not_selected);
        }
        add_remove_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBase.is_one_of_favorite(Integer.parseInt(selected_manga_id)) == false){
                    dataBase.add_favorite(Integer.parseInt(selected_manga_id));
                    add_remove_favorite.setBackgroundResource(R.drawable.favorite_selected);
                    dataBase.close();
                }
                else {
                    dataBase.remove_favorite(Integer.parseInt(selected_manga_id));
                    add_remove_favorite.setBackgroundResource(R.drawable.favorite_not_selected);
                    dataBase.close();
                }
            }
        });
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


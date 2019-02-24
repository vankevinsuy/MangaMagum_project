package com.example.mangamagum.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;
import com.squareup.picasso.Picasso;

public class Chosen_manga extends AppCompatActivity {

    private String selected_manga_id;
    private String selected_manga_name;
    private String selected_manga_cover_link;

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
                finish();
            }
        });

        go_to_favorites = findViewById(R.id.go_to_favorite);
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorites = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorites);
                finish();
            }
        });

        go_to_search = findViewById(R.id.go_to_search_button);
        go_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_layout = new Intent(getApplicationContext(), Research.class);
                startActivity(search_layout);
                finish();
            }
        });

        this.selected_manga_id = getIntent().getExtras().getString("id_book");
        this.selected_manga_name = getIntent().getExtras().getString("manga_name");
        this.selected_manga_cover_link = getIntent().getExtras().getString("cover_link");

        this.dataBase = new DataBase(getApplicationContext());
        last_chapter = this.dataBase.get_last_chapter(this.selected_manga_id.toString());


        int my_chapter = dataBase.get_chapter_to_resume(Integer.parseInt(selected_manga_id));
//        -----------------
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
        seekBar.setMax(last_chapter/10);
        seekBar.setProgress(my_chapter/10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                int progress = i*10;
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


        header_text.setText(
                selected_manga_name);
        Picasso.with(this).load(selected_manga_cover_link).into(imageView);
        description.setText("Donec eget est vel purus mollis iaculis. In tristique pellentesque suscipit. Mauris vitae facilisis erat. Maecenas ut tempor arcu, vitae varius metus. Morbi laoreet, ante feugiat convallis imperdiet, erat nisi mattis ex, id pellentesque nibh elit a augue. Vivamus dignissim pellentesque ante, ut semper ante facilisis bibendum. Donec sed arcu non velit convallis vulputate et tristique nisi. Proin dictum, eros sit amet blandit tempor, leo dolor blandit ante, id cursus massa velit eu lorem. Nam ullamcorper maximus dui in vestibulum. Nulla et blandit odio, molestie malesuada dui. Integer semper feugiat tellus vel tristique. Aliquam eleifend, mi ut pretium ultrices, augue dui sollicitudin arcu, sit amet rutrum risus arcu non mauris. In lectus risus, dignissim ut laoreet eu, hendrerit a turpis. Nullam vel augue finibus eros fermentum fermentum. Suspendisse cursus tortor id justo viverra aliquet.");
        description.setMovementMethod(new ScrollingMovementMethod());


        ImageButton play_button = findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase.update_my_chapter(chapter_target, selected_manga_id);
                Intent reading_activity = new Intent(getApplicationContext(), Reading.class);
                reading_activity.putExtra("id_book" , selected_manga_id);
                reading_activity.putExtra("chapter", Integer.toString(chapter_target));
                reading_activity.putExtra("manga_name", selected_manga_name);
                reading_activity.putExtra("cover_link", selected_manga_cover_link);

                startActivity(reading_activity);
                finish();
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
                    dataBase.remove_favorite(Integer.parseInt(selected_manga_id), getApplicationContext());
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

        startActivity(library);
        finish();
    }

}


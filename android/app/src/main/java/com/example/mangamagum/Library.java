package com.example.mangamagum;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Library extends AppCompatActivity {

    private int i;
    private ImageButton go_to_category;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);

        go_to_category = findViewById(R.id.go_to_category_button);
        test = findViewById(R.id.test);

        go_to_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i +1;
                test.setText(Integer.toString(i));
            }
        });

    }
}

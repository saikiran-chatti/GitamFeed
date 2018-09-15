package com.example.sudhaseshu.gitamfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Read_Post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read__post);

        String title = getIntent().getStringExtra("title");

        String content = getIntent().getStringExtra("content");

        String day = getIntent().getStringExtra("day");

        String month = getIntent().getStringExtra("month");

        String bookmark = getIntent().getStringExtra("bookmark");

        String likes = getIntent().getStringExtra("likes");


        Log.i("det",title);
        TextView title_text = findViewById(R.id.title_read);
        title_text.setText(title);

        title_text = findViewById(R.id.problem_content_read);
        title_text.setText(content);

        title_text = findViewById(R.id.time_read);
        title_text.setText(month);

        title_text = findViewById(R.id.date_read);
        title_text.setText(day);

    }
}

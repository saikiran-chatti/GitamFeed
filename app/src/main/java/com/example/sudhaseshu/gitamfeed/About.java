package com.example.sudhaseshu.gitamfeed;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class About extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(topToolBar);
        topToolBar.setTitleTextColor(Color.BLACK);
    }
}

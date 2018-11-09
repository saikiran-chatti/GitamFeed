package com.example.sudhaseshu.gitamfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoreInfo extends AppCompatActivity {

    TextView descrpt;
    ImageView phtoo;
    TextView hea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_more_info);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.white));

        phtoo = findViewById(R.id.phto);
        Picasso.get().load(getIntent().getStringExtra("photo")).into(phtoo);
        descrpt = findViewById(R.id.more);
        descrpt.setText(getIntent().getStringExtra("descrip"));
        hea = findViewById(R.id.head);
        hea.setText(getIntent().getStringExtra("head"));

    }
}

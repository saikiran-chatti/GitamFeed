package com.example.sudhaseshu.gitamfeed;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

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

        Toolbar topToolBar = findViewById(R.id.toolbar2_about);
        setSupportActionBar(topToolBar);
        topToolBar.setTitleTextColor(Color.BLACK);

        TextView t = findViewById(R.id.email_about);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code for opening mail when user clicks the email TextView.

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "jatedevelopers@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });

        // Highlighting the text
        String sourceString = "Email us at:"+ "<b>" + "\t"+" jatedevelopers@gmail.com" + "</b> ";
        SpannableString string = new SpannableString(Html.fromHtml(sourceString));
        string.setSpan(new UnderlineSpan(), 12, string.length(), 0);
        t.setText(string);
    }
}

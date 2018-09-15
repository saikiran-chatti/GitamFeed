package com.example.sudhaseshu.gitamfeed;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        Date date = new Date();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        TextView mDateDisplay = findViewById(R.id.date);
        mDateDisplay.setText(monthString+" "+day);


        TextView mTimeDisplay = findViewById(R.id.time);
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(date);
        mTimeDisplay.setText(currentDateTimeString);


        ImageButton send = findViewById(R.id.send_post);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText content = findViewById(R.id.problem_content);
                final String content_string =  content.getText().toString();

                TextView title = findViewById(R.id.title);
                String title_string = title.getText().toString();

                Log.i("app",content_string);
                addPost(content_string,title_string);
            }
        });
    }

    private void addPost(final String data, final String title_string) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final CollectionReference posts = db.collection("Posts");

        Date date = new Date();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        final String dateofday          = (String) DateFormat.format("dd",   date); // 20
        final String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        final String currentDateTimeString = sdf.format(date);
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        Log.i("app",title_string);

        final PostItems post = new PostItems(mAuth.getUid(),posts.getId(),currentDateTimeString,dateofday,monthString,title_string,data,"0");

        posts.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"Created",Toast.LENGTH_LONG).show();
                Handler handler = new Handler();

                Log.i("det",documentReference.getId());

                post.setPid(documentReference.getId());
                documentReference.update("pid",post.getPid());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 500);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Mingindhi",Toast.LENGTH_SHORT).show();
            }
        });

    }
}

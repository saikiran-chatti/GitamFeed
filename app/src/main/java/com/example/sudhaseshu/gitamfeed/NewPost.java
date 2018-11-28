package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewPost extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send_menu) {
            if (haveNetworkConnection()) {
                Date date = new Date();
                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day = (String) DateFormat.format("dd", date); // 20
                String monthString = (String) DateFormat.format("MMM", date); // Jun
                String monthNumber = (String) DateFormat.format("MM", date); // 06
                String year = (String) DateFormat.format("yyyy", date); // 2013

                TextView mDateDisplay = findViewById(R.id.date);
                mDateDisplay.setText(monthString + " " + day);


                TextView mTimeDisplay = findViewById(R.id.time);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(date);
                mTimeDisplay.setText(currentDateTimeString);

                EditText content = findViewById(R.id.problem_content);
                final String content_string = content.getText().toString();

                TextView title = findViewById(R.id.title);
                String title_string = title.getText().toString();

                Log.i("app", content_string);
                if(title_string.length() > 0 && title_string.length() <15)
                    addPost(content_string, title_string);
                else
                    Toast.makeText(getApplicationContext(),"Title should have max 15 characters",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Network Required",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        EditText e1 = findViewById(R.id.title);
        EditText e2 = findViewById(R.id.problem_content);

        e1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        e2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        Date date = new Date();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String day = (String) DateFormat.format("dd", date); // 20
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String year = (String) DateFormat.format("yyyy", date); // 2013

        TextView mDateDisplay = findViewById(R.id.date);
        mDateDisplay.setText(monthString + " " + day);


        TextView mTimeDisplay = findViewById(R.id.time);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(date);
        mTimeDisplay.setText(currentDateTimeString);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void addPost(final String data, final String title_string) {

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseFirestore userposts = FirebaseFirestore.getInstance();

        final CollectionReference posts = db.collection("Posts");

        Date date = new Date();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        final String dateofday = (String) DateFormat.format("dd", date); // 20
        final String monthString = (String) DateFormat.format("MMM", date); // Jun
        String monthNumber = (String) DateFormat.format("MM", date); // 06
        String year = (String) DateFormat.format("yyyy", date); // 2013

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String currentDateTimeString = sdf.format(date);
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        Log.i("app", title_string);

        final PostItems post = new PostItems(mAuth.getUid(), posts.getId(), currentDateTimeString, dateofday, monthString, title_string, data, "0");

        posts.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();

                Log.i("det", documentReference.getId());

                post.setPid(documentReference.getId());

                //Saving the post id in the user's post..(User's record)
                userposts.collection("Users/"+auth.getUid()+"/posts").document(documentReference.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){

                            Map<String, Object> mark = new HashMap<>();
                            mark.put("timestamp", FieldValue.serverTimestamp());

                            userposts.collection("Users/" + auth.getUid() + "/posts").document(documentReference.getId()).set(mark);
                        }
                    }
                });

                documentReference.update("pid", post.getPid());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 500);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Mingindhi", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

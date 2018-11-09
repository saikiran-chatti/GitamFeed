package com.example.sudhaseshu.gitamfeed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Read_Post extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.like_menu){
            Log.i("app","Liked_menu");
            Toast.makeText(getApplicationContext(),"Liked",Toast.LENGTH_SHORT).show();
            final String id = getIntent().getStringExtra("pid");
            final FirebaseFirestore likeDatabase = FirebaseFirestore.getInstance();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();

            likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (!task.getResult().exists()) {

                        Map<String, Object> likesMap = new HashMap<>();
                        likesMap.put("timestamp", FieldValue.serverTimestamp());

                        db.collection("Posts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                PostItems p = documentSnapshot.toObject(PostItems.class);
                                p.setLikes(String.valueOf(Integer.valueOf(p.getLikes())+1));

                                db.collection("Posts").document(id).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("app","Liked");
                                    }
                                });
                            }
                        });

                        likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).set(likesMap);

                    } else {

                        db.collection("Posts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                PostItems p = documentSnapshot.toObject(PostItems.class);
                                p.setLikes(String.valueOf(Integer.valueOf(p.getLikes())-1));

                                db.collection("Posts").document(id).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("app","Liked");
                                    }
                                });
                            }
                        });
                        likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).delete();

                    }

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read__post);

        Toolbar toolbar2 = findViewById(R.id.toolbar2_read);
        setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar2.setTitle("");
        toolbar2.setSubtitle("");

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

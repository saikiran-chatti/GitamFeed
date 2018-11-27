package com.example.sudhaseshu.gitamfeed;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Read_Post extends AppCompatActivity {

    final boolean[] verify = new boolean[1];
    Menu g_menu;
    String g_pid;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_post, menu);
        g_menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.like_menu) {
            Log.i("app", "Liked_menu");
            Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
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
                                if (documentSnapshot.exists()) {
                                    PostItems p = documentSnapshot.toObject(PostItems.class);
                                    p.setLikes(String.valueOf(Integer.valueOf(p.getLikes()) + 1));

                                    db.collection("Posts").document(id).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("app", "Liked");
                                        }
                                    });
                                }
                            }
                        });

                        likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).set(likesMap);

                    } else {

                        db.collection("Posts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()) {
                                    PostItems p = documentSnapshot.toObject(PostItems.class);
                                    p.setLikes(String.valueOf(Integer.valueOf(p.getLikes()) - 1));

                                    db.collection("Posts").document(id).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("app", "Liked");
                                        }
                                    });
                                }
                            }
                        });
                        likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).delete();

                    }

                }
            });
        }

        if (item.getItemId() == R.id.delete) {
            // Code to delete the file posted..
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("posts").document(g_pid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (!documentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = documentSnapshots.getDocuments();

                        for (DocumentSnapshot d: list) {
                            FirebaseFirestore temp = FirebaseFirestore.getInstance();
                            temp.collection("Users").document(d.getId()).collection("PostId's").document(g_pid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i("app","Deleted from bookmarks");
                                }
                            });
                        }
                    }
                }
            });

            db.collection("Posts").document(g_pid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("app","Deleted from Posts database");
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

        final String pid = getIntent().getStringExtra("pid");

        String title = getIntent().getStringExtra("title");

        String content = getIntent().getStringExtra("content");

        String day = getIntent().getStringExtra("day");

        String month = getIntent().getStringExtra("month");

        String bookmark = getIntent().getStringExtra("bookmark");

        String likes = getIntent().getStringExtra("likes");

        Log.i("det", title);
        TextView title_text = findViewById(R.id.title_read);
        title_text.setText(title);

        title_text = findViewById(R.id.problem_content_read);
        title_text.setText(content);

        title_text = findViewById(R.id.time_read);
        title_text.setText(month);

        title_text = findViewById(R.id.date_read);
        title_text.setText(day);

        ImageButton back_button = findViewById(R.id.back_read);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        FirebaseFirestore.getInstance().collection("Users/").document(FirebaseAuth.getInstance().getUid()).collection("posts").document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    verify[0] = true;
                    MenuItem menuItem = g_menu.findItem(R.id.delete);
                    menuItem.setVisible(true);
                    g_pid = pid;
                }
                Toast.makeText(getApplicationContext()," "+verify[0],Toast.LENGTH_SHORT).show();
            }
        });

    }
}

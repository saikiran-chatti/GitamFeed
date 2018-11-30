package com.gfeed.sudhaseshu.gitamfeed;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Yourposts extends AppCompatActivity {

    FirebaseFirestore db;
    private List<PostItems> yourpostsItemsList;
    PostItemsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourposts);

        Toolbar topToolBar = findViewById(R.id.toolbar11);
        setSupportActionBar(topToolBar);
        topToolBar.setTitleTextColor(Color.BLACK);
        yourpostsItemsList = new ArrayList<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.yourposts_recyclerview);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        Log.i("app","Reacheed Bookmark");

        adapter = new PostItemsAdapter(getApplicationContext(), yourpostsItemsList);

        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(Objects.requireNonNull(mAuth.getUid())).collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (!documentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    Log.i("app"," "+documents.size()+"size of retrieved document");

                    for (DocumentSnapshot d : documents) {
                        Log.i("app","uid "+d.getId());
                        db.collection("Posts").document(d.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.i("app", "Bookmark retrieved");
                                PostItems p = documentSnapshot.toObject(PostItems.class);
                                p.setId(documentSnapshot.getId());
                                Log.i("app", p.getId());
                                yourpostsItemsList.add(p);
                                Log.i("app",yourpostsItemsList.size()+" lkfasjdflasdj");
                                adapter.notifyDataSetChanged();
                                adapter = new PostItemsAdapter(getApplicationContext(), yourpostsItemsList);
                                recyclerView.setAdapter(adapter);
                            }
                        });

                    }

                }
            }
        });
    }
}

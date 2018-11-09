package com.example.sudhaseshu.gitamfeed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    FirebaseFirestore db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private List<PostItems> bookmarkItemsList;
    PostItemsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        bookmarkItemsList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.bookmark_recyclerview);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        Log.i("app","Reacheed Bookmark");

        adapter = new PostItemsAdapter(getApplicationContext(), bookmarkItemsList);

        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(mAuth.getUid()).collection("PostId's").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (!documentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                    Log.i("app"," "+documents.size()+"size of retrieved document");

                    for (DocumentSnapshot d : documents) {
                        Log.i("app","uid"+d.getId());
                        db.collection("Posts").document(d.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.i("app", "Bookmark retrieved");
                                PostItems p = documentSnapshot.toObject(PostItems.class);
                                p.setId(documentSnapshot.getId());
                                Log.i("app",p.getId().toString());
                                bookmarkItemsList.add(p);
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }
                    adapter = new PostItemsAdapter(getApplicationContext(), bookmarkItemsList);
                    Log.i("app",bookmarkItemsList.toString()+" lkfasjdflasdj");
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}

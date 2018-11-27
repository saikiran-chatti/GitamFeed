package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarkActivity extends AppCompatActivity {

    FirebaseFirestore db;
    // TODO: Rename and change types of parameters

    private List<PostItems> bookmarkItemsList;
    PostItemsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar topToolBar = findViewById(R.id.toolbar10);
        setSupportActionBar(topToolBar);
        topToolBar.setTitleTextColor(Color.BLACK);

        if(haveNetworkConnection()) {
            bookmarkItemsList = new ArrayList<>();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            recyclerView = findViewById(R.id.bookmark_recyclerview);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);
            Log.i("app", "Reacheed Bookmark");

            adapter = new PostItemsAdapter(getApplicationContext(), bookmarkItemsList);

            db = FirebaseFirestore.getInstance();

            db.collection("Users").document(Objects.requireNonNull(mAuth.getUid())).collection("PostId's").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (!documentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                        Log.i("app", " " + documents.size() + "size of retrieved document");

                        for (DocumentSnapshot d : documents) {
                            Log.i("app", "uid " + d.getId());
                            db.collection("Posts").document(d.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.i("app", "Bookmark retrieved");
                                    PostItems p = documentSnapshot.toObject(PostItems.class);
                                    p.setId(documentSnapshot.getId());
                                    Log.i("app", p.getId());
                                    bookmarkItemsList.add(p);
                                    Log.i("app", bookmarkItemsList.size() + " lkfasjdflasdj");
                                    adapter.notifyDataSetChanged();
                                    adapter = new PostItemsAdapter(getApplicationContext(), bookmarkItemsList);
                                    recyclerView.setAdapter(adapter);
                                }
                            });

                        }

                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Network Required",Toast.LENGTH_SHORT).show();
        }
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

package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class YourPostsItemAdapter  extends  RecyclerView.Adapter<YourPostsItemAdapter.YourPostItemsViewHolder>{

    private Context context;
    private List<PostItems> items;
    private FirebaseFirestore likeDatabase;
    private FirebaseFirestore db;

    YourPostsItemAdapter(Context context, List<PostItems> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public YourPostItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.cardview,viewGroup,false);

        return new YourPostItemsViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull YourPostItemsViewHolder yourPostItemsViewHolder, int i) {
        final PostItems post = items.get(i);

        final String id = post.getId();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        likeDatabase = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        YourPostItemsViewHolder.p_likecount.setText(post.getLikes());
        YourPostItemsViewHolder.p_month.setText(post.getPost_month());
        YourPostItemsViewHolder.p_date.setText(post.getPost_date());
        YourPostItemsViewHolder.p_post.setText(post.getPost_content());
        YourPostItemsViewHolder.title.setText(post.getTitle());

        try {

            //Get Likes Count
            likeDatabase.collection("Posts/" + id + "/Likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(!documentSnapshots.isEmpty()){

                        int count = documentSnapshots.size();

                        YourPostItemsViewHolder.updateLikesCount(count);
                        Log.i("app",String.valueOf(count));

                    } else {
                        YourPostItemsViewHolder.updateLikesCount(0);
                    }

                }
            });

            // Check bookmarks icon whether it is filled or not while Displaying
            likeDatabase.collection("Users/" + mAuth.getUid()+"/PostId's").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (!task.getResult().exists()) {
                        YourPostItemsViewHolder.bookmark.setImageResource(R.drawable.bookmarknofill); // Changing the bookmark icon when the post is bookmarked
                    }
                    else{
                        YourPostItemsViewHolder.bookmark.setImageResource(R.drawable.bookmarkfill); // Changing the bookmark icon when the post is removed from bookmarks.
                    }
                }
            });

            // Check like icon whether it is filled or not while displaying in Discussions
            likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (!task.getResult().exists()) {

                        YourPostItemsViewHolder.like_button.setImageResource(R.drawable.like1nofill); // Changing the like icon when the post is liked

                    } else {

                        YourPostItemsViewHolder.like_button.setImageResource(R.drawable.like1fill);

                    }

                }
            });



            //When bookmark button is clicked
            YourPostItemsViewHolder.bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("app",""+mAuth.getUid());
                    likeDatabase.collection("Users/" + mAuth.getUid()+"/PostId's").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                YourPostItemsViewHolder.bookmark.setImageResource(R.drawable.bookmarkfill); // Changing the bookmark icon when the post is bookmarked

                                Map<String, Object> mark = new HashMap<>();
                                mark.put("timestamp", FieldValue.serverTimestamp());

                                likeDatabase.collection("Users/" + mAuth.getUid() + "/PostId's").document(id).set(mark);
                            }
                            else{
                                YourPostItemsViewHolder.bookmark.setImageResource(R.drawable.bookmarknofill); // Changing the bookmark icon when the post is removed from bookmarks.
                                likeDatabase.collection("Users/"+mAuth.getUid()+"/PostId's").document(id).delete();
                            }
                        }
                    });
                }
            });

            //When like button is clicked
            YourPostItemsViewHolder.like_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {

                                YourPostItemsViewHolder.like_button.setImageResource(R.drawable.like1fill); // Changing the like icon when the post is liked

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

                                YourPostItemsViewHolder.like_button.setImageResource(R.drawable.like1nofill);

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

            });

            YourPostItemsViewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent intent = new Intent(context,Read_Post.class);
                        Toast.makeText(context,"name:"+context,Toast.LENGTH_SHORT).show();
                        intent.putExtra("title",post.getTitle());
                        intent.putExtra("content",post.getPost_content());
                        intent.putExtra("day",post.getPost_date());
                        intent.putExtra("month",post.getPost_month());
                        intent.putExtra("likes",post.getLikes());
                        intent.putExtra("pid",post.getPid());

                        Log.i("test2",post.getPost_content());
                        context.startActivity(intent);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            YourPostItemsViewHolder.p_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent intent = new Intent(context,Read_Post.class);
                        intent.putExtra("title",post.getTitle());
                        intent.putExtra("content",post.getPost_content());
                        intent.putExtra("day",post.getPost_date());
                        intent.putExtra("month",post.getPost_month());
                        intent.putExtra("likes",post.getLikes());
                        intent.putExtra("pid",post.getPid());
                        Log.i("test2",post.getPost_content());
                        context.startActivity(intent);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            YourPostItemsViewHolder.p_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent intent = new Intent(context,Read_Post.class);
                        intent.putExtra("title",post.getTitle());
                        intent.putExtra("content",post.getPost_content());
                        intent.putExtra("day",post.getPost_date());
                        intent.putExtra("month",post.getPost_month());
                        intent.putExtra("likes",post.getLikes());
                        intent.putExtra("pid",post.getPid());

                        Log.i("test2",post.getPost_content());
                        context.startActivity(intent);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            YourPostItemsViewHolder.p_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Intent intent = new Intent(context,Read_Post.class);
                        intent.putExtra("title",post.getTitle());
                        intent.putExtra("content",post.getPost_content());
                        intent.putExtra("day",post.getPost_date());
                        intent.putExtra("month",post.getPost_month());
                        intent.putExtra("likes",post.getLikes());
                        intent.putExtra("pid",post.getPid());

                        Log.i("test2",post.getPost_content());
                        context.startActivity(intent);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

     static class YourPostItemsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        static TextView p_date;
         static TextView p_month;
         static TextView p_post;
         static TextView title;
         static TextView p_likecount;
        static ImageButton like_button;
         static ImageButton bookmark;

        YourPostItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            title = mView.findViewById(R.id.Title_problem);
            bookmark = mView.findViewById(R.id.bookmark);
            like_button = mView.findViewById(R.id.like);
            p_date = mView.findViewById(R.id.day);
            p_month = mView.findViewById(R.id.month);
            p_post = mView.findViewById(R.id.problem);
            p_likecount = mView.findViewById(R.id.like_count);

        }

        public static void updateLikesCount(int count){
            p_likecount.setText(String.valueOf(count));
        }
    }
}

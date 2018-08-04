package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class PostItemsAdapter  extends  RecyclerView.Adapter<PostItemsAdapter.PostItemsViewHolder>{

    private Context context;
    private List<PostItems> items;
    private boolean postlike = false;
    private FirebaseFirestore likeDatabase;
     PostItemsAdapter(Context context, List<PostItems> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public PostItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i("app","View created");
        View view = LayoutInflater.from(context).inflate(R.layout.cardview,viewGroup,false);

        return new PostItemsViewHolder(
                LayoutInflater.from(context).inflate(R.layout.cardview, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final PostItemsViewHolder postItemsViewHolder, int i) {
        final PostItems post = items.get(i);

        final String id = post.getId();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        likeDatabase = FirebaseFirestore.getInstance();

        postItemsViewHolder.like_button.setText(post.getLikes());
        postItemsViewHolder.p_month.setText(post.getPost_month());
        postItemsViewHolder.p_date.setText(post.getPost_date());
        postItemsViewHolder.p_post.setText(post.getPost_content());


        try {

            //Get Likes Count
            likeDatabase.collection("Posts/" + id + "/Likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(!documentSnapshots.isEmpty()){

                        int count = documentSnapshots.size();

                        postItemsViewHolder.updateLikesCount(count);
                        Log.i("app",String.valueOf(count));

                    } else {
                        postItemsViewHolder.updateLikesCount(0);
                    }

                }
            });



            postItemsViewHolder.like_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {

                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).set(likesMap);

                            } else {

                                likeDatabase.collection("Posts/" + id + "/Likes").document(mAuth.getUid()).delete();

                            }

                        }
                    });

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

     class PostItemsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView p_date,p_month,p_post;
        Button like_button;

         PostItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            like_button = mView.findViewById(R.id.like);
            p_date = mView.findViewById(R.id.day);
            p_month = mView.findViewById(R.id.month);
            p_post = mView.findViewById(R.id.problem);

        }

         public void updateLikesCount(int count){

             like_button.setText(String.valueOf(count));
         }
    }
}

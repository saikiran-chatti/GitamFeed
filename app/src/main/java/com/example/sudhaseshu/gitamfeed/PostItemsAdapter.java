package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PostItemsAdapter  extends  RecyclerView.Adapter<PostItemsAdapter.PostItemsViewHolder>{

    private Context context;
    private List<PostItems> items;

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
    public void onBindViewHolder(@NonNull PostItemsViewHolder postItemsViewHolder, int i) {
        PostItems post = items.get(i);

        postItemsViewHolder.p_month.setText(post.getPost_month());
        postItemsViewHolder.p_date.setText(post.getPost_date());
        postItemsViewHolder.p_post.setText(post.getPost_content());
        Log.i("app","The date is "+postItemsViewHolder.p_date.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

     class PostItemsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView p_date,p_month,p_post;

         PostItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            p_date = mView.findViewById(R.id.day);
            p_month = mView.findViewById(R.id.month);
            p_post = mView.findViewById(R.id.problem);


        }
    }
}

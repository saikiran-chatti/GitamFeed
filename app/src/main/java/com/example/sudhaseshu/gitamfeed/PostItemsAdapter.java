package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PostItemsAdapter  extends  RecyclerView.Adapter<PostItemsAdapter.PostItemsViewHolder>{

    private Context context;
    private List<PostItems> items;

    public PostItemsAdapter(Context context, List<PostItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PostItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PostItemsViewHolder postItemsViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PostItemsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PostItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}

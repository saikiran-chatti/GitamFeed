package com.example.sudhaseshu.gitamfeed;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class NewsItemsAdapter extends RecyclerView.ViewHolder {

    View mview;

    public NewsItemsAdapter(View view) {
        super(view);
        mview = view;
    }

    public void setDetails(Context des, String description, String image) {
        TextView descr = mview.findViewById(R.id.desc);
        ImageView pht = mview.findViewById(R.id.image);

        descr.setText(description);
        Picasso.get().load(image).into(pht);
    }
}

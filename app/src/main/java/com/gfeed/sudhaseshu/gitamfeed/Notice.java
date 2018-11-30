package com.gfeed.sudhaseshu.gitamfeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Notice.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Notice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notice extends Fragment {

    private RecyclerView news_view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public Notice() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notice.
     */

    public static Notice newInstance(String param1, String param2) {
        Notice fragment = new Notice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            String mParam1 = getArguments().getString(ARG_PARAM1);
//            String mParam2 = getArguments().getString(ARG_PARAM2);
//
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("app","Reached");
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
            news_view = view.findViewById(R.id.news_view);
            news_view.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            news_view.setLayoutManager(mLayoutManager);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Data");

        return view;
    }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<NewsModel,NewsItemsAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NewsModel, NewsItemsAdapter>(NewsModel.class,R.layout.news_list_item, NewsItemsAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(final NewsItemsAdapter viewHolder, final NewsModel model, final int position) {
                viewHolder.setDetails(getActivity(),model.getHeading(),model.getImage());
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Notice.this.getActivity(),MoreInfo.class);
                        intent.putExtra("photo",model.image);
                        intent.putExtra("descrip",model.description);
                        intent.putExtra("head",model.heading);
                        startActivity(intent);
                    }
                });

                viewHolder.mview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, model.description);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        return true;
                    }
                });

            }
        };
        news_view.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}
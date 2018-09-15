package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Discussion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Discussion#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Discussion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String mTime,mDate,mData;
    private RecyclerView recyclerView;

    PostItemsAdapter adapter;
    StorageReference reference;
    private List<PostItems> postItemsList;

    FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;
    FirebaseFirestore db;
    private LinearLayoutManager mLayoutManager;

    public Discussion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Discussion.
     */
    // TODO: Rename and change types and number of parameters
    public static Discussion newInstance(String param1, String param2) {
        Discussion fragment = new Discussion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_discussion, container, false);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        reference = FirebaseStorage.getInstance().getReference();

        postItemsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view2);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new PostItemsAdapter(getActivity(),postItemsList);

        recyclerView.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
        //recyclerView.setLayoutManager(layoutManager);


        FloatingActionButton add = view.findViewById(R.id.floatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method to call new notepad
                Intent intent = new Intent(getActivity(),NewPost.class);
                startActivity(intent);
            }
        });

        //addPost("data");
        displayPosts();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                
            }
        }, 1000);

        return view;

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void displayPosts(){

        db.collection("Posts").orderBy("likes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Toast.makeText(getContext(),"sucess",Toast.LENGTH_SHORT).show();
                        Log.i("app","sucess");


                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d: list) {
                                PostItems p = d.toObject(PostItems.class);
                                Log.i("app","sucess"+p.getPost_content());
                                p.setId(d.getId());
                                Log.i("det",p.getTitle());
                                Log.i("det",p.getId());
                                Log.i("det",p.getLikes());

                                postItemsList.add(p);
                            }

                            Log.i("app","the content is "+postItemsList.get(0).getPost_content());
                            adapter.notifyDataSetChanged();

                            adapter = new PostItemsAdapter(getActivity(),postItemsList);

                            recyclerView.setAdapter(adapter);
                        }

                    }
                });


        //OnClick Listener for every post created
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //handle click event
                //Intent intent = new Intent()
                try {
                    TextView dump = view.findViewById(R.id.Title_problem);
                    String title_string = dump.getText().toString();

                    dump = view.findViewById(R.id.problem);
                    String content_string = dump.getText().toString();

                    dump = view.findViewById(R.id.day);
                    String day_string = dump.getText().toString();

                    dump = view.findViewById(R.id.month);
                    String month_string = dump.getText().toString();

                    Log.i("test2",month_string);

                    Button dump_button = view.findViewById(R.id.bookmark);
                    String bookmark_string = dump_button.getText().toString();

                    Log.i("test2",bookmark_string);

                    dump_button = view.findViewById(R.id.like);
                    String likes_string = dump_button.getText().toString();
                    Log.i("test2",likes_string);
                Intent intent = new Intent(getContext(),Read_Post.class);
                intent.putExtra("title",title_string);
                intent.putExtra("content",content_string);
                intent.putExtra("day",day_string);
                intent.putExtra("month",month_string);
                intent.putExtra("bookmark",bookmark_string);
                intent.putExtra("likes",likes_string);

                Log.i("test2",title_string+" "+day_string+month_string);
                startActivity(intent);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void addPost(String data) {

        CollectionReference posts = db.collection("Posts");

        Date date = new Date();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String dateofday          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(date);

        PostItems post = new PostItems(mAuth.getUid(),posts.getId(),currentDateTimeString,dateofday,monthString,"title",data,"0");

        posts.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"Created",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Mingindhi",Toast.LENGTH_SHORT).show();
            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
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

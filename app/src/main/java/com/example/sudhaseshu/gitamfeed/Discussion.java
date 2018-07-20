package com.example.sudhaseshu.gitamfeed;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


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

    StorageReference reference;
    DatabaseReference databaseReference;
    DatabaseReference posts;
    FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;
    private DatabaseReference postRef;

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

        reference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        posts = FirebaseDatabase.getInstance().getReference().child("Posts").child(mAuth.getUid());
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton add = view.findViewById(R.id.floatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method to call new notepad
            }
        });

        storeToFirebase("post");
        displayAllPosts();

        return view;
    }


    private void storeToFirebase(String post) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        mDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
        mTime = timeFormat.format(calendar.getTime());

        Log.i("app",mDate+mTime);

        String filename ="1215316609";
        StorageReference filepath = reference.child("Discussions").child(filename+".txt");
        mData = "1234567";

        filepath.putBytes(mData.getBytes()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"Successfully sent", Toast.LENGTH_SHORT).show();
                    storeToDatabase();
                }
                else{
                    Toast.makeText(getActivity(),"Not Sent:(",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeToDatabase() {
        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.i("app","Present in database");
                    HashMap inf = new HashMap();
                    inf.put("id","1215316609");
                    inf.put("data",mData);
                    inf.put("time",mTime);
                    inf.put("date",mDate);
                    inf.put("Uid",mAuth.getUid());
                    posts.updateChildren(inf).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                                Log.i("app","Posts added");
                        }
                    });
                    HashMap information = new HashMap();
                    information.put("id","1215316609");
                    information.put("dob","06051999");
                    information.put("data",mData);
                    databaseReference.child(mAuth.getUid()).updateChildren(information).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.i("app","Updated data in database");
                                displayAllPosts();
                            }
                            else{
                                Log.i("app","Database creation failed!");
                            }
                        }
                    });
                }
                else{
                    DatabaseReference firebaseDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
                    DatabaseReference firebaseDatabase2 = FirebaseDatabase.getInstance().getReference().child("Posts").child(mAuth.getUid());

                    HashMap information = new HashMap();
                    information.put("id","1215316609");
                    information.put("dob","06051999");
                    information.put("data",null);
                    firebaseDatabase1.updateChildren(information).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(),"Database Created For you",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    HashMap inf = new HashMap();
                    inf.put("id","1215316609");
                    inf.put("data",null);
                    inf.put("time",null);
                    inf.put("date",null);
                    inf.put("Uid",mAuth.getUid());
                    firebaseDatabase2.updateChildren(inf).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                                Log.i("app","Posts added");
                        }
                    });
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayAllPosts() {
        Log.i("app","reached display method");
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Posts");

        FirebaseRecyclerOptions<PostItems> options = new FirebaseRecyclerOptions.Builder<PostItems>()
                .setQuery(query,PostItems.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<PostItems,PostItemsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostItemsViewHolder holder, int position, @NonNull PostItems model) {
                Toast.makeText(getActivity(),"Reached adapter",Toast.LENGTH_SHORT).show();
                Log.i("app","reached adapter");
                holder.bind(model);
            }

            @NonNull
            @Override
            public PostItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                PostItemsViewHolder object = new PostItemsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview,viewGroup,false));
                return object;
            }
        };

        recyclerView.setAdapter(adapter);
    }
    public class PostItemsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        TextView post_date,post_month,post_time,post_content;

        public PostItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            post_date = itemView.findViewById(R.id.day);
            post_month = itemView.findViewById(R.id.month);
            post_content = itemView.findViewById(R.id.problem);
        }

        public void bind(PostItems postItems){
            PostItems temp = postItems;
            Log.i("app",temp.getPost_content());

            post_date.setText(temp.getPost_date());
            post_month.setText(temp.getPost_month());
            post_content.setText(temp.getPost_content());

        }
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

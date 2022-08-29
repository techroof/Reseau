package com.techroof.reseau.Creator.CreatorFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.reseau.Adapter.Creator.CreatorNewsfeedAdapter;
import com.techroof.reseau.Creator.AddNewsfeedActivity;
import com.techroof.reseau.Model.Newsfeed;
import com.techroof.reseau.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreatorNewsfeedFragment extends Fragment {

    private RecyclerView newsfeedRv;
    private EditText newsfeedEt;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Newsfeed> newsfeedArrayList;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CreatorNewsfeedAdapter adapter;

    public CreatorNewsfeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_creator_newsfeed, container, false);

        newsfeedEt=view.findViewById(R.id.newsfeed_et);
        newsfeedRv=view.findViewById(R.id.newsfeed_rv);

        layoutManager=new LinearLayoutManager(getContext());
        newsfeedRv.setHasFixedSize(true);
        newsfeedRv.setLayoutManager(layoutManager);

        newsfeedArrayList=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        newsfeedEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addNewsfeed = new Intent(getContext(), AddNewsfeedActivity.class);
                startActivity(addNewsfeed);
                getActivity().overridePendingTransition( R.anim.slide_up, R.anim.slide_down );

            }
        });

        firestore.collection("newsfeeds")
                .whereEqualTo("creatorId",mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){

                                Newsfeed newsfeed=queryDocumentSnapshot.toObject(Newsfeed.class);
                                newsfeedArrayList.add(newsfeed);
                            }

                            adapter=new CreatorNewsfeedAdapter(getContext(),newsfeedArrayList);
                            newsfeedRv.setAdapter(adapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        return view;
    }
}
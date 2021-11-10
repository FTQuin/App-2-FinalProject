package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.databinding.FragmentPostBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPostRef = mRootRef.child("posts");

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using view binding
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // This event is triggered after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Move/ change this code for filling in post fields.
        mPostRef.child("-Mny-YIDGwEfaESAqsUO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                binding.postTitleText.setText(title);

                String content = snapshot.child("content").getValue(String.class);
                binding.postContentText.setText(content);

                Long numVotes = snapshot.child("numVotes").getValue(Long.class);
                binding.numVotesText.setText(String.valueOf(numVotes));

                Long numComments = snapshot.child("numComments").getValue(Long.class);
                binding.numCommentsText.setText(String.valueOf(numComments));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.upVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Update number of votes in database. Update text view with new number.
            }
        });

        binding.downVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Update number of votes in database. Update text view.
            }
        });

        binding.postOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: show options. (delete, edit, etc)
            }
        });

        binding.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: show view post fragment.
            }
        });

    }
}
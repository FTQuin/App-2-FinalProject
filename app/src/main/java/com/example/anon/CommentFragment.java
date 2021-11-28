package com.example.anon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anon.database.DBViewModel;
import com.example.anon.databinding.FragmentCommentBinding;

public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;
    private DBViewModel viewModel;
    private boolean upvoted = false;
    private boolean downvoted = false;
    private boolean voted = false;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return createBinding(inflater, container).getRoot();
    }

    // This event is triggered after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.upVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteComment(0);
            }
        });
        binding.downVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteComment(1);
            }
        });

        //binding.upVoteBtn.setOnClickListener(view -> voteComment(comment, 0));

        //binding.downVoteBtn.setOnClickListener(view -> voteComment(comment, 1));

        /* Comment options. layout not added yet
        AtomicInteger i = new AtomicInteger();
        binding.postOptionsBtn.setOnClickListener(view -> {
            if (i.get() == 0){
                binding.optionsContainer.setVisibility(View.VISIBLE);
                i.set(1);
            } else {
                binding.optionsContainer.setVisibility(View.INVISIBLE);
                i.set(0);
            }
        });*/
    }

    public FragmentCommentBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        // Inflate the layout for this fragment using view binding
        if(binding == null)
            binding = FragmentCommentBinding.inflate(inflater, container, false);

        return binding;
    }

    public FragmentCommentBinding getBinding() {
        return binding;
    }

    private void voteComment(int vote){
        //TODO: Fix voted, upvoted, and downvoted. currently not updating.
        switch (vote){
            //Upvote
            case 0:
                if(!voted) {
                    voted = true;
                    int numVotes = Integer.parseInt(binding.numVotesText.toString());
                    numVotes = numVotes + 1;
                    binding.numVotesText.setText(String.valueOf(numVotes));
                }
                if(!upvoted){
                    Toast.makeText(binding.getRoot().getContext(), "Up Voted Post!", Toast.LENGTH_SHORT).show();
                    upvoted = true;
                    downvoted = false;
                }else{
                    Toast.makeText(binding.getRoot().getContext(), "You cannot up vote twice.", Toast.LENGTH_SHORT).show();
                }
                break;

            //Downvote
            case 1:
                if(!voted) {
                    voted = true;
                    int numVotes = Integer.parseInt(binding.numVotesText.toString());
                    numVotes = numVotes + 1;
                    binding.numVotesText.setText(String.valueOf(numVotes));
                }
                if(!downvoted){
                    Toast.makeText(binding.getRoot().getContext(), "Down Voted Post!", Toast.LENGTH_SHORT).show();
                    int numVotes = Integer.parseInt(binding.numVotesText.toString());
                    numVotes = numVotes + 1;
                    binding.numVotesText.setText(String.valueOf(numVotes));
                    downvoted = true;
                    upvoted = false;
                }else{
                    Toast.makeText(binding.getRoot().getContext(), "You cannot down vote twice.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
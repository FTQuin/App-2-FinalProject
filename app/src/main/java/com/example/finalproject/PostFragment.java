package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.database.Post;
import com.example.finalproject.databinding.FragmentPostBinding;
import com.example.finalproject.viewpost.ViewPostFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPostRef = mRootRef.child("posts");

    private boolean upvoted = false;
    private boolean downvoted = false;
    private boolean voted = false;

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
        return createBinding(inflater, container).getRoot();
    }

    // This event is triggered after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //button onClick listeners located in PostListAdapter.java
    }

    public FragmentPostBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        // Inflate the layout for this fragment using view binding
        if(binding == null)
            binding = FragmentPostBinding.inflate(inflater, container, false);

        return binding;
    }

    public FragmentPostBinding getBinding() {
        return binding;
    }

    public void setPostView(Post post, ViewModel viewModel){
        if (post != null) {
            binding.postTitleText.setText(post.getTitle());
            binding.postContentText.setText(post.getContent());
            binding.postDateText.setText(post.getDate());
            binding.numVotesText.setText(String.valueOf(post.getNumVotes()));
            binding.numCommentsText.setText(String.valueOf(post.getNumComments()));

        } else {
            // Covers the case of data not being ready yet.
            binding.postTitleText.setText(R.string.error_loading_posts);
            binding.postContentText.setText(R.string.error_loading_posts_desc);
        }

        binding.getRoot().setOnClickListener(view -> {
            openPost(post, view);
        });

        binding.upVoteBtn.setOnClickListener(view -> {
            //to update vote count in db only once.
            //up and down vote counts don't actually do anything yet.
            Toast.makeText(binding.getRoot().getContext(), "Voted Status: " + voted, Toast.LENGTH_SHORT).show();
            if(!voted){
                //access database via votePost(post.getPostId()).
                voted = true;
            }
            if(!upvoted){
                Toast.makeText(binding.getRoot().getContext(), "Up Voted Post!", Toast.LENGTH_SHORT).show();
                //access database via votePost(post.getPostId()).
                upvoted = true;
                downvoted = false;
            }else{
                Toast.makeText(binding.getRoot().getContext(), "You cannot upvote twice.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.downVoteBtn.setOnClickListener(view -> {
            //to update vote count in db only once.
            //up and down vote counts don't actually do anything yet.
            Toast.makeText(binding.getRoot().getContext(), "Voted Status: " + voted, Toast.LENGTH_SHORT).show();
            if(!voted){
                //TODO: access database via votePost(post.getPostId()).
                voted = true;
            }
            if(!downvoted){
                Toast.makeText(binding.getRoot().getContext(), "Down Voted Post!", Toast.LENGTH_SHORT).show();
                downvoted = true;
                upvoted = false;
            }else{
                Toast.makeText(binding.getRoot().getContext(), "You cannot down twice.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.postOptionsBtn.setOnClickListener(view -> {
            Log.d("++Post Button", "Post options button clicked");
            //TODO: show options. (delete, edit, etc)
        });

        binding.commentBtn.setOnClickListener(view -> {
            openPost(post, view);
        });
    }

    //Opens specified ViewPostFragment.
    private void openPost(Post post, View view){
        ViewPostFragment mFragment = new ViewPostFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("post_id", post.getPostId());
        mFragment.setArguments(mBundle);

        //TODO: implement scroll or delete code
        int loc[] = new int[2];
        view.getLocationInWindow(loc);
//            Toast.makeText(view.getContext(),"X "+loc[0] +"\nY "+loc[1],Toast.LENGTH_SHORT).show();

        ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, mFragment).addToBackStack(null).commit();
    }
    // TODO: make set post (post) and setPost(post, binding)
    // use sP(p, b) in PostListAdapter and sP(p) in ViewPostFragment
    // TODO: add CommentFragment class and create setComment function like above.
}
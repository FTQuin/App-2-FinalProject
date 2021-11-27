package com.example.anon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;
import com.example.anon.databinding.FragmentPostBinding;
import com.example.anon.feed.FeedHolder;
import com.example.anon.viewpost.ViewPostFragment;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private DBViewModel viewModel;

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

    public FragmentPostBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        // Inflate the layout for this fragment using view binding
        if(binding == null)
            binding = FragmentPostBinding.inflate(inflater, container, false);

        return binding;
    }

    public FragmentPostBinding getBinding() {
        return binding;
    }

    public void setPostView(Post post){
        if (post != null) {
            binding.continueReadingTxt.setVisibility(View.INVISIBLE);
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

        //gets number of lines in post to add a "continue reading" prompt.
        binding.postContentText.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = binding.postContentText.getLineCount();
                int shownLines = binding.postContentText.getMaxLines();

                if (shownLines <= 6) {
                    if (lineCount > 6){
                        ConstraintLayout constraintLayout = binding.container;
                        ConstraintSet constraintSet = new ConstraintSet();

                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(R.id.divider,ConstraintSet.TOP, R.id.continueReadingTxt,ConstraintSet.BOTTOM,0);
                        constraintSet.applyTo(constraintLayout);

                        binding.continueReadingTxt.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.getRoot().setOnClickListener(view -> {
            openPost(post, view);
        });

        binding.upVoteBtn.setOnClickListener(view -> {
            //to update vote count in db only once.
            //up and down vote counts don't actually do anything yet.
            Toast.makeText(binding.getRoot().getContext(), "Voted Status: " + voted, Toast.LENGTH_SHORT).show();
            if(!voted){
                //access database via votePost(post.getPostId()).
                //votePost(post);
                voted = true;
            }
            if(!upvoted){
                Toast.makeText(binding.getRoot().getContext(), "Up Voted Post!", Toast.LENGTH_SHORT).show();
                //access database via votePost(post.getPostId()).
                upvoted = true;
                downvoted = false;
            }else{
                Toast.makeText(binding.getRoot().getContext(), "You cannot up vote twice.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(binding.getRoot().getContext(), "You cannot down vote twice.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.postOptionsBtn.setOnClickListener(view -> {
            //TODO: show options. (delete, edit, etc)
        });

        binding.commentBtn.setOnClickListener(view -> {
            openPost(post, view);
        });
    }

    private void votePost(Post post, View view){
        String postID = post.getPostId();

        if(postID != null){
            Log.d("postFrag", "Post ID" + postID);
            viewModel.votePost(postID);
        }else{
            Log.d("postFrag", "ERROR: post id null.");
        }
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

        final Fragment fragmentInFrame = ((MainActivity) view.getContext())
                .getSupportFragmentManager().findFragmentById(R.id.mainFragmentContainerView);

        //To prevent infinite adding to back stack when clicking post in ViewPostFragment.
        if (fragmentInFrame instanceof FeedHolder){
            ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.mainFragmentContainerView, mFragment).addToBackStack(null).commit();
        }
    }
    // TODO: make set post (post) and setPost(post, binding)
    // use sP(p, b) in PostListAdapter and sP(p) in ViewPostFragment
    // TODO: add CommentFragment class and create setComment function like above.
}
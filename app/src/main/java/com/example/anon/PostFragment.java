package com.example.anon;

import android.os.Bundle;
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

    public void setPostView(Post post, DBViewModel vm){

        //Sets viewmodel from calling fragment.
        viewModel = vm;

        if (post != null) {
            binding.continueReadingTxt.setVisibility(View.INVISIBLE);
            binding.postTitleText.setText(post.getTitle());
            binding.postContentText.setText(post.getContent());
            binding.postDateText.setText(post.getDate());
            //binding.numVotesText.setText(String.valueOf(post.getNumVotes()));
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

        binding.getRoot().setOnClickListener(view -> openPost(post, view));

        binding.upVoteBtn.setOnClickListener(view -> votePost(post, 0));

        binding.downVoteBtn.setOnClickListener(view -> votePost(post, 1));

        binding.postOptionsBtn.setOnClickListener(view -> {
            //TODO: show options. (delete, edit, etc)
        });

        binding.commentBtn.setOnClickListener(view -> openPost(post, view));
    }

    private void votePost(Post post, int vote){
        switch (vote){
            //Upvote
            case 0:
                Toast.makeText(binding.getRoot().getContext(), "Voted Status: " + voted, Toast.LENGTH_SHORT).show();
                if(!voted){
                    voted = true;
                    viewModel.votePost(post);
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
                Toast.makeText(binding.getRoot().getContext(), "Voted Status: " + voted, Toast.LENGTH_SHORT).show();
                if(!voted){
                    voted = true;
                    viewModel.votePost(post);
                }
                if(!downvoted){
                    Toast.makeText(binding.getRoot().getContext(), "Down Voted Post!", Toast.LENGTH_SHORT).show();
                    downvoted = true;
                    upvoted = false;
                }else{
                    Toast.makeText(binding.getRoot().getContext(), "You cannot down vote twice.", Toast.LENGTH_SHORT).show();
                }
                break;
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
}
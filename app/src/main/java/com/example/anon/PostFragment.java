package com.example.anon;

import android.content.Context;
import android.content.res.Configuration;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;
import com.example.anon.databinding.FragmentPostBinding;
import com.example.anon.feed.FeedHolder;
import com.example.anon.viewpost.ViewPostFragment;

import java.util.concurrent.atomic.AtomicInteger;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private DBViewModel viewModel;
    private boolean upvoted = false;
    private boolean downvoted = false;
    private boolean voted = false;
    private Context context;

    public PostFragment() {}

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
        context = container.getContext();
        viewModel = new ViewModelProvider((FragmentActivity) context).get(DBViewModel.class);
        // Inflate the layout for this fragment using view binding
        if(binding == null)
            binding = FragmentPostBinding.inflate(inflater, container, false);

        return binding;
    }

    public FragmentPostBinding getBinding() {
        return binding;
    }

    public void setPostView(Post post, DBViewModel vm){
        //Sets view model from calling fragment.
//        viewModel = vm;

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
        binding.postContentText.post(() -> {
            int lineCount = binding.postContentText.getLineCount();
            int shownLines = binding.postContentText.getMaxLines();

            //To prevent "continue reading" from being shown in viewPostFragment
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
        });

        binding.getRoot().setOnClickListener(view -> openPost(post, view));

        binding.upVoteBtn.setOnClickListener(view -> votePost(post, 0));

        binding.downVoteBtn.setOnClickListener(view -> votePost(post, 1));

        AtomicInteger i = new AtomicInteger();
        binding.postOptionsBtn.setOnClickListener(view -> {
            if (i.get() == 0){
                binding.optionsContainer.setVisibility(View.VISIBLE);
                i.set(1);
            } else {
                binding.optionsContainer.setVisibility(View.INVISIBLE);
                i.set(0);
            }
        });

        binding.commentBtn.setOnClickListener(view -> openPost(post, view));
    }

    private void votePost(Post post, int vote){
        //TODO: Fix voted, upvoted, and downvoted. currently not updating.
        Toast.makeText(binding.getRoot().getContext(), "Voted Status: " + voted, Toast.LENGTH_SHORT).show();
        if (!voted) {
            viewModel.votePost(post);
            voted = true;
        }
        switch (vote){
            //Upvote
            case 0:
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
                if(!downvoted){
                    Toast.makeText(binding.getRoot().getContext(), "Down Voted Post!", Toast.LENGTH_SHORT).show();
                    downvoted = true;
                    upvoted = false;
                }else{
                    Toast.makeText(binding.getRoot().getContext(), "You cannot down vote twice.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        int x =1;
    }

    //Opens specified ViewPostFragment.
    private void openPost(Post post, View view){
        ViewPostFragment mFragment = new ViewPostFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("post_id", post.getPostId());
        mFragment.setArguments(mBundle);
        boolean isLandscape = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if(isLandscape) {
            int lineCount = binding.postContentText.getLineCount();
            int shownLines = binding.postContentText.getMaxLines();
            //To prevent "continue reading" from being shown in viewPostFragment
            if (shownLines <= 6) {
                if (lineCount > 6){
                    binding.postContentText.setMaxLines(Integer.MAX_VALUE);
                    binding.continueReadingTxt.setVisibility(View.INVISIBLE);
                }
            }
        }

        final Fragment fragmentInFrame = ((MainActivity) view.getContext())
                .getSupportFragmentManager().findFragmentById(R.id.mainFragmentContainerView);

        int replaceID = R.id.mainFragmentContainerView;
        if(isLandscape) {
            replaceID = R.id.mainFragmentContainerViewLeft;
            ((MainActivity) view.getContext()).binding.mainFragmentContainerViewLeft.setVisibility(View.VISIBLE);
        }

        //To prevent infinite adding to back stack when clicking post in ViewPostFragment.
        if (fragmentInFrame instanceof FeedHolder){
            FragmentTransaction ft = ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(replaceID, mFragment);
            if(!isLandscape) ft = ft.addToBackStack(null);
            ft.commit();
        }
    }
}
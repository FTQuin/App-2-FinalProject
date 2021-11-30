/*==================================================================================================
* File: PostFragment.java
* Description: Java Class for fragment_post.xml, used mainly in FeedAdapter.java when a new post
*              is created
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.anonApp.anon;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.anonApp.anon.database.DBViewModel;
import com.anonApp.anon.R;
import com.anonApp.anon.database.Post;
import com.anonApp.anon.databinding.FragmentPostBinding;
import com.anonApp.anon.feed.FeedHolder;
import com.anonApp.anon.viewpost.ViewPostFragment;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Java Class for fragment_post.xml, used mainly in FeedAdapter.java when a new post
 * is created
 */
public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private DBViewModel viewModel;
    private Context context;

    public PostFragment() {}

    public static PostFragment newInstance() {
        return new PostFragment();
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

    /**
     * used in feed adapter to get the binding for the current post
     * @param inflater layout inflater for post_fragment
     * @param container viewgroup of the post
     * @return binding of the current post fragment
     */
    public FragmentPostBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        if (container != null)
            context = container.getContext();
        viewModel = new ViewModelProvider((FragmentActivity) context).get(DBViewModel.class);
        // Inflate the layout for this fragment using view binding
        if(binding == null)
            binding = FragmentPostBinding.inflate(inflater, container, false);

        return binding;
    }

    /**
     * @return binding of the current post
     */
    public FragmentPostBinding getBinding() {
        return binding;
    }

    /**
     * used in feed adapter to set the post for the postFragment
     * @param post post for the current fragment
     */
    public void setPostToView(Post post){

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

        binding.getRoot().setOnClickListener(view -> {
            if (post != null)
                openPost(post, view);
        });

        binding.upVoteBtn.setOnClickListener(view -> viewModel.upVotePost(post));

        binding.downVoteBtn.setOnClickListener(view -> viewModel.downVotePost(post));

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

        binding.commentBtn.setOnClickListener(view -> {
            if (post != null)
                openPost(post, view);
        });
    }

    /**
     * Opens specified ViewPostFragment.
     * @param post post to open
     * @param view used to get the context
     */
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
            Objects.requireNonNull(((MainActivity) view.getContext()).binding.mainFragmentContainerViewLeft).setVisibility(View.VISIBLE);
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
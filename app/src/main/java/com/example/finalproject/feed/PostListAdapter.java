package com.example.finalproject.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.database.DBViewModel;
import com.example.finalproject.database.Post;
import com.example.finalproject.databinding.FragmentPostBinding;
import com.example.finalproject.viewpost.CommentRecycler;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private final List<Post> postList;
    private DBViewModel viewModel;
    private boolean upvoted = false;
    private boolean downvoted = false;
    private boolean voted = false;

    public PostListAdapter(List<Post> posts){
        postList = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(FragmentPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(postList.get(position), position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (postList != null)
            return postList.size();
        else return 0;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final FragmentPostBinding binding;

        public PostViewHolder(FragmentPostBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(Post post, int position){

            if (postList != null) {
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
                CommentRecycler mFragment = new CommentRecycler();
                Bundle mBundle = new Bundle();
                mBundle.putString("post_id", post.getPostId());
                mFragment.setArguments(mBundle);

                int loc[] = new int[2];
                view.getLocationInWindow(loc);
                Toast.makeText(view.getContext(),"X "+loc[0] +"\nY "+loc[1],Toast.LENGTH_SHORT).show();

                ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, mFragment).addToBackStack(null).commit();
            });

            binding.upVoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //to update vote count in db only once.
                    //up and down vote counts don't actually do anything yet.
                    if(voted == false){
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
                    //TODO: Update number of votes in database. Update text view with new number.
                }
            });

            binding.downVoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //to update vote count in db only once.
                    //up and down vote counts don't actually do anything yet.
                    if(voted == false){
                        //access database via votePost(post.getPostId()).
                        voted = true;
                    }
                    if(!downvoted){
                        Toast.makeText(binding.getRoot().getContext(), "Down Voted Post!", Toast.LENGTH_SHORT).show();
                        downvoted = true;
                        upvoted = false;
                    }else{
                        Toast.makeText(binding.getRoot().getContext(), "You cannot down twice.", Toast.LENGTH_SHORT).show();
                    }
                    //TODO: Update number of votes in database. Update text view.
                }
            });

            binding.postOptionsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("++Post Button", "Post options button clicked");
                    //TODO: show options. (delete, edit, etc)
                }
            });

            binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("++Post Button", "Comment button clicked");
                    //TODO: show view post fragment.
                }
            });
        }
    }
}

package com.example.finalproject.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.database.Post;
import com.example.finalproject.databinding.FragmentPostBinding;
import com.example.finalproject.viewpost.ViewPostFragment;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private final List<Post> postList;

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
                ViewPostFragment mFragment = new ViewPostFragment();
                Bundle mBundle = new Bundle();
                mBundle.putString("post_id", post.getPostId());
                mFragment.setArguments(mBundle);

                ((MainActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, mFragment).addToBackStack(null).commit();
            });
        }
    }
}

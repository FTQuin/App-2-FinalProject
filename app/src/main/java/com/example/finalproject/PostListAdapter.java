package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.database.Post;
import com.example.finalproject.databinding.FragmentCommentBinding;
import com.example.finalproject.databinding.FragmentPostBinding;

import java.util.ArrayList;
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
        if (postList != null) {
            Post current = postList.get(position);

            holder.postTitle.setText(current.getTitle());
            holder.postContent.setText(current.getContent());
            holder.postDate.setText(current.getDate());
            holder.postNumVotes.setText(String.valueOf(current.getNumVotes()));
            holder.postNumComments.setText(String.valueOf(current.getNumComments()));

        } else {
            // Covers the case of data not being ready yet.
            holder.postTitle.setText(R.string.error_loading_posts);
            holder.postContent.setText(R.string.error_loading_posts_desc);
        }
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
        private final TextView postTitle;
        private final TextView postContent;
        private final TextView postDate;
        private final TextView postNumVotes;
        private final TextView postNumComments;

        public PostViewHolder(FragmentPostBinding binding) {
            super(binding.getRoot());

            postTitle = binding.postTitleText;
            postContent = binding.postContentText;
            postDate = binding.postDateText;
            postNumVotes = binding.numVotesText;
            postNumComments = binding.numCommentsText;
        }
    }
}

/*==================================================================================================
* File: CommentAdapter.java
* Description: Java Class for comment_recycler.xml
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.anonApp.anon.viewpost;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.anonApp.anon.database.DBViewModel;
import com.anonApp.anon.database.Comment;
import com.anonApp.anon.databinding.FragmentCommentBinding;

import java.util.List;

/**
 * Java Class for comment_recycler.xml
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> commentList;
    private DBViewModel viewModel;

    public CommentAdapter(List<Comment> comments) {
        commentList = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewModel = new ViewModelProvider((FragmentActivity) parent.getContext()).get(DBViewModel.class);
        return new ViewHolder(FragmentCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = commentList.get(position);
        holder.mContentView.setText(commentList.get(position).getContent());
        holder.mNumVotesView.setText(String.valueOf(commentList.get(position).getNumVotes()));
        holder.mDateTimeView.setText(commentList.get(position).getDate());
        holder.mUpvoteButton.setOnClickListener(v -> {
            viewModel.upVoteComment(commentList.get(position));
            holder.mNumVotesView.setText(String.valueOf(commentList.get(position).getNumVotes()));
        });
        holder.mDownvoteButton.setOnClickListener(v -> {
            viewModel.downVoteComment(commentList.get(position));
            holder.mNumVotesView.setText(String.valueOf(commentList.get(position).getNumVotes()));
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final TextView mNumVotesView;
        public final TextView mDateTimeView;
        public final ImageButton mUpvoteButton;
        public final ImageButton mDownvoteButton;
        public Comment mItem;

        public ViewHolder(FragmentCommentBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mNumVotesView = binding.numVotesText;
            mDateTimeView = binding.commentDateText;
            mUpvoteButton = binding.upVoteBtn;
            mDownvoteButton = binding.downVoteBtn;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
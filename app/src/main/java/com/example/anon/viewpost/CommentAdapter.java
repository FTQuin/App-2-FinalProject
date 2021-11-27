package com.example.anon.viewpost;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.anon.database.Comment;
import com.example.anon.databinding.FragmentCommentBinding;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> commentList;

    public CommentAdapter(List<Comment> comments) {
        commentList = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = commentList.get(position);
        holder.mContentView.setText(commentList.get(position).getContent());
        holder.mNumVotesView.setText(String.valueOf(commentList.get(position).getNumVotes()));
        holder.mDateTimeView.setText(commentList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public final TextView mNumVotesView;
        public final TextView mDateTimeView;
        public Comment mItem;

        public ViewHolder(FragmentCommentBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
            mNumVotesView = binding.numVotesText;
            mDateTimeView = binding.commentDateText;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
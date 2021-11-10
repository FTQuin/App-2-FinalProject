package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.database.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private Context context;
    private List<Post> posts;
    private final LayoutInflater mInflater;
    private List<Post> mPosts; // Cached copy of words
    //private FragmentPostBinding binding;

    public PostListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (mPosts != null) {
            Post current = mPosts.get(position);

            holder.postTitle.setText(current.getTitle());
            holder.postContent.setText(current.getContent());
            holder.postDate.setText(current.getDate());
            holder.postNumVotes.setText(String.valueOf(current.getNumVotes()));
            holder.postNumComments.setText(String.valueOf(current.getNumComments()));

        } else {
            // Covers the case of data not being ready yet.
            holder.postTitle.setText("Error. See Below.");
            holder.postContent.setText("Error communicating with database to retrieve posts. Please try again later.");
        }
    }

    public void setPosts(List<Post> posts){
        mPosts = posts;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPosts != null)
            return mPosts.size();
        else return 0;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView postTitle;
        private final TextView postContent;
        private final TextView postDate;
        private final TextView postNumVotes;
        private final TextView postNumComments;

        private PostViewHolder(View itemView) {
            super(itemView);

            postTitle = itemView.findViewById(R.id.postTitleText);
            postContent = itemView.findViewById(R.id.postContentText);
            postDate = itemView.findViewById(R.id.postDateText);
            postNumVotes = itemView.findViewById(R.id.numVotesText);
            postNumComments = itemView.findViewById(R.id.numCommentsText);
        }
    }
}

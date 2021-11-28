package com.example.anon.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anon.PostFragment;
import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final Context context;
    FragmentManager fragmentManager;
    private DBViewModel viewModel;
    public PostListAdapter(List<Post> posts, Context context){
        postList = posts;
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewModel = new ViewModelProvider((FragmentActivity) parent.getContext()).get(DBViewModel.class);
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        PostFragment pFrag = new PostFragment();
        pFrag.createBinding(LayoutInflater.from(parent.getContext()), parent);

        return new PostViewHolder(pFrag);
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
        private final PostFragment postFragment;

        public PostViewHolder(PostFragment postFragment) {
            super(postFragment.getBinding().getRoot());
            this.postFragment = postFragment;
        }

        public void bind(Post post, int position){
            postFragment.setPostView(post, viewModel);
        }
    }
}

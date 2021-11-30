/*==================================================================================================
* File: PostListAdapter.java
* Description: Java Class for feed_recycler.xml
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.anon.anon.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.anon.anon.PostFragment;
import com.anon.anon.database.DBViewModel;
import com.anon.anon.database.Post;

import java.util.List;

/**
 * Java Class for feed_recycler.xml
 */
public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final Context context;
    FragmentManager fragmentManager;
    private DBViewModel viewModel;
    public PostListAdapter(List<Post> posts, Context context){
        postList = posts;
        this.context = context;
    }

    @NonNull
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

        holder.bind(postList.get(position));

    }

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

        public void bind(Post post){
            postFragment.setPostToView(post);
        }
    }
}

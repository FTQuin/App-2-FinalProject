/*==================================================================================================
* File: FeedRecycler.java
* Description: Java Class for feed_recycler.xml
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.anon.anon.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anon.anon.database.DBViewModel;
import com.anon.anon.R;

/**
 * Java Class for feed_recycler.xml
 */
public class FeedRecycler extends Fragment {

    public FeedRecycler() {
    }

    public static FeedRecycler newInstance() {
        return new FeedRecycler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_recycler, container, false);

        DBViewModel viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            viewModel.getAllPosts().observe(getViewLifecycleOwner(),
                    posts -> recyclerView.setAdapter(new PostListAdapter(posts, getContext())));
        }
        return view;
    }
}
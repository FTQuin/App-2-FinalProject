package com.example.anon.feed;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anon.R;
import com.example.anon.database.DBViewModel;

public class FeedRecycler extends Fragment {

    private int mColumnCount = 1;
    private DBViewModel viewModel;

    public FeedRecycler() {
    }

    public static FeedRecycler newInstance() {
        FeedRecycler fragment = new FeedRecycler();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_recycler, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            viewModel.getAllPosts().observe(getViewLifecycleOwner(),
                    posts -> recyclerView.setAdapter(new PostListAdapter(posts, getContext())));
        }
        return view;
    }
}
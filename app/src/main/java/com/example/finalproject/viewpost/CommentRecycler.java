package com.example.finalproject.viewpost;

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

import com.example.finalproject.R;
import com.example.finalproject.database.DBViewModel;

/**
 * A fragment representing a list of Items.
 */
public class CommentRecycler extends Fragment {

    private static final String ARG_POST_ID = "post-id";
    private int mColumnCount;

    private DBViewModel viewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentRecycler() {
    }

    public static CommentRecycler newInstance(int postID) {
        CommentRecycler fragment = new CommentRecycler();
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ID, postID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_POST_ID);
        }
    }

    //TODO: move to onViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_recycler, container, false);

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
            //TODO: change this to get just comments for specific post
            viewModel.getAllComments().observe(getViewLifecycleOwner(),
                    comments -> recyclerView.setAdapter(new CommentAdapter(comments)));
        }
        return view;
    }
}
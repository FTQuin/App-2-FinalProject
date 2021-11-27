package com.example.anon.viewpost;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anon.R;
import com.example.anon.database.DBViewModel;

/**
 * A fragment representing a list of Items.
 */
public class CommentRecycler extends Fragment {

    private static final String ARG_POST_ID = "post_id";

    private DBViewModel viewModel;

    private String postID;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentRecycler() {
    }

    public static CommentRecycler newInstance(String postID) {
        CommentRecycler fragment = new CommentRecycler();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            postID = getArguments().getString(ARG_POST_ID);
        }
    }

    //TODO: move to onViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_recycler, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("postidtest", "post id received in Comment Recycler: " + postID);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //TODO: get clicked post id from post fragment
            viewModel.refreshComments();
            viewModel.getCommentsForPost(postID).observe(getViewLifecycleOwner(),
                    comments -> recyclerView.setAdapter(new CommentAdapter(comments)));
        }
    }
}
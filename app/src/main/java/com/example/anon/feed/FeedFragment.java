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
import com.example.anon.database.DBRepository;
import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;
import com.example.anon.databinding.FeedRecyclerBinding;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FeedFragment extends Fragment {

    private int mColumnCount = 1;
    private FeedRecyclerBinding binding;

    private DBViewModel viewModel;
    private DBRepository repository;
    private List<Post> getPosts;
    private PostListAdapter postListAdapter;
    private RecyclerView recyclerView;

    private static final String ARG_LOCALITY = "locality";
    private static final String ARG_SUB_ADMIN_AREA = "sub_admin_area";
    private String locality, subAdminArea;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    public static FeedFragment newInstance(String locality, String subAdminArea) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCALITY, locality);
        args.putString(ARG_SUB_ADMIN_AREA, subAdminArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            locality = getArguments().getString(ARG_LOCALITY);
            subAdminArea = getArguments().getString(ARG_SUB_ADMIN_AREA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_recycler, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        //Checks if view model currently has location data before initializing.
        if (viewModel.getLocality() == null || viewModel.getSubAdmin() == null) {
            viewModel.passLocation("Kamloops", "Thompson-Nicola");
            //viewModel.passLocation(locality, subAdminArea);
        }

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
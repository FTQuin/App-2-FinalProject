package com.example.finalproject.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.PostListAdapter;
import com.example.finalproject.R;
import com.example.finalproject.database.DBRepository;
import com.example.finalproject.database.DBViewModel;
import com.example.finalproject.database.Post;
import com.example.finalproject.placeholder.PlaceholderContent;

import com.example.finalproject.databinding.FeedRecyclerBinding;

import java.util.ArrayList;
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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed_recycler, container, false);

        binding = FeedRecyclerBinding.inflate(inflater, container, false);
        //return binding.getRoot();

        // Set the adapter
        repository = new DBRepository(getActivity().getApplication());
        getPosts = new ArrayList<>();
        recyclerView = binding.recyclerview;
        recyclerView.setAdapter(postListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postListAdapter = new PostListAdapter(view.getContext());

        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = new ViewModelProvider(this).get(DBViewModel.class);

        makeRequest();

        viewModel.getAllPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                recyclerView.setAdapter(postListAdapter);
                //Updating cached copy of posted in the adapter.
                postListAdapter.setPosts(posts);
            }
        });


        /*if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS));
        }*/
        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        makeRequest();
    }*/

    private void makeRequest(){
        //Call<List<Post>> call = repository.getAllPosts();


    }
}
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalproject.PostListAdapter;
import com.example.finalproject.R;
import com.example.finalproject.database.DBRepository;
import com.example.finalproject.database.DBViewModel;
import com.example.finalproject.database.Post;
import com.example.finalproject.placeholder.PlaceholderContent;

import com.example.finalproject.databinding.FeedRecyclerBinding;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.maps.model.LatLng;

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
        binding = FeedRecyclerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.recyclerview;
        getPosts = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            String t = "title" + String.valueOf(i);
            String c = "Content"  + String.valueOf(i);
            LatLng l = new LatLng(100, 100);
            Post p = new Post (String.valueOf(i), t, c, l, "11/04/2021", 0, 0);
            getPosts.add(p);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postListAdapter = new PostListAdapter(getPosts);
        //postListAdapter = new PostListAdapter(binding.getRoot().getContext());
        recyclerView.setAdapter(postListAdapter);


        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = new ViewModelProvider(this).get(DBViewModel.class);

        //makeRequest();

        viewModel.getAllPosts().observe(getViewLifecycleOwner(), posts -> {
            recyclerView.setAdapter(postListAdapter);
            //Updating cached copy of posted in the adapter.
            postListAdapter.setPosts(posts);
        });

    }

    private void makeRequest(){



        /*Call<List<Post>> call = repository.getAllPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Log.d("MVVMX", "--- Not successful");
                } else {
                    Log.d("MVVMX", "------" + response.body());
                    List<Post> mAllPosts = response.body();
                    if (mAllPosts != null){
                        for (Post post : mAllPosts) {
                            //Log.d("MVVMX", "---" + post.getId());
                            //Log.d("MVVMX", "---" + post.getEmail());
                            repository.

                            repository.insert(new Post(post.getId(), post.getEmail()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("MVVMX", "--- FAILED " + t.getMessage());
            }
        }); */


    }
}
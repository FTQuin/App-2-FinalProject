package com.example.anon.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.anon.PostFragment;
import com.example.anon.R;
import com.example.anon.database.DBViewModel;
import com.example.anon.databinding.FeedHolderBinding;

public class FeedHolder extends Fragment {

    private FeedHolderBinding binding;
    private PostFragment postFragment;
    private DBViewModel viewModel;

    FeedRecycler feedRecycler;

    public FeedHolder(){}

    public static FeedHolder newInstance(){
        FeedHolder fragment = new FeedHolder();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FeedHolderBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager f = getChildFragmentManager();

        feedRecycler = (FeedRecycler) f.findFragmentById(R.id.fragmentContainerViewFeed);

        SwipeRefreshLayout swipeRefreshContainer = binding.swipeRefreshContainer;

        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Async task here to refresh comments.
                //viewModel.refreshRepository();
                Toast.makeText(binding.getRoot().getContext(), "Refreshing feed", Toast.LENGTH_SHORT).show();
                Log.d("refresh", "Refreshing comments");
                swipeRefreshContainer.setRefreshing(false); //uncomment this in onSuccess.
            }
        });

        swipeRefreshContainer.setColorSchemeResources(R.color.theme_colour);
    }

}
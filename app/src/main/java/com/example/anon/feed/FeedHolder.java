/*==================================================================================================
* File: FeedHolder.java
* Description: Java Class for feed_holder.xml, it is need inorder add swipe refresh
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.example.anon.feed;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.anon.MainActivity;
import com.example.anon.R;
import com.example.anon.database.DBViewModel;
import com.example.anon.databinding.FeedHolderBinding;

/**
 * Java Class for feed_holder.xml, it is need inorder add swipe refresh
 */
public class FeedHolder extends Fragment {

    private FeedHolderBinding binding;

    private static final String ARG_LOCALITY = "locality";
    private static final String ARG_SUB_ADMIN_AREA = "sub_admin_area";
    private String locality, subAdminArea;

    public FeedHolder(){}

    public static FeedHolder newInstance(String locality, String subAdminArea){
        FeedHolder fragment = new FeedHolder();
        Bundle args = new Bundle();
        args.putString(ARG_LOCALITY, locality);
        args.putString(ARG_SUB_ADMIN_AREA, subAdminArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            locality = getArguments().getString(ARG_LOCALITY);
            subAdminArea = getArguments().getString(ARG_SUB_ADMIN_AREA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FeedHolderBinding.inflate(getLayoutInflater());
        DBViewModel viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        //Checks if view model has any location data before initializing.
        if (viewModel.getLocality() == null || viewModel.getSubAdmin() == null) {
            //sends current location data if either fields are null.
            viewModel.passLocation(locality, subAdminArea);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwipeRefreshLayout swipeRefreshContainer = binding.swipeRefreshContainer;
        swipeRefreshContainer.setColorSchemeResources(R.color.theme_colour);

        swipeRefreshContainer.setOnRefreshListener(() -> {
            swipeRefreshContainer.setRefreshing(true);

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                //Refresh repository
                if(getActivity()!=null)
                    ((MainActivity) getActivity()).getDeviceLocation();
            }, 500);
            //Stop refreshing animation when repository refresh is complete.
            swipeRefreshContainer.setRefreshing(false);
        });
    }
}
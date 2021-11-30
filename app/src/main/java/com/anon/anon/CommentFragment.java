/*==================================================================================================
* File: CommentFragment.java
* Description: Java Class for fragment_comment.xml
* Authors: Shea Holden, Quin Adam
* Date: November 03, 2021
* Project: Anon
==================================================================================================*/
package com.anon.anon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anon.anon.databinding.FragmentCommentBinding;

/**
 * Java Class for fragment_comment.xml
 */
public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;

    public CommentFragment() {}

    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return createBinding(inflater, container).getRoot();
    }

    public FragmentCommentBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container){
        // Inflate the layout for this fragment using view binding
        if(binding == null)
            binding = FragmentCommentBinding.inflate(inflater, container, false);

        return binding;
    }
}
package com.example.anon.viewpost;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.anon.PostFragment;
import com.example.anon.R;
import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;
import com.example.anon.databinding.FragmentViewPostBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPostFragment extends Fragment {

    DBViewModel viewModel;
    FragmentViewPostBinding binding;

    PostFragment postFragment;
    CommentRecycler commentRecycler;

    private static final String ARG_POST_ID = "post_id";

    private String postID;

    public ViewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPostFragment newInstance(String postID) {
        ViewPostFragment fragment = new ViewPostFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewPostBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager f = getChildFragmentManager();

        commentRecycler = (CommentRecycler) f.findFragmentById(R.id.fragmentContainerViewComments);
        postFragment = (PostFragment) f.findFragmentById(R.id.fragmentContainerViewPost);
        postFragment.createBinding(getLayoutInflater(), binding.getRoot());

        Post correctPost = null;
        for(Post p : viewModel.getAllPosts().getValue())
            if(p.getPostId().equals(postID))
                correctPost = p;

        postFragment.setPostView(correctPost, viewModel);

        if (postFragment.getBinding().postContentText.getText().length() > 360){
            //TODO: Either set max lines higher or uncomment scrolling line.
            postFragment.getBinding().postContentText.setMaxLines(Integer.MAX_VALUE);
            //postFragment.getBinding().postContentText.setMovementMethod(new ScrollingMovementMethod());
        }

        SwipeRefreshLayout swipeRefreshContainer = binding.swipeRefreshContainer;

        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Async task here to refresh comments.
                //viewModel.getCommentsForPost(postID);
                Log.d("refresh", "Refreshing comments");
                swipeRefreshContainer.setRefreshing(false); //uncomment this in onSuccess.
            }
        });

        swipeRefreshContainer.setColorSchemeResources(R.color.theme_colour);
    }
}
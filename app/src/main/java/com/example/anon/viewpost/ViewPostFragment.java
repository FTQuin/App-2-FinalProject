package com.example.anon.viewpost;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.anon.PostFragment;
import com.example.anon.R;
import com.example.anon.database.Comment;
import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;
import com.example.anon.databinding.FragmentViewPostBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPostFragment extends Fragment {

    DBViewModel viewModel;
    FragmentViewPostBinding binding;

    SwipeRefreshLayout swipeRefreshContainer;
    ImageButton btnPostComment;
    TextView txtComment;

    PostFragment postFragment;
    CommentRecycler commentRecycler;

    private static final String ARG_POST_ID = "post_id";
    private String postID;
    Post currentPost;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public ViewPostFragment() {
        // Required empty public constructor
    }

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

        commentRecycler = new CommentRecycler();
        commentRecycler = CommentRecycler.newInstance(postID);

        f.beginTransaction().replace(R.id.fragmentContainerViewComments, commentRecycler).commit();

        postFragment = (PostFragment) f.findFragmentById(R.id.fragmentContainerViewPost);
        postFragment.createBinding(getLayoutInflater(), binding.getRoot());

        currentPost = viewModel.getPost(postID);
        postFragment.setPostView(currentPost);
        viewModel.getAllPosts().observe(getViewLifecycleOwner(), postList -> {
            currentPost = viewModel.getPost(postID);
            postFragment.setPostView(currentPost);
        });

        Post correctPost = null;
        for(Post p : viewModel.getAllPosts().getValue())
            if(p.getPostId().equals(postID))
                correctPost = p;

        postFragment.setPostView(correctPost);

        if (postFragment.getBinding().postContentText.getText().length() > 360){
            postFragment.getBinding().postContentText.setMaxLines(Integer.MAX_VALUE);
            //postFragment.getBinding().postContentText.setMovementMethod(new ScrollingMovementMethod());
        }

        swipeRefreshContainer = binding.swipeRefreshContainer;

        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Async task here to refresh comments.
                viewModel.refreshFeed();
                viewModel.refreshComments();

                Toast.makeText(binding.getRoot().getContext(), "Refreshing comments", Toast.LENGTH_SHORT).show();
                Log.d("refresh", "Refreshing comments");
                swipeRefreshContainer.setRefreshing(false); //uncomment this in onSuccess.
            }
        });

        swipeRefreshContainer.setColorSchemeResources(R.color.theme_colour);

        txtComment = binding.txtComment;
        btnPostComment = binding.postCommentBtn;
        btnPostComment.setOnClickListener(v -> {

            String commentContent = txtComment.getText().toString();

            //Checks if title and content are blank before attempting to write to DB.
            if (TextUtils.isEmpty(commentContent)){
                Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy. hh:mm aa", Locale.getDefault());
                String dateStr = df.format(d);

                String id = mRootRef.push().getKey();

                Comment comment = new Comment(id, postID, commentContent, 1, dateStr);

                viewModel.insertComment(comment, currentPost);
                postFragment.setPostView(currentPost);
                viewModel.refreshComments();

                // hide keyboard and clear text view
                txtComment.setText("");
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            }
        });
    }
}
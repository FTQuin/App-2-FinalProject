package com.example.anon.viewpost;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private String postID = "0";
    Post currentPost;

    public ViewPostFragment() {}

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
        swipeRefreshContainer = binding.swipeRefreshContainer;

        swipeRefreshContainer.setColorSchemeResources(R.color.theme_colour);
        swipeRefreshContainer.setRefreshing(true);

        f.beginTransaction().replace(R.id.fragmentContainerViewComments, commentRecycler).commit();

        postFragment = (PostFragment) f.findFragmentById(R.id.fragmentContainerViewPost);
        postFragment.createBinding(getLayoutInflater(), binding.getRoot());

        viewModel.getAllPosts().observe(getViewLifecycleOwner(), postList -> {
            currentPost = viewModel.getPost(postID);
            if(currentPost != null) {
                postFragment.setPostView(currentPost, viewModel);
                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    binding.postTitleTextLandscape.setText(currentPost.getTitle());
            }
        });

        postFragment.getBinding().postContentText.setMaxLines(Integer.MAX_VALUE);

        swipeRefreshContainer.setRefreshing(false);

        swipeRefreshContainer.setOnRefreshListener(() -> {
            swipeRefreshContainer.setRefreshing(true);
            viewModel.refreshFeed();
            viewModel.refreshComments();

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                //Stop refreshing animation when repository refresh is complete.
                swipeRefreshContainer.setRefreshing(false);
                Log.d("refresh", "Refreshing comments");
            }, 100);
        });

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

                String id = viewModel.getNewKey();

                Comment comment = new Comment(id, postID, commentContent, 1, dateStr);

                viewModel.insertComment(comment, currentPost);
                postFragment.setPostView(currentPost, viewModel);
                viewModel.refreshComments();

                // hide keyboard and clear text view
                txtComment.setText("");
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            }
        });

    }
}
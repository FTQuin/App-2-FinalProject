package com.example.finalproject.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finalproject.database.Post;
import com.example.finalproject.database.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository mRepository;

    private LiveData<List<Post>> mAllPosts;

    public PostViewModel(Application application) {
        super(application);
        mRepository = new PostRepository(application);
        mAllPosts = mRepository.getAllPosts();
    }

    LiveData<List<Post>> getAllPosts() { return mAllPosts; }

    public void insert(Post post) { mRepository.insert(post); }
}
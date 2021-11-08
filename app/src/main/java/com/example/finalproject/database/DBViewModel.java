package com.example.finalproject.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private DBRepository mRepository;

    private LiveData<List<Post>> mAllPosts;
    private LiveData<List<Comment>> mAllComments;

    public DBViewModel(Application application) {
        super(application);
        mRepository = new DBRepository(application);
        mAllPosts = mRepository.getAllPosts();
        mAllComments = mRepository.getAllComments();
    }

    LiveData<List<Post>> getAllPosts() { return mAllPosts; }
    LiveData<List<Comment>> getAllComments() { return mAllComments; }

    public void insertPost(Post post) { mRepository.insertPost(post); }
    public void insertComment(Comment comment) { mRepository.insertComment(comment); }
}
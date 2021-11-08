package com.example.finalproject.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private DBRepository mRepository;

    private LiveData<List<Post>> mAllPosts;

    public DBViewModel(Application application) {
        super(application);
        mRepository = new DBRepository(application);
        mAllPosts = mRepository.getAllPosts();
    }

    LiveData<List<Post>> getAllPosts() { return mAllPosts; }

    public void insert(Post post) { mRepository.insert(post); }
}
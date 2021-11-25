package com.example.anon.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private DBRepository mRepository;

    private LiveData<List<Post>> mAllPosts;
    private LiveData<List<Comment>> mAllComments;

    private String locality, subAdmin;

    public DBViewModel(Application application) {
        super(application);
    }

    //Initializes repository with received locality and subAdminArea.
    public void initRepository(){
        mRepository = new DBRepository(getApplication(), locality, subAdmin);
        mAllPosts = mRepository.getAllPosts();

        Log.d("viewModel", "Locations in view model: " + locality + subAdmin);
    }

    //Refreshes repository. Used after new post is inserted.
    public void refreshRepository(){
        mRepository = new DBRepository(getApplication(), locality, subAdmin);
    }

    public String getLocality(){
        return locality;
    }

    public String getSubAdmin(){
        return subAdmin;
    }

    //Receives location names.
    public void passLocation(String loc, String saa){
        this.locality = loc;
        this.subAdmin = saa;
    }

    public LiveData<List<Post>> getAllPosts() { return mAllPosts; }
    public LiveData<List<Comment>> getAllComments() { return mAllComments; }

    public LiveData<List<Comment>> getCommentsForPost(String postID){
        return mRepository.getCommentsForPost(postID);
    }

    public void votePost(String postId) {mRepository.votePost(postId);}

    public void insertPost(Post post) { mRepository.insertPost(post); }
    public void insertComment(Comment comment) { mRepository.insertComment(comment); }
}
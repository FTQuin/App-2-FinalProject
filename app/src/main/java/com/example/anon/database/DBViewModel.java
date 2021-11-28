package com.example.anon.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private DBRepository mRepository;

    private LiveData<List<Comment>> mAllComments;

    private String locality, subAdmin;

    public DBViewModel(Application application) {
        super(application);
        mRepository = new DBRepository(getApplication());
    }

    public String getLocality(){
        return locality;
    }

    public String getSubAdmin(){
        return subAdmin;
    }

    //Receives location data
    public void passLocation(String loc, String saa){
        if(!loc.equals(this.locality) || !saa.equals(this.subAdmin)) {
            this.locality = loc;
            this.subAdmin = saa;

            mRepository.passLocation(loc, saa);
        }
    }

    public String getNewKey(){
        return mRepository.getNewKey();
    }

    /*=========================================================================
    * Feed & Post Functions
    =========================================================================*/
    //Refreshes feed. Used after new post is inserted.
    public void refreshFeed(){
        mRepository.refreshFeed();
        //mRepository.loadPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return mRepository.getAllPosts();
    }

    public Post getPost(String postID){
        for(Post p : getAllPosts().getValue())
            if(p.getPostId().equals(postID))
                 return p;
        return null;
    }

    public void upVotePost(Post post) {
        mRepository.upVotePost(post);
    }
    public void downVotePost(Post post) {
        mRepository.downVotePost(post);
    }
    public void insertPost(Post post) { mRepository.insertPost(post); }

    /*=========================================================================
    * Comment Functions
    =========================================================================*/
    public LiveData<List<Comment>> getAllComments() { return mAllComments; }

    public LiveData<List<Comment>> getCommentsForPost(String postID){
        return mRepository.getCommentsForPost(postID);
    }

    public void refreshComments(){
        mRepository.refreshComments();
    }
    public void insertComment(Comment comment, Post post) {
        mRepository.insertComment(comment, post);
    }

    public void upVoteComment(Comment comment) {
        mRepository.upVoteComment(comment);
    }
    public void downVoteComment(Comment comment) {
        mRepository.downVoteComment(comment);
    }
}
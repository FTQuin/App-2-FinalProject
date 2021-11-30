/*==================================================================================================
* File: DBViewModel.java
* Description: View model class to pass commands/ data between view & repository.
* Authors: Shea Holden, Quin Adam
* Date: November 26, 2021
* Project: Anon
==================================================================================================*/
package com.anonApp.anon.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Arrays;
import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private final DBRepository mRepository;
    private String locality, subAdmin;

    public DBViewModel(Application application) {
        super(application);
        mRepository = new DBRepository(getApplication());
    }

    //Getters for location locality & sub admin area
    public String getLocality(){
        return locality;
    }
    public String getSubAdmin(){
        return subAdmin;
    }

    //Receives location data & passes it to repository
    public void passLocation(String loc, String saa){
        if(!Arrays.asList(new String[]{loc, saa}).contains(null) &&
            (!loc.equals(this.locality) || !saa.equals(this.subAdmin))
        ) {
            this.locality = loc;
            this.subAdmin = saa;

            mRepository.passLocation(loc, saa);
        }
    }

    //Requests new key from repository to use when creating new post or comment
    public String getNewKey(){
        return mRepository.getNewKey();
    }

    /*==============================================================================================
    * Feed & Post Functions
    ==============================================================================================*/
    //Send command to repository to get new posts for feed
    public void refreshFeed(){
        mRepository.refreshFeed();
    }

    //Send command to repository to get lists of all posts in device location
    public LiveData<List<Post>> getAllPosts() {
        return mRepository.getAllPosts();
    }

    //gets a single specified post
    public Post getPost(String postID){
        for(Post p : getAllPosts().getValue())
            if(p.getPostId().equals(postID))
                 return p;
        return null;
    }

    //Send up vote, down vote, an insert post command/ data to repository
    public void upVotePost(Post post) {
        mRepository.upVotePost(post);
    }
    public void downVotePost(Post post) {
        mRepository.downVotePost(post);
    }
    public void insertPost(Post post) {
        mRepository.insertPost(post);
    }

    /*==============================================================================================
    * Comment Functions
    ==============================================================================================*/
    //Sends command to repository to get list of comments for specified post
    public LiveData<List<Comment>> getCommentsForPost(String postID){
        return mRepository.getCommentsForPost(postID);
    }

    //Send command to repository to get new comments for post.
    public void refreshComments(){
        mRepository.refreshComments();
    }

    //Send up vote, down vote, and insert comment commands/ data to repository
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
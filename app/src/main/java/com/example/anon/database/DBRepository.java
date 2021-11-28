package com.example.anon.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DBRepository {

    private PostDao mPostDao;
    private CommentDao mCommentDao;
    private LiveData<List<Post>> mAllPosts;
    private LiveData<List<Comment>> mAllComments;

    private static DatabaseReference mRootRef;
    private static DatabaseReference mFeedRef;
    private static DatabaseReference mComRef;

    public DBRepository(Application application) {
        DBRoomDatabase db = DBRoomDatabase.getDatabase(application);
        mPostDao = db.postDao();
        mCommentDao = db.commentDao();
        mAllPosts = mPostDao.getAllPosts();
        mAllComments = mCommentDao.getAllComments();


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mComRef = mRootRef.child("comments");

        //TODO: set mFeedRef to subAdmin if no posts in locality
        /*mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(locality)){
                    mFeedRef = mRootRef.child("feeds").child(subAdmin).child(locality);
                }else{
                    mFeedRef = mRootRef.child("feeds").child(subAdmin);
                    //Need to then access all locality children to get posts from them.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("repo-error", "Failed to retrieve posts.", error.toException());
            }
        });*/

    }

    public void passLocation(String loc, String saa){
        if(loc==null) loc = "null";
        if(saa==null) saa = "null";
        mFeedRef = mRootRef.child("feeds").child(saa).child(loc);

        Log.d("repo", "Location in repository: " + loc + saa);
        refreshFeed();
    }

    //Provides new key for adding posts or comments to database.
    public String getNewKey(){
        return mRootRef.push().getKey();
    }

    /*==============================================================================================
    * Post Functionality
    ==============================================================================================*/
    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void refreshFeed(){
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Post p = data.getValue(Post.class);
                    new insertPostAsyncTask(mPostDao).execute(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("repo-error", "Failed to retrieve posts.", error.toException());
            }
        });
    }

    public void insertPost(Post post) {
        mFeedRef.child(post.getPostId()).setValue(post).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                new insertPostAsyncTask(mPostDao).execute(post);
                Log.d("===TESTING: NEW_POST===", "Publish successful.");
            }
        });

    }

    public void deletePost(String postId) {
        mPostDao.deletePost(postId);
    }

    public void votePost(Post post){
        post.setNumVotes(post.getNumVotes() + 1);
        //TODO: uncast
        int votes = (int) post.getNumVotes();
        mFeedRef.child(post.getPostId()).child("numVotes").setValue(votes).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                new votePostAsyncTask(mPostDao).execute(post.getPostId());
                Log.d("repository", "===TESTING: VOTE_POST=== Vote successful.");
            }
        });
    }

    //Async tasks
    private static class insertPostAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        insertPostAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("repository", "Init successful.");
        }
    }

    private static class votePostAsyncTask extends AsyncTask<String, Void, Void> {

        private PostDao mAsyncTaskDao;

        votePostAsyncTask(PostDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... postid) {
            Log.d("repo-test", "VotePostAsyncTask id: " + postid[0]);
            mAsyncTaskDao.votePost(postid[0]);
            return null;
        }
    }

    /*==============================================================================================
    * Comment Functionality
    ==============================================================================================*/
    public LiveData<List<Comment>> getAllComments() {
        return mAllComments;
    }

    public LiveData<List<Comment>> getCommentsForPost(String postID) {
        return mCommentDao.getCommentsForPost(postID);
    }

    //Initializes comments. Not called until post is clicked.
    //TODO: change this to initialize comments based off post ID.
    //Currently initialized ALL comments, but only loads post id based ones into room db.
    public void refreshComments(){
        mComRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Comment c = data.getValue(Comment.class);
                    new initCommentsAsyncTask(mCommentDao).execute(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("repo-error", "Failed to retrieve comments.", error.toException());
            }
        });
    }

    public void insertComment(Comment comment) {
        new insertCommentAsyncTask(mCommentDao).execute(comment);
    }

    public void insertComment(Comment comment, Post post) {
        post.setNumComments(post.getNumComments() + 1);

        mComRef.child(comment.getId()).setValue(comment).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                new insertCommentAsyncTask(mCommentDao).execute(comment);

                mFeedRef.child(post.getPostId()).child("numComments").setValue(post.getNumComments());
            }
        });
    }

    public void deleteComment(String commentId) {
        deleteComment(commentId);
    }

    //Async tasks
    private static class insertCommentAsyncTask extends AsyncTask<Comment, Void, Void> {

        private CommentDao mAsyncTaskDao;

        insertCommentAsyncTask(CommentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Comment... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class initCommentsAsyncTask extends AsyncTask<Comment, Void, Void> {

        private CommentDao mAsyncTaskDao;

        initCommentsAsyncTask(CommentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Comment... params) {
            mAsyncTaskDao.insert(params[0]);

            Log.d("commentinit", "Comments" + params[0]);

            return null;
        }
    }
}
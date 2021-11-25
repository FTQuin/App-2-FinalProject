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
        mFeedRef = mRootRef.child("feeds").child(saa).child(loc);

        Log.d("repo", "Location in repository: " + loc + saa);
        refreshFeed();
    }

    public void refreshFeed(){
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){

                    Post p = data.getValue(Post.class);

                    new initPostsAsyncTask(mPostDao).execute(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("repo-error", "Failed to retrieve posts.", error.toException());
            }
        });
    }

    //Initializes comments. Not called until post is clicked.
    //TODO: change this to initialize comments based off post ID.
    //Currently initialized ALL comments, but only loads post id based ones into room db.
    public void commentInit(){
        mComRef.addValueEventListener(new ValueEventListener() {
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

    /*===================================================================
    * Post Functionality
    ===================================================================*/
    public LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void insertPost(Post post) {
        new insertPostAsyncTask(mPostDao).execute(post);

    }

    public void deletePost(String postId) {
        mPostDao.deletePost(postId);
    }

    public void votePost(String postId){
        //mPostDao.votePost(postId);
        new votePostAsyncTask(mPostDao).execute(postId);
    }

    //Async tasks
    private static class insertPostAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        insertPostAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            // add to firebase first
            String id = params[0].getPostId();

            /*
            * if we create seperate "posts" child under root and store just id's under locality:
            * - change this to mRootRef.child("posts").child(id).....*/
            mFeedRef.child(id).setValue(params[0]).addOnCompleteListener(task -> {
            //mPostsRef.child(id).setValue(params[0]).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    // then add to local database
                    //TODO: Stop this from crashing app.
                    /*Moving it to onPostExecute function below d.i.b may have fixed it but
                     Apparently Async Task is depreciated. Switch to Java.util.concurrent? */
                    //mAsyncTaskDao.insert(params[0]);
                    Log.d("===TESTING: NEW_POST===", "Publish successful.");
                }
            });
            return null;
        }
    }

    private static class initPostsAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        initPostsAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        /*
         * if we create seperate "posts" child under root and store just id's under locality:
         * - mPostsRef.child(id).setValue(params[0]).addOnCompleteListener(task -> {....*/
        @Override
        protected Void doInBackground(final Post... params) {
            //mFeedRef.child()

            mAsyncTaskDao.insert(params[0]);

            return null;
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

    /*===================================================================
    * Comment Functionality
    ===================================================================*/
    public LiveData<List<Comment>> getAllComments() {
        return mAllComments;
    }

    public LiveData<List<Comment>> getCommentsForPost(String postID) {
        commentInit();
        return mCommentDao.getCommentsForPost(postID);
    }

    public void insertComment(Comment comment) {
        new insertCommentAsyncTask(mCommentDao).execute(comment);
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
            // add to firebase first
            Log.d("=TESTING: NEW_COMMENT=", "Trying to publish Comment.");
            mComRef.push().setValue(params[0]).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    // then add to local database
//                    mAsyncTaskDao.insert(params[0]);
                    Log.d("=TESTING: NEW_COMMENT=", "Publish successful.");
                }
                else Log.d("=TESTING: NEW_COMMENT=", "Publish not successful.");
            });
            Log.d("=TESTING: NEW_COMMENT=", "Publish done.");
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
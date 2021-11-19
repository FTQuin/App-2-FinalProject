package com.example.finalproject.database;

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
    private static DatabaseReference mPostRef;
    private static DatabaseReference mComRef;

    public DBRepository(Application application) {
        DBRoomDatabase db = DBRoomDatabase.getDatabase(application);
        mPostDao = db.postDao();
        mCommentDao = db.commentDao();
        mAllPosts = mPostDao.getAllPosts();
        mAllComments = mCommentDao.getAllComments();

        mRootRef = FirebaseDatabase.getInstance().getReference();;
        mPostRef = mRootRef.child("posts");
        mComRef = mRootRef.child("comments");

        // add data listener for firebase
        // posts
        mPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Post p = data.getValue(Post.class);
                    new initPostsAsyncTask(mPostDao).execute(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // comments
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
        mPostDao.votePost(postId);
    }

    private static class insertPostAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        insertPostAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            // add to firebase first
            mPostRef.push().setValue(params[0]).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    // then add to local database
                    mAsyncTaskDao.insert(params[0]);
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

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.insert(params[0]);

            return null;
        }
    }

    /*===================================================================
    * Comment Functionality
    ===================================================================*/
    public LiveData<List<Comment>> getAllComments() {
        return mAllComments;
    }

    public void insertComment(Comment comment) {
        new insertCommentAsyncTask(mCommentDao).execute(comment);
    }

    public void deleteComment(String commentId) {
        deleteComment(commentId);
    }

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

            return null;
        }
    }
}
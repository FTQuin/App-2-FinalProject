package com.example.finalproject.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DBRepository {

    private PostDao mPostDao;
    private CommentDao mCommentDao;
    private LiveData<List<Post>> mAllPosts;
    private LiveData<List<Comment>> mAllComments;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();;
    private DatabaseReference mPostRef = mRootRef.child("posts");
    private DatabaseReference mComRef = mRootRef.child("comments");

    DBRepository(Application application) {
        DBRoomDatabase db = DBRoomDatabase.getDatabase(application);
        mPostDao = db.postDao();
        mCommentDao = db.commentDao();
        mAllPosts = mPostDao.getAllPosts();
        mAllComments = mCommentDao.getAllComments();
    }

    /*===================================================================
    * Post Functionality
    ===================================================================*/
    LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void insertPost(Post post) {
        new insertPostAsyncTask(mPostDao).execute(post);
        mPostRef.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("===TESTING: NEW_POST===", "Publish successful.");

                }
            }
        });
    }

    public void deletePost(String postId) {
        deletePost(postId);
    }

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
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
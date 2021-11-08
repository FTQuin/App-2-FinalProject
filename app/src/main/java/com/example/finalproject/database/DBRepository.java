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

    private static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();;
    private static DatabaseReference mPostRef = mRootRef.child("posts");
    private static DatabaseReference mComRef = mRootRef.child("comments");

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
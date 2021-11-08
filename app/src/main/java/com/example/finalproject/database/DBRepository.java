package com.example.finalproject.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DBRepository {

    private PostDao mPostDao;
    private CommentDao mCommentDao;
    private LiveData<List<Post>> mAllPosts;
    private LiveData<List<Comment>> mAllComments;

    DBRepository(Application application) {
        DBRoomDatabase db = DBRoomDatabase.getDatabase(application);
        mPostDao = db.postDao();
        mCommentDao = db.commentDao();
        mAllPosts = mPostDao.getAllPosts();
        mAllComments = mCommentDao.getAllComments();
    }

    LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void insert (Post post) {
        new insertPostAsyncTask(mPostDao).execute(post);
    }

    public LiveData<List<Comment>> getAllComments() {
        return mAllComments;
    }

    public void insertPost(Post post) {
        new insertPostAsyncTask(mPostDao).execute(post);
    }

    public void insertComment(Comment comment) {
        new insertCommentAsyncTask(mCommentDao).execute(comment);
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
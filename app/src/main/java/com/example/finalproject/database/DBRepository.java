package com.example.finalproject.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DBRepository {

    private PostDao mPostDao;
    private LiveData<List<Post>> mAllPosts;

    DBRepository(Application application) {
        DBRoomDatabase db = DBRoomDatabase.getDatabase(application);
        mPostDao = db.postDao();
        mAllPosts = mPostDao.getAllPosts();
    }

    LiveData<List<Post>> getAllPosts() {
        return mAllPosts;
    }

    public void insert (Post post) {
        new insertAsyncTask(mPostDao).execute(post);
    }

    private static class insertAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        insertAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
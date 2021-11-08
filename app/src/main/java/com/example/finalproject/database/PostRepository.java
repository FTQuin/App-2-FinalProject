package com.example.finalproject.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.finalproject.database.Post;
import com.example.finalproject.database.PostDao;
import com.example.finalproject.database.PostRoomDatabase;

import java.util.List;

public class PostRepository {

    private PostDao mPostDao;
    private LiveData<List<Post>> mAllPosts;

    PostRepository(Application application) {
        PostRoomDatabase db = PostRoomDatabase.getDatabase(application);
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
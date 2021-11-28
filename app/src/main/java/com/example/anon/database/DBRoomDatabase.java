/*==================================================================================================
* File: DBRoomDatabase.java
* Description: ROOM database class to create new instance & populate device's local memory.
* Authors: Shea Holden, Quin Adam
* Date: November 26, 2021
* Project: Anon
==================================================================================================*/
package com.example.anon.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Post.class, Comment.class}, version = 13)
public abstract class DBRoomDatabase extends RoomDatabase {

    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

    private static DBRoomDatabase INSTANCE = null;

    static DBRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DBRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DBRoomDatabase.class, "database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more posts, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PostDao pDao;
        private final CommentDao cDao;

        PopulateDbAsync(DBRoomDatabase db) {
            pDao = db.postDao();
            cDao = db.commentDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            pDao.deleteAllPosts();
            cDao.deleteAllComments();

            return null;
        }
    }
}
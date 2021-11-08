package com.example.finalproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class}, version = 1)
public abstract class DBRoomDatabase extends RoomDatabase {

    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

    private static DBRoomDatabase INSTANCE;

    static DBRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DBRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DBRoomDatabase.class, "post_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
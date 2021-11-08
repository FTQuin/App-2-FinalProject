package com.example.finalproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDao {
    // The conflict strategy defines what happens,
    // if there is an existing entry.
    // The default action is ABORT.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    // Update multiple entries with one call.
    @Update
    public void updateComments(Comment... comments);

    // Simple query that does not take parameters and returns nothing.
    @Query("DELETE FROM comment_table")
    void deleteAll();

    // Simple query without parameters that returns values.
    @Query("SELECT * from comment_table ORDER BY comment ASC")
    LiveData<List<Comment>> getAllComments();

    // Query with parameter that returns a specific comment or comments.
    @Query("SELECT * FROM comment_table WHERE comment LIKE :comment ")
    public List<Comment> findComment(String comment);
}

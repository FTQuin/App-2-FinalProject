package com.anon.anon.database;

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

    @Query("DELETE FROM comment_table ")
    void deleteAllComments();

    // Simple query that does not take parameters and returns nothing.
    @Query("DELETE FROM comment_table WHERE id LIKE :com_id")
    void deleteComment(String com_id);

    // Simple query without parameters that returns values.
    @Query("SELECT * from comment_table ORDER BY numVotes ASC")
    LiveData<List<Comment>> getAllComments();

    @Query("SELECT * FROM comment_table WHERE postId LIKE :post_id ORDER BY id DESC")
    LiveData<List<Comment>> getCommentsForPost(String post_id);

    // Query with parameter that returns a specific comment or comments.
    @Query("SELECT * FROM comment_table WHERE id LIKE :search_id ")
    public List<Comment> findComment(String search_id);
}

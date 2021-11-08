package com.example.finalproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {

    // The conflict strategy defines what happens,
    // if there is an existing entry.
    // The default action is ABORT.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    // Update multiple entries with one call.
    @Update
    public void updatePosts(Post... posts);

    // Simple query that does not take parameters and returns nothing.
    @Query("DELETE FROM post_table")
    void deleteAll();

    // Simple query without parameters that returns values.
    @Query("SELECT * from post_table ORDER BY postId ASC")
    LiveData<List<Post>> getAllPosts();

    // Query with parameter that returns a specific post or posts.
    @Query("SELECT * FROM post_table WHERE postId LIKE :post ")
    public List<Post> findPost(String post);
}
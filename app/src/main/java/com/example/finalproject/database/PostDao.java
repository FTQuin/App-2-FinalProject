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
    void updatePosts(Post... posts);

    // Simple query that does not take parameters and returns nothing.
    @Query("DELETE FROM post_table WHERE postId LIKE :post ")
    void deletePost(String post);

    @Query("DELETE FROM post_table ")
    void deleteAllPosts();

    // Simple query without parameters that returns values.
    @Query("SELECT * from post_table ORDER BY postId DESC")
    LiveData<List<Post>> getAllPosts();

    // Query with parameter that returns a specific post or posts.
    @Query("SELECT * FROM post_table WHERE postId LIKE :post ")
    List<Post> findPost(String post);

    //Query to upvote post
    @Query("UPDATE post_table SET numVotes = (numVotes + 1) WHERE postId LIKE :post_id")
    void votePost(String post_id);
}
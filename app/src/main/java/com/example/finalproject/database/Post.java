package com.example.finalproject.database;

//Class to represent structure of post in database.

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "post_table")
public class Post {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "postId")

    private String postId;
    private String title;
    private String content;
    private LatLng location;
    private long numVotes;
    private long numComments;
    private String date;

    public Post(){};

    public Post(@NonNull String postId, String title, String content, LatLng location, String date, long numVotes, long numComments){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.location = location;
        this.date = date;
        this.numVotes = numVotes;
        this.numComments = numComments;
    }

    public String getPostId(){
        return postId;
    }
    public void setPostId(String postId){
        this.postId = postId;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public LatLng getLocation(){
        return location;
    }
    public void setLocation(LatLng location){
        this.location = location;
    }

    public long getNumVotes(){
        return numVotes;
    }
    public void setNumVotes(long numVotes){
        this.numVotes = numVotes;
    }

    public long getNumComments(){
        return numComments;
    }
    public void setNumComments(long numComments){
        this.numComments = numComments;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String toString(){
        return "Title: " + getTitle() +
                "\nContent: " + getContent() +
                "\nLocation: " + getLocation() +
                "\nDate: " + getDate() +
                "\nNumVotes: " + getNumVotes() +
                "\nNumComments: " + getNumComments();
    }
}

package com.example.finalproject.database;

//Class to represent structure of post in database.

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "post_table")
public class Post {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "postId")
    private String postId;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @NonNull
    @ColumnInfo(name = "content")
    private String content;
    @NonNull
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "numVotes")
    private long numVotes;
    @ColumnInfo(name = "numComments")
    private long numComments;
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @Ignore
    private LatLng location;

    public Post(){};

    public Post(@NonNull String postId, @NonNull String title, @NonNull String content, @NonNull LatLng location, @NonNull String date, long numVotes, long numComments){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.location = location;
        this.date = date;
        this.numVotes = numVotes;
        this.numComments = numComments;

        this.latitude = location.latitude;
        this.longitude = location.longitude;
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
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

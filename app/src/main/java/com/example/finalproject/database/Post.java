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
    @NonNull
    @ColumnInfo(name = "longitude")
    private double longitude;
    @NonNull
    @ColumnInfo(name = "numVotes")
    private long numVotes;
    @NonNull
    @ColumnInfo(name = "numComments")
    private long numComments;
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public Post(){};

    public Post(@NonNull String postId, @NonNull String title, @NonNull String content,
                @NonNull String date, @NonNull double lat, @NonNull double lon,
                @NonNull long numVotes, @NonNull long numComments){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.numVotes = numVotes;
        this.numComments = numComments;

        this.latitude = lat;
        this.longitude = lon;
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

    public void setLocation(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
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

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", numVotes=" + numVotes +
                ", numComments=" + numComments +
                ", date='" + date + '\'' +
                '}';
    }
}

package com.anon.anon.database;

//Class to represent structure of post in database.

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    @ColumnInfo(name = "locality")
    private String locality;
    @NonNull
    @ColumnInfo(name = "subAdminArea")
    private String subAdminArea;
    @NonNull
    @ColumnInfo(name = "numVotes")
    private int numVotes;
    @NonNull
    @ColumnInfo(name = "numComments")
    private int numComments;
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public Post(){}
    public Post(@NonNull String postId, @NonNull String title, @NonNull String content,
                @NonNull String date, @NonNull String locality, @NonNull String subAdminArea,
                @NonNull int numVotes, @NonNull int numComments){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.numVotes = numVotes;
        this.numComments = numComments;
        this.locality = locality;
        this.subAdminArea = subAdminArea;
    }

    @NonNull
    public String getPostId(){
        return postId;
    }
    public void setPostId(String postId){
        this.postId = postId;
    }

    @NonNull
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    @NonNull
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    @NonNull
    public String getLocality(){
        return locality;
    }
    public void setLocality(@NonNull String locality) {
        this.locality = locality;
    }

    @NonNull
    public String getSubAdminArea() {
        return subAdminArea;
    }
    public void setSubAdminArea(@NonNull String subAdminArea) {
        this.subAdminArea = subAdminArea;
    }

    public int getNumVotes(){
        return numVotes;
    }
    public void setNumVotes(int numVotes){
        this.numVotes = numVotes;
    }

    public int getNumComments(){
        return numComments;
    }
    public void setNumComments(int numComments){
        this.numComments = numComments;
    }

    @NonNull
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
                ", locality=" + locality +
                ", subAdminArea=" + subAdminArea +
                ", numVotes=" + numVotes +
                ", numComments=" + numComments +
                ", date='" + date + '\'' +
                '}';
    }
}

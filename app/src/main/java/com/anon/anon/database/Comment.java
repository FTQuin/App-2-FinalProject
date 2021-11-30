package com.anon.anon.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment_table")
public class Comment {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;
    @NonNull
    @ColumnInfo(name = "postId")
    private String postId;
    @NonNull
    @ColumnInfo(name = "content")
    private String content;
    @NonNull
    @ColumnInfo(name = "numVotes")
    private long numVotes;
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public Comment(){}

    public Comment(@NonNull String id, @NonNull String postId, @NonNull String content, @NonNull long numVotes, @NonNull String date){
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.numVotes = numVotes;
        this.date = date;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public long getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(long numVotes) {
        this.numVotes = numVotes;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", numVotes=" + numVotes +
                ", date='" + date + '\'' +
                '}';
    }
}

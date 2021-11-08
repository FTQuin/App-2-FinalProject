package com.example.finalproject.database;


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
    @ColumnInfo(name = "content")
    private String content;
    @NonNull
    @ColumnInfo(name = "numVotes")
    private long numVotes;
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public Comment(){}

    public Comment(@NonNull String id, @NonNull String content, @NonNull long numVotes, @NonNull String date){
        this.id = id;
        this.content = content;
        this.numVotes = numVotes;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(long numVotes) {
        this.numVotes = numVotes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

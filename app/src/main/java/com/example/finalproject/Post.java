package com.example.finalproject;

//Class to represent structure of post in database.

public class Post {

    private String title;
    private String content;
    private String location;
    private long numVotes;
    private long numComments;
    private String date;

    public Post(){};

    public Post(String title, String content, String location, String date, long numVotes, long numComments){
        this.title = title;
        this.content = content;
        this.location = location;
        this.date = date;
        this.numVotes = numVotes;
        this.numComments = numComments;
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

    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
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

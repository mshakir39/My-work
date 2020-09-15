package com.example.poetrious.Models;

public class Comment_list {
    String Name;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Comment_list(String name, String time, String commentId, String postId, String comment, String profile, String publisher) {
        Name = name;
        this.time = time;
        this.commentId = commentId;
        PostId = postId;
        this.comment = comment;
        this.profile = profile;
        this.publisher = publisher;
    }

    String commentId;
    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    String PostId;



    public Comment_list()
    {

    }

    String comment;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    String profile;
    String publisher;
}

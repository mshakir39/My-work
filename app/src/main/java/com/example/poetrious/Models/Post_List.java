package com.example.poetrious.Models;

public class Post_List {


    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public Post_List(String post_time, String name_usr, String profile_img_Download_link, String comment, String publisher, String post_id, String pub_pic, String user_Name, String img_post, String description, String post_image, String usr_id, String publisher1) {
        this.post_time = post_time;
        Name_usr = name_usr;
        Profile_img_Download_link = profile_img_Download_link;
        this.comment = comment;
        this.publisher = publisher;
        this.post_id = post_id;
        this.pub_pic = pub_pic;
        User_Name = user_Name;
        this.img_post = img_post;
        Description = description;
        this.post_image = post_image;
        this.usr_id = usr_id;
        Publisher = publisher1;
    }

    String post_time;


    public String getProfile_img_Download_link() {
        return Profile_img_Download_link;
    }

    public void setProfile_img_Download_link(String profile_img_Download_link) {
        Profile_img_Download_link = profile_img_Download_link;
    }
    public Post_List()
    {

    }

    public String getName_usr() {
        return Name_usr;
    }

    public void setName_usr(String name_usr) {
        Name_usr = name_usr;
    }

    String Name_usr;
    String Profile_img_Download_link;
    public String getImg_post() {
        return img_post;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getPub_pic() {
        return pub_pic;
    }

    public void setPub_pic(String pub_pic) {
        this.pub_pic = pub_pic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String comment;



    String publisher;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    String post_id;
    String pub_pic;
    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    String User_Name;
    public void setImg_post(String img_post) {
        this.img_post = img_post;
    }

    String img_post;
    public String getDescription() {

        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    String Description;




    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }



    private String post_image;



    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    private String usr_id;

    private String Publisher;
}

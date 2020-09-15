package com.example.poetrious.Models;

public class post {
public post()
{

}
    public String getProfile_img_Download_link() {
        return Profile_img_Download_link;
    }

    public void setProfile_img_Download_link(String profile_img_Download_link) {
        Profile_img_Download_link = profile_img_Download_link;
    }

    String Profile_img_Download_link;
    public String getImg_post() {
        return img_post;
    }

    public String getUser_Name() {
        return User_Name;
    }

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

    public post(String img_post, String description, String post_image, String usr_id, String publisher) {
        this.img_post = img_post;
        Description = description;
        this.post_image = post_image;
        this.usr_id = usr_id;
        Publisher = publisher;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    private String usr_id;

    private String Publisher;


}

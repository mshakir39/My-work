package com.example.poetrious.Models;

public class User_data {
    String Gender;

    public User_data(String gender, String profile_img_Download_link, String user_DOB, String user_Email, String user_Name, String user_Number) {
        Gender = gender;
        Profile_img_Download_link = profile_img_Download_link;
        User_DOB = user_DOB;
        User_Email = user_Email;
        User_Name = user_Name;
        User_Number = user_Number;
    }
public User_data()
{}

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProfile_img_Download_link() {
        return Profile_img_Download_link;
    }

    public void setProfile_img_Download_link(String profile_img_Download_link) {
        Profile_img_Download_link = profile_img_Download_link;
    }

    public String getUser_DOB() {
        return User_DOB;
    }

    public void setUser_DOB(String user_DOB) {
        User_DOB = user_DOB;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Number() {
        return User_Number;
    }

    public void setUser_Number(String user_Number) {
        User_Number = user_Number;
    }

    String Profile_img_Download_link;
    String User_DOB;
    String User_Email;
    String User_Name;
    String User_Number;

    public User_data(String gender, String profile_img_Download_link, String user_DOB, String user_Email, String user_Name, String user_Number, String user_ID) {
        Gender = gender;
        Profile_img_Download_link = profile_img_Download_link;
        User_DOB = user_DOB;
        User_Email = user_Email;
        User_Name = user_Name;
        User_Number = user_Number;
        User_ID = user_ID;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    String User_ID;
}

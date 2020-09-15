package com.example.poetrious.Models;

public class Search_item {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Search_item(String name, String uid, String img) {
        this.name = name;
        this.uid = uid;
        this.img = img;
    }

    public Search_item(String name, String img) {
        this.name = name;
        this.img = img;
    }

    private String name;



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private  String uid;
    private String img;
}

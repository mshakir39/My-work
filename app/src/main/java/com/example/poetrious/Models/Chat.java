package com.example.poetrious.Models;

public class Chat {
    public Chat()
    {

    }
    public Chat(String msg, String receiver, String sender) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = msg;
    }

    String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    String receiver;
    String message;

}

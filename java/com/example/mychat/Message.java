package com.example.mychat;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String text;
    private String senderName;
    @ServerTimestamp
    private Date timestamp;

    public Message() {
        // Required empty constructor for Firestore
    }

    public Message(String text, String senderName, Date timestamp) {
        this.text = text;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

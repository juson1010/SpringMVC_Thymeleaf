package com.juson.springMVC.dto;

import java.util.Date;

/**
 * Created by qianliang on 26/7/2016.
 */
public class Message {
    private String message;
    private Date date;

    public Message() {
        this.date = new Date();
    }

    public Message(String message) {
        this.message = message;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

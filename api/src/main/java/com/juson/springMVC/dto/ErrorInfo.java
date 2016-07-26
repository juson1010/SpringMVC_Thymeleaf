package com.juson.springMVC.dto;

import java.util.Date;

/**
 * Created by qianliang on 26/7/2016.
 */
public class ErrorInfo {
    private String message;
    private Date date;

    public ErrorInfo() {
        this.date = new Date();
    }

    public ErrorInfo(String message) {
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

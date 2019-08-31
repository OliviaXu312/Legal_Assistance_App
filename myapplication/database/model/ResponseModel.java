package com.example.joan.myapplication.database.model;

import java.io.Serializable;

public class ResponseModel implements Serializable {

    private String date;
    private String content;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

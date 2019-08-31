package com.example.joan.myapplication.database.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CounselingModel implements Serializable{
    private String create_time;
    private String question;
    private List<String> pictures;
    private List<ResponseModel> response;

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ResponseModel> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseModel> response) {
        this.response = response;
    }
}

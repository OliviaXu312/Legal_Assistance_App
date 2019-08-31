package com.example.joan.myapplication.database.model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class JudgementModel implements Serializable {
    private String Id;
    private String jId;
    private String jDate;
    private String jReason;
    private String jContent;
    private List<Document> jRelavent;
    private List<Document> jPrevious;
    private String jLaws;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getjId() {
        return jId;
    }

    public void setjId(String jId) {
        this.jId = jId;
    }

    public String getjDate() {
        return jDate;
    }

    public void setjDate(String jDate) {
        this.jDate = jDate;
    }

    public String getjReason() {
        return jReason;
    }

    public void setjReason(String jReason) {
        this.jReason = jReason;
    }

    public String getjContent() {
        return jContent;
    }

    public void setjContent(String jContent) {
        this.jContent = jContent;
    }

    public List<Document> getjRelavent() {
        return jRelavent;
    }

    public void setjRelavent(List<Document> jRelavent) {
        this.jRelavent = jRelavent;
    }

    public List<Document> getjPrevious() {
        return jPrevious;
    }

    public void setjPrevious(List<Document> jPrevious) {
        this.jPrevious = jPrevious;
    }

    public String getjLaws() {
        return jLaws;
    }

    public void setjLaws(String jLaws) {
        this.jLaws = jLaws;
    }
}

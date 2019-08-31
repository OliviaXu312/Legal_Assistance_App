package com.example.joan.myapplication.database.model;
import java.io.Serializable;

public class LawModel implements Serializable{
    private String id;
    private String article;
    private String name;
    private String content;
    private String start;
    private String end;
    private String abandon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getAbandon() {
        return abandon;
    }

    public void setAbandon(String abandon) {
        this.abandon = abandon;
    }
}


package com.example.joan.myapplication.database.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class QuickResponseModel implements Serializable {
    private ObjectId id;
    private RegisterModel author;
    private String content;
    private Date createTime;
    private List<ReplyModel> replies;
    private int viewCount;

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public RegisterModel getAuthor() {
        return author;
    }

    public void setAuthor(RegisterModel author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ReplyModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyModel> replies) {
        this.replies = replies;
    }
}

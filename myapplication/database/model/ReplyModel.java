package com.example.joan.myapplication.database.model;

import org.w3c.dom.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReplyModel implements Serializable {
    private RegisterModel answerer;
    private Date createTime;
    private Document content;
    private List<ReplyModel> replies;

    public RegisterModel getAnswerer() {
        return answerer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setAnswerer(RegisterModel answerer) {
        this.answerer = answerer;
    }

    public Document getContent() {
        return content;
    }

    public void setContent(Document content) {
        this.content = content;
    }

    public List<ReplyModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyModel> replies) {
        this.replies = replies;
    }
}

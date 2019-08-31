package com.example.joan.myapplication.database.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class QuickConsultModel {

    private ObjectId id;
    private ObjectId author;
    private String content;
    private String[] picture;
    private String author_name;
    private Date create_time;
    private String date;
    private List<Reply> lawyer_reply;
    private List<Reply> user_comment;
    private int view_count;
    private int like;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getAuthor() {
        return author;
    }

    public void setAuthor(ObjectId auther) {
        this.author = auther;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getPicture() {
        return picture;
    }

    public void setPicture(String[] picture) {
        this.picture = picture;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public List<Reply> getLawyer_reply() {
        return lawyer_reply;
    }

    public void setLawyer_reply(List<Reply> lawyer_reply) {
        this.lawyer_reply = lawyer_reply;
    }

    public List<Reply> getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(List<Reply> user_comment) {
        this.user_comment = user_comment;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public class Reply{
        private int index;
        private ObjectId id;
        private ObjectId author;
        private Date create_time;
        private String date;
        private String content;
        private String author_name;
        private List<String> replies;
        private ObjectId parent;
        private int like;
        private int parent_index;
        private boolean is;

        public Reply(){

        }

        public ObjectId getId() {
            return id;
        }

        public void setId(ObjectId id) {
            this.id = id;
        }

        public ObjectId getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = new ObjectId(author);
        }

        public Date getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Date create_time) {
            this.create_time = create_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getReplies() {
            return replies;
        }

        public void setReplies(List<String> replies) {
            this.replies = replies;
        }

        public ObjectId getParent() {
            return parent;
        }

        public void setParent(ObjectId parent) {
            this.parent = parent;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isIs() {
            return is;
        }

        public void setIs(boolean is) {
            this.is = is;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getParent_index() {
            return parent_index;
        }

        public void setParent_index(int parent_index) {
            this.parent_index = parent_index;
        }
    }

}

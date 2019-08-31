package com.example.joan.myapplication.database.model;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class LawyerModel implements Serializable {
    private String id;
    private String regMsg;
    private String name;
    private String job;
    private String company;
    private String major;
    private String education;
    private String experience;
    private String description;
    private int price;
    private List<LegalCounselingModel> counselingList;
    private double comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegMsg() {
        return regMsg;
    }

    public void setRegMsg(String regMsg) {
        this.regMsg = regMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<LegalCounselingModel> getCounselingList() {
        return counselingList;
    }

    public void setCounselingList(List<LegalCounselingModel> counselingList) {
        this.counselingList = counselingList;
    }

    public double getComment() {
        return comment;
    }

    public void setComment(double comment) {
        this.comment = comment;
    }
}

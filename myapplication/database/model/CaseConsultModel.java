package com.example.joan.myapplication.database.model;

import com.example.joan.myapplication.database.model.JudgementModel;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class CaseConsultModel {

    private ObjectId id;
    private String case_id;
    private String content;
    private String result;
    private List<String> imglst;
    private List<Document> similar;
    private List<Document> refer;
    private List<JudgementModel> judgementModels;
    private List<LawModel> lawModels;
    private String createTime;
    private int state;

    public List<String> getImglst() {
        return imglst;
    }

    public void setImglst(List<String> imglst) {
        this.imglst = imglst;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Document> getSimilar() {
        return similar;
    }

    public void setSimilar(List<Document> similar) {
        this.similar = similar;
    }

    public List<Document> getRefer() {
        return refer;
    }

    public void setRefer(List<Document> refer) {
        this.refer = refer;
    }

    public List<JudgementModel> getJudgementModels() {
        return judgementModels;
    }

    public void setJudgementModels(List<JudgementModel> judgementModels) {
        this.judgementModels = judgementModels;
    }

    public List<LawModel> getLawModels() {
        return lawModels;
    }

    public void setLawModels(List<LawModel> lawModels) {
        this.lawModels = lawModels;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

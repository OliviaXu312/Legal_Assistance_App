package com.example.joan.myapplication.database.model;

public class Case {

    private String ID;
    private String userID;
    private String brief;
    private String caseDate;
    private String caseTime;
    private String lawyerID;
    private int visit;

    public Case (){

    }

    public Case(String ID, String userID, String brief, String caseDate, String caseTime, String lawyerID){

        this.ID = ID;
        this.userID = userID;
        this.brief = brief;
        this.caseDate = caseDate;
        this.caseTime = caseTime;
        this.lawyerID = lawyerID;

    }

    public Case(String ID, String userID, String brief, String caseDate, String caseTime, String lawyerID, int visit){

        this.ID = ID;
        this.userID = userID;
        this.brief = brief;
        this.caseDate = caseDate;
        this.caseTime = caseTime;
        this.lawyerID = lawyerID;
        this.visit = visit;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public String getCaseTime() {
        return caseTime;
    }

    public void setCaseTime(String caseTime) {
        this.caseTime = caseTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLawyerID() {
        return lawyerID;
    }

    public void setLawyerID(String lawyerID) {
        this.lawyerID = lawyerID;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }
}

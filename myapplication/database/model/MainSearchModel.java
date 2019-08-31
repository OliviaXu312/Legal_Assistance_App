package com.example.joan.myapplication.database.model;

import java.io.Serializable;
import java.util.List;

public class MainSearchModel implements Serializable {

    private List<LawModel> law;
    private List<LawyerModel> lawyer;
    private List<LawFirmModel> firm;
    private List<LegalCounselingModel> counsel;

    public List<LawModel> getLaw() {
        return law;
    }

    public void setLaw(List<LawModel> law) {
        this.law = law;
    }

    public List<LawyerModel> getLawyer() {
        return lawyer;
    }

    public void setLawyer(List<LawyerModel> lawyer) {
        this.lawyer = lawyer;
    }

    public List<LawFirmModel> getFirm() {
        return firm;
    }

    public void setFirm(List<LawFirmModel> firm) {
        this.firm = firm;
    }

    public List<LegalCounselingModel> getCounsel() {
        return counsel;
    }

    public void setCounsel(List<LegalCounselingModel> counsel) {
        this.counsel = counsel;
    }
}

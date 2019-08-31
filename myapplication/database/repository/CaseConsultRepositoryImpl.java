package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.CaseConsultModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaseConsultRepositoryImpl {

    public List<CaseConsultModel> convert(JSONArray s){
        List<CaseConsultModel> counselings = new ArrayList<CaseConsultModel>();
        for (int i = 0 ; i < s.size(); i++) {
            JSONObject a = s.getJSONObject(i);
            final CaseConsultModel counseling = new CaseConsultModel();

            counseling.setCase_id(a.getString("id"));
            counseling.setContent(a.getString("content"));
            counseling.setState(a.getInt("state"));
            counseling.setCreateTime(a.getString("create_time").replace("T"," "));
            counselings.add(counseling);
        }
        return counselings;
    }

}

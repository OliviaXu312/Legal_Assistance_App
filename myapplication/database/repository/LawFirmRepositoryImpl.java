package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.MongoDBUtil;
import com.example.joan.myapplication.database.model.LawFirmModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LawFirmRepositoryImpl implements LawFirmRepository {
    MongoDBUtil mongoDb = new MongoDBUtil("wxby");
    MongoCollection<Document> collection = mongoDb.getCollection("law_firm");

    //检索所有文档
    public List<LawFirmModel> findAll(){
        MongoCursor<Document> cursor = collection.find().limit(50).iterator();
        List<LawFirmModel> lawFirmList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                LawFirmModel lawFirm = new LawFirmModel();
                Document current_cursor = cursor.next();
//                lawFirm.setId(current_cursor.getObjectId("_id"));
                lawFirm.setType(current_cursor.getString("firm_type"));
                lawFirm.setDescription(current_cursor.getString("firm_dscrpt"));
                lawFirm.setEmployee(current_cursor.getString("firm_employee"));
                lawFirm.setCapital(current_cursor.getString("firm_capital"));
                lawFirm.setContact(current_cursor.getString("firm_contact"));
                lawFirm.setAddress(current_cursor.getString("firm_addr"));
                lawFirm.setPhone(current_cursor.getString("firm_phone"));
                lawFirm.setFax(current_cursor.getString("firm_fax"));
                lawFirm.setSource_url(current_cursor.getString("firm_url"));
                lawFirm.setIntroduction(current_cursor.getString("firm_intro"));
                lawFirm.setMajor(current_cursor.getString("firm_major"));
                lawFirm.setSource_url(current_cursor.getString("url"));
                lawFirmList.add(lawFirm);
            }
        } finally {
            cursor.close();
        }
        return lawFirmList;
    }

    //有条件的检索文档
    private List<LawFirmModel> find(Document condition)
    {
        MongoCursor<Document> cursor = collection.find(condition).limit(50).iterator();
        List<LawFirmModel> lawFirmList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                LawFirmModel lawFirm = new LawFirmModel();
                Document current_cursor = cursor.next();
//                lawFirm.setId(current_cursor.getObjectId("_id"));
                lawFirm.setType(current_cursor.getString("firm_type"));
                lawFirm.setDescription(current_cursor.getString("firm_dscrpt"));
                lawFirm.setEmployee(current_cursor.getString("firm_employee"));
                lawFirm.setCapital(current_cursor.getString("firm_capital"));
                lawFirm.setContact(current_cursor.getString("firm_contact"));
                lawFirm.setAddress(current_cursor.getString("firm_addr"));
                lawFirm.setPhone(current_cursor.getString("firm_phone"));
                lawFirm.setFax(current_cursor.getString("firm_fax"));
                lawFirm.setSource_url(current_cursor.getString("firm_url"));
                lawFirm.setIntroduction(current_cursor.getString("firm_intro"));
                lawFirm.setMajor(current_cursor.getString("firm_major"));
                lawFirm.setSource_url(current_cursor.getString("url"));
                lawFirmList.add(lawFirm);
            }
        } finally {
            cursor.close();
        }
        return lawFirmList;
    }

    //以id检索文档
    public LawFirmModel findById(ObjectId code){
        Document condition = new Document();
        condition.append("_id", code);
        return find(condition).get(0);
    }

    //以关键字检索
    public List<LawFirmModel> findByCondition(String keyWord){
        List<Document> condition = new ArrayList<>();
        //设置正则表达
        Pattern regular = Pattern.compile("(?i)" + keyWord + ".*$", Pattern.MULTILINE);
        condition.add(new Document("firm_type" , regular));
        condition.add(new Document("firm_dscrpt" , regular));
        condition.add(new Document("firm_intro" , regular));
        condition.add(new Document("firm_major" , regular));
        return find(new Document("$or",condition));
    }

    public List<LawFirmModel> convert(JSONArray s){
        List<LawFirmModel> firms = new ArrayList<LawFirmModel>();
        for (int i = 0 ; i < s.size(); i++) {
            try {
                JSONObject a = s.getJSONObject(i);

                LawFirmModel lawFirm = new LawFirmModel();
                lawFirm.setId( a.getString("_id"));
                lawFirm.setName(a.getString("firm_name"));
                lawFirm.setType(a.getString("firm_type"));
                lawFirm.setDescription(a.getString("firm_dscrpt"));
                lawFirm.setEmployee(a.getString("firm_employee"));
                lawFirm.setCapital(a.getString("firm_capital"));
                lawFirm.setContact(a.getString("firm_contact"));
                lawFirm.setAddress(a.getString("firm_addr").replaceAll("地圖", ""));
                lawFirm.setPhone(a.getString("firm_phone"));
                lawFirm.setFax(a.getString("firm_fax"));
                lawFirm.setSource_url(a.getString("firm_url"));
                lawFirm.setIntroduction(a.getString("firm_intro"));
                lawFirm.setMajor(a.getString("firm_major"));
                lawFirm.setSource_url(a.getString("url"));
                firms.add(lawFirm);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return firms;
    }

}

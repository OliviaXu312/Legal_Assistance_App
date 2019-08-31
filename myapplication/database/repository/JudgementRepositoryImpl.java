package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.JudgementModel;
import com.example.joan.myapplication.database.MongoDBUtil;
import com.example.joan.myapplication.database.model.LawyerModel;
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

public class JudgementRepositoryImpl implements JudgementRepository{
    MongoDBUtil mongoDb = new MongoDBUtil("wxbyt");
    MongoCollection<Document> collection = mongoDb.getCollection("judgement");

    //检索所有文档
    public List<JudgementModel> findAll()
    {
        MongoCursor<Document> cursor = collection.find().limit(50).iterator();
        List<JudgementModel> judgementList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                JudgementModel judgement = new JudgementModel();
                Document current_cursor = cursor.next();
//                judgement.setId(current_cursor.getObjectId("_id"));
                judgement.setjId(current_cursor.getString("j_id"));
                judgement.setjDate(current_cursor.getString("j_date"));
                judgement.setjReason(current_cursor.getString("j_reason"));
                judgement.setjContent(current_cursor.getString("j_content"));
                judgement.setjRelavent(current_cursor.get("j_relevant",new ArrayList<Document>()));
                judgement.setjPrevious(current_cursor.get("j_previous",new ArrayList<Document>()));
                judgement.setjLaws(current_cursor.getString("j_laws"));
                judgementList.add(judgement);
            }
        } finally {
            cursor.close();
        }
        return judgementList;
    }

    //有条件的检索文档
    private List<JudgementModel> find(Document condition)
    {
        MongoCursor<Document> cursor = collection.find(condition).limit(50).iterator();
        List<JudgementModel> judgementList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                JudgementModel judgement = new JudgementModel();
                Document current_cursor = cursor.next();
//                judgement.setId(current_cursor.getObjectId("_id"));
                judgement.setjId(current_cursor.getString("j_id"));
                judgement.setjDate(current_cursor.getString("j_date"));
                judgement.setjReason(current_cursor.getString("j_reason"));
                judgement.setjContent(current_cursor.getString("j_content"));
                judgement.setjRelavent(current_cursor.get("j_relevant",new ArrayList<Document>()));
                judgement.setjPrevious(current_cursor.get("j_previous",new ArrayList<Document>()));
                judgement.setjLaws(current_cursor.getString("j_laws"));
                judgementList.add(judgement);
            }
        } finally {
            cursor.close();
        }
        return judgementList;
    }

    //以id检索文档
    public JudgementModel findById(ObjectId code) {
        Document condition = new Document();
        condition.append("_id", code);
        return find(condition).get(0);
    }

    //以关键字检索
    public  List<JudgementModel> findByCondition(String keyWord)
    {
        List<Document> condition = new ArrayList<>();
        //设置正则表达
        Pattern regular = Pattern.compile("(?i)" + keyWord + ".*$", Pattern.MULTILINE);
        condition.add(new Document("j_reason" , regular));
        condition.add(new Document("j_content" , regular));
        condition.add(new Document("j_laws" , regular));
        return find(new Document("$or",condition));
    }

    public List<JudgementModel> convert(JSONArray s){
        List<JudgementModel> judgementList = new ArrayList<>();
        for (int i = 0 ; i < s.size(); i++) {
            try {
                JSONObject a = s.getJSONObject(i);

                JudgementModel judgement = new JudgementModel();
                judgement.setId(a.getString("_id"));
                judgement.setjId(a.getString("j_id"));
                judgement.setjReason(a.getString("j_reason"));
                judgement.setjContent(a.getString("j_content"));
//                judgement.setjRelavent(a.get("j_relevant",new ArrayList<Document>()));
//                judgement.setjPrevious(a.get("j_previous",new ArrayList<Document>()));
                judgementList.add(judgement);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return judgementList;

    }

    public JudgementModel convertSingle(JSONObject s){
        JudgementModel judgement = new JudgementModel();
        try {
            judgement.setId(s.getString("_id"));
            judgement.setjId(s.getString("j_id"));
            judgement.setjDate(s.getString("j_date"));
            judgement.setjReason(s.getString("j_reason"));
            judgement.setjContent(s.getString("j_content"));
//                judgement.setjRelavent(a.get("j_relevant",new ArrayList<Document>()));
//                judgement.setjPrevious(a.get("j_previous",new ArrayList<Document>()));
            judgement.setjLaws(s.getString("j_laws"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return judgement;

    }

}

package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.MongoDBUtil;
import com.example.joan.myapplication.database.model.LawFirmModel;
import com.example.joan.myapplication.database.model.LawModel;
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

public class LawRepositoryImpl implements LawRepository {
    MongoDBUtil mongoDb = new MongoDBUtil("wxby");
    MongoCollection<Document> collection = mongoDb.getCollection("law");

    //检索所有文档
    public List<LawModel> findAll()
    {
        MongoCursor<Document> cursor = collection.find().limit(50).iterator();
        List<LawModel> lawList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                LawModel law = new LawModel();
                Document current_cursor = cursor.next();
//                law.setId(current_cursor.getObjectId("_id"));
//                law.setBook(current_cursor.getString("book"));
//                law.setChapter(current_cursor.getString("chapter"));
                law.setName(current_cursor.getString("name"));
                law.setContent(current_cursor.getString("content"));
                law.setStart(current_cursor.getString("start"));
//                law.setEnd(current_cursor.getString("end"));
                lawList.add(law);
            }
        } finally {
            cursor.close();
        }
        return lawList;
    }

    //有条件的检索文档
    private List<LawModel> find(Document condition)
    {
        MongoCursor<Document> cursor = collection.find(condition).limit(50).iterator();
        List<LawModel> lawList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                LawModel law = new LawModel();
                Document current_cursor = cursor.next();
//                law.setId(current_cursor.getObjectId("_id"));
//                law.setBook(current_cursor.getString("book"));
//                law.setChapter(current_cursor.getString("chapter"));
                law.setName(current_cursor.getString("name"));
                law.setContent(current_cursor.getString("content"));
                law.setStart(current_cursor.getString("start"));
//                law.setEnd(current_cursor.getString("end"));
                lawList.add(law);
            }
        } finally {
            cursor.close();
        }
        return lawList;
    }

    //以id检索文档
    public LawModel findById(ObjectId code){
        Document condition = new Document();
        condition.append("_id", code);
        return find(condition).get(0);
    }

    //以关键字检索
    public List<LawModel> findByCondition(String keyWord){
        List<Document> condition = new ArrayList<>();
        //设置正则表达
        Pattern regular = Pattern.compile("(?i)" + keyWord + ".*$", Pattern.MULTILINE);
        condition.add(new Document("book" , regular));
        condition.add(new Document("content", regular));
        return find(new Document("$or",condition));
    }

    public List<LawModel> convert(JSONArray s){
        List<LawModel> laws = new ArrayList<>();
        for (int i = 0 ; i < s.size(); i++) {
            try {
                JSONObject a = s.getJSONObject(i);

                LawModel law = new LawModel();
                law.setId(a.getString("_id"));
                law.setStart(a.getString("start"));
                law.setAbandon(a.getString("abandon"));
                law.setName(a.getString("name"));
                law.setArticle(a.getString("article"));
                law.setContent(a.getString("content"));
                laws.add(law);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return laws;
    }


}

package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.CourtModel;
import com.example.joan.myapplication.database.MongoDBUtil;
import com.example.joan.myapplication.database.model.LawFirmModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

public class CourtRepositoryImpl implements CourtRepository{
    MongoDBUtil mongoDb = new MongoDBUtil("wxby");
    MongoCollection<Document> collection = mongoDb.getCollection("court");

    //检索所有文档
    public List<CourtModel> findAll()
    {
        MongoCursor<Document> cursor = collection.find().limit(50).iterator();
        List<CourtModel> courtList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                CourtModel court = new CourtModel();
                Document current_cursor = cursor.next();
//                court.setId(current_cursor.getObjectId("_id"));
                court.setName(current_cursor.getString("name"));
                court.setSimpleName(current_cursor.getString("simple_name"));
                court.setCode(current_cursor.getString("code"));
                courtList.add(court);
            }
        } finally {
            cursor.close();
        }
        return courtList;
    }

    //有条件的检索文档
    private List<CourtModel> find(Document condition)
    {
        MongoCursor<Document> cursor = collection.find(condition).limit(50).iterator();
        List<CourtModel> courtList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                CourtModel court = new CourtModel();
                Document current_cursor = cursor.next();

//                court.setId(current_cursor.getObjectId("_id"));
                court.setName(current_cursor.getString("name"));
                court.setSimpleName(current_cursor.getString("simple_name"));
                court.setCode(current_cursor.getString("code"));
                courtList.add(court);
            }
        } finally {
            cursor.close();
        }
        return courtList;
    }

    //以id检索文档
    public CourtModel findById(ObjectId code) {
        Document condition = new Document();
        condition.append("_id", code);
        return find(condition).get(0);
    }

    //以关键字检索
    public  List<CourtModel> findByCondition(String keyWord)
    {
        List<Document> condition = new ArrayList<>();
        //设置正则表达
        Pattern regular = Pattern.compile("(?i)" + keyWord + ".*$", Pattern.MULTILINE);
        condition.add(new Document("name" , regular));
        condition.add(new Document("simple_name", regular));
        condition.add(new Document("code" , regular));
        return find(new Document("$or",condition));
    }

    public List<CourtModel> convert(JSONArray s){
        List<CourtModel> courts = new ArrayList<CourtModel>();
        for (int i = 0 ; i < s.size(); i++) {
            try {
                JSONObject a = s.getJSONObject(i);
                CourtModel court = new CourtModel();
                court.setId(a.getString("_id"));
                court.setName(a.getString("name"));
                court.setSimpleName(a.getString("simple_name"));
                court.setCode(a.getString("code"));
                courts.add(court);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return courts;
    }

}

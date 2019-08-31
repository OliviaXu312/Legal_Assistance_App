package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.MongoDBUtil;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.RegisterModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class RegisterRepositoryImpl implements RegisterRepository {
    MongoDBUtil mongoDb = new MongoDBUtil("wxby");
    MongoCollection<org.bson.Document> collection = mongoDb.getCollection("register");

    //添加文档
    public void create(RegisterModel user){
        Document register = new Document();
        register.append("reg_id", user.getRegId());
        register.append("phone", user.getPhone());
        register.append("name", user.getName());
        register.append("sex", user.getSex());
        register.append("reg_date", new Date());
        register.append("home_addr", user.getHomeAddr());
        register.append("password", user.getPassword());
        register.append("role", 1);
        register.append("status", 1);
        register.append("kill_time", null);
        collection.insertOne(register);
    }

    //更新文档
    public void update(RegisterModel user){
        Document registerOld = new Document();
        registerOld.append("_id",user.getId());

        Document registerNew = new Document();
        registerNew.append("phone", user.getPhone());
        registerNew.append("name", user.getName());
        registerNew.append("sex", user.getSex());
        registerNew.append("home_addr", user.getHomeAddr());
        registerNew.append("password", user.getPassword());
        registerNew.append("status", user.getStatus());
        registerNew.append("kill_time", user.getKillTime());
        Document registerNewSet = new Document("$set", registerNew);
        collection.updateOne(registerOld,registerNewSet);
    }

    //检索所有文档
    public List<RegisterModel> findAll(){
        MongoCursor<Document> cursor = collection.find().limit(50).iterator();
        List<RegisterModel> registerList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                RegisterModel register = new RegisterModel();
                Document current_cursor = cursor.next();
//                register.setId(current_cursor.getObjectId("_id"));
                register.setRegId(current_cursor.getString("reg_id"));
                register.setPhone(current_cursor.getString("phone"));
                register.setName(current_cursor.getString("name"));
                register.setSex(current_cursor.getString("sex"));
                register.setRegDate(current_cursor.getDate("reg_date"));
                register.setHomeAddr(current_cursor.getString("home_addr"));
                register.setPassword(current_cursor.getString("password"));
                register.setRole(current_cursor.getInteger("role"));
                register.setStatus(current_cursor.getInteger("status"));
                register.setKillTime(current_cursor.getDate("kill_time"));

                registerList.add(register);
            }
        } finally {
            cursor.close();
        }
        return registerList;
    }

    //有条件的检索文档
    private List<RegisterModel> find(Document condition){
        MongoCursor<Document> cursor = collection.find(condition).limit(50).iterator();
        List<RegisterModel> registerList = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                RegisterModel register = new RegisterModel();
                Document current_cursor = cursor.next();
//                register.setId(current_cursor.getObjectId("_id"));
                register.setRegId(current_cursor.getString("reg_id"));
                register.setPhone(current_cursor.getString("phone"));
                register.setName(current_cursor.getString("name"));
                register.setSex(current_cursor.getString("sex"));
                register.setRegDate(current_cursor.getDate("reg_date"));
                register.setHomeAddr(current_cursor.getString("home_addr"));
                register.setPassword(current_cursor.getString("password"));
                register.setRole(current_cursor.getInteger("role"));
                register.setStatus(current_cursor.getInteger("status"));
                register.setKillTime(current_cursor.getDate("kill_time"));

                registerList.add(register);
            }
        } finally {
            cursor.close();
        }
        return registerList;
    }

    //以id检索文档
    public RegisterModel findById(ObjectId code){
        Document condition = new Document();
        condition.append("_id", code);
        return find(condition).get(0);
    }

    //以关键字检索
    public List<RegisterModel> findByCondition(String keyWord){
        //设置正则表达
        Pattern regular = Pattern.compile("(?i)" + keyWord + ".*$", Pattern.MULTILINE);
        return find(new Document("name" , regular));
    }

    public boolean attemptLogin(final int type, String username, String password){

        final int[] result = new int[1];

        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/loginAndRegister.action");
            params.addQueryStringParameter("type", String.valueOf(type));
            params.addQueryStringParameter("username", username);
            params.addQueryStringParameter("password", password);
            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    result[0] = jsonObject.get("resultCode").getAsInt();
                }

                @Override
                public void onError(Throwable throwable, boolean b) {

                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result[0] == 1? true: false;
    }

}

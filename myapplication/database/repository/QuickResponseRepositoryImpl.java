package com.example.joan.myapplication.database.repository;

import com.example.joan.myapplication.database.model.QuickConsultModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class QuickResponseRepositoryImpl implements QuickResponseRepository{
    //MongoDBUtil mongoDb = new MongoDBUtil("wxby");
    //MongoCollection<Document> collection = mongoDb.getCollection("quick_response");

    public QuickConsultModel convert(JsonObject jo){
        QuickConsultModel data = new QuickConsultModel();
        try {

            data.setAuthor(new ObjectId(jo.get("author").getAsString()));
            data.setAuthor_name(jo.get("author_name").getAsString());
            data.setContent(jo.get("content").getAsString());
            data.setDate(jo.get("create_time").getAsString().replace("T", " "));
            data.setId(new ObjectId(jo.get("_id").getAsString()));
            data.setLike(jo.get("like").getAsInt());

            JsonArray pictures = jo.getAsJsonArray("picture");
            for (JsonElement picture : pictures) {
            }

            JsonArray comments = jo.getAsJsonArray("lawyer_reply");
            List<QuickConsultModel.Reply> replies = new ArrayList<>();
            try {
                for (JsonElement comment : comments) {
                    QuickConsultModel what = new QuickConsultModel();
                    QuickConsultModel.Reply reply = what.new Reply();
                    JsonObject temp = comment.getAsJsonObject();

                    reply.setIndex(temp.get("index").getAsInt());
//                            ObjectId tempid = new ObjectId(temp.get("author").getAsString());
                    reply.setAuthor(temp.get("author").getAsString());
                    reply.setAuthor_name(temp.get("author_name").getAsString());
                    reply.setDate((temp.get("create_time").toString().replace("T", " ")));
                    reply.setContent(temp.get("content").getAsString());
                    reply.setIs(temp.get("is_lawyer_author").getAsBoolean());
                    JsonArray ror = temp.getAsJsonArray("replies");
                    List<String> rsor = new ArrayList<>();
                    for (JsonElement r : ror) {
                        String rr = r.getAsString();
                        rsor.add(rr);
                    }
                    reply.setReplies(rsor);
//                            }
                    reply.setParent(new ObjectId(temp.get("parent").getAsString()));
                    reply.setId(new ObjectId(temp.get("reply_id").getAsString()));
                    reply.setLike(temp.get("like").getAsInt());
                    reply.setParent_index(temp.get("parent_index").getAsInt());
                    replies.add(reply);
//                            likeList.add(0);
                }
            } catch (Exception e) {

            }
            data.setLawyer_reply(replies);
            data.setView_count(jo.get("view_count").getAsInt());
        }catch (Exception e){}

        return data;
    }

    public List<QuickConsultModel> convertList(JSONArray jsonArray){
        List<QuickConsultModel> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i ++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String transfer = jsonObject.toString();
            JsonObject data = (JsonObject)new JsonParser().parse(transfer);
            System.out.println(data);
            result.add(convert(data));

        }

        return result;
    }


}

package com.example.joan.myapplication.database.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawFirmModel;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.model.CounselingModel;
import com.example.joan.myapplication.database.model.ResponseModel;
import com.google.gson.JsonObject;
import com.mongodb.DocumentToDBRefTransformer;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CounselingRepositoryImpl {

    public List<LegalCounselingModel>  convert(JSONArray s){
        List<LegalCounselingModel> counselings = new ArrayList<LegalCounselingModel>();
        for (int i = 0 ; i < s.size(); i++) {
            try {
                JSONObject a = s.getJSONObject(i);

                final LegalCounselingModel counseling = new LegalCounselingModel();
//                counseling.setLawyer(a.getString(""));

//                counseling.setQuestioner(a.getString("questioner"));
                LawyerModel l = new LawyerModel();
                l.setName(a.getJSONObject("lawyer").getString("name"));
                l.setJob(a.getJSONObject("lawyer").getString("job"));
                counseling.setLawyer(l);

                counseling.setId(a.getString("_id"));
                counseling.setCreateTime(a.getString("create_time").replace("T", " "));
                counseling.setViewCount(a.getInt("view_count"));

                //问答列表
                JSONArray content = a.getJSONArray("content");
                System.out.println(content);
                List<CounselingModel> contentList = new ArrayList<>();
                //封装每次提问
                for(int j = 0; j < content.size(); j++){
                    JSONObject c = content.getJSONObject(j);
                    CounselingModel ccontent = new CounselingModel();
//                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    ParsePosition pos1 = new ParsePosition(0);
//                    Date createTime1 = formatter.parse(c.getString("create_time"), pos);
                    ccontent.setCreate_time(c.getString("create_time").replace("T", " "));
                    ccontent.setQuestion(c.getString("question"));
                    List<ResponseModel> relies = new ArrayList<>();
                    JSONArray responses = c.getJSONArray("response");
                    //封装每次回答
                    for (int k = 0; k < responses.size(); k++){
                        JSONObject response = responses.getJSONObject(k);
                        ResponseModel rm = new ResponseModel();
                        rm.setDate(response.getString("time").replace("T", " "));
                        rm.setContent(response.getString("content"));
                        relies.add(rm);
                    }
                    ccontent.setResponse(relies);
                    contentList.add(ccontent);
                }

                counseling.setContent(contentList);
                counseling.setState(a.getInt("state"));
//                if(counseling.getState() == 3){
//                    counseling.setComment(a.getDouble("comment"));
//                }

                counselings.add(counseling);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return counselings;
    }

    public LegalCounselingModel convertSingle(JSONObject a){
        final LegalCounselingModel counseling = new LegalCounselingModel();
//                counseling.setLawyer(a.getString(""));

        JSONObject lawyer = a.getJSONObject("lawyer");
        JSONArray b = new JSONArray();
        b.add(lawyer);
        LawyerModel l = new LawyerRepositoryImpl().convertList(b).get(0);
        counseling.setLawyer(l);
//        counseling.setQuestioner(a.getString("questioner"));

        counseling.setId(a.getString("_id"));
        counseling.setCreateTime(a.getString("create_time").replace("T", " "));
        counseling.setViewCount(a.getInt("view_count"));

        //问答列表
        JSONArray content = a.getJSONArray("content");
        System.out.println(content);
        List<CounselingModel> contentList = new ArrayList<>();
        //封装每次提问
        for(int j = 0; j < content.size(); j++){
            JSONObject c = content.getJSONObject(j);
            CounselingModel ccontent = new CounselingModel();
//                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    ParsePosition pos1 = new ParsePosition(0);
//                    Date createTime1 = formatter.parse(c.getString("create_time"), pos);
            ccontent.setCreate_time(c.getString("create_time").replace("T", " "));
            ccontent.setQuestion(c.getString("question"));

            try{
                JSONArray picLst = c.getJSONArray("picture_lst");
                List<String> pl = new ArrayList<>();
                for(int i = 0; i < picLst.size(); i++){
                    pl.add(picLst.getString(i));
                }
                ccontent.setPictures(pl);
            }catch(Exception e){
                System.out.println("================没有图片呢===============");
            }
            List<ResponseModel> relies = new ArrayList<>();
            JSONArray responses = c.getJSONArray("response");
            //封装每次回答
            for (int k = 0; k < responses.size(); k++){
                JSONObject response = responses.getJSONObject(k);
                ResponseModel rm = new ResponseModel();
                rm.setDate(response.getString("time").replace("T", " "));
                rm.setContent(response.getString("content"));
                relies.add(rm);
            }
            ccontent.setResponse(relies);
            contentList.add(ccontent);
        }

        counseling.setContent(contentList);
        counseling.setState(a.getInt("state"));
        if(counseling.getState() == 3){
            counseling.setComment(a.getDouble("comment"));
        }

        return counseling;
    }

    public String disconvert(LegalCounselingModel l){
        Document  counseling = new Document();
        counseling.append("_id",l.getId());
        counseling.append("view_count",l.getViewCount());
        return counseling.toJson();
    }

    public String createNew(String question,String lawyer,String questioner,List<String> imglst) throws Exception{
        Document counseling = new Document();
        //目前時間
        Date date = new Date();
        //設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        //進行轉換
        String dateString = sdf.format(date);
        counseling.append("questioner",questioner);
        counseling.append("lawyer",lawyer);
        counseling.append("create_time",sdf.parse(dateString));
        counseling.append("view_count",0);
        CounselingModel c = new CounselingModel();
        c.setQuestion(question);
        c.setCreate_time(dateString);
        List<Document> cs = new ArrayList<>();
        Document content = new Document();
        content.append("create_time",sdf.parse(c.getCreate_time()));
        content.append("question",c.getQuestion());
        content.append("response",new ArrayList<Document>());
        content.append("picture_lst", imglst);
        cs.add(content);
        counseling.append("content",cs);
        counseling.append("publishFlag",1);
        counseling.append("state",0);

        return counseling.toJson();
    }

    public String ChangeState(int state, LegalCounselingModel counseling){
        Document update = new Document();
        update.append("state", state);
        update.append("_id", counseling.getId());

        return update.toJson();
    }
    public String ChangeState(double score,LegalCounselingModel counseling){
        Document update = new Document();
        update.append("comment", score);
        update.append("state", 3);
        update.append("_id", counseling.getId());

        return update.toJson();
    }


    public String QuestionAgain(String question, LegalCounselingModel counseling) throws Exception{
        List<CounselingModel> coundelingList = counseling.getContent();
        List<Document> questions = new ArrayList<>();
        //目前時間
        Date date = new Date();
        //設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        //進行轉換
        String dateString = sdf.format(date);
        for (CounselingModel coun: coundelingList) {
            Document q = new Document();
//            q.append("create_time",sdf.parse(dateString));
            q.append("create_time",sdf.parse(coun.getCreate_time()));
            q.append("question",coun.getQuestion());
            List<ResponseModel> responses = coun.getResponse();
            List<Document> responseList = new ArrayList<>();
            for (ResponseModel response: responses) {
                Document res = new Document();
                res.append("time", sdf.parse(response.getDate()));
                res.append("content", response.getContent());
                responseList.add(res);
            }
            q.append("response", responseList);
            questions.add(q);
        }
        Document newOne = new Document();
        newOne.append("create_time", sdf.parse(dateString));
        newOne.append("question",question);
        newOne.append("response",new ArrayList<Document>());
        questions.add(newOne);

        Document update = new Document();
        update.append("_id", counseling.getId());
        update.append("content", questions);
        if(counseling.getContent().size() >= 3){
            update.append("state", 5);//提問次數達上限
        }else{
            update.append("state", counseling.getState());
        }

        return update.toJson();
    }

    public String AnswerQuestion(String answer, LegalCounselingModel counseling) throws Exception{
        List<CounselingModel> coundelingList = counseling.getContent();
        List<Document> questions = new ArrayList<>();
        //目前時間
        Date date = new Date();
        //設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        //進行轉換
        String dateString = sdf.format(date);
        int count = 0;
        for (CounselingModel coun: coundelingList) {
            count++;
            Document q = new Document();
//            q.append("create_time",sdf.parse(dateString));
            q.append("create_time",sdf.parse(coun.getCreate_time()));
            q.append("question",coun.getQuestion());
            List<ResponseModel> responses = coun.getResponse();
            List<Document> responseList = new ArrayList<>();
            for (ResponseModel response: responses) {
                Document res = new Document();
                res.append("time", sdf.parse(response.getDate()));
                res.append("content", response.getContent());
                responseList.add(res);
            }
            if(count == coundelingList.size()){
                Document res = new Document();
                res.append("time", sdf.parse(sdf.format(new Date())));
                res.append("content", answer);
                responseList.add(res);
            }
            q.append("response", responseList);
            questions.add(q);
        }

        Document update = new Document();
        update.append("_id", counseling.getId());
        update.append("content", questions);
        update.append("state", 1);
        return update.toJson();
    }
}

package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.repository.JudgementRepositoryImpl;
import com.example.joan.myapplication.tools.DownloadImageTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsDetailActivity extends AppCompatActivity {

    private  TextView title;
    private TextView content;
    private TextView time;
    private ImageView picture;
    private String newsId;
    private JSONObject news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_main);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        initItems();
        getData();
    }

    private void initItems() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        picture = findViewById(R.id.picture);
        newsId = getIntent().getStringExtra("id");
    }

    private void getData(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/getNews.action");
            params.addQueryStringParameter("condition",newsId);
            params.addQueryStringParameter("type","1");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    System.out.println(jArray);
                    news = jArray.getJSONObject(0);
                    initView();
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    System.out.println(throwable.getMessage());
                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView(){
        title.setText(news.getString("title"));
        Pattern p = Pattern.compile("好文推薦.*");
        Matcher m = p.matcher(news.getString("article").trim());
        content.setText(m.replaceAll(""));
        time.setText(news.getString("time"));

        String img;
        if(news.getJSONArray("src").size() > 0){
            img = news.getJSONArray("src").getString(0);
        }else{
            img = "https://i.imgur.com/qM6pytU.jpg";
        }

        //加載圖片
        try{
//            Bitmap pngBM = BitmapFactory.decodeStream(new URL(textContent).openStream());
//            picture.setImageBitmap(pngBM);
            if(img.isEmpty()){
                picture.setVisibility(View.GONE);
            }else{
                new DownloadImageTask(picture).execute(img);
            }
        }
        catch(Exception e){
            System.out.println("url格式錯誤");
            System.out.println(e.getMessage());
        }
    }

}

package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.tools.DownloadImageTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CaseConsultContentActivity extends AppCompatActivity {

    private TextView title;
    private TextView content;
    private ImageView picture;
    private String contentM;
    private List<String> img = new ArrayList<>();
    private JSONObject news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_consult_content);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        initItems();
        initView();
    }

    private void initItems() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        picture = findViewById(R.id.picture);
        contentM = getIntent().getStringExtra("content");
        img = (List<String>)getIntent().getSerializableExtra("imglst");
    }


    private void initView(){
//        title.setText(news.getString("title"));
        content.setText(contentM);

        if(!img.isEmpty()){
            picture.setVisibility(View.VISIBLE);
            new DownloadImageTask(picture).execute("http://140.136.155.131:8081/picture/" + img.get(0));
        }

//        String img;
//        if(news.getJSONArray("src").size() > 0){
//            img = news.getJSONArray("src").getString(0);
//        }else{
//            img = "https://i.imgur.com/qM6pytU.jpg";
//        }

        //加載圖片
//        try{
////            Bitmap pngBM = BitmapFactory.decodeStream(new URL(textContent).openStream());
////            picture.setImageBitmap(pngBM);
//            if(img.isEmpty()){
//                picture.setVisibility(View.GONE);
//            }else{
//                new DownloadImageTask(picture).execute(img);
//            }
//        }
//        catch(Exception e){
//            System.out.println("url格式錯誤");
//            System.out.println(e.getMessage());
//        }
    }

}

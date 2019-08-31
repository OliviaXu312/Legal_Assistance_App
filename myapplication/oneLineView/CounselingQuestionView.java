package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.tools.DownloadImageTask;

public class CounselingQuestionView extends LinearLayout {

    //正文
    private TextView content;

    //時間
    private TextView time;

    //图片
    private ImageView picture;


    public CounselingQuestionView(Context context) {
        super(context);
    }

    public CounselingQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public CounselingQuestionView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counseing_question_content_layout_questioner, this, true);
        time = findViewById(R.id.time);
        content = findViewById(R.id.content);
        picture = findViewById(R.id.picture);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @return
     */
    public CounselingQuestionView init(String content, String time) {
        init();
        setContent(content);
        setTime(time);

        return this;
    }

    public CounselingQuestionView init(String content, String time, String picture) {
        init();
        setContent(content);
        setTime(time);
        setPicture(picture);
        return this;
    }

    public CounselingQuestionView setContent(String x){
        content.setText(x);
        return  this;
    }

    public CounselingQuestionView setTime(String x){
        time.setText(x);
        return  this;
    }

    public CounselingQuestionView setPicture(String img){
        //加載圖片
        try{
            picture.setVisibility(View.VISIBLE);
            new DownloadImageTask(picture).execute("http://140.136.155.131:8081/picture/" + img + ".jpg");
        }
        catch(Exception e){
            System.out.println("url格式錯誤");
            System.out.println(e.getMessage());
        }
        return  this;
    }

}

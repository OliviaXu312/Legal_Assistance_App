package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

import java.util.List;

public class CounselingAnswerView extends LinearLayout {

    //律師頭像
    private ImageView picture;

    //律師姓名
    private  TextView name;

    //正文
    private TextView content;

    //正文圖片列表
    private LinearLayout imageList;

    //時間
    private TextView time;


    public CounselingAnswerView(Context context) {
        super(context);
    }

    public CounselingAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public CounselingAnswerView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counseling_question_content_layout_lawyer, this, true);
        time = findViewById(R.id.time);
        content = findViewById(R.id.content);
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.name);
        imageList = findViewById(R.id.imageList);
        return this;
    }

    /**
     * 非提問人和律師的其他使用者 看不到圖片
     *
     * @return
     */
    public CounselingAnswerView init(String content, String time, String lawyerName) {
        init();
        name.setText(lawyerName);
        setConent(content);
        setTime(time);

        return this;
    }

    public CounselingAnswerView init(String content, String time, String lawyerName, List<String> imgList) {
        init();
        name.setText(lawyerName);
        setConent(content);
        setTime(time);
        setImageList(imgList);

        return this;
    }

    public CounselingAnswerView setConent(String x){
        content.setText(x);
        return  this;
    }

    public CounselingAnswerView setTime(String x){
        time.setText(x);
        return  this;
    }

    public CounselingAnswerView setImageList(List<String> x){
        for (String a : x) {
            imageList.addView(new LawyerCounselingImageView(getContext()).init(a));
        }
        return  this;
    }

}

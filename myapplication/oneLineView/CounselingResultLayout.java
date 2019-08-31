package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

public class CounselingResultLayout extends LinearLayout {

    //頭像
    private ImageView picture;

    //用户名稱
    private TextView name;

    //问答内容
    private TextView question;

    //问答时间
    private TextView time;

    //浏览量
    private TextView viewCount;

    //整一行
    private LinearLayout llRoot;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public CounselingResultLayout(Context context) {
        super(context);
    }

    public CounselingResultLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public CounselingResultLayout init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counseling_result_layout, this, true);
        picture = findViewById(R.id.counseling_picture);
        name = findViewById(R.id.counseling_name);
        question = findViewById(R.id.counseling_question);
        time = findViewById(R.id.counseling_time);
        viewCount = findViewById(R.id.counseling_view);
        llRoot = findViewById(R.id.ll_root);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @param question 问题内容
     * @param time 提问时间
     * @param view 浏览量
     * @return
     */
    public CounselingResultLayout init(String question, String time, String view) {
        init();

        setQuestion(question);
        setCTime(time);
        setCView(view);

        return this;
    }


    public CounselingResultLayout setPicture(Uri imgsource) {
        picture.setImageURI(imgsource);
        return this;
    }

    public CounselingResultLayout setCName(String textContent) {
        name.setText(textContent);
        return this;
    }

    public CounselingResultLayout setQuestion(String textContent) {
        question.setText(textContent);
        return this;
    }

    public CounselingResultLayout setCTime(String textContent) {
        time.setText(textContent);
        return this;
    }

    public CounselingResultLayout setCView(String textContent) {
        viewCount.setText(textContent);
        return this;
    }

    public CounselingResultLayout setOnRootClickListener(final CounselingResultLayout.OnRootClickListener onRootClickListener, final int tag) {
        llRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llRoot.setTag(tag);
                onRootClickListener.onRootClick(llRoot);
            }
        });
        return this;
    }
}

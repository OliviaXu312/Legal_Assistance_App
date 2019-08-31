package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

public class MyLawyerConsultLayout extends LinearLayout {
    //頭像
    private ImageView picture;
    private TextView name;
    private TextView job;
    private TextView state;
    private TextView content;
    private TextView time;
    private TextView view;

    //整一行
    private LinearLayout llRoot;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public MyLawyerConsultLayout(Context context) {
        super(context);
    }

    public MyLawyerConsultLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public MyLawyerConsultLayout init(){
        LayoutInflater.from(getContext()).inflate(R.layout.lawyer_consult_list_layout, this, true);
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.name);
        job = findViewById(R.id.job);
        state = findViewById(R.id.state);
        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        llRoot = findViewById(R.id.ll_root);
        view = findViewById(R.id.view);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @return
     */
    public MyLawyerConsultLayout init(String name,String job,int state,String content, String time) {
        init();
        setName(name);
        setJob(job);
        setContent(content);
        setTime(time);
        setView("");
        if(state == 0 || state == 1){
            setState("進行中");
            setStateColor(R.color.colorAccent);
        }else{
            setState("已結束");
            setStateColor(R.color.bbb);
        }

        return this;
    }

    public MyLawyerConsultLayout init(int state,String content, String time) {
        init();
        setName("三尺小律師");
        setContent(content);
        setTime(time);
        picture.setImageResource(R.drawable.robot);
        setJob("");
        setView("");
        if(state == 0 || state == 1){
            setState("分析中");
//            setStateColor(R.color.colorAccent);
        }else{
            setState("已完成");
            setStateColor(R.color.bbb);
            setStateColor(R.color.colorAccent);
        }

        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @return
     */
    public MyLawyerConsultLayout initUser(String name,String job,int view_count,String content, String time) {
        init();
        setName(name);
        setJob(job);
        setContent(content);
        setTime(time);
        setView(view_count + "人看過");
        setState("");

        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public MyLawyerConsultLayout setName(String textContent) {
        name.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public MyLawyerConsultLayout setNameColor(int colorRes) {
        name.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public MyLawyerConsultLayout setJob(String textContent) {
        job.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public MyLawyerConsultLayout setJobColor(int colorRes) {
        job.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public MyLawyerConsultLayout setState(String textContent) {
        state.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public MyLawyerConsultLayout setStateColor(int colorRes) {
        state.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public MyLawyerConsultLayout setContent(String textContent) {
        content.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public MyLawyerConsultLayout setContentColor(int colorRes) {
        content.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public MyLawyerConsultLayout setTime(String textContent) {
        time.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public MyLawyerConsultLayout setTimeColor(int colorRes) {
        time.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    public MyLawyerConsultLayout setView(String textContent) {
        view.setText(textContent);
        return this;
    }

    public MyLawyerConsultLayout setOnRootClickListener(final MyLawyerConsultLayout.OnRootClickListener onRootClickListener, final int tag) {
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

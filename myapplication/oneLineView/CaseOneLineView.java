package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

public class CaseOneLineView extends LinearLayout{
    //頭像
    private ImageView picture;

    //判决地院
    private TextView case_court;

    //判决名称
    private TextView case_name;

    //判决内容
    private TextView case_detail;

    //判決正文
    private TextView case_content;

    //判决标签
    private TextView case_type;

    //整一行
    private LinearLayout llRoot;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public CaseOneLineView(Context context) {
        super(context);
    }

    public CaseOneLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public CaseOneLineView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.case_layout, this, true);
        picture = findViewById(R.id.case_picture);
        case_court = findViewById(R.id.case_court);
        case_name = findViewById(R.id.case_name);
        case_detail = findViewById(R.id.case_detail);
        case_type = findViewById(R.id.case_type);
        case_content = findViewById(R.id.case_content);
        llRoot = findViewById(R.id.ll_root);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @param court 案件地院
     * @param name 案件名称
     * @param detail 案件內容
     * @param tag 律所标签
     * @return
     */
    public CaseOneLineView init(String court,String name, String detail, String tag,String content) {
        init();
        setCaseCourt(court);
        setCaseName(name);
        setCaseDetail(detail);
        setCaseTag(tag);
        setCaseContent(content);
        return this;
    }

    /**
     * 设置案件地院
     *
     * @param textContent
     * @return
     */
    public CaseOneLineView setCaseCourt(String textContent) {
        case_court.setText(textContent);
        return this;
    }

    /**
     * 设置案件地院的文字颜色
     *
     * @return
     */
    public CaseOneLineView setCaseCourtColor(int colorRes) {
        case_court.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置案件名称
     *
     * @param textContent
     * @return
     */
    public CaseOneLineView setCaseName(String textContent) {
        case_name.setText(textContent);
        return this;
    }

    /**
     * 设置案件名称的文字颜色
     *
     * @return
     */
    public CaseOneLineView setFirmNameColor(int colorRes) {
        case_name.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public CaseOneLineView setCaseNameSize(int textSizeSp) {
        case_name.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public CaseOneLineView setCaseDetail(String textContent) {
        case_detail.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public CaseOneLineView setCaseDetailColor(int colorRes) {
        case_detail.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public CaseOneLineView setCaseDetailSize(int textSizeSp) {
        case_detail.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置律所地址
     *
     * @param textContent
     * @return
     */
    public CaseOneLineView setCaseTag(String textContent) {
        case_type.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public CaseOneLineView setCaseTagColor(int colorRes) {
        case_type.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public CaseOneLineView setCaseTagSize(int textSizeSp) {
        case_type.setTextSize(textSizeSp);
        return this;
    }

    public CaseOneLineView setCaseContent(String textContent) {
        case_content.setText(textContent);
        return this;
    }

    //----------------------下面是一些点击事件

    public CaseOneLineView setOnRootClickListener(final CaseOneLineView.OnRootClickListener onRootClickListener, final int tag) {
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

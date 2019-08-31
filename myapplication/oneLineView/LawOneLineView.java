package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

public class LawOneLineView extends LinearLayout {
    //頭像
    private ImageView picture;

    //法條名稱
    private TextView law_name;

    //法條正文
    private TextView law_content;

    //法條類型
    private TextView law_type;

    //整一行
    private LinearLayout llRoot;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public LawOneLineView(Context context) {
        super(context);
    }

    public LawOneLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public LawOneLineView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.search_law_layout, this, true);
        picture = findViewById(R.id.case_picture);
        law_name = findViewById(R.id.law_name);
        law_content = findViewById(R.id.law_content);
        law_type = findViewById(R.id.law_type);
        llRoot = findViewById(R.id.ll_root);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @return
     */
    public LawOneLineView init(String name,String content, String type) {
        init();
        setLawName(name);
        setLawContent(content);
        setLawType(type);
        return this;
    }

    /**
     * 设置案件地院
     *
     * @param textContent
     * @return
     */
    public LawOneLineView setLawName(String textContent) {
        law_name.setText(textContent);
        return this;
    }

    /**
     * 设置案件地院的文字颜色
     *
     * @return
     */
    public LawOneLineView setLawNameColor(int colorRes) {
        law_name.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置案件名称
     *
     * @param textContent
     * @return
     */
    public LawOneLineView setLawContent(String textContent) {
        law_content.setText(textContent);
        return this;
    }

    /**
     * 设置案件名称的文字颜色
     *
     * @return
     */
    public LawOneLineView setLawContentColor(int colorRes) {
        law_content.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public LawOneLineView setLawContentSize(int textSizeSp) {
        law_content.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置案件內容
     *
     * @param textContent
     * @return
     */
    public LawOneLineView setLawType(String textContent) {
        law_type.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public LawOneLineView setLawTypeColor(int colorRes) {
        law_type.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public LawOneLineView setLawTypeSize(int textSizeSp) {
        law_type.setTextSize(textSizeSp);
        return this;
    }

    //----------------------下面是一些点击事件

    public LawOneLineView setOnRootClickListener(final LawOneLineView.OnRootClickListener onRootClickListener, final int tag) {
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

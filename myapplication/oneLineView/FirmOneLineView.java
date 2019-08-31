package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

public class FirmOneLineView extends LinearLayout{
    //頭像
    private ImageView picture;

    //律所名稱
    private TextView firm_name;

    //律所地址
    private TextView firm_addr;

    //律所標籤
    private TextView firm_type;

    //整一行
    private LinearLayout llRoot;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public FirmOneLineView(Context context) {
        super(context);
    }

    public FirmOneLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public FirmOneLineView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.law_firm_layout, this, true);
        picture = findViewById(R.id.firm_picture);
        firm_name = findViewById(R.id.firm_name);
        firm_addr = findViewById(R.id.firm_addr);
        firm_type = findViewById(R.id.firm_type);
        llRoot = findViewById(R.id.ll_root);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @param name 律所名称
     * @param addr 律所地址
     * @param tag 律所标签
     * @return
     */
    public FirmOneLineView init(String name, String addr, String tag) {
        init();
        setFirmName(name);
        setFirmAddr(addr);
        setFirmTag(tag);
        return this;
    }

    /**
     * 设置律所名称
     *
     * @param textContent
     * @return
     */
    public FirmOneLineView setFirmName(String textContent) {
        firm_name.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public FirmOneLineView setFirmNameColor(int colorRes) {
        firm_name.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public FirmOneLineView setFirmNameSize(int textSizeSp) {
        firm_name.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置律所地址
     *
     * @param textContent
     * @return
     */
    public FirmOneLineView setFirmAddr(String textContent) {
        firm_addr.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public FirmOneLineView setFirmAddrColor(int colorRes) {
        firm_addr.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public FirmOneLineView setFirmAddrSize(int textSizeSp) {
        firm_addr.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置律所地址
     *
     * @param textContent
     * @return
     */
    public FirmOneLineView setFirmTag(String textContent) {
        firm_type.setText(textContent);
        return this;
    }

    /**
     * 设置律所名称的文字颜色
     *
     * @return
     */
    public FirmOneLineView setFirmTagColor(int colorRes) {
        firm_type.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    /**
     * 设置律所名称的文字大小
     *
     * @return
     */
    public FirmOneLineView setFirmTagSize(int textSizeSp) {
        firm_type.setTextSize(textSizeSp);
        return this;
    }

    //----------------------下面是一些点击事件

    public FirmOneLineView setOnRootClickListener(final FirmOneLineView.OnRootClickListener onRootClickListener, final int tag) {
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

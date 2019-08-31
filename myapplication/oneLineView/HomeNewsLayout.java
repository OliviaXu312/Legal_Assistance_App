package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.tools.DownloadImageTask;

import java.net.URL;

public class HomeNewsLayout extends LinearLayout {

    private TextView title;
    private TextView content;
    private ImageView picture;

    //整一行
    private LinearLayout llRoot;

    /**
     * 整个一行被点击
     */
    public static interface OnRootClickListener {
        void onRootClick(View view);
    }

    public HomeNewsLayout(Context context) {
        super(context);
    }

    public HomeNewsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public HomeNewsLayout init(){
        LayoutInflater.from(getContext()).inflate(R.layout.shouye_sample, this, true);
        picture = findViewById(R.id.picture);
        content = findViewById(R.id.content);
        title = findViewById(R.id.title);
        llRoot = findViewById(R.id.ll_root);
        return this;
    }

    public HomeNewsLayout initNews(String title, String content, String picture) {
        init();
        setTitle(title);
        setContent(content);
        setPicture(picture);
        return this;
    }

    public HomeNewsLayout setTitle(String textContent) {
        title.setText(textContent);
        return this;
    }

    public HomeNewsLayout setContent(String textContent) {
        content.setText(textContent);
        return this;
    }

    public HomeNewsLayout setPicture(String textContent) {
        try{
//            Bitmap pngBM = BitmapFactory.decodeStream(new URL(textContent).openStream());
//            picture.setImageBitmap(pngBM);
            new DownloadImageTask(picture).execute(textContent);
        }
        catch(Exception e){
            System.out.println("url格式錯誤");
            System.out.println(e.getMessage());
        }
        return this;
    }

    public HomeNewsLayout setOnRootClickListener(final HomeNewsLayout.OnRootClickListener onRootClickListener, final int tag) {
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

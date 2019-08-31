package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.joan.myapplication.R;

public class LawyerCounselingImageView extends LinearLayout{

    private ImageView img;

    public LawyerCounselingImageView(Context context) {
        super(context);
    }

    public LawyerCounselingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public LawyerCounselingImageView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counseling_question_content_layout_picture, this, true);
        img = findViewById(R.id.image);
        return this;
    }

    /**
     * 非提問人和律師的其他使用者 看不到圖片
     *
     * @return
     */
    public LawyerCounselingImageView init(String imgUrl) {
        init();
        setImg(imgUrl);

        return this;
    }

    public LawyerCounselingImageView setImg(String x){
//        imgUrl.setText(x);
        return  this;
    }


}

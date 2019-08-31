package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.joan.myapplication.R;

public class FindNothingView extends LinearLayout {

    public FindNothingView(Context context) {
        super(context);
    }

    public FindNothingView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public FindNothingView init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fiind_nothing, this, true);
        return this;
    }
}

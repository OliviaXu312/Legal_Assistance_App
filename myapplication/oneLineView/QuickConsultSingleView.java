package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;

public class QuickConsultSingleView extends LinearLayout {

    private TextView content, time,views, reply;

    private ImageView image;

    private LinearLayout ll;

    public QuickConsultSingleView(Context context) {
        super(context);
    }

    public QuickConsultSingleView init(){

        LayoutInflater.from(getContext()).inflate(R.layout.sample_quick_consult_single, this, true);
        content = findViewById(R.id.quick_consult_single_content);
        time = findViewById(R.id.quick_consult_single_time);
        reply = findViewById(R.id.quick_consult_single_reply);
        views = findViewById(R.id.quick_consult_single_views);
        image = findViewById(R.id.quick_consult_single_image);
        ll = findViewById(R.id.quick_consult_single_line);
        return this;

    }

    public QuickConsultSingleView init(String textContent, int textViews, int textReply, String textTime){
        init();

        setQuickContent(textContent);
        setQuickViews(textViews);
        setQuickReply(textReply);
        setQuickTime(textTime);

        return this;
    }

    public QuickConsultSingleView setQuickContent(String textContent){
        content.setText(textContent);
        content.setMaxLines(3);
        return this;
    }

    public QuickConsultSingleView setQuickViews(int textViews){
        views.setText(String.valueOf(textViews) + "次瀏覽");
        views.setTextColor(getResources().getColor(R.color.text2));
        return this;
    }

    public QuickConsultSingleView setQuickReply(int textReply){
        if (textReply == 0){
            reply.setText("沒有回復");
            reply.setTextColor(getResources().getColor(R.color.title));
        }else{
            reply.setTextColor(getResources().getColor(R.color.text2));
            reply.setText(String.valueOf(textReply) + "條回復");
        }
        return this;
    }
    public QuickConsultSingleView setQuickTime(String textTime){
        time.setText(textTime);
        return this;
    }

    public QuickConsultSingleView setOnRootClickListener(final FirmOneLineView.OnRootClickListener onRootClickListener, final int tag) {
        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setTag(tag);
                onRootClickListener.onRootClick(ll);
            }
        });
        return this;
    }

}

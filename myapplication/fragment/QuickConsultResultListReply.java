package com.example.joan.myapplication.fragment;

import android.support.v4.app.Fragment;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.database.model.QuickConsultModel;

import java.util.List;

public class QuickConsultResultListReply extends Fragment {

    private LinearLayout ll;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.activity_quick_consult_result_list_reply, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        ll = view.findViewById(R.id.quick_consult_result_reply_list);

    }

    public void setData(List<QuickConsultModel.Reply> list){

        for (QuickConsultModel.Reply reply: list){

            View singleReply = getLayoutInflater().inflate(R.layout.sample_quick_consult_single_comment, null);

            Button image, like, comment;
            TextView content, time, replies, favorites, name;

            image = view.findViewById(R.id.quick_consult_result_single_user_image);
            like = view.findViewById(R.id.quick_consult_result_single_like);
            comment = view.findViewById(R.id.quick_consult_result_single_reply);

            content = view.findViewById(R.id.quick_consult_result_single_content);
            time = view.findViewById(R.id.quick_consult_result_single_time);
            replies = view.findViewById(R.id.quick_consult_result_single_replies);
            favorites = view.findViewById(R.id.quick_consult_result_single_favorites);
            name = view.findViewById(R.id.quick_consult_result_single_user_name);

            content.setText(reply.getContent());
            time.setText(new SimpleDateFormat("MM-dd hh:mm").format(reply.getCreate_time()));
            replies.setText(reply.getReplies().size() + " 條回復");
            favorites.setText(reply.getLike());
            name.setText(reply.getAuthor_name());

            replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

    }

    public void temp(){

        View singleComment = getLayoutInflater().inflate(R.layout.sample_quick_consult_single_comment, null);

        for (int i = 0; i < 10; i ++){
            ll.addView(singleComment);
        }

    }


}

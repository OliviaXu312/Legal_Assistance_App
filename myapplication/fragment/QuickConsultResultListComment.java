package com.example.joan.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.joan.myapplication.R;


public class QuickConsultResultListComment extends Fragment {

    private LinearLayout ll;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_quick_consult_result_list_comment, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        ll = view.findViewById(R.id.quick_consult_result_reply_list);

    }

    public void temp(){

        View singleComment = getLayoutInflater().inflate(R.layout.sample_quick_consult_single_comment, null);

        for (int i = 0; i < 10; i ++){
            ll.addView(singleComment);
        }

    }
}

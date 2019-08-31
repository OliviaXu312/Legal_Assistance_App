package com.example.joan.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.CaseResultData;
import com.example.joan.myapplication.R;

public class CaseResultReasonListFragment extends Fragment {

    private LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_case_result_list_reason, null);
        ll = view.findViewById(R.id.case_consult_reason_linear);
        initView();
        return view;
    }

    public void initView(){
        LayoutInflater li = LayoutInflater.from(getContext());
        for (int i = 0; i < 3; i ++){
            View view = li.inflate(R.layout.sample_case_result_single_reason, null);
            ll.addView(view);
        }

    }

    public void initData(CaseResultData.Reason[] reasons) {
        for(int i = 0; i < 3; i ++){
            View view = ll.getChildAt(i);
            TextView title, number;
            title = view.findViewById(R.id.case_result_reason_single_title);
            number = view.findViewById(R.id.case_result_reason_single_number);
            title.setText(reasons[i].content);
            number.setText((i+1)+".");
        }
    }
}
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

public class CaseResultCompensateListFragment extends Fragment {

    private LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_case_result_list_compensate, null);
        ll = view.findViewById(R.id.case_consult_compensate_linear);
        initView();
        return view;
    }

    public void initView(){
        LayoutInflater li = LayoutInflater.from(getContext());
        for (int i = 0; i < 3; i ++){
            View view = li.inflate(R.layout.sample_case_result_single_compensate, null);
            ll.addView(view);
        }
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("InitCompensate!!!!!!!!!!!!!!!");
        System.out.println("-------------------");
        System.out.println("-------------------");
        System.out.println("-------------------");
    }

    public void initData(CaseResultData.Compensate compensates){

        String[] names = new CaseResultData().cname;
        String[] ct = new CaseResultData().ct;
//        View single = new case_result_similar_single(getContext());
        for(int i = 0; i < 3; i ++){
            View view = ll.getChildAt(i);
            TextView max, ave, min, number;
//            View view = new CaseResultSimilarFragment().getView();
            max = view.findViewById(R.id.case_result_compensate_single_max);
            ave = view.findViewById(R.id.case_result_compensate_single_ave);
            min = view.findViewById(R.id.case_result_compensate_single_min);
            number = view.findViewById(R.id.case_result_compensate_single_number);
            max.setText(ct[0] + ": " +compensates.c[i][0]);
            ave.setText(ct[1] + ": " +compensates.c[i][1]);
            min.setText(ct[2] + ": " +compensates.c[i][2]);
            number.setText(names[i]);
//            ft.add(R.id.case_consult_result_linear, new CaseResultSimilarFragment());

//            ll.addView(single);
//            CaseResultSimilarFragment single = new CaseResultSimilarFragment();
//            single.init(similars[i], i + 1);
//            ll.addView(single.getView().findViewById(R.id.case_result_similar_single_title));

        }
//        ft.commit();
    }
}

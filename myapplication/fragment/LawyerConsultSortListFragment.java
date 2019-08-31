package com.example.joan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.LawyerConsultActivity;
import com.example.joan.myapplication.LawyerConsultSortedListActivity;
import com.example.joan.myapplication.R;
import com.example.joan.myapplication.Sort;

import java.util.ArrayList;
import java.util.List;

public class LawyerConsultSortListFragment extends Fragment {

    private LinearLayout ll;
    private LayoutInflater li;
    private int lNumber = 10;
    private TextView name, detail, image;
    private List<Sort> totalData = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_lawyer_consult_sort_list, null);

        initView(view);
        addView();

        return view;
    }

    private void initView(View view) {
        ll = view.findViewById(R.id.consult_sort_list);
        li = LayoutInflater.from(getContext());
    }

    public void addView() {
        List<Sort> list = getData();
        for (Sort sort: list){
            View view = li.inflate(R.layout.sample_lawyer_consult_single_sort, null);
            setData(view, sort);
            setListener(view, sort.getName());
            ll.addView(view);
        }
    }

    private void setListener(View view, final String name) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerConsultActivity.page, LawyerConsultSortedListActivity.class);
                intent.putExtra("sortName", name.toString());
                startActivity(intent);
                LawyerConsultActivity.page.overridePendingTransition(R.anim.right, R.anim.left);
            }
        });

    }

    private void setData(View view, Sort sort) {

        name = view.findViewById(R.id.lawyer_consult_single_sort);
        detail = view.findViewById(R.id.lawyer_consult_single_explain);

        name.setText(sort.getName());
        detail.setText(sort.getDetail());

    }

    private List getData(){
        List<Sort> list = new ArrayList();
        for (int i = 0; i < lNumber; i ++){
            list.add(new Sort("Name" + i, "Sort" + i));
        }
        totalData.addAll(list);
        return list;
    }

    public List getTotalList(){ return totalData;}

    public void setlNumber(int num) {this.lNumber = num;}

    public int getlNumber(){return lNumber;}

}

package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.joan.myapplication.fragment.CaseConsultAdapter;
import com.example.joan.myapplication.fragment.LawyerConsultLawyerListFragment;
import com.example.joan.myapplication.fragment.LawyerConsultSortListFragment;

import java.util.ArrayList;
import java.util.List;

public class LawyerConsultActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager pager;
    private TabLayout tabLayout;
    private List<Fragment> list;
    private LawyerConsultLawyerListFragment lawyerList;
    private LawyerConsultSortListFragment sortList;
    private ImageView back;
    public static LawyerConsultActivity page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_consult);

        getData();
        initItems();

    }

    private void getData() {
    }

    private void initItems() {

        page = this;

        back = findViewById(R.id.consult_lawyer_back);
        pager = findViewById(R.id.consult_lawyer_pager);
        tabLayout = findViewById(R.id.lawyer_consult_layout);

        back.setOnClickListener(this);

        lawyerList = new LawyerConsultLawyerListFragment();
        sortList = new LawyerConsultSortListFragment();

        initTabLayout();

    }

    private void initTabLayout() {

        list = new ArrayList<>();
        list.add(lawyerList);
        list.add(sortList);

        pager.setAdapter(new CaseConsultAdapter(getSupportFragmentManager(), list));
        tabLayout.setupWithViewPager(pager);

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("推薦律師"));
        tabLayout.addTab(tabLayout.newTab().setText("專業分類"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.consult_lawyer_back:
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                break;
        }
    }
}

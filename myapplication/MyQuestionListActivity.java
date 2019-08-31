package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.joan.myapplication.fragment.MyCaseCounselingFragment;
import com.example.joan.myapplication.fragment.MyQuestionFragment;
import com.example.joan.myapplication.fragment.MyQuickResponseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyQuestionListActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewpager;
    private List<String> tabs;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_question_list);


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        initTabLayout();
        initDatas();
        initViewPager();
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.main_tab);
        viewpager = findViewById(R.id.main_body);
//        //指示条的颜色
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.holo_blue_dark));
//        tabLayout.setSelectedTabIndicatorHeight((int) getResources().getDimension(R.dimen.indicatorHeight));
        //关联tabLayout和ViewPager,两者的选择和滑动状态会相互影响
        tabLayout.setupWithViewPager(viewpager);
        //自定义标签布局
//        for (int i = 0; i < tabs.size(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.frag_base, tabLayout, false);
//            tv.setText(tabs.get(i));
//            tab.setCustomView(tv);
//        }
    }

    private void initDatas() {
        tabs = new ArrayList<>();
        tabs.add("律師咨詢");
        tabs.add("案件咨詢");
        tabs.add("快問快答");

        fragments = new ArrayList<>(3);
        MyQuestionFragment homeFragment = new MyQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("position", "0");
        homeFragment.setArguments(bundle);

        MyCaseCounselingFragment meFragment = new MyCaseCounselingFragment();
        bundle = new Bundle();
        bundle.putString("position", "1");
        meFragment.setArguments(bundle);

        MyQuickResponseFragment orderFragment = new MyQuickResponseFragment();
        bundle = new Bundle();
        bundle.putString("position", "2");
        orderFragment.setArguments(bundle);

        fragments.add(homeFragment);
        fragments.add(meFragment);
        fragments.add(orderFragment);

    }

    private void initViewPager() {
        viewpager.setAdapter(new VpAdapter(getSupportFragmentManager(), fragments));
    }

    private class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }

}

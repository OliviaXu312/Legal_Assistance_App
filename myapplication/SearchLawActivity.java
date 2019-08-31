package com.example.joan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joan.myapplication.fragment.LawFragment;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SearchLawActivity extends AppCompatActivity{
    TabLayout tabLayout;
    ViewPager viewpager;
    private List<String> tabs;
    private List<Fragment> fragments;
    Document d_condition = new Document();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_law);

        initTabLayout();
        initDatas();
        initViewPager();
        initOnClickListener();

    }

    @Override
    protected void onStart(){
        super.onStart();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition()==1){
                    findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) SearchLawActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.toggleSoftInput(0,0);
                    }
                }else{
                    findViewById(R.id.submit).setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) SearchLawActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.toggleSoftInput(1,0);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private  void initOnClickListener(){
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager imm = (InputMethodManager) SearchLawActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.toggleSoftInput(0,0);
                }
                if(packCondition() == 1){
                    Intent intent=new Intent();
                    intent.setClass(v.getContext(), SearchLawListActivity.class); //设置跳转的Activity
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("condition", d_condition.toJson());
                    bundle.putSerializable("type", "0");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
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

    private void initViewPager() {
        viewpager.setAdapter(new SearchLawActivity.VpAdapter(getSupportFragmentManager(), fragments));
    }

    private void initDatas() {
        tabs = new ArrayList<>();
        tabs.add("法規檢索");
        tabs.add("熱門法規");

        fragments = new ArrayList<>(3);
        LawFragment homeFragment = new LawFragment();
        Bundle bundle = new Bundle();
        bundle.putString("position", "0");
        homeFragment.setArguments(bundle);

        LawFragment orderFragment = new LawFragment();
        bundle = new Bundle();
        bundle.putString("position", "1");
        orderFragment.setArguments(bundle);

        fragments.add(homeFragment);
        fragments.add(orderFragment);

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

    private int packCondition(){
        d_condition.clear();
        EditText keyword = findViewById(R.id.keyword);
        EditText and = findViewById(R.id.and);
        EditText or = findViewById(R.id.or);
        EditText not = findViewById(R.id.not);
        RadioGroup item = findViewById(R.id.item);
        RadioGroup state = findViewById(R.id.state);
        TextView start = findViewById(R.id.start);
        TextView end = findViewById(R.id.end);
        EditText num = findViewById(R.id.number);

        if(!keyword.getText().toString().isEmpty()){
            d_condition.append("keyword",keyword.getText().toString());
        }else{
            d_condition.clear();
            Toast.makeText(this, "請填寫檢索字彙",Toast.LENGTH_LONG).show();
            return 0;
        }
        if(!and.getText().toString().isEmpty()){
            d_condition.append("and",and.getText().toString());
        }
        else if(!or.getText().toString().isEmpty()){
            d_condition.append("or",or.getText().toString());
        }
        else if(!not.getText().toString().isEmpty()){
            d_condition.append("not",not.getText().toString());
        }
        switch (item.getCheckedRadioButtonId()){
            case R.id.name: d_condition.append("item", 0);break;

            case R.id.content: d_condition.append("item", 1);break;

            default: {
                d_condition.clear();
                Toast.makeText(this, "請選擇檢索項目",Toast.LENGTH_LONG).show();
                return 0;
            }

        }
        switch (state.getCheckedRadioButtonId()){
            case R.id.valid: d_condition.append("state", 0);break;

            case R.id.abandon: d_condition.append("state", 1);break;
        }

        if(!start.getText().toString().isEmpty()){
            d_condition.append("start",start.getText().toString());
        }
        if(!end.getText().toString().isEmpty()){
            d_condition.append("end",end.getText().toString());
        }
        return 1;
    }

}

package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.joan.myapplication.DIYComponent.NoScrollViewPager;
import com.example.joan.myapplication.fragment.BaseFragment;
import com.example.joan.myapplication.database.repository.RegisterRepositoryImpl;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * author : duyb
 * time : 2018/01/17
 * desc :主页
 */
public class MainHomeActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private BottomNavigationViewEx bnve;
    private VpAdapter adapter;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);

        initView();
        initData();
        initBNVE();
        initEvent();
        initRegister();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            if(sp.getBoolean("login", false) && position == 2){
                TextView myName = findViewById(R.id.name);
                findViewById(R.id.logout).setVisibility(View.VISIBLE);
                myName.setText(sp.getString("name","呂小布"));
            }

        }else{
        }

    }

    /**
     * init BottomNavigationViewEx envent
     */
    private void initEvent() {
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                resetToDefaultIcon();
                switch (item.getItemId()) {
                    case R.id.menu_main:
                        item.setIcon(R.drawable.main_selected);
                        //setIconSize();
                        position = 0;

                        break;
                    case R.id.menu_me:
                        item.setIcon(R.drawable.me_selected);
                        position = 2;
                        break;
                    case R.id.menu_empty: {
                        position = 1;

                        // 生成一个Intent对象
                        Intent intent=new Intent();
                        intent.setClass(MainHomeActivity.this, ConsultingActivity.class); //设置跳转的Activity
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //此处return false且在FloatingActionButton没有自定义点击事件时 会屏蔽点击事件
                        return false;
                    }
                    default:
                        break;
                }
                if (previousPosition != position) {
                    viewPager.setCurrentItem(position, false);
                    previousPosition = position;
                }if(position == 2){
                    TextView myName = findViewById(R.id.name);
                    if(sp.getBoolean("login", false)){
                        findViewById(R.id.logout).setVisibility(View.VISIBLE);
                        myName.setText(sp.getString("name","呂小布"));
                    }else{
                        myName.setText("你還沒有註冊喲");
                        findViewById(R.id.logout).setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (1 == position) {
                    setContentView(R.layout.main);

                } else {
                    floatingActionButton.setImageResource(R.drawable.begin);

                }
               /* // 1 is center 此段结合屏蔽FloatingActionButton点击事件的情况使用
                  //在viewPage滑动的时候 跳过最中间的page也
                if (position >= 1) {
                    position++;
                }*/
                bnve.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/**
 * fab 点击事件结合OnNavigationItemSelectedListener中return false使用
 */
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 生成一个Intent对象
                Intent intent=new Intent();
                intent.setClass(MainHomeActivity.this, ConsultingActivity.class); //设置跳转的Activity
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void resetToDefaultIcon(){
        MenuItem a =  bnve.getMenu().getItem(0);
        a.setIcon(R.drawable.main_unselect);
        MenuItem b =  bnve.getMenu().getItem(2);
        b.setIcon(R.drawable.me_unselected);
    }

    private void initView() {
        builder = new AlertDialog.Builder(this);
        floatingActionButton = findViewById(R.id.fab);
        viewPager = findViewById(R.id.main_body);
        viewPager.setNoScroll(true);
        bnve = findViewById(R.id.bnve);
    }

    /**
     * create fragments
     */
    private void initData() {
        fragments = new ArrayList<>(3);
        BaseFragment homeFragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("position", "0");
        homeFragment.setArguments(bundle);

        BaseFragment orderFragment = new BaseFragment();
        bundle = new Bundle();
        bundle.putString("position", "1");
        orderFragment.setArguments(bundle);

        BaseFragment meFragment = new BaseFragment();
        bundle = new Bundle();
        bundle.putString("position", "2");
        meFragment.setArguments(bundle);

        sp = this.getSharedPreferences("account_info", Context.MODE_PRIVATE);
        fragments.add(homeFragment);
        fragments.add(orderFragment);
        fragments.add(meFragment);
    }

    /**
     * init BottomNavigationViewEx
     */
    private void initBNVE() {

        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextSize(13);

        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
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
    }

    private void initRegister(){
        builder = new AlertDialog.Builder(this);
        SharedPreferences sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);
        if (sp.getBoolean("login", false) && !sp.getString("user_name", "invalid").equals("invalid")){
//            SharedPreferences.Editor editor;
//            editor = sp.edit();
//            editor.putBoolean("login",false);
//            editor.commit();
            new RegisterRepositoryImpl().attemptLogin(sp.getInt("login_type",0),sp.getString("user_name","wobuzhidao"),sp.getString("user_pswd","wobuzhidao"));
//            new LoginActivity().autoLogin();
        }else{

            builder.setMessage("你還沒有登錄哦，登錄后可享受完整功能哦~");
            builder.setPositiveButton("現在登錄", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.cancel();
                    Intent intent = new Intent(MainHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("稍後登錄", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.cancel();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }
}
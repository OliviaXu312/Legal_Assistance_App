package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.LawFirmModel;

public class SearchLawFirmDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name ;
    TextView tag;
    TextView addr;
    TextView phone;
    TextView intro;
    TextView major;

    LawFirmModel firm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_firm_detail);
        firm = (LawFirmModel) getIntent().getSerializableExtra("firm");

        initView();
        initData();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.like:{

            }break;

            case R.id.follow:{

            }break;

            case R.id.contact:{

            }break;
        }
    }

    private void initView(){
        name = findViewById(R.id.name);
        tag = findViewById(R.id.tag);
        addr = findViewById(R.id.addr);
        phone = findViewById(R.id.phone);
        intro = findViewById(R.id.intro);
        major = findViewById(R.id.major);
    }

    private void initData(){
        name.setText(firm.getName());
        addr.setText("地址：" + firm.getAddress());
        phone.setText("電話：" + firm.getPhone());
        intro.setText(firm.getIntroduction());
        major.setText(firm.getMajor());
    }
}

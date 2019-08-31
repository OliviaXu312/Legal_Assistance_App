package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.joan.myapplication.database.model.CounselingModel;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;

public class AfterFeePaidActivity extends AppCompatActivity{

    private LegalCounselingModel counseling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_finish_layout);
        counseling = (LegalCounselingModel) getIntent().getSerializableExtra("counseling");

        initClick();
    }

    private void initClick(){
        findViewById(R.id.question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //跳至詳情頁面
                Intent intent = new Intent();
                intent.setClass(AfterFeePaidActivity.this, MyQuestionLawyerConsultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("counseling",counseling.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //跳至詳情頁面
                Intent intent = new Intent();
                intent.setClass(AfterFeePaidActivity.this,MainHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

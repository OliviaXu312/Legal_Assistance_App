package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ConsultingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lawyer_consult, case_consult, quick_consult;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal_counseling);

        initButtons();
        initListener();

    }

    protected void initButtons(){
        lawyer_consult = findViewById(R.id.consult_lawyer);
        case_consult = findViewById(R.id.consult_case);
        quick_consult = findViewById(R.id.consult_quick);

        cancel = findViewById(R.id.consult_cancel);
    }

    protected void initListener(){
        lawyer_consult.setOnClickListener(this);
        case_consult.setOnClickListener(this);
        quick_consult.setOnClickListener(this);

        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch(view.getId()){
            case R.id.consult_lawyer:
                intent.setClass(ConsultingActivity.this, LawyerConsultActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
                break;
            case R.id.consult_case:
                intent.setClass(ConsultingActivity.this, CaseConsultActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
                break;
            case R.id.consult_quick:
                intent.setClass(ConsultingActivity.this, QuickConsultActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
                break;
            case R.id.consult_cancel:
                finish();
//                overridePendingTransition(R.anim.left, R.anim.push_bottom_out);
                break;
            default:
                break;
        }
    }
}

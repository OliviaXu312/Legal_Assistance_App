package com.example.joan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joan.myapplication.datePicker.CustomDatePicker;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SearchCasesActivity  extends AppCompatActivity implements View.OnClickListener{
    private TextView currentDateStart = null, currentDateEnd = null;
    private CustomDatePicker customDatePicker1, customDatePicker2;

    private Document d_condition = new Document();
    private int caidin=0, panjue=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_cases);

        initOnClickListener();

        currentDateStart = (TextView) findViewById(R.id.start);
        currentDateEnd = (TextView) findViewById(R.id.end);
        initDatePicker();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:{
                // 日期格式为yyyy-MM-dd

                //拿到InputMethodManager
                InputMethodManager imm = (InputMethodManager)SearchCasesActivity.this.getSystemService(SearchCasesActivity.this.INPUT_METHOD_SERVICE);
                //如果window上view获取焦点 && view不为空
                if(imm.isActive() && SearchCasesActivity.this.getCurrentFocus()!=null) {
                    //拿到view的token 不为空
                    SearchCasesActivity.this.getCurrentFocus().clearFocus();
//                                view_search.findViewById(R.id.down_start).setFocusable(true);
//                                view_search.findViewById(R.id.down_start).requestFocus();
                    if (SearchCasesActivity.this.getCurrentFocus().getWindowToken() != null) {
                        //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                        imm.hideSoftInputFromWindow(SearchCasesActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                customDatePicker1.show(currentDateStart.getText().toString());
            }break;
            case R.id.end:{
                // 日期格式为yyyy-MM-dd

                //拿到InputMethodManager
                InputMethodManager imm = (InputMethodManager)SearchCasesActivity.this.getSystemService(SearchCasesActivity.this.INPUT_METHOD_SERVICE);
                //如果window上view获取焦点 && view不为空
                if(imm.isActive() && SearchCasesActivity.this.getCurrentFocus()!=null) {
                    //拿到view的token 不为空
                    SearchCasesActivity.this.getCurrentFocus().clearFocus();
//                                view_search.findViewById(R.id.down_end).setFocusable(true);
//                                view_search.findViewById(R.id.down_end).requestFocus();
                    if (SearchCasesActivity.this.getCurrentFocus().getWindowToken() != null) {
                        //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                        imm.hideSoftInputFromWindow(SearchCasesActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                showEndDate();
            }break;
        }

    }


    private void initOnClickListener(){
        final RadioGroup type = findViewById(R.id.type);
        findViewById(R.id.caidin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panjue = 0;
                if(caidin == 0){
                    caidin = 1;
                }else{
                    caidin = 0;
                    type.clearCheck();
                }
            }
        });
        findViewById(R.id.panjue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caidin = 0;
                if(panjue == 0){
                    panjue = 1;
                }else{
                    panjue = 0;
                    type.clearCheck();
                }
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager imm = (InputMethodManager) SearchCasesActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.toggleSoftInput(0,0);
                }
                if(packCondition() == 1){
                    Intent intent=new Intent();
                    intent.setClass(v.getContext(), SearchCasesListActivity.class); //设置跳转的Activity
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("condition", d_condition.toJson());
                    bundle.putSerializable("type", "0");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.down_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currentDateStart.setText(null);
                currentDateStart.setHint("請選擇");
                findViewById(R.id.down_start).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.down_end).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currentDateEnd.setText(null);
                currentDateEnd.setHint("請選擇");
                findViewById(R.id.down_end).setVisibility(View.GONE);
            }
        });
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
//        currentDateStart.setText(now.split(" ")[0]);
//        currentDateEnd.setText(now.split(" ")[0]);

        customDatePicker1 = new CustomDatePicker(SearchCasesActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDateStart.setText(time.split(" ")[0]);
                findViewById(R.id.down_start).setVisibility(View.VISIBLE);
            }
        }, "1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(SearchCasesActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDateEnd.setText(time.split(" ")[0]);
                findViewById(R.id.down_end).setVisibility(View.VISIBLE);
            }
        }, "1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 不显示时和分
        customDatePicker2.setIsLoop(false); // 允许循环滚动
    }

    private void showEndDate(){
        if(currentDateStart.getText().toString().isEmpty()){
            Toast.makeText(SearchCasesActivity.this, "請選擇開始時間", Toast.LENGTH_SHORT).show();
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String now = sdf.format(new Date());
            customDatePicker2 = new CustomDatePicker(SearchCasesActivity.this, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) { // 回调接口，获得选中的时间
                    currentDateEnd.setText(time.split(" ")[0]);
                    findViewById(R.id.down_end).setVisibility(View.VISIBLE);
                }
            }, currentDateStart.getText().toString() + " 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker2.showSpecificTime(false); // 不显示时和分
            customDatePicker2.setIsLoop(false); // 允许循环滚动

            customDatePicker2.show(currentDateStart.getText().toString());
        }
    }

    private int packCondition(){
        d_condition.clear();
        EditText keyword = findViewById(R.id.keyword);
        EditText year = findViewById(R.id.year);
        EditText zihao = findViewById(R.id.zihao);
        EditText num = findViewById(R.id.number);
        RadioGroup type = findViewById(R.id.type);
        TextView start = findViewById(R.id.start);
        TextView end = findViewById(R.id.end);
        EditText reason = findViewById(R.id.reason);
        EditText content = findViewById(R.id.content);
        EditText judge = findViewById(R.id.judge);
        int flag = 0;

        if(!keyword.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("keyword", keyword.getText().toString());
        }
        if(!year.getText().toString().isEmpty() || !zihao.getText().toString().isEmpty()
                || !num.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("year", year.getText().toString());
            d_condition.append("zihao", zihao.getText().toString());
            d_condition.append("num", num.getText().toString());
        }

        switch (type.getCheckedRadioButtonId()){
            case R.id.caidin: flag = 1;d_condition.append("type", "0");break;
            case R.id.panjue: flag = 1;d_condition.append("type", "1");break;
            default:break;
        }

        if(!start.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("start", start.getText().toString());
        }
        if(!end.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("end", end.getText().toString());
        }
        if(!reason.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("reason", reason.getText().toString());
        }
        if(!content.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("content", content.getText().toString());
        }
        if(!judge.getText().toString().isEmpty()){
            flag = 1;
            d_condition.append("judge", judge.getText().toString());
        }

        if(flag == 0){
            d_condition.clear();
            Toast.makeText(this,"請填寫檢索條件",Toast.LENGTH_LONG).show();
            return 0;
        }else{
            return 1;
        }
    }
}

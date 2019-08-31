package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private AlertDialog finished;
    private LawyerModel lawyer;
    private String question;
    private TextView content, lawyer_name, lawyer_job, lawyer_company, price,total;
    private List<String> imglst = new ArrayList<>();
    private Switch publish;
    private int state;
    private boolean isSubmitted;
    private LegalCounselingModel counseling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_payment_layout);

        lawyer = (LawyerModel)getIntent().getSerializableExtra("lawyer");
        question = getIntent().getStringExtra("content") ;
        imglst = (List<String>) getIntent().getSerializableExtra("imglst");

        initView();
        setData();

    }

    private void initView(){
        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);

        content = findViewById(R.id.content);
        lawyer_name = findViewById(R.id.lawyer_name);
        lawyer_job = findViewById(R.id.lawyer_job);
        lawyer_company = findViewById(R.id.lawyer_company);
        price = findViewById(R.id.price);
        publish = findViewById(R.id.publish);
        total = findViewById(R.id.total);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
                createCase();
            }
        });

        //返回按鈕
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void setData(){
        content.setText(question);
        lawyer_name.setText(lawyer.getName());
        lawyer_job.setText(lawyer.getJob());
        lawyer_company.setText(lawyer.getCompany());
        price.setText("￥" + lawyer.getPrice());
        total.setText("￥" + lawyer.getPrice());
    }

    private void createCase(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchCounseling.action");
            String newOne = new CounselingRepositoryImpl().createNew(content.getText().toString(),lawyer.getId(),sp.getString("_id","0"),imglst);
//            String newOne = new CounselingRepositoryImpl().createNew(text.getText().toString(),lawyerID,lawyerID);
//            URLEncoder.encode(newOne, "UTF-8");
            params.addQueryStringParameter("type","1");
            params.addQueryStringParameter("condition",newOne);
            params.setMaxRetryCount(0);
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray = new JSONArray().fromObject(s);
                    counseling = new CounselingRepositoryImpl().convertSingle(jArray.getJSONObject(0));
                    System.out.println(s);
                    setNext();
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    System.out.println("============" + throwable.getMessage());

                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setNext() {
        if (isSubmitted) {
            success();
        } else {
            state = failed();
            afterCreateDialog(state);
        }
    }

    private int failed() {
        autoSave();
        finished = new AlertDialog.Builder(this)
                .setTitle("您的諮詢信息")//设置对话框的标题
                .setMessage("送出失敗了誒~")//设置对话框的内容
                .setPositiveButton("重試", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submit();
                    }
                })
                .setNegativeButton("再看看~", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                        finish();
                    }
                })
                .create();
        return -1;
    }

    private void submit() {//提交
        isSubmitted = true;
        return;
    }

    private void success() {
        finish();
        QuestionLawyerActivity.questionLawyerActivity.finish();
        //跳至詳情頁面
        Intent intent = new Intent(ConfirmPaymentActivity.this, AfterFeePaidActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("counseling",counseling );
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void afterCreateDialog(int state) {

        finished.show();
        switch (state){
            case 1:
                Button back, cancel;
                back = finished.getButton(DialogInterface.BUTTON_POSITIVE);
                cancel = finished.getButton(DialogInterface.BUTTON_NEGATIVE);
                back.setTextColor(getResources().getColor(R.color.selector_item_color));
                cancel.setTextColor(getResources().getColor(R.color.selector_item_color));
                break;
            case -1:
                Button no, next;
                next = finished.getButton(DialogInterface.BUTTON_POSITIVE);
                no = finished.getButton(DialogInterface.BUTTON_NEGATIVE);
                next.setTextColor(getResources().getColor(R.color.selector_item_color));
                no.setTextColor(getResources().getColor(R.color.selector_item_color));
                break;
            case -2:
                Button confirm = finished.getButton(DialogInterface.BUTTON_POSITIVE);;
                confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
                break;
        }

    }

    private void autoSave() {

    }
}

package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.joan.myapplication.DIYComponent.SelectPicPopupWindow;
import com.example.joan.myapplication.database.model.BaseModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuickConsultActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next, upload;
    private ImageView back;
    private SelectPicPopupWindow upWindow;
    private EditText text;
    private boolean isSubmitted;
    private AlertDialog finished;
    private int length = 14, state;
    private String content, id, oi;

    SharedPreferences.Editor editor;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_consult);

        initItems();
    }

    private void initItems() {

        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);

        back = findViewById(R.id.quick_consult_back);
        next = findViewById(R.id.quick_consult_next);
        upload = findViewById(R.id.quick_consult_upload);
        text = findViewById(R.id.quick_consult_text);

        back.setOnClickListener(this);
        upload.setOnClickListener(this);
        next.setOnClickListener(this);

        upWindow = new SelectPicPopupWindow(QuickConsultActivity.this, itemsOnClick);
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = df.format(new Date().getTime());
        id = newDate + sp.getString("user_name", "invalid");
//        System.out.println(id);

        autoLoad();
    }

    private void autoLoad() {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.quick_consult_back:
                goBack();
                break;
            case R.id.quick_consult_upload:
                showWindow();
                break;
            case R.id.quick_consult_next:
                setNext();
                break;
        }
    }

    private void goBack() {

        if (isEmpty()){
            finish();
            overridePendingTransition(R.anim.left, R.anim.left_exit);
        }else{
            autoSave();
            state = confirmBack();
            finished.show();
            afterCreateDialog(state);
        }
    }

    private void autoSave() {

    }

    private void showWindow() {
        upWindow.showAtLocation(QuickConsultActivity.this.findViewById(R.id.quick_consult_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private void setNext() {
        if (isEnoughLength()) {
            submit();
        }else{
            state = setNotEnough();
            finished.show();
        }
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
            case 0:
            case -1:
                Button no, next;
                next = finished.getButton(DialogInterface.BUTTON_POSITIVE);
                no = finished.getButton(DialogInterface.BUTTON_NEGATIVE);
                next.setTextColor(getResources().getColor(R.color.selector_item_color));
                no.setTextColor(getResources().getColor(R.color.selector_item_color));
                break;
            case -2:
                Button confirm = finished.getButton(DialogInterface.BUTTON_POSITIVE);
                confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
                break;
        }

    }

    private int setNotEnough() {
        finished = new AlertDialog.Builder(this)
                .setTitle("字數不夠喔~")//设置对话框的标题
                .setMessage("最少字數需要" + length + "才可以提交！")//设置对话框的内容
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                    }
                })
                .create();
        return -2;
    }

    private int failed() {
        autoSave();
        finished = new AlertDialog.Builder(this)
                .setTitle("您的咨詢信息")//设置对话框的标题
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
                    }
                })
                .create();
        return -1;
    }

    private int success() {
        finished = new AlertDialog.Builder(this)
                .setTitle("您的咨詢信息")//设置对话框的标题
                .setMessage("已經成功送出咯~")//设置对话框的内容
                .setPositiveButton("查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setClass(QuickConsultActivity.this, QuickConsultResultActivity.class);
//                        intent.putExtra("id", );
                        intent.putExtra("id", oi);
                        startActivity(intent);
                        //跳至詳情頁面
                        finish();
                    }
                })
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create();
        return 0;
    }

    private int confirmBack(){
        finished = new AlertDialog.Builder(this)
                .setTitle("確定要退出嗎")//设置对话框的标题
                .setMessage("距離獲得幫助已經不遠了！現在退出您已經輸入的信息可能會丟失喔！")//设置对话框的内容
                .setPositiveButton("確定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        overridePendingTransition(R.anim.left, R.anim.left_exit);
                    }
                })
                .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finished.cancel();
                    }
                })
                .create();
        return 1;
    }

    private int repeat(){
        finished = new AlertDialog.Builder(this)
                .setTitle("重复的提交")//设置对话框的标题
                .setMessage("請不要心急，稍等片刻")//设置对话框的内容
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        overridePendingTransition(R.anim.left, R.anim.left_exit);
                    }
                })
                .create();
        return 1;
    }

    private void submit() {//提交
        isSubmitted = false;
        content = text.getText().toString();

        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/submitQuickConsult.action");
            params.addQueryStringParameter("id", id);
            params.addQueryStringParameter("author_id", sp.getString("_id", "invalid"));
            params.addQueryStringParameter("author_name", sp.getString("user_name", "invalid"));
            params.addQueryStringParameter("content", content);
            params.setMaxRetryCount(0);
            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
//                    System.out.println("111111111111111111111111111");
                    switch(jsonObject.get("state").getAsInt()){

                        case 1:
                            oi = jsonObject.get("id").getAsString();
//                            System.out.println(oi);
                            isSubmitted = true;
                            success();
                            afterCreateDialog(state);
//                            System.out.println("222222222222222222222222222");
                            break;
                        case 0:
                            failed();
                            afterCreateDialog(state);
                            break;
                        case -1:
                            repeat();
                            afterCreateDialog(state);
                    }

                }

                @Override
                public void onError(Throwable throwable, boolean b) {

                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            upWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    break;
                case R.id.btn_pick_photo:
                    break;
                default:
                    break;
            }
        }
    };

    private boolean isEnoughLength(){
        return text.getText().length() > length ? true : false;
    }

    private boolean isEmpty() {return text.getText().length() == 0? true: false;}
}

package com.example.joan.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joan.myapplication.DIYComponent.SelectPicPopupWindow;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class AnswerConsultActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private Button back, next, upload;
    private String lawyerName, lawyerID, fee;
    private String[] titleString = {"回答", "的提問"};
    private SelectPicPopupWindow upWindow;
    private int length = 20, state;
    private EditText text;
    private AlertDialog finished;
    private LegalCounselingModel counseling;
    private LawyerModel lawyer;
    private boolean isSubmitted;
    private LegalCounselingModel target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_lawyer);
        counseling = (LegalCounselingModel)getIntent().getSerializableExtra("counseling");
        lawyer = counseling.getLawyer();

        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {

        lawyerID = lawyer.getId();
        lawyerName = lawyer.getName();
        fee = lawyer.getPrice()+"";

//        upload = findViewById(R.id.question_lawyer_upload);
        title = findViewById(R.id.question_lawyer_title);
        back = findViewById(R.id.question_lawyer_back);
        next = findViewById(R.id.question_lawyer_next);
        text = findViewById(R.id.question_lawyer_text);

        next.setOnClickListener(this);
        back.setOnClickListener(this);
        upload.setOnClickListener(this);

        title.setText(titleString[0] + lawyerName + titleString[1]);
        upWindow = new SelectPicPopupWindow(AnswerConsultActivity.this, itemsOnClick);

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

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.question_lawyer_back:
                goBack();
                break;
//            case R.id.question_lawyer_upload:
//                showWindow();
//                break;
            case R.id.question_lawyer_next:
                if(!isEnoughLength()){
                    state = setNotEnough();
                    afterCreateDialog(state);
                }else{
                    createCase();
                }
                break;
        }

    }

    private void setNext() {
        if (isEnoughLength()) {
            submit();
            if (isSubmitted) {
                state = success();
            } else {
                state = failed();
            }
        }else{
            state = setNotEnough();
        }
        afterCreateDialog(state);
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
                        finished.cancel();
                        finish();
                        //跳至詳情頁面
                        Intent intent = new Intent(AnswerConsultActivity.this, MyAnswerLawyerConsultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("counseling", target);
                        intent.putExtras(bundle);
                        startActivity(intent);
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
                Button confirm = finished.getButton(DialogInterface.BUTTON_POSITIVE);;
                confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
                break;
        }

    }

    private void autoSave() {

    }


    private void submit() {//提交
        isSubmitted = true;
        return;
    }

    private void createCase(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/updateCounseling.action");
            String newOne = new CounselingRepositoryImpl().AnswerQuestion(text.getText().toString(),counseling);
            params.addQueryStringParameter("type","1");
            params.addQueryStringParameter("condition",newOne);
            params.setMaxRetryCount(0);
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray = new JSONArray().fromObject(s);
                    System.out.println(jArray);
                    target = new CounselingRepositoryImpl().convert(jArray).get(0);
                    setNext();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showWindow() {
        upWindow.showAtLocation(AnswerConsultActivity.this.findViewById(R.id.question_lawyer_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private boolean isEnoughLength(){
        return text.getText().length() > length ? true : false;
    }

    private boolean isEmpty() {return text.getText().length() == 0? true: false;}
}

package com.example.joan.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.DIYComponent.RatingBar;
import com.example.joan.myapplication.oneLineView.CounselingContentView;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.CounselingModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.URLEncoder;
import java.util.List;

public class MyQuestionLawyerConsultActivity extends AppCompatActivity {

    private ImageView lawyerPicture;
    private TextView nameJob;
    private TextView firm;
    private TextView title;
    private TextView cancel;
    private TextView message;
    private TextView scoreText;
    private LinearLayout content;
    private EditText consult;
    private Button submit;
    private int length = 14, state;
    private boolean isSubmitted;
    private AlertDialog finished;
    private LegalCounselingModel target;
    private RatingBar ratingBar;
//    private String[] text = {"向", "律師提問(剩餘機會" , "次)"};

    private LegalCounselingModel counseling;
    private String counselingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_lawyer_again);

        counselingId = (String) getIntent().getSerializableExtra("counseling");
        initView();
        searchCounseling();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isEnoughLength()){
                    state = confirmBack();
                    afterCreateDialog(state);
                }else{
                    finish();
                }
            }
        });
    }

    private void initView(){
        lawyerPicture = findViewById(R.id.lawyer_picture);
        nameJob = findViewById(R.id.name_job);
        firm = findViewById(R.id.firm);
        content = findViewById(R.id.counseling_main);
        consult = findViewById(R.id.lawyer_consult);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        cancel = findViewById(R.id.cancel_case);
        message = findViewById(R.id.message);
        ratingBar = findViewById(R.id.rc_rate);
        scoreText = findViewById(R.id.score_text);
    }

    private void searchCounseling(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchCounseling.action");
            params.addQueryStringParameter("condition",counselingId);
            params.addQueryStringParameter("type","4");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    counseling = new CounselingRepositoryImpl().convertSingle(jArray.getJSONObject(0));
                    initData();
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    System.out.println(throwable.getMessage());
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

    @SuppressLint("SetTextI18n")
    private void initData(){
//        lawyerPicture.sP
//        nameJob.setText(counseling.getLawyer().getName() + "  " + counseling.getLawyer().getJob());
//        firm.setText(counseling.getLawyer().getFirm());

        ratingBar.setStar((float)counseling.getLawyer().getComment());
        if(counseling.getLawyer().getComment() > 4){
            scoreText.setText("力薦");
        }
        else if(counseling.getLawyer().getComment() > 3){
            scoreText.setText("推薦");
        }
        else if(counseling.getLawyer().getComment() > 2){
            scoreText.setText("尚可");
        }
        else if(counseling.getLawyer().getComment() > 1){
            scoreText.setText("不推薦");
        }
        else{
            scoreText.setText("很糟糕");
        }

        consult.setHint("你還有" + (3 - counseling.getContent().size()) + "次追問機會，點此輸入");
        if(counseling.getContent().size() == 3){
            consult.setHint("您的提問次數已達上限");
            consult.setEnabled(false);
        }
        nameJob.setText(counseling.getLawyer().getName() + " " + counseling.getLawyer().getJob());
        firm.setText(counseling.getLawyer().getCompany());
//        consult.setText(text[0] + counseling.getLawyer().getName() + text[1] + (3 - counseling.getContent().size()) + text[2]);

        if(counseling.getState() == 0){
            message.setText("該律師平均回應時間為3小時，請耐心等候");
            cancel.setText("取消提問");
        }
        else if(counseling.getState() == 1){
            message.setText("該律師平均回應時間為3小時，請耐心等候");
            cancel.setText("結束提問");
        }
        else if(counseling.getState() == 5){
            message.setText("您的提問次數已達上限");
            cancel.setText("結束提問");
        }

        final List<CounselingModel> counselings = counseling.getContent();

        for(int i = 0; i < counselings.size(); i++){
            CounselingModel cur = counselings.get(i);
            content.addView(new CounselingContentView(getBaseContext()).init(i+1,cur,counseling.getLawyer().getName()));
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNext();
            }
        });

        consult.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(consult.isFocused()){
                    consult.setLines(6);
                    consult.setSingleLine(false);
//                    consult.setSelection(consult.length());
                    title.setVisibility(View.VISIBLE);
                    title.setText("第" + (counseling.getContent().size() + 1) + "次提問");
                }else{
                    InputMethodManager imm = (InputMethodManager) MyQuestionLawyerConsultActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.toggleSoftInput(0,0);
                    }
                    consult.setLines(1);
                    consult.setSingleLine(true);
                    findViewById(R.id.title).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.law_firm_result_scroll).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.law_firm_result_scroll).setFocusable(true);
                findViewById(R.id.law_firm_result_scroll).setFocusableInTouchMode(true);
                findViewById(R.id.law_firm_result_scroll).requestFocus();
                return false;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counseling.getState() == 0){//取消提問
                    cancelCase();
                }
                else if(counseling.getState() == 1 || counseling.getState() == 5){//結束提問
                    finishCase();
                }
            }
        });

        lawyerPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyQuestionLawyerConsultActivity.this, LawyerConsultDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("lawyer", counseling.getLawyer().getId());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });
    }

    private void setNext() {
        if (isEnoughLength()) {
            submit();
            if (isSubmitted) {
                createCase();
            } else {
                state = failed();
                afterCreateDialog(state);
            }
        }else{
            state = setNotEnough();
            afterCreateDialog(state);
        }
    }

    private int failed() {
//        autoSave();
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

    private void success() {
        Intent intent=new Intent();
        intent.setClass(MyQuestionLawyerConsultActivity.this, MyQuestionLawyerConsultActivity.class); //设置跳转的Activity
        Bundle bundle = new Bundle();
        bundle.putSerializable("counseling", target.getId());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
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

    private void submit() {//提交
        isSubmitted = true;
        return;
    }

    private void createCase(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/updateCounseling.action");
            String newOne = new CounselingRepositoryImpl().QuestionAgain(consult.getText().toString(),counseling);
            System.out.println(newOne);
//            URLEncoder.encode(newOne, "UTF-8");
            params.addQueryStringParameter("type","1");
            params.addQueryStringParameter("condition",newOne);
            params.setMaxRetryCount(0);
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray = new JSONArray().fromObject(s);
                    target = new CounselingRepositoryImpl().convert(jArray).get(0);
                    success();
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

    private void cancelCase(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/updateCounseling.action");
            String newOne = new CounselingRepositoryImpl().ChangeState(4, counseling);
            System.out.println(newOne);
            URLEncoder.encode(newOne, "UTF-8");
            params.addQueryStringParameter("type","1");
            params.addQueryStringParameter("condition",newOne);
            params.setMaxRetryCount(0);
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray = new JSONArray().fromObject(s);
                    target = new CounselingRepositoryImpl().convert(jArray).get(0);
                    Intent intent = new Intent(MyQuestionLawyerConsultActivity.this, QuestionLawyerFinishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("counseling", target.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
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

    private void finishCase(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/updateCounseling.action");
            String newOne = new CounselingRepositoryImpl().ChangeState(2, counseling);
            System.out.println(newOne);
            URLEncoder.encode(newOne, "UTF-8");
            params.addQueryStringParameter("type","1");
            params.addQueryStringParameter("condition",newOne);
            params.setMaxRetryCount(0);
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray = new JSONArray().fromObject(s);
                    target = new CounselingRepositoryImpl().convert(jArray).get(0);
                    Intent intent = new Intent(MyQuestionLawyerConsultActivity.this, QuestionLawyerFinishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("counseling", target.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
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

    private boolean isEnoughLength(){
        return consult.length() > length ? true : false;
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
}




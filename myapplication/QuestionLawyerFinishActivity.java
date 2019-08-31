package com.example.joan.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
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

public class QuestionLawyerFinishActivity extends AppCompatActivity {

    private ImageView lawyerPicture;
    private TextView nameJob;
    private TextView message;
    private TextView delete;
    private TextView cancel;
    private TextView firm;
    private TextView scoreText;
    private LinearLayout content;
    private RatingBar ratingBar, select;
    private  AlertDialog comment;
    private double score;
    private LegalCounselingModel counseling;
    private String counselingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_lawyer_finish);

        counselingId = (String) getIntent().getSerializableExtra("counseling");
        initView();
        searchCounseling();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void initView(){
        lawyerPicture = findViewById(R.id.lawyer_picture);
        nameJob = findViewById(R.id.name_job);
        firm = findViewById(R.id.firm);
        content = findViewById(R.id.counseling_main);
        delete = findViewById(R.id.delete);
        message = findViewById(R.id.message);
        cancel = findViewById(R.id.cancel_case);
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

        nameJob.setText(counseling.getLawyer().getName() + " " + counseling.getLawyer().getJob());
        firm.setText(counseling.getLawyer().getCompany());

        final List<CounselingModel> counselings = counseling.getContent();

        for(int i = 0; i < counselings.size(); i++){
            CounselingModel cur = counselings.get(i);
            content.addView(new CounselingContentView(getBaseContext()).init(i+1,cur,counseling.getLawyer().getName()));
        }

        if(counseling.getState() == 2){
            message.setText("該咨詢已結束，你還未評價喲");
            findViewById(R.id.comment).setVisibility(View.GONE);
            cancel.setText("前往評價");
        }
        else if(counseling.getState() == 3){//結束
            message.setText("已結束");
            ratingBar.setClickable(false);
            ratingBar.setStar((float) counseling.getComment());
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
            cancel.setVisibility(View.GONE);
        }
        else if(counseling.getState() == 4){//結束
            message.setText("已取消");
            findViewById(R.id.comment).setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(QuestionLawyerFinishActivity.this);
                final View v = inflater.inflate(R.layout.select_stars, null);
                select = v.findViewById(R.id.select_rate);
                select.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float ratingCount) {
                        TextView text = v.findViewById(R.id.star_text);
                        if(ratingCount == 5){
                            text.setText("力薦");
                        }
                        else if(ratingCount > 3){
                            text.setText("推薦");
                        }
                        else if(ratingCount > 2){
                            text.setText("尚可");
                        }
                        else if(ratingCount > 1){
                            text.setText("不推薦");
                        }
                        else{
                            text.setText("很糟糕");
                        }
                    }
                });
                comment = new AlertDialog.Builder(QuestionLawyerFinishActivity.this)
                        .setTitle("請輸入評價")
                        .setView(v)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                comment.cancel();
                            }
                        })
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createCase();
                                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + select.getStarStep() + "###################################");
                                comment.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private void createCase(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/updateCounseling.action");
            String newOne = new CounselingRepositoryImpl().ChangeState(select.getStarStep(), counseling);
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
                    LegalCounselingModel target = new CounselingRepositoryImpl().convert(jArray).get(0);
                    Intent intent=new Intent();
                    intent.setClass(QuestionLawyerFinishActivity.this, QuestionLawyerFinishActivity.class); //设置跳转的Activity
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

}

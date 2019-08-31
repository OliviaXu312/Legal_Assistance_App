package com.example.joan.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;
import com.example.joan.myapplication.oneLineView.CounselingContentView;
import com.example.joan.myapplication.database.model.CounselingModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class CounselingDetailActivity extends AppCompatActivity {

    private ImageView lawyerPicture;
    private TextView nameJob;
    private TextView firm;
    private LinearLayout content;
    private Button consult;
    private String[] text = {"向", "律師付費提問(NT$", ")"};

    private  String counselingId;
    private  LegalCounselingModel counseling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counseling_detail);

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
        consult = findViewById(R.id.lawyer_consult);
    }

    @SuppressLint("SetTextI18n")
    private void initData(){
//        lawyerPicture.sP
//        nameJob.setText(counseling.getLawyer().getName() + "  " + counseling.getLawyer().getJob());
//        firm.setText(counseling.getLawyer().getFirm());

        nameJob.setText(counseling.getLawyer().getName() + " " + counseling.getLawyer().getJob());
        firm.setText(counseling.getLawyer().getCompany());
        consult.setText(text[0] + counseling.getLawyer().getName() + text[1] + counseling.getLawyer().getPrice() + text[2]);

        List<CounselingModel> counselings = counseling.getContent();

        for(int i = 0; i < counselings.size(); i++){
            CounselingModel cur = counselings.get(i);
            content.addView(new CounselingContentView(getBaseContext()).init(i+1,cur,counseling.getLawyer().getName()));
        }

        consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CounselingDetailActivity.this, QuestionLawyerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("lawyer", counseling.getLawyer());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });

        lawyerPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CounselingDetailActivity.this, LawyerConsultDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("lawyer", counseling.getLawyer().getId());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });

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

}

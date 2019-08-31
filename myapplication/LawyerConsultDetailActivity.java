package com.example.joan.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.Case;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.LawyerRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LawyerConsultDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String id;
    private String lawyerId;
    private LawyerModel lawyer = new LawyerModel();
    private TextView name, detail, year, times, scholar, occupation, special, personality;
    private Button question, follow, seeAll;
    private ImageView back;
    private String[] text = {"向", "律師付費提問(NT$", ")"};
    private Case[] cases = new Case[3];
    private LayoutInflater li;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_consult_detail);
        lawyerId = (String)getIntent().getSerializableExtra("lawyer") ;

        initView();
        getData();

    }

    private void initView() {
//        getID();
//        getCases();

        name = findViewById(R.id.consult_lawyer_detail_name);
        detail = findViewById(R.id.consult_lawyer_detail_detail);
        year = findViewById(R.id.consult_lawyer_detail_year);
        times = findViewById(R.id.consult_lawyer_detail_times);
        scholar = findViewById(R.id.consult_lawyer_detail_scholar);
        occupation = findViewById(R.id.consult_lawyer_detail_occupation);
        special = findViewById(R.id.consult_lawyer_detail_special);
        personality = findViewById(R.id.consult_lawyer_detail_personal);
        question = findViewById(R.id.consult_lawyer_detail_question);
        back = findViewById(R.id.consult_lawyer_detail_back);
        follow = findViewById(R.id.consult_lawyer_detail_follow);
        seeAll = findViewById(R.id.consult_lawyer_detail_seeall);
        
        back.setOnClickListener(this);
        question.setOnClickListener(this);
        follow.setOnClickListener(this);
        seeAll.setOnClickListener(this);

        li = LayoutInflater.from(this);
        ll = findViewById(R.id.consult_lawyer_detail_cases);

    }

    private void getData(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchLawyer.action");
            params.addQueryStringParameter("condition",lawyerId);
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            params.addQueryStringParameter("type","2");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    lawyer = new LawyerRepositoryImpl().convert_single(jArray.getJSONObject(0));
                    /**/
                    setData();
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

    @Override
    public void onClick(View view) {
        
        switch (view.getId()){
            case R.id.consult_lawyer_detail_back:
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                break;
            case R.id.consult_lawyer_detail_question:
                questionTheLawyer();
                break;
            case R.id.consult_lawyer_detail_follow:
                followTheLawyer();
                break;
            case R.id.consult_lawyer_detail_seeall:
                seeAll();
                break;
        }
        
    }

    private void questionTheLawyer() {
        Intent intent = new Intent(LawyerConsultDetailActivity.this, QuestionLawyerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lawyer", lawyer);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.right, R.anim.left);
    }

    private void followTheLawyer(){
        overridePendingTransition(R.anim.right, R.anim.left);
    }

    private void seeAll(){
        Intent intent = new Intent(LawyerConsultDetailActivity.this, LawyerConsultAllCasesActivity.class);
        intent.putExtra("lawyerID", lawyer.getId());
        intent.putExtra("name", lawyer.getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right, R.anim.left);
    }

//    public void getID() {
//
//        Intent intent = getIntent();
//        id = intent.getStringExtra("lawyerID");
//        System.out.println(id);
//
//    }

//    private void getCases() {
//
//        for (int i = 0; i < 3; i ++){
//            Case newCase = new Case();
//            newCase.setBrief("This is the case brief");
//            newCase.setCaseDate("2018/9/8");
//            newCase.setCaseTime("19:59");
//            newCase.setLawyerID(lawyer.getId());
//            newCase.setUserID("用戶123456");
//            cases[i] = newCase;
//        }
//
//    }

    @SuppressLint("SetTextI18n")
    private void setData() {

        name.setText(lawyer.getName());
        detail.setText(lawyer.getJob() + " " + lawyer.getCompany());
//        year.setText(String.valueOf(lawyer.getYear()));
//        times.setText(String.valueOf(lawyer.getTimes()));
        scholar.setText(lawyer.getEducation());
        occupation.setText(lawyer.getJob() + " " + lawyer.getCompany());
        special.setText(lawyer.getMajor());
        personality.setText(lawyer.getDescription());
        question.setText(text[0] + lawyer.getName() + text[1] + lawyer.getPrice() + text[2]);

        setCases();

    }

    private void setCases() {

        for (LegalCounselingModel singleCase: lawyer.getCounselingList()) {
            View view = li.inflate(R.layout.sample_lawyer_consult_single_case, null);
            setCaseData(view, singleCase);
            setCaseListener(view, singleCase.getId());
            ll.addView(view);
        }
        System.out.println(lawyer.getCounselingList().get(0).getContent().get(0).getQuestion());

    }

    private void setCaseData(View view, LegalCounselingModel singleCase) {

        TextView userName, brief, date, time, visit;

        userName = view.findViewById(R.id.lawyer_consult_single_case_username);
        brief = view.findViewById(R.id.lawyer_consult_single_case_brief);
        date = view.findViewById(R.id.lawyer_consult_single_case_date);
        time = view.findViewById(R.id.lawyer_consult_single_case_time);
        visit = view.findViewById(R.id.lawyer_consult_single_case_visit);

        userName.setText("匿名用户");
        brief.setText(singleCase.getContent().get(0).getQuestion());
        date.setText(singleCase.getCreateTime().split(" ")[0]);
        time.setText(singleCase.getCreateTime().split(" ")[1]);
        visit.setText(singleCase.getViewCount() + "人看過");

    }

    private void setCaseListener(View view, final String id) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerConsultDetailActivity.this, CounselingDetailActivity.class);
                intent.putExtra("counseling", id);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });
    }

}

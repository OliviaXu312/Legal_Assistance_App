package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;
import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.oneLineView.MyLawyerConsultLayout;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LawyerConsultAllCasesActivity extends AppCompatActivity implements MyLawyerConsultLayout.OnRootClickListener{

    private String id, name, titleString = "律師的所有公開案件";
    private Button back;
    private TextView title;
    private LinearLayout ll;
    private LayoutInflater li;
    private List<LegalCounselingModel> cases = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_consult_all_cases);

        initView();
        getData();

    }

    private void initView() {
        li = LayoutInflater.from(this);
        Intent intent = getIntent();

        id = intent.getStringExtra("lawyerID");
        name = intent.getStringExtra("name");

        back = findViewById(R.id.consult_lawyer_all_cases_back);
        title = findViewById(R.id.consult_lawyer_all_cases_title);
        ll = findViewById(R.id.consult_lawyer_all_cases_list);

        title.setText(name + titleString);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
            }
        });

    }

    private void initList() {

        for (LegalCounselingModel singleCase: cases){
            View view = li.inflate(R.layout.sample_lawyer_consult_single_case, null);
            setData(view, singleCase);
            setListener(view, singleCase.getId());
            ll.addView(view);
        }

    }

    public void CounselingView(){
        if(cases.isEmpty()){
            ll.addView(new FindNothingView(this).init());
        }else{
            int index = 0;
            for (LegalCounselingModel counseling: cases
                    ) {
                String question = counseling.getContent().get(0).getQuestion();

                String createTime = counseling.getCreateTime().replace('T',' ');
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateString = formatter.format(createTime);

                String name = counseling.getQuestioner();
                String job = "";
                int view_count = counseling.getViewCount();

                ll.addView(new MyLawyerConsultLayout(this)
                        .initUser("匿名用戶",job,view_count,question, createTime)
                        .setOnRootClickListener(this, index));
                index++;
            }
        }
    }

    private void setData(View view, LegalCounselingModel singleCase){

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
        visit.setText(singleCase.getViewCount());

    }

    private void setListener(View view, String id) {
    }

    public void getData() {

        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchCounseling.action");
            params.addQueryStringParameter("condition",id);
            params.addQueryStringParameter("type","3");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    cases = new CounselingRepositoryImpl().convert(jArray);
                    CounselingView();
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
    public void onRootClick(View v) {
        LegalCounselingModel counseling = cases.get((int)v.getTag());
        Intent intent=new Intent();
        intent.setClass(v.getContext(), CounselingDetailActivity.class); //设置跳转的Activity
        Bundle bundle = new Bundle();
        bundle.putSerializable("counseling", counseling.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

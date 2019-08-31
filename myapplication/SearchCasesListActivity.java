package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.oneLineView.CaseOneLineView;
import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.JudgementModel;
import com.example.joan.myapplication.database.repository.JudgementRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchCasesListActivity extends AppCompatActivity implements CaseOneLineView.OnRootClickListener{

    private List<JudgementModel> judgementList = new ArrayList<>();
    private String condition, type;
    private TextView searching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_case_result);

        condition = getIntent().getStringExtra("condition");
        type = getIntent().getStringExtra("type");
        searching = findViewById(R.id.searching);
        searchCase();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    private void searchCase(){
        SearchCasesListActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchJudgement.action");
            params.addQueryStringParameter("condition",condition);
            params.addQueryStringParameter("type",type);
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    System.out.println(jArray);
                    judgementList = new JudgementRepositoryImpl().convert(jArray);
                    searching.setVisibility(View.GONE);
                    initView();
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

    private void initView(){
        LinearLayout result = findViewById(R.id.case_result);
        if(judgementList.size() == 0){
            result.addView(new FindNothingView(getBaseContext()).init());
        }else{
            for(int i = 0 ; i < judgementList.size(); i++){
                String[] a = judgementList.get(i).getjId().split(" ",2);
                System.out.println(judgementList.get(i).getjId());
                String mainData = judgementList.get(i).getjContent().replace("\\r", "")
                        .replace("\\n", "")
                        .replace("\n", "")
                        .replace("\r", "")
                        .replace("\t", "")
                        .replace("\\s", "");

                Pattern p = Pattern.compile("理由.*", Pattern.DOTALL);
                Matcher m = p.matcher(mainData);
//            System.out.println(mainData);
                System.out.println(m.find());
                String content = m.group().replaceAll("理由","");
                result.addView(new CaseOneLineView(getBaseContext())
                        .init(a[0],a[1].split(" \\[")[0],judgementList.get(i).getjReason(),
                                "#民事",content)
                        .setOnRootClickListener(this, i));
            }
        }

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    public void onRootClick(View v) {
//        LawFirmModel firm = lawFirmRepository.findById((ObjectId)v.getTag());
//        setContentView(R.layout.law_firm_detail);
//
        switch (v.getId()){
            case R.id.btn_back:{
                findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        finish();
                    }
                });
            }break;

            default:{
                Intent intent=new Intent();
                intent.setClass(v.getContext(), SearchCaseDetailActivity.class); //设置跳转的Activity
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", judgementList.get((int)v.getTag()).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }break;
        }

    }

    private String getContent(String mainData) {

        String content = "";

        content = mainData.substring(mainData.indexOf("主文") + 2, mainData.indexOf("理由"));
        content.replaceAll("中華民國","\n\r        中華民國");

        return content;

    }
}

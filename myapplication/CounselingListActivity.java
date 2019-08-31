package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.oneLineView.CounselingResultLayout;
import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CounselingListActivity extends AppCompatActivity implements CounselingResultLayout.OnRootClickListener{

    private  List<LegalCounselingModel> counselingList = new ArrayList<>();
    private String condition, type;
    private TextView searching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counseling_result);

        condition = getIntent().getStringExtra("condition");
        type = getIntent().getStringExtra("type");
        searching = findViewById(R.id.searching);

        searchCounseling();
    }

    private void searchCounseling(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchCounseling.action");
            params.addQueryStringParameter("condition",condition);
            params.addQueryStringParameter("type",type);
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    counselingList = new CounselingRepositoryImpl().convert(jArray);
                    searching.setVisibility(View.GONE);
                    CounselingView(counselingList);
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

    public void CounselingView(List<LegalCounselingModel> counselingList){
        LinearLayout result = findViewById(R.id.counseling_result);
        if(counselingList.isEmpty()){
            result.addView(new FindNothingView(getBaseContext()).init());
        }else{
            int index = 0;
            for (LegalCounselingModel counseling: counselingList
                    ) {
                String question = counseling.getContent().get(0).getQuestion();

                String createTime = counseling.getCreateTime().replace('T',' ');
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateString = formatter.format(createTime);

                String viewCount = counseling.getViewCount() + "个人看过";

                result.addView(new CounselingResultLayout(getBaseContext())
                        .init(question, createTime,viewCount)
                        .setOnRootClickListener(this, index));
                index++;
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
                LegalCounselingModel a = counselingList.get((int)v.getTag());
                Intent intent=new Intent();
                intent.setClass(v.getContext(), CounselingDetailActivity.class); //设置跳转的Activity
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable("counseling", a.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }break;
        }
    }
}

package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.oneLineView.LawOneLineView;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawModel;
import com.example.joan.myapplication.database.repository.LawRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class SearchLawListActivity extends AppCompatActivity implements LawOneLineView.OnRootClickListener {

    String condition, type;
    List<LawModel> lawList;
    TextView searching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_law_result);

        condition = getIntent().getStringExtra("condition");
        type = getIntent().getStringExtra("type");
        searching = findViewById(R.id.searching);
        searchLaw();
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });


    }

    private void searchLaw(){
        SearchLawListActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchLaw.action");
            params.addQueryStringParameter("condition",condition);
            params.addQueryStringParameter("type", type);
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    lawList = new LawRepositoryImpl().convert(jArray);
                    searching.setVisibility(View.GONE);
                    LawView(lawList);
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

    private void LawView(List<LawModel> lawList){
        LinearLayout result = findViewById(R.id.law_result);
        for(int i = 0 ; i < lawList.size(); i++){
            result.addView(new LawOneLineView(getBaseContext())
                    .init(lawList.get(i).getName(),lawList.get(i).getContent(),"#民事")
                    .setOnRootClickListener(this, i));
        }
        if(lawList.size()==0){
            result.addView(new FindNothingView(getBaseContext()).init());
        }

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                Intent intent = new Intent(v.getContext(), SearchLawActivity.class);
//                startActivity(intent);
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
                LawModel l = lawList.get((int)v.getTag());
                Intent intent=new Intent();
                intent.setClass(v.getContext(), SearchLawDetailActivity.class); //设置跳转的Activity
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bunble = new Bundle();
                bunble.putSerializable("law", l);
                intent.putExtras(bunble);
                startActivity(intent);
            }break;
        }

    }


}

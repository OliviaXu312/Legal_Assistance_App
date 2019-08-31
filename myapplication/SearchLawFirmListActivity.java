package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.oneLineView.FirmOneLineView;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawFirmModel;
import com.example.joan.myapplication.database.repository.LawFirmRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class SearchLawFirmListActivity extends AppCompatActivity implements FirmOneLineView.OnRootClickListener{

    private  String condition, type;
    List<LawFirmModel> firmList;
    private TextView searching;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_firm_search_result);

        condition = getIntent().getStringExtra("condition");
        type = getIntent().getStringExtra("type");
        searching = findViewById(R.id.searching);
        searchLawFirm();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    private void searchLawFirm(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchFirm.action");
            params.addQueryStringParameter("condition",condition);
            params.addQueryStringParameter("type", "0");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    firmList = new LawFirmRepositoryImpl().convert(jArray);
                    searching.setVisibility(View.GONE);
                    FirmView(firmList);
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

    public void FirmView(List<LawFirmModel> firmList){
        LinearLayout result = findViewById(R.id.law_firm_result);
        if(firmList.size() == 0){
            result.addView(new FindNothingView(getBaseContext()).init());
        }else{
            int index = 0;
            for (LawFirmModel firm: firmList
                    ) {
                result.addView(new FirmOneLineView(getBaseContext())
                        .init(firm.getName(), firm.getAddress() ,"#民事")
                        .setOnRootClickListener(this, index));
            }
            if(firmList.size()==0){
                result.addView(new FindNothingView(getBaseContext()).init());
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
                LawFirmModel l = firmList.get((int)v.getTag());
                Intent intent=new Intent();
                intent.setClass(v.getContext(), SearchLawFirmDetailActivity.class); //设置跳转的Activity
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bunble = new Bundle();
                bunble.putSerializable("firm", l);
                intent.putExtras(bunble);
                startActivity(intent);
            }break;
        }

    }
}

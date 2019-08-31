package com.example.joan.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.example.joan.myapplication.database.model.LawModel;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;
import com.example.joan.myapplication.database.repository.LawyerRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LawyerConsultSortedListActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll;
    private LayoutInflater li;
    private int lNumber = 10;
    private TextView title, name, identity, branch, special,response, fee, rate, image;
    private List<LawyerModel> totalData = new ArrayList<>();
    private Intent intent;
    private String sortName;
    private Button back;

    private List<LawyerModel> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_consult_sorted_list);

        initView();
        addView();

    }

    private void initView() {
        ll = findViewById(R.id.consult_lawyer_sorted_list);
        li = LayoutInflater.from(this);

        intent = getIntent();
        sortName = intent.getStringExtra("sortName");

        title = findViewById(R.id.consult_lawyer_sorted_list_title);
        title.setText(sortName.toString());

        back = findViewById(R.id.consult_lawyer_sorted_list_back);
        back.setOnClickListener(this);

    }

    public void addView() {
        List<LawyerModel> list = getData();
        for (LawyerModel lawyer: list){
            View view = li.inflate(R.layout.sample_lawyer_consult_single_lawyer, null);
            setData(view, lawyer);
            setListener(view, lawyer.getId());
            ll.addView(view);
        }
    }

    private void setListener(View view, final String id) {//跳转至律师详情// 页面
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerConsultSortedListActivity.this, LawyerConsultDetailActivity.class);
                intent.putExtra("lawyerID", id.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });
    }

    private List getData(){
        try{
            RequestParams params = new RequestParams("http://140.136.7.72:8080/searchLawyer.action");
            params.addQueryStringParameter("condition","");
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            params.addQueryStringParameter("type","0");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    list = new LawyerRepositoryImpl().convert(jArray);
                    LawyerModel l = list.get(0);
                    for(int i = 0 ; i < 10 ; i++){
                        list.add(l);
                    }
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


        totalData.addAll(list);
        return list;
    }

    private void setData(View view, LawyerModel lawyer){

        name = view.findViewById(R.id.lawyer_consult_single_name);
        identity = view.findViewById((R.id.lawyer_consult_single_identity));
        branch = view.findViewById((R.id.lawyer_consult_single_branch));
        special = view.findViewById((R.id.lawyer_consult_single_special));
        response = view.findViewById((R.id.lawyer_consult_single_responce));
        fee = view.findViewById((R.id.lawyer_consult_single_fee));
        rate = view.findViewById((R.id.lawyer_consult_single_rate));
//        image = view.findViewById((R.id.lawyer_consult_single_image));

        name.setText(lawyer.getName());
        identity.setText(lawyer.getJob());
//        branch.setText(lawyer.getBranch());
//        special.setText(lawyer.getSpecial());
//        response.setText(lawyer.getResponse());
        fee.setText(lawyer.getPrice()+"");
        rate.setText(lawyer.getComment()+"");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.consult_lawyer_sorted_list_back:
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                break;
        }
    }
}

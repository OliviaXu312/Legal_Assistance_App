package com.example.joan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.LawyerConsultActivity;
import com.example.joan.myapplication.LawyerConsultDetailActivity;
import com.example.joan.myapplication.R;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawyerModel;
import com.example.joan.myapplication.database.repository.LawyerRepositoryImpl;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LawyerConsultLawyerListFragment extends Fragment {

    private LinearLayout ll;
    private LayoutInflater li;
    private int lNumber = 10;
    private TextView name, identity, branch, special,response, fee, rate, image, searching;
    private List<LawyerModel> totalData = new ArrayList<>();
    List<LawyerModel> list = new ArrayList();

    @Override
    public View onCreateView (LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_lawyer_consult_lawyer_list, null);

        initView(view);
        getData();

        return view;
    }

    private void initView(View view) {
        ll = view.findViewById(R.id.consult_lawyer_list);
        li = LayoutInflater.from(getContext());
        searching = view.findViewById(R.id.searching);
    }

    public void addView() {
//        List<LawyerModel> listl = getData();
        searching.setVisibility(View.GONE);

        for (LawyerModel lawyer: list){
            View view = li.inflate(R.layout.sample_lawyer_consult_single_lawyer, null);
            System.out.println("我想要输出" + lawyer.getComment());
            setData(view, lawyer);
            setListener(view, lawyer);
            ll.addView(view);
        }
    }

    private void setListener(View view, final LawyerModel lawyer) {//跳转至律师详情页面
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerConsultActivity.page, LawyerConsultDetailActivity.class);
                intent.putExtra("lawyer", lawyer.getId());
                startActivity(intent);
                LawyerConsultActivity.page.overridePendingTransition(R.anim.right, R.anim.left);
            }
        });
    }

    private void getData(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/searchLawyer.action");
            params.addQueryStringParameter("condition","");
//            params.addQueryStringParameter("condition","吕浩然觉得不用写");
            params.addQueryStringParameter("type","0");
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    list = new LawyerRepositoryImpl().convertList(jArray);
                    /**/
                    addView();
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
        branch.setText(lawyer.getCompany());
        special.setText("擅長：" + lawyer.getMajor());
        response.setText("平均響應時間：1小時2分鐘");
        fee.setText("NT " + lawyer.getPrice());
        rate.setText(lawyer.getComment()+"");
    }

    public List getTotalList(){return totalData;}

    public void setlNumber(int num) {this.lNumber = num;}

    public int getlNumber(){return lNumber;}

}

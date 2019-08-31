package com.example.joan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class MainSearchResultLawyer extends Fragment {
    public static List<LawyerModel> lawyerModels= new ArrayList<>();
    private static int flag = 1;
    int position = 0;
    private TextView ft;
    private LinearLayout ll;
    private LayoutInflater li;
    private View view;
    private String keyWord;


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        if (flag == 1){
            view = inflater.inflate(R.layout.activity_main_search_result_lawyer, container, false);
            ft = view.findViewById(R.id.main_search_result_lawyer_text);
            ll = view.findViewById(R.id.main_search_result_lawyer_list);
            li = LayoutInflater.from(getContext());
            flag = 0;
//                if  (getData(position) == 1) initView();
//                else failGetData();
            try {
                RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
                params.addQueryStringParameter("key", keyWord);
                params.addQueryStringParameter("pageType", String.valueOf(position));
                params.setMaxRetryCount(0);
                System.out.println(params);
                x.http().get(params, new Callback.CommonCallback<String>(){

                    @Override
                    public void onSuccess(String s) {
                        JSONArray jArray= JSONArray.fromObject(s);
                        lawyerModels = new LawyerRepositoryImpl().convertList(jArray);
//                            result[position] = data.get("lawyer").toString();
                        initView();
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        initFail();
                    }
                    @Override
                    public void onCancelled(CancelledException e) {
                        initFail();
                    }
                    @Override
                    public void onFinished() { }
                });
            }catch (Exception e){
            }

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//            initView();
    }

    public void initView() {
        if (lawyerModels.size() == 0) setNothing();
        else setList();
    }

    private void setNothing(){
        ft.setText(R.string.main_search_result_nothing);
    }

    private void setList(){
        ll.removeAllViews();
        for (LawyerModel lawyer: lawyerModels){
            View view = li.inflate(R.layout.sample_lawyer_consult_single_lawyer, null);
//                System.out.println("我想要输出" + lawyer.getComment());
            setData(view, lawyer);
            setListener(view, lawyer);
            ll.addView(view);
        }
    }

    private void setData(View view, LawyerModel lawyer){
        TextView name, identity, branch, special,response, fee, rate, image, searching;
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

    private void setListener(View view, final LawyerModel lawyer) {//跳转至律师详情页面
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LawyerConsultDetailActivity.class);
                intent.putExtra("lawyer", lawyer.getId());
                startActivity(intent);
                LawyerConsultActivity.page.overridePendingTransition(R.anim.right, R.anim.left);
            }
        });
    }

    public void initFail(){
        ft.setText(R.string.main_search_result_error);
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

package com.example.joan.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.QuickConsultResultActivity;
import com.example.joan.myapplication.R;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.QuickConsultModel;
import com.example.joan.myapplication.database.repository.QuickResponseRepositoryImpl;
import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.oneLineView.FirmOneLineView;
import com.example.joan.myapplication.oneLineView.QuickConsultSingleView;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class MyQuickResponseFragment extends Fragment implements FirmOneLineView.OnRootClickListener {


    private SharedPreferences sp;

    private String position;
    private TextView ft;
    private LinearLayout ll;
    private List<QuickConsultModel> counselingModels;
    private View view = null;

//    public int getFlag() {
//        return flag;
//    }

//    public void setFlag(int flag) {
//        this.flag = flag;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get title
        position = getArguments().getString("position");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("account_info", Context.MODE_PRIVATE);

        if (view == null){
            view= inflater.inflate(R.layout.lawyer_consult_list, container, false);
            ft = view.findViewById(R.id.searching);
            ll = view.findViewById(R.id.consult_list);

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }


    private void getData(){
        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/getMyQuickConsultList.action");
            params.addQueryStringParameter("keyword", sp.getString("_id","0"));
//                params.addQueryStringParameter("pageType", String.valueOf(position));
            params.setMaxRetryCount(0);
            System.out.println(params);
            x.http().get(params, new Callback.CommonCallback<String>(){

                @Override
                public void onSuccess(String s) {
                    JSONArray data =JSONArray.fromObject(s);
                    counselingModels = new QuickResponseRepositoryImpl().convertList(data);
//                            System.out.println(counselingModels.get(0).getAuthor_name());
//                            result[position] = data.get("counseling").toString();
                    ResponseView();
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

    public void ResponseView() {
        if (counselingModels.size() == 0) setNothing();
        else setList();
    }

    private void setNothing(){
        ll.addView(new FindNothingView(getContext()).init());
    }

    private void setList(){
        ll.removeAllViews();
        for(int i = 0 ; i < counselingModels.size(); i++){
            ll.addView(new QuickConsultSingleView(getContext())
                    .init(counselingModels.get(i).getContent(),counselingModels.get(i).getView_count(),
                            counselingModels.get(i).getLawyer_reply().size(),
                            counselingModels.get(i).getDate())
                    .setOnRootClickListener(this, i));
        }
    }

    public void initFail(){
        ft.setText(R.string.main_search_result_error);
    }

    @Override
    public void onRootClick(View view) {
        QuickConsultModel c = counselingModels.get((int)view.getTag());
        Intent intent=new Intent();
        intent.setClass(view.getContext(), QuickConsultResultActivity.class); //设置跳转的Activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Bundle bunble = new Bundle();
//            bunble.putSerializable("law", l);
        intent.putExtra("id", c.getId().toString());
        startActivity(intent);
    }



























































}

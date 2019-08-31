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

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.SearchLawFirmDetailActivity;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawFirmModel;
import com.example.joan.myapplication.database.repository.LawFirmRepositoryImpl;
import com.example.joan.myapplication.oneLineView.FirmOneLineView;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainSearchResultFirm extends Fragment implements FirmOneLineView.OnRootClickListener {

    private static int flag = 1;
    int position = 3;
    private TextView ft;
    private LinearLayout ll;
    public List<LawFirmModel> firmModels= new ArrayList<>();
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
            view = inflater.inflate(R.layout.activity_main_search_result_firm, container, false);
            ft = view.findViewById(R.id.main_search_result_firm_text);
            ll = view.findViewById(R.id.main_search_result_firm_list);
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
                        firmModels = new LawFirmRepositoryImpl().convert(jArray);
//                            result[position] = data.get("firm").toString();
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
        if (firmModels.size() == 0) setNothing();
        else setList();
    }

    private void setNothing(){
        ft.setText(R.string.main_search_result_nothing);
    }

    private void setList(){
        ll.removeAllViews();
        int index = 0;
        for (LawFirmModel firm: firmModels) {
            ll.addView(new FirmOneLineView(getContext())
                    .init(firm.getName(), firm.getAddress() ,"hahaha")
                    .setOnRootClickListener(this, index));
        }
    }

    public void initFail(){
        ft.setText(R.string.main_search_result_error);
    }

    @Override
    public void onRootClick(View view) {
        Intent intent=new Intent();
        intent.setClass(view.getContext(), SearchLawFirmDetailActivity.class); //设置跳转的Activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bunble = new Bundle();
        LawFirmModel l = firmModels.get((int)view.getTag());
        bunble.putSerializable("firm", l);
        intent.putExtras(bunble);
        startActivity(intent);
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

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
import com.example.joan.myapplication.SearchLawDetailActivity;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.LawModel;
import com.example.joan.myapplication.database.repository.LawRepositoryImpl;
import com.example.joan.myapplication.oneLineView.LawOneLineView;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainSearchResultLaw extends Fragment implements LawOneLineView.OnRootClickListener  {

    private static int flag = 1;
    int position = 2;
    private TextView ft;
    private LinearLayout ll;
    public static List<LawModel> lawModels= new ArrayList<>();
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
            view = inflater.inflate(R.layout.activity_main_search_result_law, container, false);
            ft = view.findViewById(R.id.main_search_result_law_text);
            ll = view.findViewById(R.id.main_search_result_law_list);
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
                        lawModels = new LawRepositoryImpl().convert(jArray);
                        System.out.println(jArray);
                        initView();
//                            result[position] = data.get("law").toString();
//                            initView();
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
        if (lawModels.size() == 0) setNothing();
        else setList();
    }

    private void setNothing(){
        ft.setText(R.string.main_search_result_nothing);
    }

    private void setList(){
        ll.removeAllViews();
        for(int i = 0 ; i < lawModels.size(); i++){
            ll.addView(new LawOneLineView(getContext())
                    .init(lawModels.get(i).getName(),lawModels.get(i).getContent(),"#民事")
                    .setOnRootClickListener(this, i));
        }
    }

    public void initFail(){
        ft.setText(R.string.main_search_result_error);
    }

    @Override
    public void onRootClick(View view) {
        LawModel l = lawModels.get((int)view.getTag());
        Intent intent=new Intent();
        intent.setClass(view.getContext(), SearchLawDetailActivity.class); //设置跳转的Activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bunble = new Bundle();
        bunble.putSerializable("law", l);
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

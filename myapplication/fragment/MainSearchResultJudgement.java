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
import com.example.joan.myapplication.SearchCaseDetailActivity;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.JudgementModel;
import com.example.joan.myapplication.database.repository.JudgementRepositoryImpl;
import com.example.joan.myapplication.oneLineView.CaseOneLineView;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainSearchResultJudgement extends Fragment implements CaseOneLineView.OnRootClickListener {

    private static int flag = 1;
    int position = 4;
    private TextView ft;
    private LinearLayout ll;
    public static List<JudgementModel> judgementModels= new ArrayList<>();
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
            view = inflater.inflate(R.layout.activity_main_search_result_judgement, container, false);
            ft = view.findViewById(R.id.main_search_result_judgement_text);
            ll = view.findViewById(R.id.main_search_result_judgement_list);
            setFlag(0);
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
//                            JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
                        JSONArray jArray= JSONArray.fromObject(s);
                        System.out.println(jArray);
//                            result[position] = data.get("judgement").toString();
                        judgementModels = new JudgementRepositoryImpl().convert(jArray);
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
        if (judgementModels.size() == 0) setNothing();
        else setList();
    }

    private void setNothing(){
        ft.setText(R.string.main_search_result_nothing);
    }

    private void setList(){
        ll.removeAllViews();
        for(int i = 0 ; i < judgementModels.size(); i++) {
            String[] a = judgementModels.get(i).getjId().split(" ", 2);
            String mainData = judgementModels.get(i).getjContent().replace("\\r", "")
                    .replace("\\n", "")
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace("\t", "")
                    .replace(" ", "");
//                System.out.println(mainData);
            String content = getContent(mainData);
            ll.addView(new CaseOneLineView(getContext())
                    .init(a[0],a[1].split(" \\[")[0],judgementModels.get(i).getjReason(),
                            "#民事",content)
                    .setOnRootClickListener(this, i));
        }
    }

    private String getContent(String mainData) {

        String content = "";

        content = mainData.substring(mainData.indexOf("主文") + 2, mainData.indexOf("理由"));
        content.replaceAll("中華民國","\n\r        中華民國");

        return content;

    }

    public void initFail(){
        ft.setText(R.string.main_search_result_error);
    }

    @Override
    public void onRootClick(View view) {
        Intent intent=new Intent();
        intent.setClass(view.getContext(), SearchCaseDetailActivity.class); //设置跳转的Activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", judgementModels.get((int)view.getTag()).getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

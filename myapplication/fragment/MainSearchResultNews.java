package com.example.joan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.SearchResultMoreNewsActivity;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.oneLineView.HomeNewsLayout;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MainSearchResultNews extends Fragment {

    private static int flag = 1;
    int position = 5;
    private TextView ft;
    private LinearLayout ll;
    private JSONArray newsModels;
    private JSONArray commentModels;
    private String keyWord;
    private View view;

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
            view = inflater.inflate(R.layout.activity_main_search_result_news, container, false);
            ft = view.findViewById(R.id.main_search_result_news_text);
            ll = view.findViewById(R.id.main_search_result_news_list);
            flag = 0;
//                if  (getData(position) == 1) initView();
//                else failGetData();
            try {
                RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
                params.addQueryStringParameter("key", getKeyWord());
                params.addQueryStringParameter("pageType", String.valueOf(position));
                params.setMaxRetryCount(0);
                System.out.println(params);
                x.http().get(params, new Callback.CommonCallback<String>(){

                    @Override
                    public void onSuccess(String s) {
                        JSONArray jsonArray = JSONArray.fromObject(s);
//                            result[position] = data.get("news").toString();
                        newsModels = jsonArray.getJSONObject(0).getJSONArray("hotNews");
                        commentModels = jsonArray.getJSONObject(1).getJSONArray("comment");
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
        if (newsModels.size() + commentModels.size() == 0) setNothing();
        else setList();
    }

    private void setNothing(){
        ft.setText(R.string.main_search_result_nothing);
    }

    private void setList(){
        ll.removeAllViews();
        try{

            setTopText(R.string.hot_news);

            if (newsModels.size() < 2)
                setOneNewsView(newsModels.size());
            else {
                setOneNewsView(2);
                setBottomButton(0);
            }

            setTopText(R.string.famous_comment);

            if (commentModels.size() < 2)
                setOneCommentView(commentModels.size());
            else {
                setOneCommentView(2);
                setBottomButton(1);
            }

        }catch (Exception e){ }
    }

    private void setTopText(int id){
        TextView topText = new TextView(getContext());
        topText.setText(getResources().getText(id));
        topText.setPadding(32, 16, 32, 8);
        topText.setGravity(Gravity.LEFT);
        topText.setTextSize(20);
        topText.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        ll.addView(topText);
    }

    private void setBottomButton(final int type){

        TextView buttonText = new TextView(getContext());
        buttonText.setText("查看更多>");
        buttonText.setBackgroundColor(getResources().getColor(R.color.look_more));
        buttonText.setGravity(Gravity.CENTER);
        buttonText.setPadding(8,4,8,8);
        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchResultMoreNewsActivity.class);
                intent.putExtra("keyword", getKeyWord());
                intent.putExtra("type", type);
                if (type == 0)
                    intent.putExtra("data", newsModels.toString());
                else
                    intent.putExtra("data", commentModels.toString());
                startActivity(intent);
            }
        });
//            Button newsButton = new Button(getContext());
//            newsButton.setText("查看更多>");
//            newsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            newsButton.setPadding(8,8,8,8);
        ll.addView(buttonText);
    }

    private void setOneNewsView(int num){
        for (int i = 0; i < num; i++) {
            String img;
            if (newsModels.getJSONObject(i).getJSONArray("src").size() > 0) {
                img = newsModels.getJSONObject(i).getJSONArray("src").getString(0);
            } else {
                img = "https://i.imgur.com/qM6pytU.jpg";
            }

            ll.addView(new HomeNewsLayout(getContext())
//                          .init());
                    .initNews(newsModels.getJSONObject(i).getString("title").replace("／", " | "), newsModels.getJSONObject(i).getString("article").replaceAll("\n", ""), img));
        }
    }

    private void setOneCommentView(int num){
        for (int i = 0; i < num; i++) {
            String img;
            if (commentModels.getJSONObject(i).getJSONArray("src").size() > 0) {
                img = commentModels.getJSONObject(i).getJSONArray("src").getString(0);
            } else {
                img = "https://i.imgur.com/qM6pytU.jpg";
            }

            ll.addView(new HomeNewsLayout(getContext())
//                          .init());
                    .initNews(commentModels.getJSONObject(i).getString("title").replace("／", " | "), commentModels.getJSONObject(i).getString("article").replaceAll("\n", ""), img));
        }
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

package com.example.joan.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private ImageView back;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private static List<View> views = new ArrayList<>();
    private static String keyWord;
    public static List<List> result= new ArrayList<>();
    //    public static MainSearchModel result;
//    public static int[] result = new int[5];
//    public static String[] result = new String[6];
//    public static List<LawyerModel> lawyerModels= new ArrayList<>();
//    public static List<QuickConsultModel> counselingModels= new ArrayList<>();
//    public static List<LawModel> lawModels= new ArrayList<>();
//    public static List<LawFirmModel> firmModels= new ArrayList<>();
//    public static List<JudgementModel> judgementModels= new ArrayList<>();
//    public static List<NewsModel> newsModels= new ArrayList<>();
//    private static String[] tabName = {"lawyer", "counsel", "law", "firm", "judgement", "news"};
//    private static int[] layoutId = {};
//
//    @Override
//    protected void onStart(){
//        super.onStart();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initBase();
        initViews();
//        getData();
    }

    private void initBase() {
//        views.clear();
//        for (int i = 0; i < 6; i ++)
//            views.add(new ImageView(this));

//        result.add(lawyerModels);
//        result.add(counselingModels);
//        result.add(lawModels);
//        result.add(firmModels);
//        result.add(lawModels);
//        result.add(newsModels);

        x.Ext.init(getApplication());
        Intent intent = getIntent();
        keyWord = intent.getStringExtra("keyWord");

        back = findViewById(R.id.main_search_result_back);
        back.setOnClickListener(this);
        title = findViewById(R.id.main_search_result_title);
        title.setText(keyWord + " 的搜尋結果");

    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = findViewById(R.id.main_search_result_viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        myFragmentPagerAdapter.setKeyword(keyWord);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //将TabLayout与ViewPager绑定在一起
        mTabLayout = findViewById(R.id.main_search_result_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setSelectedTabIndicatorColor(Color.rgb(0x5F, 0x9B, 0x95));
        mTabLayout.setSelectedTabIndicatorHeight(12);
        mTabLayout.setTabTextColors(Color.rgb(0x55, 0x55, 0x55),
                Color.rgb(0x5F, 0x9B, 0x95));
//        mTabLayout.setBackgroundColor(0xFFFFFF);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_search_result_back:
                while (!views.isEmpty()) {
                    views.remove(0);
                }
//                myFragmentPagerAdapter.clear();
                clean();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed(){

        clean();
        finish();

    }

    private void clean(){myFragmentPagerAdapter.clear();}

    /**原lawyer fragment*/
//    public static class MainSearchResultLawyer extends Fragment {
//
//        private static int flag = 1;
//        int position = 0;
//        private TextView ft;
//        private LinearLayout ll;
//        private LayoutInflater li;
//
//        public static int getFlag() {
//            return flag;
//        }
//
//        public static void setFlag(int flag) {
//            MainSearchResultLawyer.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//
//
//            if (flag == 1){
//                views.remove(position);
//                views.add(position, inflater.inflate(R.layout.activity_main_search_result_lawyer, container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_lawyer_text);
//                ll = views.get(position).findViewById(R.id.main_search_result_lawyer_list);
//                li = LayoutInflater.from(getContext());
//                flag = 0;
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
//                            JSONArray jArray= JSONArray.fromObject(s);
//                            lawyerModels = new LawyerRepositoryImpl().convertList(jArray);
////                            result[position] = data.get("lawyer").toString();
//                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//
//            }
//            return views.get(position);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
////            initView();
//        }
//
//        public void initView() {
//            if (lawyerModels.size() == 0) setNothing();
//            else setList();
//        }
//
//        private void setNothing(){
//            ft.setText(R.string.main_search_result_nothing);
//        }
//
//        private void setList(){
//            ll.removeAllViews();
//            for (LawyerModel lawyer: lawyerModels){
//                View view = li.inflate(R.layout.sample_lawyer_consult_single_lawyer, null);
////                System.out.println("我想要输出" + lawyer.getComment());
//                setData(view, lawyer);
//                setListener(view, lawyer);
//                ll.addView(view);
//            }
//        }
//
//        private void setData(View view, LawyerModel lawyer){
//            TextView name, identity, branch, special,response, fee, rate, image, searching;
//            name = view.findViewById(R.id.lawyer_consult_single_name);
//            identity = view.findViewById((R.id.lawyer_consult_single_identity));
//            branch = view.findViewById((R.id.lawyer_consult_single_branch));
//            special = view.findViewById((R.id.lawyer_consult_single_special));
//            response = view.findViewById((R.id.lawyer_consult_single_responce));
//            fee = view.findViewById((R.id.lawyer_consult_single_fee));
//            rate = view.findViewById((R.id.lawyer_consult_single_rate));
////        image = view.findViewById((R.id.lawyer_consult_single_image));
//            name.setText(lawyer.getName());
//            identity.setText(lawyer.getJob());
//            branch.setText(lawyer.getCompany());
//            special.setText("擅長：" + lawyer.getMajor());
//            response.setText("平均響應時間：1小時2分鐘");
//            fee.setText("NT " + lawyer.getPrice());
//            rate.setText(lawyer.getComment()+"");
//        }
//
//        private void setListener(View view, final LawyerModel lawyer) {//跳转至律师详情页面
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), LawyerConsultDetailActivity.class);
//                    intent.putExtra("lawyer", lawyer.getId());
//                    startActivity(intent);
//                    LawyerConsultActivity.page.overridePendingTransition(R.anim.right, R.anim.left);
//                }
//            });
//        }
//
//        public void initFail(){
//            ft.setText(R.string.main_search_result_error);
//        }
//
//    }
    /**原counsel fragment*/
//    public static class MainSearchResultCounsel extends Fragment implements FirmOneLineView.OnRootClickListener {
//
//        private static int flag = 1;
//        int position = 1;
//        private TextView ft;
//        private LinearLayout ll;
//
//        public static int getFlag() {
//            return flag;
//        }
//
//        public static void setFlag(int flag) {
//            MainSearchResultCounsel.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            if (flag == 1){
//                views.remove(position);
//                views.add(position, inflater.inflate(R.layout.activity_main_search_result_counsel, container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_counsel_text);
//                ll = views.get(position).findViewById(R.id.main_search_result_counsel_list);
//                flag = 0;
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
//                            JSONArray data =JSONArray.fromObject(s);
//                            counselingModels = new QuickResponseRepositoryImpl().convertList(data);
////                            System.out.println(counselingModels.get(0).getAuthor_name());
////                            result[position] = data.get("counseling").toString();
//                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//
//            }
//            return views.get(position);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
////            initView();
//        }
//
//        public void initView() {
//            if (counselingModels.size() == 0) setNothing();
//            else setList();
//        }
//
//        private void setNothing(){
//            ft.setText(R.string.main_search_result_nothing);
//        }
//
//        private void setList(){
//            ll.removeAllViews();
//            for(int i = 0 ; i < counselingModels.size(); i++){
//                ll.addView(new QuickConsultSingleView(getContext())
//                        .init(counselingModels.get(i).getContent(),
//                                counselingModels.get(i).getView_count(),
//                                counselingModels.get(i).getLawyer_reply().size(),
//                                counselingModels.get(i).getDate())
//                        .setOnRootClickListener(this, i));
//            }
//        }
//
//        public void initFail(){
//            ft.setText(R.string.main_search_result_error);
//        }
//
//        @Override
//        public void onRootClick(View view) {
//            QuickConsultModel c = counselingModels.get((int)view.getTag());
//            Intent intent=new Intent();
//            intent.setClass(view.getContext(), QuickConsultResultActivity.class); //设置跳转的Activity
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            Bundle bunble = new Bundle();
////            bunble.putSerializable("law", l);
//            intent.putExtra("id", c.getId().toString());
//            startActivity(intent);
//        }
//    }
    /**原law fragment*/
//    public static class MainSearchResultLaw extends Fragment implements LawOneLineView.OnRootClickListener {
//
//        private static int flag = 1;
//        int position = 2;
//        private TextView ft;
//        private LinearLayout ll;
//
//        public static int getFlag() {
//            return flag;
//        }
//
//        public static void setFlag(int flag) {
//            MainSearchResultLaw.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            if (flag == 1){
//                views.remove(position);
//                views.add(position, inflater.inflate(R.layout.activity_main_search_result_law, container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_law_text);
//                ll = views.get(position).findViewById(R.id.main_search_result_law_list);
//                flag = 0;
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
//                            JSONArray jArray= JSONArray.fromObject(s);
//                            lawModels = new LawRepositoryImpl().convert(jArray);
//                            System.out.println(jArray);
//                            initView();
////                            result[position] = data.get("law").toString();
////                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//
//            }
//            return views.get(position);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
////            initView();
//        }
//
//        public void initView() {
//            if (lawModels.size() == 0) setNothing();
//            else setList();
//        }
//
//        private void setNothing(){
//            ft.setText(R.string.main_search_result_nothing);
//        }
//
//        private void setList(){
//            ll.removeAllViews();
//            for(int i = 0 ; i < lawModels.size(); i++){
//                ll.addView(new LawOneLineView(getContext())
//                        .init(lawModels.get(i).getName(),lawModels.get(i).getContent(),"#民事")
//                        .setOnRootClickListener(this, i));
//            }
//        }
//
//        public void initFail(){
//            ft.setText(R.string.main_search_result_error);
//        }
//
//        @Override
//        public void onRootClick(View view) {
//            LawModel l = lawModels.get((int)view.getTag());
//            Intent intent=new Intent();
//            intent.setClass(view.getContext(), SearchLawDetailActivity.class); //设置跳转的Activity
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Bundle bunble = new Bundle();
//            bunble.putSerializable("law", l);
//            intent.putExtras(bunble);
//            startActivity(intent);
//        }
//    }
    /**原firm fragment*/
//    public static class MainSearchResultFirm extends Fragment implements FirmOneLineView.OnRootClickListener {
//
//        private static int flag = 1;
//        int position = 3;
//        private TextView ft;
//        private LinearLayout ll;
//
//        public static int getFlag() {
//            return flag;
//        }
//
//        public static void setFlag(int flag) {
//            MainSearchResultFirm.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//
//            if (flag == 1){
//                views.remove(position);
//                views.add(position, inflater.inflate(R.layout.activity_main_search_result_firm, container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_firm_text);
//                ll = views.get(position).findViewById(R.id.main_search_result_firm_list);
//                flag = 0;
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
//                            JSONArray jArray= JSONArray.fromObject(s);
//                            firmModels = new LawFirmRepositoryImpl().convert(jArray);
////                            result[position] = data.get("firm").toString();
//                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//            }
//            return views.get(position);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
////            initView();
//        }
//
//        public void initView() {
//            if (firmModels.size() == 0) setNothing();
//            else setList();
//        }
//
//        private void setNothing(){
//            ft.setText(R.string.main_search_result_nothing);
//        }
//
//        private void setList(){
//            ll.removeAllViews();
//            int index = 0;
//            for (LawFirmModel firm: firmModels) {
//                ll.addView(new FirmOneLineView(getContext())
//                        .init(firm.getName(), firm.getAddress() ,"hahaha")
//                        .setOnRootClickListener(this, index));
//            }
//        }
//
//        public void initFail(){
//            ft.setText(R.string.main_search_result_error);
//        }
//
//        @Override
//        public void onRootClick(View view) {
//            Intent intent=new Intent();
//            intent.setClass(view.getContext(), SearchLawFirmDetailActivity.class); //设置跳转的Activity
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Bundle bunble = new Bundle();
//            LawFirmModel l = firmModels.get((int)view.getTag());
//            bunble.putSerializable("firm", l);
//            intent.putExtras(bunble);
//            startActivity(intent);
//        }
//    }
    /**原judgement fragment*/
//    public static class MainSearchResultJudgement extends Fragment implements CaseOneLineView.OnRootClickListener {
//
//        private static int flag = 1;
//        int position = 4;
//        private TextView ft;
//        private LinearLayout ll;
//
//        public static int getFlag() {
//            return flag;
//        }
//
//        public static void setFlag(int flag) {
//            MainSearchResultJudgement.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            if (flag == 1){
//                views.remove(position);
//                views.add(position, inflater.inflate(R.layout.activity_main_search_result_judgement, container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_judgement_text);
//                ll = views.get(position).findViewById(R.id.main_search_result_judgement_list);
//                setFlag(0);
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
////                            JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
//                            JSONArray jArray= JSONArray.fromObject(s);
//                            System.out.println(jArray);
////                            result[position] = data.get("judgement").toString();
//                            judgementModels = new JudgementRepositoryImpl().convert(jArray);
//                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//            }
//            return views.get(4);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
////            initView();
//        }
//
//        public void initView() {
//            if (judgementModels.size() == 0) setNothing();
//            else setList();
//        }
//
//        private void setNothing(){
//            ft.setText(R.string.main_search_result_nothing);
//        }
//
//        private void setList(){
//            ll.removeAllViews();
//            for(int i = 0 ; i < judgementModels.size(); i++) {
//                String[] a = judgementModels.get(i).getjId().split(" ", 2);
//                String mainData = judgementModels.get(i).getjContent().replace("\\r", "")
//                        .replace("\\n", "")
//                        .replace("\n", "")
//                        .replace("\r", "")
//                        .replace("\t", "")
//                        .replace(" ", "");
////                System.out.println(mainData);
//                String content = getContent(mainData);
//                ll.addView(new CaseOneLineView(getContext())
//                        .init(a[0],a[1].split(" \\[")[0],judgementModels.get(i).getjReason(),
//                                "#民事",content)
//                        .setOnRootClickListener(this, i));
//            }
//        }
//
//        private String getContent(String mainData) {
//
//            String content = "";
//
//            content = mainData.substring(mainData.indexOf("主文") + 2, mainData.indexOf("理由"));
//            content.replaceAll("中華民國","\n\r        中華民國");
//
//            return content;
//
//        }
//
//        public void initFail(){
//            ft.setText(R.string.main_search_result_error);
//        }
//
//        @Override
//        public void onRootClick(View view) {
//            Intent intent=new Intent();
//            intent.setClass(view.getContext(), SearchCaseDetailActivity.class); //设置跳转的Activity
//            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("id", judgementModels.get((int)view.getTag()).getId());
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//    }
    /**原news fragment*/
//    public static class MainSearchResultNews extends Fragment {
//
//        private static int flag = 1;
//        int position = 5;
//        private TextView ft;
//        private LinearLayout ll;
//        private JSONArray newsModels;
//        private JSONArray commentModels;
//
//        public static int getFlag() {
//            return flag;
//        }
//
//        public static void setFlag(int flag) {
//            MainSearchResultNews.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            if (flag == 1){
//                views.remove(position);
//                views.add(position, inflater.inflate(R.layout.activity_main_search_result_news, container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_news_text);
//                ll = views.get(position).findViewById(R.id.main_search_result_news_list);
//                flag = 0;
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
//                            JSONArray jsonArray = JSONArray.fromObject(s);
////                            result[position] = data.get("news").toString();
//                            newsModels = jsonArray.getJSONObject(0).getJSONArray("hotNews");
//                            commentModels = jsonArray.getJSONObject(1).getJSONArray("comment");
//                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//
//            }
//            return views.get(position);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
////            initView();
//        }
//
//        public void initView() {
//            if (newsModels.size() + commentModels.size() == 0) setNothing();
//            else setList();
//        }
//
//        private void setNothing(){
//            ft.setText(R.string.main_search_result_nothing);
//        }
//
//        private void setList(){
//            ll.removeAllViews();
//            try{
//
//                setTopText(R.string.hot_news);
//
//                if (newsModels.size() < 2)
//                    setOneNewsView(newsModels.size());
//                else {
//                    setOneNewsView(2);
//                    setBottomButton(0);
//                }
//
//                setTopText(R.string.famous_comment);
//
//                if (commentModels.size() < 2)
//                    setOneCommentView(commentModels.size());
//                else {
//                    setOneCommentView(2);
//                    setBottomButton(1);
//                }
//
//            }catch (Exception e){ }
//        }
//
//        private void setTopText(int id){
//            TextView topText = new TextView(getContext());
//            topText.setText(getResources().getText(id));
//            topText.setPadding(32, 16, 32, 8);
//            topText.setGravity(Gravity.LEFT);
//            topText.setTextSize(20);
//            topText.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            ll.addView(topText);
//        }
//
//        private void setBottomButton(final int type){
//
//            TextView buttonText = new TextView(getContext());
//            buttonText.setText("查看更多>");
//            buttonText.setBackgroundColor(getResources().getColor(R.color.look_more));
//            buttonText.setGravity(Gravity.CENTER);
//            buttonText.setPadding(8,4,8,8);
//            buttonText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), SearchResultMoreNewsActivity.class);
//                    intent.putExtra("keyword", keyWord);
//                    intent.putExtra("type", type);
//                    if (type == 0)
//                        intent.putExtra("data", newsModels.toString());
//                    else
//                        intent.putExtra("data", commentModels.toString());
//                    startActivity(intent);
//                }
//            });
////            Button newsButton = new Button(getContext());
////            newsButton.setText("查看更多>");
////            newsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
////            newsButton.setPadding(8,8,8,8);
//            ll.addView(buttonText);
//        }
//
//        private void setOneNewsView(int num){
//            for (int i = 0; i < num; i++) {
//                String img;
//                if (newsModels.getJSONObject(i).getJSONArray("src").size() > 0) {
//                    img = newsModels.getJSONObject(i).getJSONArray("src").getString(0);
//                } else {
//                    img = "https://i.imgur.com/qM6pytU.jpg";
//                }
//
//                ll.addView(new HomeNewsLayout(getContext())
////                          .init());
//                        .initNews(newsModels.getJSONObject(i).getString("title").replace("／", " | "), newsModels.getJSONObject(i).getString("article").replaceAll("\n", ""), img));
//            }
//        }
//
//        private void setOneCommentView(int num){
//            for (int i = 0; i < num; i++) {
//                String img;
//                if (commentModels.getJSONObject(i).getJSONArray("src").size() > 0) {
//                    img = commentModels.getJSONObject(i).getJSONArray("src").getString(0);
//                } else {
//                    img = "https://i.imgur.com/qM6pytU.jpg";
//                }
//
//                ll.addView(new HomeNewsLayout(getContext())
////                          .init());
//                        .initNews(commentModels.getJSONObject(i).getString("title").replace("／", " | "), commentModels.getJSONObject(i).getString("article").replaceAll("\n", ""), img));
//            }
//        }
//
//        public void initFail(){
//            ft.setText(R.string.main_search_result_error);
//        }
//
//    }


//    public static int getData(final int position) {
//
//        final int[] state = {0};
//
//        try {
//            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//            params.addQueryStringParameter("key", keyWord);
//            params.addQueryStringParameter("pageType", String.valueOf(position));
//            System.out.println(params);
//            x.http().get(params, new Callback.CommonCallback<String>(){
//
//                @Override
//                public void onSuccess(String s) {
//                    JsonObject jsonObject =(JsonObject) new JsonParser().parse(s);
////                    System.out.println(jsonObject);
//                    switch(position){
//                        case 0:
//                            result[position] = jsonObject.get("lawyer").toString();
//                            System.out.println(result[position]);
//                            break;
//                        case 1:
//                            result[position] = jsonObject.get("counseling").toString();
//                            System.out.println(result[position]);
//                            break;
//                        case 2:
//                            result[position] = jsonObject.get("law").toString();
//                            System.out.println(result[position]);
//                            break;
//                        case 3:
//                            result[position] = jsonObject.get("firm").toString();
//                            System.out.println(result[position]);
//                            break;
//                        case 4:
//                            result[position] = jsonObject.get("judgement").toString();
//                            System.out.println(result[position]);
//                            break;
//                        case 5:
//                            result[position] = jsonObject.get("news").toString();
//                            System.out.println(result[position]);
//                            break;
//                    }
//                    state[0] = 1;
//                }
//
//                @Override
//                public void onError(Throwable throwable, boolean b) {
//                    state[0] = -1;
//                }
//
//                @Override
//                public void onCancelled(CancelledException e) {
//                }
//
//                @Override
//                public void onFinished() {
//                }
//            });
//        }catch (Exception e){
//            state[0] = -1;
//        }
//
//        while(state[0] == 0){ }
//        return state[0];
//    }

//    public static void failGetData(){
//
//    }


//    public static class BaseMainResultFragment extends Fragment{
//
//        private static int flag = 1;
//        int position;
//        private TextView ft;
//
//        public static void setFlag(int flag) {
//            MainSearchResultLawyer.flag = flag;
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            if (flag == 1){
//                views.add(inflater.inflate(layoutId[position], container, false));
//                ft = views.get(position).findViewById(R.id.main_search_result_lawyer_text);
//                flag = 0;
////                if  (getData(position) == 1) initView();
////                else failGetData();
//                try {
//                    RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchEverything.action");
//                    params.addQueryStringParameter("key", keyWord);
//                    params.addQueryStringParameter("pageType", String.valueOf(position));
//                    System.out.println(params);
//                    x.http().get(params, new Callback.CommonCallback<String>(){
//
//                        @Override
//                        public void onSuccess(String s) {
//                            JsonObject data =(JsonObject) new JsonParser().parse(s);
//                            System.out.println(data);
//                            result[position] = data.get("lawyer").toString();
//                            initView();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable, boolean b) {
//                            initFail();
//                        }
//                        @Override
//                        public void onCancelled(CancelledException e) {
//                            initFail();
//                        }
//                        @Override
//                        public void onFinished() { }
//                    });
//                }catch (Exception e){
//                }
//            return views.get(position);
//        }
//
//

//    public void initView() {
//        LinearLayout ll = views.get(position).findViewById(R.id.main_search_result_counsel_list);
//        if (result[position].replace("\"", "").equals(String.valueOf(position))) {
//            ImageView im = new ImageView(getContext());
//            im.setImageResource(R.drawable.findnothing);
//            ft.setText(R.string.main_search_result_nothing);
//            ll.addView(im);
//        }
//        else ft.setText(String.valueOf(result[position]));
//    }

//    }
}

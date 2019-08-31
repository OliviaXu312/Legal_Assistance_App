package com.example.joan.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.CaseConsultModel;
import com.example.joan.myapplication.database.model.JudgementModel;
import com.example.joan.myapplication.database.model.LawModel;
import com.example.joan.myapplication.fragment.CaseConsultAdapter;
import com.example.joan.myapplication.fragment.CaseResultReferListFragment;
import com.example.joan.myapplication.fragment.CaseResultSimilarListFragment;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CaseConsultResultActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private Button favor, toContent;
    private ImageView back;
    private TextView num;
    private ViewPager pager;
    private List<Fragment> list;
    private CaseResultSimilarListFragment similarList;
    private CaseResultReferListFragment referList;
    private CaseConsultModel result;
    private String resultID;
    private int AIResult;
    private int state, stateflag = 0;
    private int flag;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private Thread thread;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_consult_result);

        x.Ext.init(getApplication());
//        getData();
//        while (state == 0){}
        initItems();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
        }else{
        }

    }

    protected void initItems(){
//        mongoDb = new MongoDBUtil("wxby");
//        collection = mongoDb.getCollection("case_consult");

        back = findViewById(R.id.consult_case_result_back);
        favor = findViewById(R.id.consult_case_result_favor);
        tabLayout = findViewById(R.id.consult_case_result_tablayout);
        pager = findViewById(R.id.consult_case_result_pager);
        num = findViewById(R.id.consult_case_result_number);
        toContent = findViewById(R.id.consult_case_result_favor);
//        bar = findViewById(R.id.consult_case_result_percent);
//        percent = findViewById(R.id.consult_case_result_number);

        back.setOnClickListener(this);
        favor.setOnClickListener(this);

        similarList = new CaseResultSimilarListFragment();
        referList = new CaseResultReferListFragment();

        list = new ArrayList<>();
        list.add(similarList);
        list.add(referList);
        pager.setAdapter(new CaseConsultAdapter(getSupportFragmentManager(),list));

        resultID = getIntent().getStringExtra("id");
        flag = getIntent().getIntExtra("flag",0);

        alert = new AlertDialog.Builder(CaseConsultResultActivity.this);
        if(flag == 0){
            alert.setMessage("案件分析中，請稍後");
            alert.setPositiveButton("先逛逛", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stateflag = 1;
                    timerTask.cancel();
                    timer.purge();
                    timer.cancel();
                    dialog.cancel();
                    finish();
                }
            });
            dialog = alert.create();
            dialog.show();
            Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
        }else{
            alert.setMessage("加載中，請稍後");
            dialog = alert.create();
            dialog.show();
            Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
        }

        thread = new Thread(){
          public void run(){
              try {
                  final Timer timer = new Timer();
                  timerTask = new TimerTask(){//也可以用匿名類別的方式，

                      @Override
                      public void run() {
                          // TODO Auto-generated method stub
                          getData();
                      }
                  };
                  timer.schedule(timerTask, 0, 6000);//一秒後開始，之後每過六秒再執行
              }catch (Exception e){

              }
          }
        };
        thread.start();

//        setSimilarData();
//        setReferData();

        result = new CaseConsultModel();

        initTabLayout();
        initTabLayoutData();

    }

    private void initTabLayoutData() {
    }

//    private void initBar() {
//
//        bar.setIndeterminate(false);
//        bar.setProgress(result.percentage);
//        percent.setText(String.valueOf(result.percentage/100));
//
//    }

    protected void initTabLayout(){

        tabLayout.setupWithViewPager(pager);

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(R.string.consult_case_result_similar));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.consult_case_result_refer));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    protected void getData(){

        final CaseConsultModel[] data = new CaseConsultModel[1];

        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/caseConsultResult.action");
//            params.setConnectTimeout(600000);
//            System.out.println(params.getConnectTimeout());
//            params.setCacheMaxAge(600000);
            params.setMaxRetryCount(0);
            params.addQueryStringParameter("case_id", resultID);
//            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    if(stateflag == 0){
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = (JsonObject) jsonParser.parse(s);

                        state = jsonObject.get("state").getAsInt();
                        System.out.println(state);
                        if(state == 2){//算完le
                            dialog.cancel();
                            timerTask.cancel();
                            timer.purge();
                            timer.cancel();

                            AIResult = jsonObject.get("result").getAsInt();
                            if(AIResult == 0){
                                result.setResult("勝訴");
                            }else if(AIResult == 1){
                                result.setResult("平手");
                            }else if(AIResult == 2){
                                result.setResult("敗訴");
                            }

                            num.setText(result.getResult());
                            result.setContent(jsonObject.get("content").getAsString());
                            result.setImglst(JSONArray.fromObject(jsonObject.get("picture_lst").toString()));

//                    if (jsonObject.get("state").getAsInt() == 1) {

//                        data[0] = new CaseConsultModel();
//                        //                    System.out.println(s);
//                        //                    System.out.println("SetState");
//                        data[0].setState(jsonObject.get("state").getAsInt());
//                        //                    System.out.println("SetResult");
//                        data[0].setResult(jsonObject.get("result").getAsString());
//                        //                    System.out.println("SetID");
//                        data[0].setCase_id(jsonObject.get("case_id").getAsString());
//                        //                    System.out.println("SetContent");
//                        data[0].setContent(jsonObject.get("content").getAsString());

                        List<JudgementModel> similar = new ArrayList<>();
                        for (JsonElement je : jsonObject.getAsJsonArray("similar")) {
                            JsonObject tempJson = je.getAsJsonObject();
                            JudgementModel temp = new JudgementModel();
                            System.out.println(tempJson);
                            //                        System.out.println();
                            temp.setjId(tempJson.get("j_id").getAsString().split("\\[")[0]);
                            temp.setjReason(tempJson.get("j_reason").getAsString());
                            temp.setjContent(tempJson.get("j_content").getAsString());
                            temp.setjDate(tempJson.get("j_date").getAsString());
                            temp.setId(tempJson.get("_id").getAsString());
                            similar.add(temp);
                        }
                        result.setJudgementModels(similar);

                        List<LawModel> refer = new ArrayList<>();
                        for (JsonElement je : jsonObject.getAsJsonArray("refer")) {
                            JsonObject tempJson = je.getAsJsonObject();
                            LawModel temp = new LawModel();
                            System.out.println(tempJson);
                            temp.setId(tempJson.get("_id").getAsString());
                            temp.setStart(tempJson.get("start").getAsString());
                            temp.setAbandon(tempJson.get("abandon").getAsString());
                            temp.setArticle(tempJson.get("article").getAsString());
                            temp.setContent(tempJson.get("content").getAsString());
                            temp.setName(tempJson.get("name").getAsString());
                            if (tempJson.has("end")) {
                                System.out.println("yes");
                                temp.setEnd(tempJson.get("end").getAsString());
                            }
                            refer.add(temp);
                        }
                        result.setLawModels(refer);
                        setSimilarData();
                        //                    System.out.println("setRefer");
                        setReferData();
                        System.out.println("vquququququu");

//                    }
//                    else if (jsonObject.get("state").getAsInt() == -2){
//
//                        alert.setMessage("您的咨詢還在等待執行，請稍等5至10分鐘~");
//                        alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialog.cancel();
//                                finish();
//                            }
//                        });
//                        dialog = alert.create();
//                        dialog.show();
//                        Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
//
//                    }else if (jsonObject.get("state").getAsInt() == -1){
//
//                        alert.setMessage("糟糕！您的資訊可能出了一些問題，可能需要重新提交試試！");
//                        alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialog.cancel();
//                                finish();
//                            }
//                        });
//                        dialog = alert.create();
//                        dialog.show();
//                        Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
//
//                    }else if (jsonObject.get("state").getAsInt() == 0){
//
//                        alert.setMessage("您的咨詢正在進行處理，請稍等3至5分鐘~");
//                        alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialog.cancel();
//                                finish();
//                            }
//                        });
//                        dialog = alert.create();
//                        dialog.show();
//                        Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
//
//                    }else{
//
//                    }
                        }

                    }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    System.out.println(throwable.getMessage());
                    alert.setMessage("網絡可能開了會小差……再試試看吧！");
                    alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    dialog = alert.create();
                    dialog.show();
                    Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    confirm.setTextColor(getResources().getColor(R.color.selector_item_color));

                }

                @Override
                public void onCancelled(CancelledException e) {

                }

                @Override
                public void onFinished() {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
//        result = ;
//        while (state == 0){}
//        System.out.println(data[0].getJudgementModels().get(0).getjId());
        return;

//        MongoDBUtil mongoDb = new MongoDBUtil("wxby");
//        MongoCollection<Document> collection = mongoDb.getCollection("case_consult");
//
//        MongoCursor<Document> cursor = collection.find(new Document("case_id", "2018092123350067876787678")).iterator();
//        CaseConsultModel caseConsult = new CaseConsultModel();
//        Document current_cursor = cursor.next();
//
//        caseConsult.setId(current_cursor.getObjectId("_id"));
//        caseConsult.setContent(current_cursor.getString("content"));
//        caseConsult.setResult(current_cursor.getString("result"));
//        caseConsult.setSimilar(current_cursor.get("similar", new ArrayList<Document>()));
//        caseConsult.setRefer(current_cursor.get("refer", new ArrayList<Document>()));
//        caseConsult.setState(current_cursor.getInteger("state"));
//
//        result = caseConsult;
//
//        System.out.println(resultID);

//        CaseResultData crd = new CaseResultData();
//        result.percentage = 8000;
//        result.similars[0] = crd.new Similar("testTitle1", "testSubTitle1", "testLawyer1");
//        result.similars[1] = crd.new Similar("testTitle2", "testSubTitle2", "testLawyer2");
//        result.similars[2] = crd.new Similar("testTitle3", "testSubTitle3", "testLawyer3");
//        result.reasons[0] = crd.new Reason("testName1");
//        result.reasons[1] = crd.new Reason("testName2");
//        result.reasons[2] = crd.new Reason("testName3");
//        result.refers[0] = crd.new Refer("testRefer1", "testSubTitle1");
//        result.refers[1] = crd.new Refer("testRefer2", "testSubTitle2");
//        result.refers[2] = crd.new Refer("testRefer3", "testSubTitle3");
//        result.compensates = crd.new Compensate(new int[]{10, 10, 10}, new int[]{10, 10, 10}, new int[]{10, 10, 10});

    }

    protected void setSimilarData(){
//        System.out.println("111111111111111111111111111111111111");
        similarList = (CaseResultSimilarListFragment)pager.getAdapter().instantiateItem(pager, 0);
//        System.out.println("1111111111111111111111111");
        System.out.println(result.getJudgementModels().get(0).getjId());
        similarList.initData(result.getJudgementModels());
    }

//    protected void setReasonData(){
//        reasonList = (CaseResultReasonListFragment)pager.getAdapter().instantiateItem(pager, 1);
//        reasonList.initData(result.reasons);
//    }

    protected void setReferData(){
        referList = (CaseResultReferListFragment)pager.getAdapter().instantiateItem(pager, 1);
        referList.initData(result.getLawModels());
    }

//    protected void setCompensateData(){
//        compensateList = (CaseResultCompensateListFragment)pager.getAdapter().instantiateItem(pager, 3);
//        compensateList.initData(result.compensates);
//    }

//    protected void setData(){
//
//        setSimilarData();
////        setReasonData();
//        setReferData();
////        setCompensateData();
//
////        System.out.println();System.out.println();System.out.println();System.out.println();System.out.println();System.out.println();
////        if (similarList == null) System.out.println("You fault again!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////        else System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////        System.out.println();System.out.println();System.out.println();System.out.println();System.out.println();
////        similarList.initData(result.similars);
////        LinearLayout ll = similarList.getView().findViewById(R.id.case_consult_result_linear);
////
////        LayoutInflater li = LayoutInflater.from(similarList.getContext());
////        View view = li.inflate(R.layout.case_result_single_similar, null);
//
////        ll.addView(findViewById(R.id.case_result_similar_single_title));
////        ll.addView(findViewById(R.id.case_result_similar_single_title));
////        ll.addView(findViewById(R.id.case_result_similar_single_title));
////
////        tabLayout.setupWithViewPager(pager);
//
////        if (similarList.getView() == null) System.out.println("WTF!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
////        else System.out.println("000000000000000000000000000000000000");
////         LinearLayout ll = similarList.getView().findViewById(R.id.case_consult_result_linear);
////         ll.addView(new case_result_similar_single(this));
////        FragmentManager fm = getSupportFragmentManager();
////        FragmentTransaction ft = fm.beginTransaction();
////        ft.add(R.id.case_consult_result_linear, new CaseResultSimilarFragment());
////        ft.commit();
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.consult_case_result_back:
//                Intent intent = new Intent(this, ConsultingActivity.class);
//                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                break;
            case R.id.consult_case_result_favor:
                Intent intent=new Intent();
                intent.setClass(this, CaseConsultContentActivity.class); //设置跳转的Activity
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("content", result.getContent());
                Bundle bundle = new Bundle();
                bundle.putSerializable("imglst", (Serializable) result.getImglst());
                System.out.println(result.getImglst());
                intent.putExtras(bundle);
                startActivity(intent);
        }

    }
}

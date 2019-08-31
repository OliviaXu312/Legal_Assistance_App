package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class JudgementConsultActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView main, name, reason, date, mainTitle;
    private Button back, bottom;
    private String id, content, fact;
    private int state, fOrC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_consult);

        initItems();
        getData();

    }

    private void getData() {

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        System.out.println(id);

        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/judgementConsult.action");
            params.addQueryStringParameter("_id", id);
            params.setMaxRetryCount(0);
            System.out.println(params);
//            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
//                    System.out.println(s);
                    String mainData;
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    state = jsonObject.get("state").getAsInt();
                    switch (state){
                        case 1:
                            JsonObject data = jsonObject.getAsJsonObject("data");
        //                    System.out.println(data.get("j_id").getAsString());
                            name.setText(data.get("j_id").getAsString());
                            date.setText(data.get("j_date").getAsString());
                            reason.setText(data.get("j_reason").getAsString());
                            mainData = data.get("j_content").getAsString().replace("\\r", "")
                                    .replace("\\n", "")
                                    .replace("\n", "")
                                    .replace("\r", "")
                                    .replace("\t", "")
                                    .replace(" ", "");
                            System.out.println(mainData);
                            content = getContent(mainData);
                            fact = getFact(mainData);
                            main.setText(content);
                            break;
                        case 0:

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getFact(String mainData) {

        String fact = "";

        fact = mainData.substring(mainData.indexOf("理由") + 2);

        return fact;

    }
    private String getContent(String mainData) {

        String content = "";

        content = mainData.substring(mainData.indexOf("主文") + 2, mainData.indexOf("理由"));
        content.replaceAll("中華民國","\n\r        中華民國");

        return content;

    }

    private void initItems() {

        main = findViewById(R.id.judgement_consult_main);
        name = findViewById(R.id.judgement_consult_name);
        reason = findViewById(R.id.judgement_consult_reason);
        date = findViewById(R.id.judgement_consult_date);
        back = findViewById(R.id.judgement_consult_back);
        bottom = findViewById(R.id.judgement_consult_bottom);
        mainTitle = findViewById(R.id.judgement_consult_maintitle);
        
        back.setOnClickListener(this);
        bottom.setOnClickListener(this);

        fOrC = 0;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.judgement_consult_back:
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                break;
            case R.id.judgement_consult_bottom:
                if (fOrC == 0){
                    fOrC = 1;
                    main.setText(fact);
                    bottom.setText(R.string.judgement_consult_main);
                    mainTitle.setText(R.string.judgement_consult_fact);
                }else {
                    fOrC = 0;
                    main.setText(content);
                    bottom.setText(R.string.judgement_consult_fact);
                    mainTitle.setText(R.string.judgement_consult_main);
                }

        }
        
    }
}

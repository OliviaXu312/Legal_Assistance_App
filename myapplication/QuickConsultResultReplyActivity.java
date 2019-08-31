package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class QuickConsultResultReplyActivity extends AppCompatActivity implements View.OnClickListener {

    private String parentId, parentName, text, authorName, authorId, id;
    private int index;
    private Button submit;
    private ImageView back;
    private EditText content;
    private TextView title;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_consult_result_reply);

        initItems();

    }

    private void initItems() {

        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        parentId = intent.getStringExtra("parent_id");
        parentName = intent.getStringExtra("name");
        id = intent.getStringExtra("quick_id");
        authorName = sp.getString("user_name", "notValid");
        authorId = sp.getString("_id", "notValid");

        submit = findViewById(R.id.quick_consult_result_reply_submit);
        back = findViewById(R.id.quick_consult_result_reply_back);
        content = findViewById(R.id.quick_consult_result_reply_text);
        title = findViewById(R.id.quick_consult_result_reply_title);

        alert = new AlertDialog.Builder(QuickConsultResultReplyActivity.this);

        if (index == -1) title.setText("回復" + parentName);
        else title.setText("回復" + parentName + "的評論");

        submit.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.quick_consult_result_reply_submit:
                submit();
                break;

            case R.id.quick_consult_result_reply_back:

                finish();

                break;

        }

    }

    private void submit(){

        text = content.getText().toString();
        x.Ext.init(getApplication());
//        Intent intent = getIntent();
//        id = intent.getStringExtra("id");

        try {
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/quickConsultReply.action");
            params.addQueryStringParameter("author_name", authorName);
            params.addQueryStringParameter("author_id", authorId);
            params.addQueryStringParameter("parent_id", parentId);
            params.addQueryStringParameter("content", text);
            params.addQueryStringParameter("quick_id", id);
            params.addQueryStringParameter("index", String.valueOf(index));
            params.setMaxRetryCount(0);
            if(sp.getString("role", "0").equals("1")) params.addQueryStringParameter("is_l_or_a", "true");
            else params.addQueryStringParameter("is_l_or_a", "false");
            System.out.println(params.toString());
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);

                    if (jsonObject.get("state").getAsInt() == 1){

                        alert.setMessage("回復成功！");
                        alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                                Intent intent = new Intent();
                                intent.setClass(QuickConsultResultReplyActivity.this, QuickConsultResultActivity.class);
//                        intent.putExtra("id", );
                                intent.putExtra("id", id);
                                startActivity(intent);
                                //跳至詳情頁面
                                finish();
                            }
                        });
                        dialog = alert.create();
                        dialog.show();
                        Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));

                    }
                    else {
                        alert.setMessage("回復失敗！");
                        alert.setPositiveButton("重試", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                submit();
                            }
                        });
                        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                            }
                        });
                        dialog = alert.create();
                        dialog.show();
                        Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
                    }

                }

                @Override
                public void onError(Throwable throwable, boolean b) {

                    alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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

    }

}

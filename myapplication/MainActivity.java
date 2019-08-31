package com.example.joan.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.example.joan.myapplication.database.model.BaseModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class MainActivity extends BaseActivity {
//    @ViewInject(R.id.et_username)
//    private EditText etUsername;
//    @ViewInject(R.id.et_password)
//    private EditText etPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Event(value = R.id.btn_login,type = View.OnClickListener.class)
    private void onLoginBtnClick(View view){
        /**
         * 这里填写你的服务器所在的IP地址替换我的ip地址
         * */
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/searchFirm.action");
//            params.addQueryStringParameter("condition",etUsername.getText().toString());
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                    int code = jsonObject.get("code").getAsInt();
                    if (code == 0){
                        //执行登录成功操作
                        Toast.makeText(MainActivity.this,jsonObject.get("msg").getAsString(),Toast.LENGTH_SHORT).show();
                    }else {
                        //执行登录失败操作
                        Toast.makeText(MainActivity.this,jsonObject.get("msg").getAsString(),Toast.LENGTH_SHORT).show();
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
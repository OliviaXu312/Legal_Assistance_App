package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Button login, cancel, forget, register, mode;
    ImageView back;
    private EditText account, password;
    private CheckBox agree;
    private String ea, ep;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private int loginType = 2;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    boolean isLogined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initItem();

    }

    protected void initItem(){

        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);
        editor = sp.edit();

        x.Ext.init(getApplication());

        alert = new AlertDialog.Builder(LoginActivity.this);

//        editor.putBoolean("login", false);
//        if (sp.getBoolean("login", false)){successDialog("已登入！");}
//        else {
//        if (hasLoginInfo()) autoLogin();

            login = findViewById(R.id.login_login);
            account = findViewById(R.id.login_account);
            password = findViewById(R.id.login_password);
            cancel = findViewById(R.id.login_cancel);
            forget = findViewById(R.id.login_forget);
            back = findViewById(R.id.login_back);
            register = findViewById(R.id.login_register);
            agree = findViewById(R.id.login_agree);
            mode = findViewById(R.id.login_mode);

            account.setHint(R.string.login_phone);

            setListener();

            agree.setChecked(true);
        }

//    }

    protected void setListener(){

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ea = account.getText().toString();
                ep = password.getText().toString();
                attemptLogin();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                回到上一頁
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
//                回到上一頁
            }
        });

        mode.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (mode.getText().equals("以賬號登入")){
                    mode.setText(R.string.login_with_phone);
                    account.setHint(R.string.login_account);
                    account.setInputType(InputType.TYPE_CLASS_TEXT);
                    loginType = 3;
                }else if (mode.getText().equals("以電話登入")){
                    mode.setText(R.string.login_with_account);
                    account.setHint(R.string.login_phone);
                    account.setInputType(InputType.TYPE_CLASS_PHONE);
                    loginType = 2;
                }
            }

        });

        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (agree.isChecked()) {login.setEnabled(true); login.setBackgroundColor(Color.parseColor("#0b988f"));}
                if (!agree.isChecked()) {login.setEnabled(false); login.setBackgroundColor(Color.parseColor("#8a8a8a"));}
            }
        });
    }

    protected  boolean hasLoginInfo(){
        return !sp.getString("user_name", "invalid").equals("invalid");
    }

    protected int attemptLogin(){

        final int[] type = new int[1];
        type[0] = loginType;

        if (ea.equals(null) || ep.equals(null)){
            setDialog("請輸入賬號或密碼！");
        }else {

            try {
                RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/loginAndRegister.action");
                params.addQueryStringParameter("type", String.valueOf(type[0]));
                params.addQueryStringParameter("username", ea);
                params.addQueryStringParameter("password", ep);
                params.setMaxRetryCount(0);
                System.out.println(params.toString());
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = (JsonObject) jsonParser.parse(s);
                        type[0] = jsonObject.get("resultCode").getAsInt();

                        if (type[0] == 1) {
                            afterLogin(jsonObject.get("name").getAsString(),jsonObject.get("role").getAsString(),jsonObject.get("_id").getAsString());
                            successDialog("登入成功！");
                        } else {
                            setDialog(jsonObject.get("resultMessage").getAsString());
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
                setDialog("好像遇到了一些問題誒，再試試看？");
            }
        }

        return type[0];
    }

    private void setDialog(String message) {
        alert.setMessage(message);
        alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
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

    public boolean autoLogin(){

        if (hasLoginInfo()){
            ea = sp.getString("user_name", "invalid");
            ep = sp.getString("user_pswd", "");
            loginType = sp.getInt("login_type", 1);
        if (attemptLogin() == 1){
            return true;
        }else{
            return false;
        }}else return false;
    }

    protected void afterLogin(String name, String role, String _id){

        editor.putString("user_name", ea);
        editor.putString("user_pswd", ep);
        editor.putString("name", name);
        editor.putString("role", role);
        editor.putString("_id", _id);
        editor.putInt("login_type", loginType);
        editor.putBoolean("login", true);
        editor.apply();
        isLogined = true;
//        finish();

    }

    private void successDialog(String message){
        alert.setMessage(message);
        alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
            }
        });
        dialog = alert.create();
        dialog.show();
        Button confirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        confirm.setTextColor(getResources().getColor(R.color.selector_item_color));
    }

}
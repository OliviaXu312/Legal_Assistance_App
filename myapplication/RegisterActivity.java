package com.example.joan.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.BaseModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private Button register, cancel, mode;
    ImageView back;
    private TextView account, password, eight, letter;
    private int registerType;
    private String ea, ep;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private CheckBox agree;
    private boolean isAgree;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initItems();

    }

    private void initItems() {

        back = findViewById(R.id.register_back);
        register = findViewById(R.id.register_register);
        cancel = findViewById(R.id.register_cancel);
        account = findViewById(R.id.register_account);
        password = findViewById(R.id.register_password);
        mode = findViewById(R.id.register_mode);
        eight = findViewById(R.id.register_contain_eight);
        letter = findViewById(R.id.register_contain_letter);
        agree = findViewById(R.id.register_agree);

        account.setHint(R.string.register_phone);
        account.setInputType(InputType.TYPE_CLASS_PHONE);
        register.setEnabled(false);
        registerType = -1;
        register.setBackgroundColor(Color.parseColor("#8a8a8a"));

        alert = new AlertDialog.Builder(RegisterActivity.this);

        sp = getSharedPreferences("account_info", Context.MODE_PRIVATE);
        editor = sp.edit();

        mode.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        agree.setOnCheckedChangeListener(this);

        password.addTextChangedListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.register_back:

                finish();
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                break;

            case R.id.register_register:

                registerNewAccount();
                break;

            case R.id.register_cancel:

                finish();
                break;

            case R.id.register_mode:

                mode.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        if (mode.getText().equals("以賬號註冊")){
                            mode.setText(R.string.register_with_phone);
                            account.setHint(R.string.register_account);
                            account.setInputType(InputType.TYPE_CLASS_TEXT);
                            registerType = -2;
                        }else if (mode.getText().equals("以手機註冊")){
                            mode.setText(R.string.register_with_account);
                            account.setHint(R.string.register_phone);
                            account.setInputType(InputType.TYPE_CLASS_PHONE);
                            registerType = -1;
                        }
                    }

                });

        }

    }

    private int registerNewAccount() {

        final int[] type = new int[1];

        if (password.getText().equals(null) || password.equals("")){
            setDialog("請輸入賬號或密碼！");
        }else {

            ea = account.getText().toString();
            ep = password.getText().toString();

            try {
                RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR + ":8080/loginAndRegister.action");
                params.addQueryStringParameter("type", String.valueOf(registerType));
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
                        System.out.println(type[0]);

                        if (type[0] == 1) {
                            editor.putString("user_name", ea);
                            editor.putString("user_pswd", ep);
                            editor.putInt("login_type", registerType);
                            editor.apply();
                            System.out.println("Message:  " + jsonObject.get("resultMessage").getAsString());
                            successDialog(jsonObject.get("resultMessage").getAsString());
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

    private void successDialog(String message) {
        alert.setMessage(message);
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

    private boolean isEnoughLength(){

        return password.getText().toString().length() >= 8 &&
                password.getText().toString().matches(".*[a-zA-Z].*") ?  true : false;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (password.getText().toString().length() >= 8){
            eight.setTextColor(Color.parseColor("#5f9b95"));
        }else{
            eight.setTextColor(Color.parseColor("8a8585"));
        }

        if (password.getText().toString().matches(".*[a-zA-Z].*")){
            letter.setTextColor(Color.parseColor("#5f9b95"));
        }else{
            letter.setTextColor(Color.parseColor("8a8585"));
        }

        if (isEnoughLength() && isAgree) {
            register.setEnabled(true); register.setBackgroundColor(Color.parseColor("#0b988f"));
        }else{
            register.setEnabled(false); register.setBackgroundColor(Color.parseColor("#8a8a8a"));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (agree.isChecked()) isAgree = true;
        else isAgree = false;
        if (isEnoughLength() && isAgree) {
            register.setEnabled(true); register.setBackgroundColor(Color.parseColor("#0b988f"));
        }else{
            register.setEnabled(false); register.setBackgroundColor(Color.parseColor("#8a8a8a"));
        }
    }
}
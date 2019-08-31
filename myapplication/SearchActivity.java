package com.example.joan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);


        //退出时使用
        getWindow().setExitTransition(slide);
        //第一次进入时使用
        getWindow().setEnterTransition(fade);
        //再次进入时使用
        getWindow().setReenterTransition(fade);

        setContentView(R.layout.activity_search);

        final EditText inputText = findViewById(R.id.et_search);
        inputText.setFocusable(true);
        inputText.setFocusableInTouchMode(true);
        inputText.requestFocus();
        SearchActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        findViewById(R.id.main_search_go).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (inputText.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "求求你输入点东西再搜索吧", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("keyWord", inputText.getText().toString());
                    startActivity(intent);
                }

            }
        });

    }

}

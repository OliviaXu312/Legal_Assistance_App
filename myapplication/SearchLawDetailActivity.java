package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.joan.myapplication.database.model.LawFirmModel;
import com.example.joan.myapplication.database.model.LawModel;

public class SearchLawDetailActivity extends AppCompatActivity {

    TextView name ;
    TextView start;
    TextView end;
    TextView article;
    TextView content;

    LawModel law;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_law_detail);
        law = (LawModel) getIntent().getSerializableExtra("law");

        initView();
        initData();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void initView(){
        name = findViewById(R.id.name);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        article = findViewById(R.id.article);
        content = findViewById(R.id.content);
    }

    private void initData(){
        name.setText(law.getName());
        start.setText(law.getStart());
        if(law.getAbandon().equals("Not abandon yet")){
            end.setText("暂未失效");
        }else{
            end.setText(law.getAbandon());
        }
        article.setText(law.getArticle());
        content.setText(law.getContent());
    }


}

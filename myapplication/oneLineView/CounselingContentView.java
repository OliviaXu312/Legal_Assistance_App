package com.example.joan.myapplication.oneLineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.database.model.CounselingModel;
import com.example.joan.myapplication.database.model.ResponseModel;

import java.util.List;

public class CounselingContentView extends LinearLayout{

    //提問次數
    private TextView times;

    //問答
    private LinearLayout content;

    //律师姓名
    private String lawyerName;


    public CounselingContentView(Context context) {
        super(context);
    }

    public CounselingContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 初始化各个控件
     */
    public CounselingContentView init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counseling_question_times_layout, this, true);
        times = findViewById(R.id.question_times);
        content = findViewById(R.id.question_content);
        return this;
    }

    /**
     * 律所名称+地址+标签
     *
     * @return
     */
    public CounselingContentView init(int times, CounselingModel question, String name) {
        init();
        lawyerName = name;
        setTimes(times);
        setContent(question);

        return this;
    }

    public CounselingContentView setTimes(int index){
        times.setText("第" + index + "次提問");
        return  this;
    }

    public  CounselingContentView setContent(CounselingModel a){
        try{
            content.addView(new CounselingQuestionView(getContext()).init(a.getQuestion(),a.getCreate_time(),a.getPictures().get(0)));
        }catch(Exception e){
            content.addView(new CounselingQuestionView(getContext()).init(a.getQuestion(),a.getCreate_time()));
        }
        List<ResponseModel> responses = a.getResponse();
        for (ResponseModel b: responses
             ) {
            content.addView(new CounselingAnswerView(getContext()).init(b.getContent(),b.getDate(),lawyerName));
        }

        return this;
    }

}

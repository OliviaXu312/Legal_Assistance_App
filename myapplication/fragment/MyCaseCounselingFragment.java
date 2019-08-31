package com.example.joan.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.CaseConsultActivity;
import com.example.joan.myapplication.CaseConsultResultActivity;
import com.example.joan.myapplication.MyAnswerLawyerConsultActivity;
import com.example.joan.myapplication.MyQuestionLawyerConsultActivity;
import com.example.joan.myapplication.QuestionLawyerFinishActivity;
import com.example.joan.myapplication.R;
import com.example.joan.myapplication.database.model.BaseModel;
import com.example.joan.myapplication.database.model.CaseConsultModel;
import com.example.joan.myapplication.database.model.LegalCounselingModel;
import com.example.joan.myapplication.database.repository.CaseConsultRepositoryImpl;
import com.example.joan.myapplication.database.repository.CounselingRepositoryImpl;
import com.example.joan.myapplication.oneLineView.FindNothingView;
import com.example.joan.myapplication.oneLineView.MyLawyerConsultLayout;

import net.sf.json.JSONArray;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MyCaseCounselingFragment extends Fragment implements MyLawyerConsultLayout.OnRootClickListener{

    private SharedPreferences sp;
    private TextView searching;

    private String position;
    View view_case_consult = null;

    List<CaseConsultModel> counselingList = new ArrayList<>();

    LinearLayout case_consult_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get title
        position = getArguments().getString("position");
        //熱門問答區fragment初始化

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("account_info", Context.MODE_PRIVATE);
        //TextView textView = view.findViewById(R.id.tv_title);
        //textView.setText(title);
        if(view_case_consult == null){
            view_case_consult = inflater.inflate(R.layout.lawyer_consult_list,container,false);
            case_consult_list = view_case_consult.findViewById(R.id.consult_list);
            searching = view_case_consult.findViewById(R.id.searching);

        }
        return view_case_consult;
    }

    @Override
    public void onStart() {
        super.onStart();
        initLawyerView();
    }

    private void initLawyerView(){
        try{
            RequestParams params = new RequestParams("http://" + BaseModel.IP_ADDR +":8080/userCaseConsultResult.action");
            params.addQueryStringParameter("condition",sp.getString("_id","0"));//當前使用者id
//            params.addQueryStringParameter("condition","222");//當前使用者id
            params.setMaxRetryCount(0);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    JSONArray jArray= JSONArray.fromObject(s);
                    counselingList = new CaseConsultRepositoryImpl().convert(jArray);
//                    System.out.println(counselingList.get(0).getContent());

                    CounselingView();
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    System.out.println(throwable.getMessage());
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

    public void CounselingView(){
        searching.setVisibility(View.GONE);
        if(counselingList.isEmpty()){
            case_consult_list.addView(new FindNothingView(getContext()).init());
        }else{
            int index = 0;
            for (CaseConsultModel counseling: counselingList
                    ) {

                String createTime = counseling.getCreateTime();
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateString = formatter.format(createTime);

                String content = counseling.getContent();
                int state = counseling.getState();

                case_consult_list.addView(new MyLawyerConsultLayout(getContext())
                        .init(state,content, createTime)
                        .setOnRootClickListener(this, index));
                index++;
            }
        }
    }

    @Override
    public void onRootClick(View v) {
        CaseConsultModel counseling = counselingList.get((int)v.getTag());
        Intent intent = new Intent(getContext(), CaseConsultResultActivity.class);
        intent.putExtra("id", counseling.getCase_id());
        intent.putExtra("flag", 1);
        startActivity(intent);
    }
}

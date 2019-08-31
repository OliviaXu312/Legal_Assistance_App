package com.example.joan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.JudgementConsultActivity;
import com.example.joan.myapplication.R;
import com.example.joan.myapplication.SearchCaseDetailActivity;
import com.example.joan.myapplication.database.model.JudgementModel;
import com.example.joan.myapplication.oneLineView.CaseOneLineView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaseResultSimilarListFragment extends Fragment implements CaseOneLineView.OnRootClickListener{

    private LinearLayout ll;
    private List<JudgementModel> similars;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_case_result_list_similar, null);
        ll = view.findViewById(R.id.case_consult_similar_linear);
//        initView();
        return view;
    }

//    public void initView(){
//        LayoutInflater li = LayoutInflater.from(getContext());
//        for (int i = 0; i < 3; i ++){
//            View view = li.inflate(R.layout.sample_case_result_single_similar, null);
//            view.setId(i);
//            view.setOnClickListener(this);
//            ll.addView(view);
//        }
////        System.out.println("InitSimilar!!!!!!!!!!!!!!!");
//    }

    public void initData(List<JudgementModel> similar){

        similars = similar;

//        View single = new case_result_similar_single(getContext());
        for(int i = 0; i < similars.size(); i ++){
            String[] a = similars.get(i).getjId().split(" ",2);
            System.out.println(similars.get(i).getjId());
            String mainData = similars.get(i).getjContent().replaceAll("\\r", "")
                    .replaceAll("\\n", "")
                    .replaceAll("\n", "")
                    .replaceAll("\r", "")
                    .replaceAll("\t", "")
                    .replaceAll("\\s", "");
            Pattern p = Pattern.compile("理由.*", Pattern.DOTALL);
            Matcher m = p.matcher(mainData);
//            System.out.println(mainData);
            System.out.println(m.find());
            String content = m.group().replaceAll("理由","");
//            String fact = getContent(mainData);
//            System.out.println(fact);
            ll.addView(new CaseOneLineView(getContext())
                    .init(a[0],a[1],similars.get(i).getjReason(),
                            "#民事",content)
                    .setOnRootClickListener(this, i));


//            View view = ll.getChildAt(i);
////            Button lawyer;
//            TextView title, subTitle, number, time;
////            View view = new CaseResultSimilarFragment().getView();
////            lawyer = view.findViewById(R.id.case_result_similar_single_lawyer);
//            title = view.findViewById(R.id.case_result_similar_single_title);
//            subTitle = view.findViewById(R.id.case_result_similar_single_subtitle);
//            number = view.findViewById(R.id.case_result_similar_single_number);
//            time = view.findViewById(R.id.case_result_similar_single_time);
////            result = view.findViewById(R.id.case_result_similar_single_number);
////            lawyer.setText(similar.get(i).getjId());
//            title.setText(similar.get(i).getjId());
//            subTitle.setText(similar.get(i).getjReason());
//            number.setText((i+1)+".");
//            time.setText(similar.get(i).getjDate());
//            result.setText(resultt);
//            ft.add(R.id.case_consult_result_linear, new CaseResultSimilarFragment());

//            ll.addView(single);
//            CaseResultSimilarFragment single = new CaseResultSimilarFragment();
//            single.init(similars[i], i + 1);
//            ll.addView(single.getView().findViewById(R.id.case_result_similar_single_title));

        }
//        ft.commit();
    }

//    @Override
//    public void onClick(View view) {
//
//        System.out.println(similars.get(view.getId()).getId());
//        String id = similars.get(view.getId()).getId();
//        Intent intent = new Intent(getContext(), JudgementConsultActivity.class);
//        intent.putExtra("id", id);
//        startActivity(intent);
//
//    }

    private String getContent(String mainData) {

        String content = "";

        content = mainData.substring(mainData.indexOf("主文") + 2, mainData.indexOf("理由"));
        content.replaceAll("中華民國","\n\r        中華民國");

        return content;

    }

    private String getFact(String mainData) {

        String fact = "";

        fact = mainData.substring(mainData.indexOf("理由") + 2);

        return fact;

    }

    @Override
    public void onRootClick(View v) {
        Intent intent=new Intent();
        intent.setClass(v.getContext(), SearchCaseDetailActivity.class); //设置跳转的Activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", similars.get((int)v.getTag()).getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

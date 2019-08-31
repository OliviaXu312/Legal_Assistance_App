package com.example.joan.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joan.myapplication.R;
import com.example.joan.myapplication.SearchLawDetailActivity;
import com.example.joan.myapplication.database.model.LawModel;
import com.example.joan.myapplication.oneLineView.LawOneLineView;

import java.util.List;

public class CaseResultReferListFragment extends Fragment implements LawOneLineView.OnRootClickListener {

    private LinearLayout ll;
    private List<LawModel> refers;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_case_result_list_refer, null);
        ll = view.findViewById(R.id.case_consult_refer_linear);
//        initView();
        return view;
    }

//    public void initView(){
//        LayoutInflater li = LayoutInflater.from(getContext());
//        for (int i = 0; i < 3; i ++){
//            View view = li.inflate(R.layout.sample_case_result_single_refer, null);
//            view.setId(i);
////            view.setOnClickListener(this);
//            ll.addView(view);
//        }
//        if (ll == null){
//            System.out.println("-------------------");
//            System.out.println("-------------------");
//            System.out.println("-------------------");
//            System.out.println("NULL  in initView!!!!!!!!!!!!!!!");
//            System.out.println("-------------------");
//            System.out.println("-------------------");
//            System.out.println("-------------------");
//        }else{
//                System.out.println("-------------------");
//                System.out.println("-------------------");
//                System.out.println("-------------------");
//                System.out.println("NOT   NULL!!!!!!!!!!!!!!!");
//                System.out.println("-------------------");
//                System.out.println("-------------------");
//                System.out.println("-------------------");
//
//        }
//    }

    public void initData(List<LawModel> refer){

        refers = refer;

        for(int i = 0; i < refer.size(); i ++){
            ll.addView(new LawOneLineView(getContext())
                    .init(refer.get(i).getName(),refer.get(i).getContent(),"#民法")
                    .setOnRootClickListener(this,i));

//            View view = ll.getChildAt(i);
//            TextView title, number, subtitle;
////            View view = new CaseResultSimilarFragment().getView();
//            title = view.findViewById(R.id.case_result_refer_single_title);
//            number = view.findViewById(R.id.case_result_refer_single_number);
//            subtitle = view.findViewById(R.id.case_result_refer_single_subtitle);
//            title.setText(refer.get(i).getName().replace("\"", "").replace("\r", "").replace("\n", "")
//                    + "  " + refer.get(i).getArticle().replace("\"", ""));
//            subtitle.setText(refer.get(i).getContent().replace("\\r", "").replace("\\n", ""));
//            number.setText((i+1)+".");
//            ft.add(R.id.case_consult_result_linear, new CaseResultSimilarFragment());

//            ll.addView(single);
//            CaseResultSimilarFragment single = new CaseResultSimilarFragment();
//            single.init(similars[i], i + 1);
//            ll.addView(single.getView().findViewById(R.id.case_result_similar_single_title));

        }
//        ft.commit();
    }

    @Override
    public void onRootClick(View v) {
        LawModel l = refers.get((int)v.getTag());
        Intent intent=new Intent();
        intent.setClass(v.getContext(), SearchLawDetailActivity.class); //设置跳转的Activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bunble = new Bundle();
        bunble.putSerializable("law", l);
        intent.putExtras(bunble);
        startActivity(intent);
    }

//    @Override
//    public void onClick(View view) {
//
////        Intent intent = new Intent(CaseConsultResultActivity.class, LawDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("law", refers.get(view.getId()));
////        intent.putExtras(bundle);
////        startActivity(intent);
//
//    }
}

package com.example.joan.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.joan.myapplication.fragment.MainSearchResultCounsel;
import com.example.joan.myapplication.fragment.MainSearchResultFirm;
import com.example.joan.myapplication.fragment.MainSearchResultJudgement;
import com.example.joan.myapplication.fragment.MainSearchResultLaw;
import com.example.joan.myapplication.fragment.MainSearchResultLawyer;
import com.example.joan.myapplication.fragment.MainSearchResultNews;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"律師", "咨詢", "法條","律所", "判決", "新聞與評論"};

    private Fragment c, l, f, lr, j, n;

    private String keyword;

    public List<Fragment> fl = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        newFragment();
    }

    public void newFragment(){
//        c = new SearchResultActivity.MainSearchResultCounsel();
//        l = new SearchResultActivity.MainSearchResultLaw();
//        f = new SearchResultActivity.MainSearchResultFirm();
//        lr = new SearchResultActivity.MainSearchResultLawyer();
//        j = new SearchResultActivity.MainSearchResultJudgement();
//        n = new SearchResultActivity.MainSearchResultNews();
        c = new MainSearchResultCounsel();
        l = new MainSearchResultLaw();
        f = new MainSearchResultFirm();
        lr = new MainSearchResultLawyer();
        j = new MainSearchResultJudgement();
        n = new MainSearchResultNews();
        fl.add(lr);
        fl.add(c);
        fl.add(l);
        fl.add(f);
        fl.add(j);
        fl.add(n);
    }

    private void initFragmentKeyword() {
        ((MainSearchResultLawyer) lr).setKeyWord(keyword);
        ((MainSearchResultCounsel) c).setKeyWord(keyword);
        ((MainSearchResultLaw) l).setKeyWord(keyword);
        ((MainSearchResultFirm) f).setKeyWord(keyword);
        ((MainSearchResultJudgement) j).setKeyWord(keyword);
        ((MainSearchResultNews) n).setKeyWord(keyword);
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0: return lr;
//            case 1: return c;
//            case 2: return l;
//            case 3: return f;
//            case 4: return j;
//            case 5: return n;
//            default: return lr;
//        }
        return fl.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public void initView(){
//        ((SearchResultActivity.MainSearchResultLawyer) lr).initView();
//        ((SearchResultActivity.MainSearchResultCounsel) c).initView();
//        ((SearchResultActivity.MainSearchResultLaw) l).initView();
//        ((SearchResultActivity.MainSearchResultFirm) f).initView();
//        ((SearchResultActivity.MainSearchResultJudgement) j).initView();
//        ((SearchResultActivity.MainSearchResultNews) n).initView();
        ((MainSearchResultLawyer) lr).initView();
        ((MainSearchResultCounsel) c).initView();
        ((MainSearchResultLaw) l).initView();
        ((MainSearchResultFirm) f).initView();
        ((MainSearchResultJudgement) j).initView();
        ((MainSearchResultNews) n).initView();
    }

    public void clear(){
        ((MainSearchResultLawyer) lr).setFlag(1);
        ((MainSearchResultCounsel) c).setFlag(1);
        ((MainSearchResultLaw) l).setFlag(1);
        ((MainSearchResultFirm) f).setFlag(1);
        ((MainSearchResultJudgement) j).setFlag(1);
        ((MainSearchResultNews) n).setFlag(1);
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
        initFragmentKeyword();
    }
}

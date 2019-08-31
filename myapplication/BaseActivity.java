package com.example.joan.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import org.xutils.x;

/**
 * Created by HUPENG on 2017/4/30.
 */
public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
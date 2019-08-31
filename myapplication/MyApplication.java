package com.example.joan.myapplication;

import android.app.Application;
import org.xutils.x;

/**
 * Created by HUPENG on 2017/4/30.
 */
/**
 * Created by yangyang on 2017/4/24.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//Xutils初始化
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
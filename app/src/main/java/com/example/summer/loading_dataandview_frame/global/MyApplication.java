package com.example.summer.loading_dataandview_frame.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;



public class MyApplication extends Application {
    public static Context context;//定义全局的Context
    public static Handler mainHandler;//定义全局的handler
    /**
     * Android应用的入口函数：
     * 应用启动的时候，会首先执行的方法
     * 少定义
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Context，handler
        //Android3种Context:Activity, Application, Service
        context = this;
        mainHandler = new Handler();

    }
}

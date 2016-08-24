package com.example.summer.loading_dataandview_frame.util;

import android.graphics.drawable.Drawable;

import com.example.summer.loading_dataandview_frame.global.MyApplication;


/**
 * 常用工具方法,如获取各种资源的方法(字符串，颜色，dimens资源)
 */
public class Utils {
    /**
     * 在主线程执行任务
     */
    public static void runOnUIThread(Runnable runnable){
        MyApplication.mainHandler.post(runnable);
    }

    public static String getString(int resId){
        return MyApplication.context.getResources().getString(resId);
    }

    public static String[] getStringArray(int resId){
        return MyApplication.context.getResources().getStringArray(resId);
    }

    public static int getColor(int resId){
        return MyApplication.context.getResources().getColor(resId);
    }

    public static Drawable getDrawable(int resId){
        return MyApplication.context.getResources().getDrawable(resId);
    }

    /**
     * 获取定义的dp资源,并且会自动将dp值转为像素
     * @param resId
     * @return
     */
    public static int getDimens(int resId){
        return MyApplication.context.getResources().getDimensionPixelSize(resId);
    }
}

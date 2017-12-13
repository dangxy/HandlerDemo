package com.dangxy.handlerdemo;

import android.app.Application;
import android.content.Context;

/**
 * @author dangxueyi
 * @description
 * @date 2017/12/13
 */

public class HandlerDemoApplication extends Application {

    private static Context mContext;

    public static Context getContext(){

        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }
}

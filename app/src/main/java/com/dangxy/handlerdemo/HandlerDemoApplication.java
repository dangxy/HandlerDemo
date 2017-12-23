package com.dangxy.handlerdemo;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author dangxueyi
 * @description
 * @date 2017/12/13
 */

public class HandlerDemoApplication extends Application {

    private static Context mContext;

    public static Context getContext() {

        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("dognew.realm")
                .schemaVersion(2)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}

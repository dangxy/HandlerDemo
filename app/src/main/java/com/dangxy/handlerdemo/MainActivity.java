package com.dangxy.handlerdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.dangxy.handlerdemo.utils.MLog;
import com.dangxy.handlerdemo.utils.NotificationUtils;

/**
 * @author dangxy99
 * @description 描述
 * @date 2017/12/11
 */
public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle;
            String value;
            switch (msg.what) {
                case 1:
                    bundle =  msg.getData();
                    value =  bundle.getString("hello");
                    break;
                case 2:
                     bundle =  msg.getData();
                    value =  bundle.getString("hello");
                    break;
                default:
                    break;

            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("hello", "word");
        message.setData(bundle);
        message.what=1;
        Message message2 = new Message();
        Bundle bundle2 = new Bundle();
        bundle2.putString("hello", "dang");
        message2.setData(bundle2);
        message2.what=2;
        handler.sendMessageDelayed(message,3000);
        handler.sendMessageDelayed(message2,10000);

        MLog.d("DANG", NotificationUtils.isNotificationEnabled(this)+"");
    }

    /**
     * app的设置
     * @param context
     */
    private void goToSet(Context context){
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);    }

    /**
     * 系统的设置
     */

    private void goToAppSetting(){
            Intent mIntent=new Intent(Settings.ACTION_SETTINGS);
            startActivity(mIntent);
        }
}

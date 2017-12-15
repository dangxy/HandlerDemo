package com.dangxy.handlerdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.dangxy.handlerdemo.utils.MLog;

/**
 * @description  描述
 * @author  dangxy99
 * @date   2017/12/15
 */
public class PhoneStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
   MLog.e("DANG","11111");
         if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent
                .getAction())) {
             MLog.e("DANG","22222");
            AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            handleRingMode(am.getRingerMode());
        }

        if(intent.getAction().equals("android.intent.action.MY_BROADCAST")){
            MLog.e("DANG","33333");
            MLog.e("DANG",intent.getStringExtra("msg"));
        }
        if(intent.getAction().equals (Intent.ACTION_BATTERY_CHANGED)){

            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 100);
           MLog.e("剩余电量为:" , (level * 100 / scale) + "%");
        }

    }
    /**
     * 处理情景模式
     *
     * @param ringerMode
     */
    private void handleRingMode(int ringerMode) {
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                MLog.e("DANG","标准模式（铃声和震动）");
                break;
            case AudioManager.RINGER_MODE_SILENT:
                MLog.e("DANG","静音模式");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                MLog.e("DANG","震动模式");
                break;
            default:
                break;
        }

    }
}

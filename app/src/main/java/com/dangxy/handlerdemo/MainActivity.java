package com.dangxy.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.dangxy.handlerdemo.utils.MLog;

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
                    MLog.d("DANG",value);
                    break;
                case 2:
                     bundle =  msg.getData();
                    value =  bundle.getString("hello");
                    MLog.e("DANG",value);
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


    }
}

package com.dangxy.handlerdemo.room;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dangxy.handlerdemo.R;
import com.dangxy.handlerdemo.utils.MLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomActivity extends AppCompatActivity {

    @BindView(R.id.add)
    Button add;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.query)
    Button query;
    @BindView(R.id.update)
    Button update;
    private UserDataBase userDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        userDataBase = Room.databaseBuilder(this, UserDataBase.class, "user-data").build();
    }

    @OnClick({R.id.add, R.id.delete, R.id.query, R.id.update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i < 15; i++) {
                            User user = new User();
                            user.setuId(i);
                            user.setUserName("dangxy" + i);
                            user.setPassWord("dfdkfdkh" + i);
                            userDataBase.userDao().addUser(user);
                        }
                    }
                }).start();

                break;
            case R.id.delete:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User();
                        user.setuId(1);
                        user.setUserName("dangxy" + 1);
                        user.setPassWord("dfdkfdkh" + 1);
                        userDataBase.userDao().deleteUser(user);
                    }
                }).start();

                break;
            case R.id.query:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<User> userList = userDataBase.userDao().findAll();
                        for (User user1 : userList) {
                            MLog.e("DANG", user1.getPassWord());
                        }
                    }
                }).start();

                break;
            case R.id.update:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User();
                        user.setuId(5);
                        user.setUserName("dangxy" + 999);
                        user.setPassWord("dfdkfdkh" + 999);
                        userDataBase.userDao().updateUser(user);
                    }
                }).start();

                break;
            default:
                break;
        }
    }
}

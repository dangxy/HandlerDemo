package com.dangxy.handlerdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dangxy.handlerdemo.api.GankService;
import com.dangxy.handlerdemo.api.GithubService;
import com.dangxy.handlerdemo.api.RetrofitGank;
import com.dangxy.handlerdemo.api.RetrofitGithub;
import com.dangxy.handlerdemo.api.RxGankService;
import com.dangxy.handlerdemo.api.RxGithubService;
import com.dangxy.handlerdemo.entity.CommonEntity;
import com.dangxy.handlerdemo.entity.RepoEntity;
import com.dangxy.handlerdemo.utils.MLog;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView textview;
    private ImageView imageView;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView =(ImageView)findViewById(R.id.iv_image_view);
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
        mContext= this;
        //retrofitGithub();

        //rxRetrofitGithub();

      // retrofitGank();

        //rxRetrofitGank();

        RxGankService rxGankService = new RetrofitGank().newInstance(this).create(RxGankService.class);

        rxGankService.getWelfareListData("15","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<CommonEntity>() {
                    @Override
                    public void onNext(CommonEntity android) {
                        Glide.with(mContext).load(android.getResults().get(2).getUrl()).into(imageView);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void rxRetrofitGank() {
        RxGankService rxGankService = new RetrofitGank().newInstance(this).create(RxGankService.class);

        rxGankService.getListData("15","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<CommonEntity>() {
                    @Override
                    public void onNext(CommonEntity android) {
                        textview.setText(android.getResults().get(2).getDesc());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * gank获取数据
     */

    private void retrofitGank() {
        GankService gankService = new RetrofitGank().newInstance(this).create(GankService.class);
        gankService.getListData("15","1").enqueue(new Callback<CommonEntity>() {
            @Override
            public void onResponse(Call<CommonEntity> call, Response<CommonEntity> response) {
                CommonEntity android = response.body();
                MLog.e("DANG",android.getResults().get(1).getDesc())
                ;
            }

            @Override
            public void onFailure(Call<CommonEntity> call, Throwable t) {

            }
        });
    }

    /**
     * rx github获取数据
     */
    private void rxRetrofitGithub() {
        RxGithubService rxGithubService = new RetrofitGithub().newInstance(this).create(RxGithubService.class);

        rxGithubService.listRepos("dangxy")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<List<RepoEntity>>() {
                    @Override
                    public void onNext(List<RepoEntity> repoEntities) {

                        textview.setText(repoEntities.get(0).getForks_url());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * github获取数据
     */

    private void retrofitGithub() {
        GithubService githubService =  new RetrofitGithub().newInstance(this).create(GithubService.class);
        githubService.listRepos("dangxy").enqueue(new Callback<List<RepoEntity>>() {
            @Override
            public void onResponse(Call<List<RepoEntity>> call, Response<List<RepoEntity>> response) {
                List<RepoEntity> repoEntityList = response.body();
                MLog.d("DANG",repoEntityList.get(0).getFull_name());
            }

            @Override
            public void onFailure(Call<List<RepoEntity>> call, Throwable t) {
                MLog.e("DANG",t.getMessage());
            }
        });
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

    /**
     * 跳转应用市场
     * @param context
     * @param packageName
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package com.dangxy.handlerdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dangxy.handlerdemo.api.GankService;
import com.dangxy.handlerdemo.api.GithubService;
import com.dangxy.handlerdemo.api.ReadhubService;
import com.dangxy.handlerdemo.api.RebaseService;
import com.dangxy.handlerdemo.api.RetrofitGank;
import com.dangxy.handlerdemo.api.RetrofitGithub;
import com.dangxy.handlerdemo.api.RetrofitReadhub;
import com.dangxy.handlerdemo.api.RetrofitRebase;
import com.dangxy.handlerdemo.api.RxGankService;
import com.dangxy.handlerdemo.api.RxGithubService;
import com.dangxy.handlerdemo.entity.CommonEntity;
import com.dangxy.handlerdemo.entity.NewListEntity;
import com.dangxy.handlerdemo.entity.RebaseUserEntity;
import com.dangxy.handlerdemo.entity.RepoEntity;
import com.dangxy.handlerdemo.entity.TopicRsp;
import com.dangxy.handlerdemo.receiver.NetworkBroadcastReceiver;
import com.dangxy.handlerdemo.receiver.PhoneStatusReceiver;
import com.dangxy.handlerdemo.utils.MLog;
import com.dangxy.handlerdemo.utils.ShakeUtils;

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
    private Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView =(ImageView)findViewById(R.id.iv_image_view);
        save =(Button)findViewById(R.id.save);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("hello", "word");
        message.setData(bundle);
        message.what=1;
        final Message message2 = new Message();
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

        //rxGankRetrofit();
        //goToAppSetting();
        //goToSet(this);

        ShakeUtils shakeUtils = new ShakeUtils(mContext);
        shakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                MLog.e("DANG","振动");
            }
        });
        //phoneState();

        //netWorkState();

        //registerReceiver(new PhoneStatusReceiver(),new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        final ReadhubService readhubService = new RetrofitReadhub().newInstance(this).create(ReadhubService.class);

        //retrofitRebas();
        //getReadhubNewsList(readhubService);
        //getReadhubTechList(readhubService);
        //getReadhubTopisList(readhubService);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Bitmap bitmap = ViewUtils.createBitmapFromView(imageView);
               // ViewUtils.saveBitmap(mContext,bitmap);

//              Intent intent = new Intent(mContext,ReadhubActivity.class);
//              startActivity(intent);
                Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                intent.putExtra("msg", "这是一条测试广播");
                sendBroadcast(intent);

            }
        });

    }

    private void phoneState() {
        PhoneStatusReceiver receiver = new PhoneStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    private void netWorkState() {
        NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadcastReceiver, filter1);
    }

    private void retrofitRebas() {
        RebaseService rebaseService =  new RetrofitRebase().newInstance(this).create(RebaseService.class);
        rebaseService.register("dangxy99","111111","dangxy99","dangxy99@163.com","android developer").enqueue(new Callback<RebaseUserEntity>() {
            @Override
            public void onResponse(Call<RebaseUserEntity> call, Response<RebaseUserEntity> response) {

               RebaseUserEntity rebaseUserEntity =  response.body();
               MLog.e("DANG",response.body().toString());
            }

            @Override
            public void onFailure(Call<RebaseUserEntity> call, Throwable t) {

            }
        });
    }

    private void getReadhubNewsList(ReadhubService readhubService) {
        readhubService.listNews("",15).enqueue(new Callback<NewListEntity>() {
            @Override
            public void onResponse(Call<NewListEntity> call, Response<NewListEntity> response) {

                NewListEntity newListEntity = response.body();

            }

            @Override
            public void onFailure(Call<NewListEntity> call, Throwable t) {

            }
        });
    }

    private void getReadhubTechList(ReadhubService readhubService) {
        readhubService.listTechNews("",15).enqueue(new Callback<NewListEntity>() {
            @Override
            public void onResponse(Call<NewListEntity> call, Response<NewListEntity> response) {

                NewListEntity newListEntity = response.body();

            }

            @Override
            public void onFailure(Call<NewListEntity> call, Throwable t) {

            }
        });
    }

    private void getReadhubTopisList(ReadhubService readhubService) {
        readhubService.listTopicNews("",15).enqueue(new Callback<TopicRsp>() {
            @Override
            public void onResponse(Call<TopicRsp> call, Response<TopicRsp> response) {

                TopicRsp topicRsp = response.body();

            }

            @Override
            public void onFailure(Call<TopicRsp> call, Throwable t) {

            }
        });
    }


    /**
     * rx GANK 获取数据
     */

    private void rxGankRetrofit() {
        RxGankService rxGankService = new RetrofitGank().newInstance(this).create(RxGankService.class);

        rxGankService.getWelfareListData("15","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<CommonEntity>() {
                    @Override
                    public void onNext(CommonEntity android) {
                        Glide.with(mContext).load(android.getResults().get(0).getUrl()).into(imageView);
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

        rxGithubService.listStarredRepos("dangxy")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<List<RepoEntity>>() {
                    @Override
                    public void onNext(List<RepoEntity> repoEntities) {

                      for(RepoEntity repoEntity:repoEntities){
                          MLog.e("dang",repoEntities.size()+"");
                          MLog.e("dang",repoEntity.getDescription());
                      }
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

package com.dangxy.handlerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.dangxy.handlerdemo.realm.RealmActivity;
import com.dangxy.handlerdemo.receiver.NetworkBroadcastReceiver;
import com.dangxy.handlerdemo.receiver.PhoneStatusReceiver;
import com.dangxy.handlerdemo.utils.MLog;
import com.dangxy.handlerdemo.utils.ShakeUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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

    private static final int ACCESS_COARSE_LOCATION = 2;
    private static final int ACCESS_FINE_LOCATION = 3;
    private static final int TAKE_PHOTO = 4;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle;
            String value;
            switch (msg.what) {
                case 1:
                    bundle = msg.getData();
                    value = bundle.getString("hello");
                    break;
                case 2:
                    bundle = msg.getData();
                    value = bundle.getString("hello");
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
    private EditText et;
    private RxPermissions rxPermissions;
    public static final int MY_PERMISSIONS_CAMERA = 1;
    private String mMobile;
    private Uri photoURI;

    public static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};
    public static String[] mediaColumns = {MediaStore.Video.Media._ID};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv_image_view);
        save = (Button) findViewById(R.id.save);
        et = (EditText) findViewById(R.id.et);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("hello", "word");
        message.setData(bundle);
        message.what = 1;
        final Message message2 = new Message();
        Bundle bundle2 = new Bundle();
        bundle2.putString("hello", "dang");
        message2.setData(bundle2);
        message2.what = 2;
        handler.sendMessageDelayed(message, 3000);
        handler.sendMessageDelayed(message2, 10000);
        mContext = this;
        //retrofitGithub();

        //rxRetrofitGithub();

        // retrofitGank();

        //rxRetrofitGank();

        //rxGankRetrofit();
        //goToAppSetting();
        //goToSet(this);
        rxPermissions = new RxPermissions(this);


        ShakeUtils shakeUtils = new ShakeUtils(mContext);
        shakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                MLog.e("DANG", "振动");
            }
        });


        Uri videoUrl = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String path = getThumbnailPathForLocalFile(this, videoUrl);

        MLog.e("DANG", path);
        imageView.setImageURI(Uri.parse(path));


        //getImagesFromLocal();


//        RxView.clicks(imageView)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) {
//                    }
//                });

        //requestPermissions();
        //requestCallPermissions();
        RxView.clicks(save)
                .throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (Build.VERSION.SDK_INT >= 23) {
                            requestRxCamera();
                        }
                    }


                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        RxView.longClicks(save)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        MLog.e("DANG", "长点击事件");
                    }
                });

        RxTextView.textChanges(et).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {

                MLog.e("DANG", charSequence + "");
            }
        });
        RxTextView.afterTextChangeEvents(et).subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
            @Override
            public void accept(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {

                MLog.e("DANG", "输入完毕");

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

              Intent intent = new Intent(mContext,RealmActivity.class);
              startActivity(intent);
//                Intent intent = new Intent("android.intent.action.MY_BROADCAST");
//                intent.putExtra("msg", "这是一条测试广播");
//                sendBroadcast(intent);

            }
        });


    }


    public static String getThumbnailPathForLocalFile(Activity context,
                                                      Uri fileUri) {
        long fileId = getFileId(context, fileUri);
        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

        Cursor thumbCursor = null;
        try {

            thumbCursor = context.managedQuery(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                            + fileId, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }

        } finally {
        }

        return null;
    }

    public static long getFileId(Activity context, Uri fileUri) {

        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null,
                null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);

            return id;
        }

        return 0;
    }

    private void getImagesFromLocal() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = MainActivity.this.getContentResolver();
        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        //只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, largeFileSort);
        int a = 1;
        while (mCursor.moveToNext()) {
            //获取图片的路径
            if (mCursor.isFirst()) {
                String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));

                MLog.e("DANG", path);
                imageView.setImageURI(Uri.parse(path));
                break;
            }

        }
    }

    private void requestCallPermissions() {
        onCall("18236889159");
    }

    private void callDirectly(String mobile) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mobile));
        mContext.startActivity(intent);
    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    public void onCall(String mobile) {
        this.mMobile = mobile;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                //上面已经写好的拨号方法
                callDirectly("18236889159");
            }
        } else {
            //上面已经写好的拨号方法
            callDirectly("18236889159");
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                MLog.e("DANG", "qqqqqq");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
                MLog.e("DANG", "wwwww");
            }
        } else {

            MLog.e("DANG", "eeeeeee");
        }
    }

    private void requestCamera() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            MLog.e("DANG", "1111");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("app需要开启权限才能使用此功能")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                MLog.e("DANG", "222222");
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                MLog.e("DANG", "3333333");
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    MLog.e("DANG", "同意");
                } else {
                    MLog.e("DANG", "拒绝");
                }
                break;

            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    MLog.e("DANG", "同意");
                    callDirectly("18236889159");
                } else {
                    MLog.e("DANG", "拒绝");
                }
                break;
            default:
                break;
        }
    }

    private void requestRxCamera() {

        rxPermissions.request(Manifest.permission.CAMERA)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                        if (aBoolean) {
                            MLog.e("DANG", "同意");
                        } else {
                            MLog.e("DANG", "拒绝");
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

    private void takePhoto() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri());
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private Uri getUri() {

        return Uri.fromFile(getFile());
    }

    /**
     * 该方法用于获取指定路径 和 名字 的file
     *
     * @return
     */
    private File getFile() {
        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "CameraDemo");
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        //将图片保存的名字设置为当前拍照的时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = format.format(new Date());
        File file = new File(filePath.getPath() + File.separator + name + ".jpg");
        return file;

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
        RebaseService rebaseService = new RetrofitRebase().newInstance(this).create(RebaseService.class);
        rebaseService.register("dangxy99", "111111", "dangxy99", "dangxy99@163.com", "android developer").enqueue(new Callback<RebaseUserEntity>() {
            @Override
            public void onResponse(Call<RebaseUserEntity> call, Response<RebaseUserEntity> response) {

                RebaseUserEntity rebaseUserEntity = response.body();
                MLog.e("DANG", response.body().toString());
            }

            @Override
            public void onFailure(Call<RebaseUserEntity> call, Throwable t) {

            }
        });
    }

    private void getReadhubNewsList(ReadhubService readhubService) {
        readhubService.listNews("", 15).enqueue(new Callback<NewListEntity>() {
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
        readhubService.listTechNews("", 15).enqueue(new Callback<NewListEntity>() {
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
        readhubService.listTopicNews("", 15).enqueue(new Callback<TopicRsp>() {
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

        rxGankService.getWelfareListData("15", "1")
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

        rxGankService.getListData("15", "1")
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
        gankService.getListData("15", "1").enqueue(new Callback<CommonEntity>() {
            @Override
            public void onResponse(Call<CommonEntity> call, Response<CommonEntity> response) {
                CommonEntity android = response.body();
                MLog.e("DANG", android.getResults().get(1).getDesc())
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

                        for (RepoEntity repoEntity : repoEntities) {
                            MLog.e("dang", repoEntities.size() + "");
                            MLog.e("dang", repoEntity.getDescription());
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
        GithubService githubService = new RetrofitGithub().newInstance(this).create(GithubService.class);
        githubService.listRepos("dangxy").enqueue(new Callback<List<RepoEntity>>() {
            @Override
            public void onResponse(Call<List<RepoEntity>> call, Response<List<RepoEntity>> response) {
                List<RepoEntity> repoEntityList = response.body();
                MLog.d("DANG", repoEntityList.get(0).getFull_name());
            }

            @Override
            public void onFailure(Call<List<RepoEntity>> call, Throwable t) {
                MLog.e("DANG", t.getMessage());
            }
        });
    }

    /**
     * app的设置
     *
     * @param context
     */
    private void goToSet(Context context) {
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
        context.startActivity(mIntent);
    }

    /**
     * 系统的设置
     */

    private void goToAppSetting() {
        Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(mIntent);
    }

    /**
     * 跳转应用市场
     *
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

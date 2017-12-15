package com.dangxy.handlerdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dangxy.handlerdemo.adapter.ReadhubListAdapter;
import com.dangxy.handlerdemo.api.ReadhubService;
import com.dangxy.handlerdemo.api.RetrofitReadhub;
import com.dangxy.handlerdemo.api.RxReadhubService;
import com.dangxy.handlerdemo.entity.TopicRsp;
import com.dangxy.handlerdemo.utils.LoadMoreDelegate;
import com.dangxy.handlerdemo.utils.MLog;
import com.dangxy.handlerdemo.utils.SwipeRefreshDelegate;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * @description  描述
 * @author  dangxy99
 * @date   2017/12/15
 */

public class RxReadhubFragment extends Fragment implements SwipeRefreshDelegate.OnSwipeRefreshListener, LoadMoreDelegate.LoadMoreSubject {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private ReadhubListAdapter readhubListAdapter;
    private SwipeRefreshDelegate refreshDelegate;
    private AtomicInteger loadingCount;
    private View view;
    private String last;
    private LoadMoreDelegate onLoadMoreDelegate;


    public RxReadhubFragment() {
        // Required empty public constructor
    }

    public static RxReadhubFragment newInstance(String param1, String param2) {
        RxReadhubFragment fragment = new RxReadhubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_readhub, container, false);
        unbinder = ButterKnife.bind(this, view);
        refreshDelegate = new SwipeRefreshDelegate(this);
        onLoadMoreDelegate = new LoadMoreDelegate(this);
        loadingCount = new AtomicInteger(0);
        initData();
        return view;
    }

    private void initData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HandlerDemoApplication.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        //getData("");
        getRxData("");
//        rv.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page) {
//                getMoreData(last);
//            }
//        });


    }

    private void getData(String lastCursors) {
        ReadhubService readhubService = new RetrofitReadhub().newInstance(HandlerDemoApplication.getContext()).create(ReadhubService.class);

        readhubService.listTopicNews(lastCursors, 15).enqueue(new Callback<TopicRsp>() {
            @Override
            public void onResponse(Call<TopicRsp> call, Response<TopicRsp> response) {


            }

            @Override
            public void onFailure(Call<TopicRsp> call, Throwable t) {

            }
        });


        getRxData(lastCursors);
    }

    private void getRxData(String lastCursors) {
        RxReadhubService rxReadhubService = new RetrofitReadhub().newInstance(HandlerDemoApplication.getContext()).create(RxReadhubService.class);

        rxReadhubService.listTopicNews(lastCursors, 15)
                .doOnNext(new Consumer<TopicRsp>() {
                    @Override
                    public void accept(TopicRsp topicRsp) throws Exception {
                    MLog.e("DANG","doOnNext");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        refreshDelegate.setRefresh(false);
                        MLog.e("DANG","doFinally");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<TopicRsp>() {
                    @Override
                    public void onNext(TopicRsp topicRsp) {
                        refreshDelegate.attach(view);
                        last = topicRsp.getData().get(topicRsp.getData().size() - 1).getOrder();
                        readhubListAdapter = new ReadhubListAdapter(topicRsp.getData());
                        onLoadMoreDelegate.attach(rv);
                        rv.setAdapter(readhubListAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSwipeRefresh() {
        getData("");
    }

    private void getMoreData(String lastCursors) {
        notifyLoadingStarted();
        ReadhubService readhubService = new RetrofitReadhub().newInstance(HandlerDemoApplication.getContext()).create(ReadhubService.class);

        readhubService.listTopicNews(lastCursors, 15).enqueue(new Callback<TopicRsp>() {
            @Override
            public void onResponse(Call<TopicRsp> call, Response<TopicRsp> response) {
                notifyLoadingFinished();
                TopicRsp topicRsp = response.body();
                last = topicRsp.getData().get(topicRsp.getData().size() - 1).getOrder();
                readhubListAdapter.addAll(topicRsp.getData());


            }

            @Override
            public void onFailure(Call<TopicRsp> call, Throwable t) {

            }
        });
    }

    private void getRxMoreData(String lastCursors) {

        RxReadhubService rxReadhubService = new RetrofitReadhub().newInstance(HandlerDemoApplication.getContext()).create(RxReadhubService.class);

        rxReadhubService.listTopicNews(lastCursors, 15)
                .doOnNext(new Consumer<TopicRsp>() {
                    @Override
                    public void accept(TopicRsp topicRsp) throws Exception {
                        notifyLoadingStarted();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        notifyLoadingFinished();

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceObserver<TopicRsp>() {
                    @Override
                    public void onNext(TopicRsp topicRsp) {
                        last = topicRsp.getData().get(topicRsp.getData().size() - 1).getOrder();
                        readhubListAdapter.addAll(topicRsp.getData());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public boolean isLoading() {
        return loadingCount.get() > 0;
    }

    @Override
    public void onLoadMore() {
        getRxMoreData(last);

    }

    public void notifyLoadingStarted() {
        loadingCount.getAndIncrement();
    }


    public void notifyLoadingFinished() {
        loadingCount.decrementAndGet();
    }
}

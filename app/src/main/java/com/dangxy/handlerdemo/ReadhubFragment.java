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
import com.dangxy.handlerdemo.entity.NewListEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadhubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private ReadhubListAdapter readhubListAdapter;


    public ReadhubFragment() {
        // Required empty public constructor
    }

    public static ReadhubFragment newInstance(String param1, String param2) {
        ReadhubFragment fragment = new ReadhubFragment();
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
        View view = inflater.inflate(R.layout.fragment_readhub, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HandlerDemoApplication.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);

        final ReadhubService readhubService = new RetrofitReadhub().newInstance(HandlerDemoApplication.getContext()).create(ReadhubService.class);

        readhubService.listTopicNews("",15).enqueue(new Callback<NewListEntity>() {
            @Override
            public void onResponse(Call<NewListEntity> call, Response<NewListEntity> response) {

                NewListEntity newListEntity = response.body();
                readhubListAdapter = new ReadhubListAdapter(newListEntity.getData());
                rv.setAdapter(readhubListAdapter);


            }

            @Override
            public void onFailure(Call<NewListEntity> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

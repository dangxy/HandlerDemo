package com.dangxy.handlerdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.dangxy.handlerdemo.adapter.ReadhubAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadhubActivity extends FragmentActivity {

    @BindView(R.id.tl_readhub_list)
    TabLayout tlReadhubList;
    @BindView(R.id.vp_readhub_list)
    ViewPager vpReadhubList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readhub);
        ButterKnife.bind(this);

        ReadhubAdapter readhubAdapter = new ReadhubAdapter(getSupportFragmentManager());
        vpReadhubList.setAdapter(readhubAdapter);
        tlReadhubList.setupWithViewPager(vpReadhubList);
        tlReadhubList.setTabMode(TabLayout.MODE_FIXED);
        tlReadhubList.setTabsFromPagerAdapter(readhubAdapter);
    }
}

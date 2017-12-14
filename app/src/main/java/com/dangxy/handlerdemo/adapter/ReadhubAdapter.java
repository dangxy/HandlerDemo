package com.dangxy.handlerdemo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dangxy.handlerdemo.ReadhubFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dangxueyi
 * @description
 * @date 2017/12/14
 */

public class ReadhubAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> fragmentTitles;
    private List<String> titleList;
    public ReadhubAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragmentTitles = new ArrayList<>();
        titleList = new ArrayList<>();
        titleList.add("热门话题");
        titleList.add("科技动态");
        titleList.add("开发者咨询");
        initData();
    }
    private void  initData(){
        for (String s : titleList) {
            fragmentTitles.add(s);
            ReadhubFragment readhubFragment = new ReadhubFragment();
            fragments.add(readhubFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.size() > 0) {
            return fragments.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
         return fragmentTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (fragmentTitles.size() > 0) {
            return fragmentTitles.get(position);
        } else {
            return null;
        }
    }
}

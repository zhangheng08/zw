package com.zhiwang123.mobile.phone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhiwang123.mobile.phone.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/18.
 */

public class VideoPageFrgmentAdapter extends FragmentPagerAdapter {


    private ArrayList<BaseFragment> mList;

    public VideoPageFrgmentAdapter(FragmentManager fm, ArrayList<BaseFragment> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

}

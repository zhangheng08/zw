package com.zhiwang123.mobile.phone.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ty on 2016/12/7.
 */

public class TeachPlanPagerAdapter extends PagerAdapter {


    protected List<View> views;


    public TeachPlanPagerAdapter(List<View> viewList) {
        views = viewList;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View inner = views.get(position);

        if(inner.getParent() != null) {
            container.removeView(inner);
        }

        container.addView(inner);

        return inner;
    }

}
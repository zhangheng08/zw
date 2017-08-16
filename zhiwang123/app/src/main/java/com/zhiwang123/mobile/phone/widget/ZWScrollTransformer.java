package com.zhiwang123.mobile.phone.widget;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by ty on 2016/12/7.
 */

public class ZWScrollTransformer implements ViewPager.PageTransformer {

    public static final String TAG = "ZWScrollTransformer";

    /**
     * position参数指明给定页面相对于屏幕中心的位置。它是一个动态属性，会随着页面的滚动而改变。
     * 当一个页面（page)填充整个屏幕时，positoin值为0；
     * 当一个页面（page)刚刚离开屏幕右(左）侧时，position值为1（-1）；
     * 当两个页面分别滚动到一半时，其中一个页面是-0.5，另一个页面是0.5。
     * 基于屏幕上页面的位置，通过诸如setAlpha()、setTranslationX()或setScaleY()方法来设置页面的属性，创建自定义的滑动动画。
     */

    @Override
    public void transformPage(View page, float position) {

        Log.i(TAG, page.getTag() + " --- " + position);

        page.setTranslationX(- 300 * position);
        //page.setTranslationY(-100 * position);

        float seed = 1 - Math.abs(position);

        page.setScaleX(seed < 0.7f ? 0.7f : seed);
        page.setScaleY(seed < 0.8f ? 0.8f : seed);

        page.setAlpha(seed < 0.7f ? 0.7f : seed);

    }



}

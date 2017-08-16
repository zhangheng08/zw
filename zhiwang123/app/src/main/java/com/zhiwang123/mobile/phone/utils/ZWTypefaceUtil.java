package com.zhiwang123.mobile.phone.utils;

import android.graphics.Typeface;

import com.zhiwang123.mobile.ZWApplication;

import java.util.Hashtable;

/**
 * Created by ty on 2016/12/2.
 */

public class ZWTypefaceUtil
{
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(String name)
    {
        synchronized (cache)
        {
            if (!cache.containsKey(name))
            {
                Typeface t = Typeface.createFromAsset(ZWApplication.getAppContext().getAssets(), name);
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }
}

package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.zhiwang123.mobile.phone.utils.BitmapCache;

/**
 * Created by ty on 2016/11/1.
 */

public abstract class BaseZwAdapter extends BaseAdapter {

    public static final String TAG = "BaseZwAdapter";

    protected Context mContext;
    protected RequestQueue mRequestQueue;
    protected ImageLoader mImageLoader;

    public BaseZwAdapter(Context context) {

        mContext = context;

        if(mContext != null) {

            mRequestQueue = Volley.newRequestQueue(context);
            mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());

        }

    }

    protected void onRemFav() {



    }

}

package com.zhiwang123.mobile.phone.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ty on 2016/12/6.
 */

public class LocalVideo extends DownloadTask implements Serializable {

    public static final int DIR = 0x0;
    public static final int FIL = 0x1;

    public int type;
    public Bitmap fileImg;
    public int taskNum;

}

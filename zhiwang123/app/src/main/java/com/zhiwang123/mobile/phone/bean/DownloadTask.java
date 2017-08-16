package com.zhiwang123.mobile.phone.bean;

import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import com.bokecc.sdk.mobile.download.Downloader;
import com.zhiwang123.mobile.phone.service.DownLoadTaskWrapper;

/**
 * Created by ty on 2016/12/5.
 */

public class DownloadTask extends BaseEntity implements Runnable {

    public String title;

    public String videoId;

    public long activeTime;

    public long currentBits = 0l;

    public long totalBits = 0l;

    public int status = DownLoadTaskWrapper.START;

    public String filePath;

    public String cover;

    public Downloader downloader;

    public SharedPreferences sp;

    public boolean isRun = false;

    public Handler uiHandler;

    @Override
    public void run() {

        if(sp != null) {

            while(status == DownLoadTaskWrapper.START) {

                isRun = true;
                status = sp.getInt(videoId + "_int", 0);
                String str = sp.getString(videoId + "_str", "");
                String[] strs = str.split("-");

                if(strs == null || strs.length <= 1) return;

                currentBits = Long.parseLong(TextUtils.isEmpty(strs[0]) ? "0" : strs[0]);
                totalBits = Long.parseLong(TextUtils.isEmpty(strs[1]) ? "0" : strs[1]);
                title = strs[2];
                cover = strs[3];


                try {
                    Thread.sleep(100);
                    if(uiHandler != null) uiHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

            }

            isRun = false;

        }

    }

    @Override
    public boolean equals(Object other) {

        if(!(other instanceof DownloadTask)) return false;

        if(videoId.equals(((DownloadTask)other).videoId)) {
            return true;
        } else {
            return  false;
        }

    }
}

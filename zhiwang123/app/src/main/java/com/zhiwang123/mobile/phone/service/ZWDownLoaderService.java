package com.zhiwang123.mobile.phone.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhiwang123.mobile.phone.bean.DownloadTask;

import java.util.HashMap;

/**
 * Created by ty on 2016/12/5.
 */

public class ZWDownLoaderService extends Service {

    private static final String TAG = "ZWDownLoaderService";

    public static final String VID = "video_id";
    public static final String VTI = "video_title";
    public static final String SP_TOP_KEY_PRE = "sp_top_download_save_key_";
    public static final String SP_VIDS = "sp_video_ids";
    public static final String SP_VIDS_SET = "sp_video_ids_set";
    public static final String BROADCAST_REFRESH_ACTION = "com.zhiwang123.mobile.notify.refresh";

    public static HashMap<String, DownloadTask> mTaskItems = new HashMap<>();
    public static HashMap<String, DownLoadTaskWrapper> mDownLoadTasks = new HashMap<>();

    private Context mContext;

    @Override
    public void onCreate() {

        mContext = getApplicationContext();

        mDownLoadTasks = new HashMap<>();

        super.onCreate();
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String vid = intent.getStringExtra(VID);

        boolean isRestart = intent.getBooleanExtra("restart", false);

        DownloadTask taskItem = mTaskItems.get(vid);

        DownLoadTaskWrapper taskWrapper = new DownLoadTaskWrapper(mContext, taskItem);

        taskWrapper.setmTaskListener(taskListener);

        mDownLoadTasks.put(vid, taskWrapper);

        taskItem.downloader.start();

        if(isRestart) {

            notifyRef(vid);

        }

        return super.onStartCommand(intent, flags, startId);

    }

    private DownLoadTaskWrapper.TaskListener taskListener = new DownLoadTaskWrapper.TaskListener() {

        @Override
        public void onTaskExecute(long now, long total, String vid) {

            SharedPreferences sp = getSharedPreferences(SP_TOP_KEY_PRE + vid, 0);
            sp.edit().putString(vid + "_str", now + "-" + total + "-" + mTaskItems.get(vid).title + "-" + mTaskItems.get(vid).cover).commit();
            sp.edit().putInt(vid + "_int", DownLoadTaskWrapper.START).commit();

        }

        @Override
        public void onTaskFinis(String vid) {

            mDownLoadTasks.remove(vid);
            mTaskItems.remove(vid);

            notifyRef(vid);

            if(mDownLoadTasks.size() == 0) {
                stopSelf();
            }

        }
    };

    private void notifyRef(String vid) {

        Intent intent = new Intent(BROADCAST_REFRESH_ACTION);
        intent.putExtra(VID, vid);
        mContext.sendBroadcast(intent);

    }

//    public static Set<String> addLoadingIdsXml(Context context, String id) {
//
//        SharedPreferences sp = context.getSharedPreferences(SP_VIDS, 0);
//        Set<String> set = sp.getStringSet(SP_VIDS_SET, new HashSet<String>());
//        set.add(id);
//        sp.edit().putStringSet(SP_VIDS_SET, set).commit();
//
//        return set;
//
//    }
//
//    public static Set<String> removeLoadingIdsXml(Context context, String id) {
//
//        SharedPreferences sp = context.getSharedPreferences(SP_VIDS, 0);
//        Set<String> set = sp.getStringSet(SP_VIDS_SET, null);
//        if(set != null) {
//            set.remove(id);
//            sp.edit().putStringSet(SP_VIDS_SET, set).commit();
//        }
//
//        return set;
//    }
//
//    public static Set<String> getLoadingIdsXml(Context context) {
//
//        SharedPreferences sp = context.getSharedPreferences(SP_VIDS, 0);
//        Set<String> set = sp.getStringSet(SP_VIDS_SET, null);
//        return set;
//
//    }

}

package com.zhiwang123.mobile.phone.service;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.bokecc.sdk.mobile.download.DownloadListener;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.zhiwang123.mobile.phone.activity.OffLineListActivity;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;

/**
 * Created by ty on 2016/12/5.
 */

public class DownLoadTaskWrapper {

    private static final String TAG = "DownLoadTaskWrapper";

    public static final int START = 200;
    public static final int FINISH = 400;
    public static final int PAUSE = 300;

    public DownloadTask taskEntity;


    private Context mContext;
    private Downloader mCCDownLoader;
    private DownloadListener mCCDownloadListener;
    private TaskListener mTaskListener;



    private boolean bo = false;




    public DownLoadTaskWrapper(Context context, DownloadTask task) {

        mContext = context;
        taskEntity = task;
        mCCDownLoader = task.downloader;
        mCCDownloadListener = new ZWDownLoadListener();
        mCCDownLoader.setDownloadListener(mCCDownloadListener);

    }


    public class ZWDownLoadListener implements DownloadListener {

        @Override
        public void handleProcess(long start, long end, String s) {

            taskEntity.totalBits = end;
            taskEntity.currentBits = start;

//            Log.i(TAG, start + "-" + end + "-" + s);

            if(mTaskListener != null) mTaskListener.onTaskExecute(start, end, s);

        }

        @Override
        public void handleException(DreamwinException e, int i) {

            Log.i(TAG, e.getMessage() + "");

        }

        @Override
        public void handleStatus(String vid, int status) {

            Log.i(TAG, vid + "+-+" + status);

            Log.i(TAG, taskEntity.currentBits + "--------" + taskEntity.totalBits );

            if(status == FINISH) {

                taskEntity.status = FINISH;

                bo = true;

                if(mTaskListener != null) mTaskListener.onTaskFinis(vid);

                //taskEntity.currentBits = taskEntity.totalBits;

                AsyncDBHelper.saveVideoCacheDB(mContext, taskEntity, uiHandler);

            } else if(status == PAUSE) {

                if(bo) return;

                taskEntity.status = DownLoadTaskWrapper.PAUSE;

                AsyncDBHelper.saveVideoCacheDB(mContext, taskEntity, uiHandler2);

            }

        }

        @Override
        public void handleCancel(String s) {



        }

    }

    public void setmTaskListener(TaskListener tl) {
        mTaskListener = tl;
    }

    public interface TaskListener {

        public void onTaskExecute(long now, long total, String vid);

        public void onTaskFinis(String vid);

    }

    private AsyncDBHelper.UIHandler uiHandler = new AsyncDBHelper.UIHandler<DownloadTask>() {
        @Override
        public void processMessage(int what, DownloadTask cache) {
            switch (SEND_SAVE_OK) {

            }
        }
    };

    private AsyncDBHelper.UIHandler uiHandler2 = new AsyncDBHelper.UIHandler() {

        @Override
        public void handleMessage(Message msg) {

            ZWDownLoaderService.mDownLoadTasks.remove(taskEntity.videoId);
            ZWDownLoaderService.mTaskItems.remove(taskEntity.videoId);

            if(ZWDownLoaderService.mDownLoadTasks.isEmpty()) {

                Intent intent = new Intent(mContext, ZWDownLoaderService.class);
                mContext.stopService(intent);

            }

        }
    };

}

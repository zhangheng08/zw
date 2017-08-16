package com.zhiwang123.mobile.phone.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.BaseTask;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.LocalListAdapter;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.bean.LocalVideo;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.service.DownLoadTaskWrapper;
import com.zhiwang123.mobile.phone.service.ZWDownLoaderService;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by ty on 2016/11/2.
 */

public class LocalVideoListActivity extends BaseActivity implements AsyncExecuteCallback {

    private static final String TAG = LocalVideoListActivity.class.getName();

    private Context mContext;
    private View mRootView;
    private View mGobackBtn;
    private View mDelteBtn;
    private TextView mTitleTxt;
    private ProgressBar mProgress;

    private XRestrictListView mListView;

    private SharedPreferences mSp;

    private HashMap<String, String> tasks;

    private ArrayList<LocalVideo> mDataList;
    private LocalListAdapter mAdapter;

    public static final int REQUEST_CODE_FOR_REFRESH = 0;
    public static final int RESULT_CODE_NEED_REFRESH = 10;
    public static final int RESULT_CODE_NONEED_REFRESH = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.local_video_list_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mContext = this.getApplicationContext();

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_localvideo_layout);
        mSp = getSharedPreferences(ZWDownLoaderService.SP_TOP_KEY_PRE, 0);

        initLayout();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ZWDownLoaderService.BROADCAST_REFRESH_ACTION);
        registerReceiver(refreshReceiver, filter);

        AsyncDBHelper.getVideoCacheDB(mContext, uiHandler);

    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.local_action_layout_root);
        mTitleTxt = (TextView) actionView.findViewById(R.id.local_title);
        mGobackBtn = actionView.findViewById(R.id.local_go_back);
        mDelteBtn = actionView.findViewById(R.id.local_to_delete);

        mProgress = (ProgressBar) findViewById(R.id.local_page_progress);
        mListView = (XRestrictListView) findViewById(R.id.local_page_list);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);

        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDelteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mAdapter != null) {

                    if(mAdapter.isExpandAll) {

                        mAdapter = new LocalListAdapter(LocalVideoListActivity.this, mDataList, mListView);
                        mListView.setAdapter(mAdapter);
                        mAdapter.isExpandAll = false;

                    } else {

                        mAdapter = new LocalListAdapter(LocalVideoListActivity.this, mDataList, mListView);
                        mListView.setAdapter(mAdapter);
                        mAdapter.isExpandAll = true;

                    }

                    mAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    private ArrayList<LocalVideo> initPageList(ArrayList<DownloadTask> caches) {

        ArrayList<LocalVideo> dataList = null;

        if(caches != null && caches.size() != 0) {

            ArrayList<LocalVideo> localVideos = new ArrayList<>();

            LocalVideo loadingTask = new LocalVideo();

            loadingTask.type = LocalVideo.DIR;

            int taskNum = 0;

            for(DownloadTask cache : caches) {

                LocalVideo localVideo = new LocalVideo();

                if(cache.status == DownLoadTaskWrapper.START || cache.status == DownLoadTaskWrapper.PAUSE) {

                    loadingTask.taskNum = ++ taskNum;

                } else {

                    localVideo.type = LocalVideo.FIL;
                    localVideo.videoId = cache.videoId;
                    localVideo.title = cache.title;
                    localVideo.status = cache.status;
                    localVideo.cover = cache.cover;
//                   localVideo.fileImg = getVideoThumbnail(cache.filePath);
                    localVideo.filePath = cache.filePath;
                    localVideo.currentBits = cache.currentBits;
                    localVideo.totalBits = cache.totalBits;

                    localVideos.add(localVideo);

                }

            }

            if(loadingTask.taskNum != 0) {

                dataList = new ArrayList<>();
                dataList.add(loadingTask);
                dataList.addAll(localVideos);

            } else {

                dataList = localVideos;

            }

        }

        return dataList;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(refreshReceiver);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_FOR_REFRESH) {

            switch (resultCode) {
                case RESULT_CODE_NEED_REFRESH:

                    mProgress.setVisibility(View.VISIBLE);
                    mDataList.clear();
                    mAdapter.notifyDataSetChanged();
                    AsyncDBHelper.getVideoCacheDB(mContext, uiHandler);

                    break;
                case RESULT_CODE_NONEED_REFRESH:



                    break;
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public Bitmap getVideoThumbnail(String filePath) {

        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public void pre(Object... objs) {

    }

    @Override
    public BaseTask.TaskResult async(int what, BaseTask baseTask) {

        switch (what) {
            case 0:
                ArrayList<DownloadTask> caches = (ArrayList) baseTask.param;
                ArrayList<LocalVideo> list = initPageList(caches);
                BaseTask.TaskResult result = baseTask.new TaskResult();
                result.setResult(list);
                return result;
        }

        return null;
    }

    @Override
    public void post(Message msg) {

        BaseTask baseTask = (BaseTask) msg.obj;

        switch (msg.what) {
            case 0:

                mDataList = (ArrayList<LocalVideo>) baseTask.result.getResult();
                mAdapter = new LocalListAdapter(LocalVideoListActivity.this, mDataList, mListView);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

//                mListView.setOnItemClickListener(onItemClickListener);

                mProgress.setVisibility(View.GONE);

                break;
        }

    }

    AsyncDBHelper.UIHandler uiHandler = new AsyncDBHelper.UIHandler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SEND_DB_DATA:

                    if(msg.obj == null) {
                        mProgress.setVisibility(View.GONE);
                        break;
                    }


                    ArrayList<DownloadTask> caches = (ArrayList) msg.obj;
                    ArrayList<LocalVideo> list = initPageList(caches);
                    mDataList = list;
                    mAdapter = new LocalListAdapter(LocalVideoListActivity.this, mDataList, mListView);
                    mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
//                    mListView.setOnItemClickListener(onItemClickListener);
                    mProgress.setVisibility(View.GONE);

//                    BaseTask task = new BaseTask(0, msg.obj, LocalVideoListActivity.this);
//                    AsyncExecutor.getInstance().execMuliteTask(task);

                    break;

            }

        }
    };

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            mProgress.setVisibility(View.VISIBLE);
            mDataList.clear();
            mAdapter.notifyDataSetChanged();
            AsyncDBHelper.getVideoCacheDB(mContext, uiHandler);

        }
    };

//    private AdapterView.OnItemClickListener onItemClickListener =  new AdapterView.OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            Log.i(TAG, "0------ 123");
//
//
//
//        }
//    };


}

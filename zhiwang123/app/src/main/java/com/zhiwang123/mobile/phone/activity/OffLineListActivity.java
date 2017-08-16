package com.zhiwang123.mobile.phone.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bokecc.sdk.mobile.download.Downloader;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.cc.ConfigUtil;
import com.zhiwang123.mobile.phone.adapter.OfflineListAdapter;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.service.DownLoadTaskWrapper;
import com.zhiwang123.mobile.phone.service.ZWDownLoaderService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by ty on 2016/11/2.
 */

public class OffLineListActivity extends BaseActivity {

    private static final String TAG = OffLineListActivity.class.getName();

    public static final String EXTRA_SWITCH_FRAGMENT = "switch_fragment";

    public static final int FRAGMENT_COURSE_LIST   = 0;
    public static final int FRAGMENT_COURSE_CENTER = 1;

    private View mRootView;
    private View mGobackBtn;
    private View mDelteBtn;
    private TextView mTitleTxt;

    private ListView mListView;
    private OfflineListAdapter mAdpater;

    private SharedPreferences mSp;

    private HashMap<String, String> tasks;

    private ArrayList<DownloadTask> mDataList;

    private int resultCode = LocalVideoListActivity.RESULT_CODE_NONEED_REFRESH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.offline_list_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_offline_layout);

        mSp = getSharedPreferences(ZWDownLoaderService.SP_TOP_KEY_PRE, 0);

        initLayout();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ZWDownLoaderService.BROADCAST_REFRESH_ACTION);
        registerReceiver(refreshReceiver, filter);

        initPageList();

    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.offline_action_layout_root);
        mTitleTxt = (TextView) actionView.findViewById(R.id.offline_title);
        mGobackBtn = actionView.findViewById(R.id.offline_go_back);
        mDelteBtn = actionView.findViewById(R.id.offline_to_delete);

        mDelteBtn.setVisibility(View.GONE);

        mListView = (ListView) findViewById(R.id.offline_page_list);

        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initPageList() {

        mDataList = new ArrayList<>();
        mAdpater = new OfflineListAdapter(this);

        if(ZWDownLoaderService.mDownLoadTasks.isEmpty()) {

            getUnfinishDB();

        } else {

            Set<String> keys = ZWDownLoaderService.mDownLoadTasks.keySet();
            Iterator<String> i = keys.iterator();

            while (i.hasNext()) {

                String vid = i.next();
                DownloadTask task = new DownloadTask();
                task.videoId = vid;
                task.sp = getSharedPreferences(ZWDownLoaderService.SP_TOP_KEY_PRE + vid, 0);
                task.uiHandler = mUiHandler;
                task.status = DownLoadTaskWrapper.START;
                task.downloader = ZWDownLoaderService.mTaskItems.get(vid).downloader;
                task.cover = ZWDownLoaderService.mTaskItems.get(vid).cover;
                mDataList.add(task);

            }

            getUnfinishDB();

        }

    }

    private void getUnfinishDB() {

        AsyncDBHelper.getVideoCacheDB(this, new AsyncDBHelper.UIHandler() {

            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case SEND_DB_DATA:

                        if(msg.obj == null) break;

                        ArrayList<DownloadTask> caches = (ArrayList) msg.obj;

                        for(DownloadTask t : caches) {

                            if(t.status == DownLoadTaskWrapper.PAUSE) {

                                if(!mDataList.contains(t)) mDataList.add(t);

                            }

                        }

                        mAdpater.addDataList(mDataList);
                        mListView.setAdapter(mAdpater);
                        mAdpater.notifyDataSetChanged();
                        mListView.setOnItemClickListener(onItemClickListener);

                        break;

                }

            }
        });

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

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

        setResult(resultCode);

        finish();

        super.onBackPressed();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            OfflineListAdapter.CellHolder ch = (OfflineListAdapter.CellHolder) view.getTag();

            final DownloadTask task = ch.task;

            if(task.status == DownLoadTaskWrapper.START) {

                task.downloader.pause();
                task.status = DownLoadTaskWrapper.PAUSE;
                ch.pauseHint.setText(R.string.offline_item_pause);

            } else if(task.status == DownLoadTaskWrapper.PAUSE) {

                reStartVideoLoading(task);

            }


        }
    };

    public void reStartVideoLoading(final DownloadTask task) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            String path = Environment.getExternalStorageDirectory() + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/");
            File savePath = new File(path);
            if (!savePath.exists()) savePath.mkdirs();
            String filePath = path.concat(task.title).concat(".mp4");
            File file = new File(filePath);

//            if(file.exists()) return;

            Downloader downloader = new Downloader(file, task.videoId, ConfigUtil.USERID, ConfigUtil.API_KEY);
            DownloadTask taskItem = task;
            taskItem.status = DownLoadTaskWrapper.START;
            taskItem.downloader = downloader;

            AsyncDBHelper.saveVideoCacheDB(this, taskItem, new AsyncDBHelper.UIHandler<DownloadTask>() {
                @Override
                public void processMessage(int what, DownloadTask cache) {

                    switch (what) {
                        case SEND_SAVE_OK:

                            ZWDownLoaderService.mTaskItems.put(task.videoId, cache);
                            Intent intent = new Intent(OffLineListActivity.this, ZWDownLoaderService.class);
                            intent.putExtra(ZWDownLoaderService.VID, task.videoId);
                            intent.putExtra("restart", true);
                            startService(intent);

                            break;
                    }

                }
            });

        }
    }

    public Handler mUiHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if(mAdpater != null) mAdpater.notifyDataSetChanged();

        }
    };

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            mDataList.clear();
            mAdpater.notifyDataSetChanged();
            initPageList();
            resultCode = LocalVideoListActivity.RESULT_CODE_NEED_REFRESH;

        }

    };

}

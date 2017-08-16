package com.zhiwang123.mobile.phone.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.zhiwang123.mobile.R;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ty on 2016/10/27.
 */

public class UpdateDialogActivity extends Activity {

    private static final String TAG = "UpdateDialogActivity";

    private static final String EXTERNALFILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"zhiwang_app_update_file";

    public static final String MESSAEG = "com.zhiwang123.update_message";
    public static final String VERSION_STR = "com.zhiwang123.update_version_code";
    public static final String UPDATE_PATH = "com.zhiwang123.update_path";
    public static final String REMOTE_LENGTH = "com.zhiwang123.upate_length";

    private View btnConifg;
    private View btnCancel;
    private View btnClose;
    private ProgressBar progressBar;
    private TextView maxLengthTxv;
    private TextView currLengthTxv;
    private TextView titleTxv;
    private Context mContext;

    private String versionStr;
    private String updatePath;
    private long fileLength;

    private boolean isDownLoadBegin = false;


    private long downloadId;

    private DownloadManager downloadManager;

    private Timer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        mContext = getApplication().getApplicationContext();

        btnConifg = findViewById(R.id.update_confirm_btn);
        btnCancel = findViewById(R.id.update_cancel_btn);
        btnClose = findViewById(R.id.udpate_close_btn);
        maxLengthTxv = (TextView) findViewById(R.id.update_max_length);
        currLengthTxv = (TextView) findViewById(R.id.update_curr_length);
        progressBar = (ProgressBar) findViewById(R.id.update_progress);
        titleTxv = (TextView) findViewById(R.id.update_title);

        initView();

    }

    private void initView() {

        Bundle bundle = getIntent().getExtras();

        String message = bundle.getString(MESSAEG, "fix some bugs.");

        versionStr = bundle.getString(VERSION_STR, "-");
        updatePath = bundle.getString(UPDATE_PATH, "");
        fileLength = bundle.getLong(REMOTE_LENGTH, 0l);

        titleTxv.setText(getString(R.string.update_alert_dialog_str, versionStr));

        float f = (float) fileLength / (1024 * 1024);
        DecimalFormat df = new DecimalFormat("#.00");
        maxLengthTxv.setText(" / " + df.format(f) + " M");

        btnConifg.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
        btnClose.setOnClickListener(onClickListener);

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.update_confirm_btn:

                    startDownload2();

                    break;

                case R.id.update_cancel_btn:
                case R.id.udpate_close_btn:

                    if(isDownLoadBegin) {

                        Toast.makeText(mContext, R.string.update_load_background, Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                finish();

                            }

                        }, 1000);

                    } else {

                        finish();

                    }



                    break;
            }

        }
    };

    private void startDownload() {

        if(TextUtils.isEmpty(updatePath)) {

            Toast.makeText(mContext, "download path error!", Toast.LENGTH_SHORT).show();

            return;
        }

        final String downloadPath = EXTERNALFILEPATH + File.separator + "littleHelper.apk";

        Log.v(TAG, "update file url" + updatePath);

        File dir = new File(EXTERNALFILEPATH);
        File file = new File(downloadPath);
        if(!dir.exists()) dir.mkdirs();
        if (file.exists()) file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final DecimalFormat df = new DecimalFormat("#.00");

        AsyncHttpClient httpClient = new AsyncHttpClient();

        httpClient.get(updatePath, new FileAsyncHttpResponseHandler(file) {

            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
                Log.v(TAG, ">---------------load failed---------------<");
                isDownLoadBegin = false;
                arg2.printStackTrace();

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, final File arg2) {
                Log.v(TAG, "--------------->load success<---------------");

                isDownLoadBegin = false;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(arg2), "application/vnd.android.package-archive");
                mContext.startActivity(intent);
                finish();

            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                isDownLoadBegin = true;
                Log.v(TAG, bytesWritten + "----");
                progressBar.setMax(totalSize);
                progressBar.setProgress(bytesWritten);

                float curr_f = (float)bytesWritten / (1024 * 1024) ;
                float max_f = (float)totalSize / (1024 * 1024);

                currLengthTxv.setText(df.format(curr_f) + "");
                maxLengthTxv.setText(" / " + df.format(max_f) + "M");
            }
        });


    }

    private void startDownload2() {

        if(TextUtils.isEmpty(updatePath)) {

            Toast.makeText(mContext, "download path error!", Toast.LENGTH_SHORT).show();

            return;
        }

        if(isDownLoadBegin) {

            Toast.makeText(mContext, "正在下载", Toast.LENGTH_SHORT).show();
            return;

        }

        isDownLoadBegin = true;

        Uri uri = Uri.parse(updatePath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(mContext,"zhiwang_download","release_"+ versionStr +".apk");
        downloadId = downloadManager.enqueue(request);

        SharedPreferences sp = mContext.getSharedPreferences("download_ids", MODE_PRIVATE);
        sp.edit().putLong("load_apk_id", downloadId).commit();
        sp.edit().putString("load_apk_version", versionStr).commit();

        Toast.makeText(mContext, "已开始下载", Toast.LENGTH_SHORT).show();

        mTimer = new Timer();
        mTimer.schedule(ttask, 0, 10);

    }

    private void loadProgressQuery() {

        final DecimalFormat df = new DecimalFormat("#.00");
        DownloadManager.Query downloadQuery = new DownloadManager.Query();
        downloadQuery.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(downloadQuery);
        if (cursor != null && cursor.moveToFirst()) {
            int fileName = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            int fileUri = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
            String fn = cursor.getString(fileName);
            String fu = cursor.getString(fileUri);

            int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

            // 下载的文件总大小
            int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);

            // 截止目前已经下载的文件总大小
            int bytesDownloadSoFar = cursor.getInt(bytesDownloadSoFarIndex);

            progressBar.setMax(totalSizeBytes);
            progressBar.setProgress(bytesDownloadSoFar);

            float curr_f = (float)bytesDownloadSoFar / (1024 * 1024) ;
            float max_f = (float)totalSizeBytes / (1024 * 1024);

            currLengthTxv.setText(df.format(curr_f) + "");
            maxLengthTxv.setText(" / " + df.format(max_f) + "M");

            cursor.close();

            if(bytesDownloadSoFar == totalSizeBytes) {

                mTimer.cancel();
                isDownLoadBegin = false;
                finish();

            }

        }

    }

    private TimerTask ttask = new TimerTask() {
        @Override
        public void run() {

            mHandler.sendEmptyMessage(0);

        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            loadProgressQuery();

        }
    };

    @Override
    protected void onStop() {

        if(mTimer != null) {

            mTimer.cancel();

        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {

        if(isDownLoadBegin) {

            Toast.makeText(mContext, R.string.update_load_background, Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    UpdateDialogActivity.super.onBackPressed();

                }

            }, 1000);

        } else {

            super.onBackPressed();

        }


    }
}

package com.zhiwang123.mobile.phone.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by ty on 2017/8/8.
 */

public class ApkLoadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            SharedPreferences sp = context.getSharedPreferences("download_ids", Context.MODE_PRIVATE);
            long downloadId = sp.getLong("load_apk_id", (long)(Math.random()*1000000));
            String versionNumber = sp.getString("load_apk_version", "");
            long downloaded_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(downloadId == downloaded_id && !TextUtils.isEmpty(versionNumber)){

                String path = context.getExternalFilesDir("zhiwang_download") + "/release_"+ versionNumber +".apk";

                Log.i("receiver : ", "download success with path : " + path);

                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                context.startActivity(install);

            }

        }

    }

}
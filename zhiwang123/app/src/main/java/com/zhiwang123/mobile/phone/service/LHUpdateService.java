package com.zhiwang123.mobile.phone.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

public class LHUpdateService extends Service {

	private static final String TAG = "LHUpdateService";
	
	private String mUpdtePath;
	private static final String EXTERNALFILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"zhiwang_app_update_file";
	
	@Override
	public void onCreate() {
		
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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null) return super.onStartCommand(intent, flags, startId);
		
		mUpdtePath = intent.getStringExtra("update_path");
		
		if(mUpdtePath != null) {
			AlertDialog alert = new AlertDialog.Builder(this)
			.setCancelable(false)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("提示")
			.setMessage("\n检测到新版本，是否需要升级？\n")
			.setPositiveButton("确定", mPositiveButtonOnClick)
			.setNegativeButton("取消", mNegativeButtonOnClick)
			.create();
			alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			alert.show();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	DialogInterface.OnClickListener mPositiveButtonOnClick = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			popUpateDownloadDialog();
		}
	};
	
//	DialogInterface.OnClickListener mPositiveInstallButtonOnClick = new DialogInterface.OnClickListener() {
//		
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			Intent intent = new Intent(Intent.ACTION_VIEW);  
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        intent.setDataAndType(Uri.fromFile(arg2), "application/vnd.android.package-archive");  
//	        LHUpdateService.this.startActivity(intent);
//		}
//	};
	
	public void popUpateDownloadDialog() {
		
		final String path = EXTERNALFILEPATH + File.separator + "littleHelper.apk";
		final ProgressDialog pd = new ProgressDialog(this);
		pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setIcon(android.R.drawable.ic_dialog_alert);
		pd.setTitle("更新应用");
		pd.setMessage("正在下载");
		pd.show();
		
		Log.v(TAG, "update file url" + mUpdtePath);
		
		File dir = new File(EXTERNALFILEPATH);
        File file = new File(path);
		if(!dir.exists()) dir.mkdirs();
        if (file.exists()) file.delete();
        
        try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		AsyncHttpClient httpClient = new AsyncHttpClient();

		httpClient.get(mUpdtePath, new FileAsyncHttpResponseHandler(file) {

			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
				Log.v(TAG, ">---------------load failed---------------<");
				arg2.printStackTrace();
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, final File arg2) {
				Log.v(TAG, "--------------->load success<---------------");

				AlertDialog alert = new AlertDialog.Builder(LHUpdateService.this)
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示")
				.setMessage("\n应用下载完成是否安装？\n")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW);  
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        intent.setDataAndType(Uri.fromFile(arg2), "application/vnd.android.package-archive");  
				        LHUpdateService.this.startActivity(intent);
					}
				})
				.setNegativeButton("否", null)
				.create();
		
				alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				alert.show();
				
		        if(pd.isShowing()) pd.dismiss();
			}
			
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				Log.v(TAG, bytesWritten + "----");
				pd.setMax(totalSize);
				pd.setProgress(bytesWritten);
			}
		});
		
		
	}
	
	DialogInterface.OnClickListener mNegativeButtonOnClick = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

}

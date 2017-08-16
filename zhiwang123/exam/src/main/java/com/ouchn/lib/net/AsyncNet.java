package com.ouchn.lib.net;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.ouchn.lib.handler.BaseJsonResponseHandler.TypeInfoes;
import com.ouchn.lib.handler.CrtvuJsonResponseHandler;

public class AsyncNet {

	private static final String TAG = "AsyncNet";
	
	private AsyncNet() {}
	
	private static AsyncNet self = new AsyncNet();
	
	public static AsyncNet getInstance() {
		return self;
	}
	
	public void doGet(Context context, String url, final AsyncNetCallback callback) {
		doGet(context, url, callback, TypeInfoes.UNKNOWN);
	}
	
	public void doGet(Context context, String url, final AsyncNetCallback callback, TypeInfoes typeInfo) {
		new AsyncHttpClient().get(context, url, new CrtvuJsonResponseHandler(callback, typeInfo));
	}
	
	public void doPost(Context context, String url, RequestParams params, final AsyncNetCallback callback) {
		doPost(context, url, params, callback, TypeInfoes.UNKNOWN);
	}

	public void doPost(Context context, String url, RequestParams params,final AsyncNetCallback callback, TypeInfoes typeInfo) {
		new AsyncHttpClient().post(context, url, params, new CrtvuJsonResponseHandler(callback, typeInfo));
	}
	
}
package com.ouchn.lib.handler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.ouchn.lib.entity.ResultObject;
import com.ouchn.lib.net.AsyncNetCallback;


public class CrtvuJsonResponseHandler extends BaseJsonResponseHandler {
	
	private static final String TAG = "ouchnJsonResponseHandler";

	private AsyncNetCallback mCallback;
	
	public CrtvuJsonResponseHandler(AsyncNetCallback callback) {
		mCallback = callback;
	}
	
	public CrtvuJsonResponseHandler(AsyncNetCallback callback, TypeInfoes typeInfo) {
		super(typeInfo);
		mCallback = callback;
	}
	
	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		if(mCallback != null) {
			Log.v(TAG, "------------------------------:<" + type.getDescription() + " failure, status code :" + statusCode);
			mCallback.onResponseFailure(statusCode, headers, throwable, new ResultObject<String>(responseString), type);
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
		if(mCallback != null) {
			Log.v(TAG, "------------------------------:<" + type.getDescription() + " failure, status code :" + statusCode);
			mCallback.onResponseFailure(statusCode, headers, throwable, new ResultObject<JSONArray>(errorResponse), type);
		}
	}


	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		if(mCallback != null) {
			Log.v(TAG, "------------------------------:>" + type.getDescription() + " success, status code :" + statusCode);
			mCallback.onResponseSuccess(statusCode, headers, new ResultObject<JSONArray>(response), type);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String responseString) {
		if(mCallback != null) {
			Log.v(TAG, "------------------------------:<" + type.getDescription() + " failure, status code :" + statusCode);
			mCallback.onResponseSuccess(statusCode, headers, new ResultObject<String>(responseString), type);
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		if(mCallback != null) {
			Log.v(TAG, "------------------------------:<" + type.getDescription() + " failure, status code :" + statusCode);
			mCallback.onResponseFailure(statusCode, headers, throwable, new ResultObject<JSONObject>(errorResponse), type);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		if(mCallback != null) {
			Log.v(TAG, "------------------------------:>" + type.getDescription() + " success, status code :" + statusCode);
			mCallback.onResponseSuccess(statusCode, headers, new ResultObject<JSONObject>(response), type);
		}
	}
	
}

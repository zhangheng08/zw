package com.ouchn.lib.activity;

import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.ouchn.lib.entity.ResultObject;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.BaseJsonResponseHandler.TypeInfoes;
import com.ouchn.lib.handler.BaseTask;
import com.ouchn.lib.handler.BaseTask.TaskResult;
import com.ouchn.lib.net.AsyncNetCallback;

import org.apache.http.Header;

public abstract class BaseActivity extends FragmentActivity implements AsyncNetCallback, AsyncExecuteCallback {

	@Override
	public void onResponseSuccess(int statusCode, Header[] headers, ResultObject<?> result, TypeInfoes typeInfo) {
		onAsyncRespSuccess(statusCode, headers, result, typeInfo);
	}

	@Override
	public void onResponseFailure(int statusCode, Header[] headers, Throwable throwable, ResultObject<?> result, TypeInfoes typeInfo) {
		onAsyncRespFailure(statusCode, headers, throwable, result, typeInfo);
	}

	@Override
	public void pre(Object... objs) {
		
	}

	@Override
	public TaskResult async(int what, BaseTask bastTask) {
		return null;
	}

	@Override
	public void post(Message msg) {
		
	}

	public void onAsyncRespSuccess(int statusCode, Header[] headers, ResultObject<?> result, TypeInfoes typeInfo) {
		onAsyncRespSuccess(statusCode, result, typeInfo);
	}

	public void onAsyncRespFailure(int statusCode, Header[] headers, Throwable throwable, ResultObject<?> result, TypeInfoes typeInfo) {
		onAsyncRespFailure(statusCode, result, typeInfo);
	}
	
	public abstract void onAsyncRespSuccess(int statusCode, ResultObject<?> result, TypeInfoes typeInfo);
	public abstract void onAsyncRespFailure(int statusCode, ResultObject<?> result, TypeInfoes typeInfo);

}

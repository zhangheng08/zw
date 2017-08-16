package com.ouchn.lib.net;

import org.apache.http.Header;

import com.ouchn.lib.entity.ResultObject;
import com.ouchn.lib.handler.BaseJsonResponseHandler.TypeInfoes;

public interface AsyncNetCallback {

	public void onResponseSuccess(int statusCode, Header[] headers, ResultObject<?> result, TypeInfoes typeInfo);
	
	public void onResponseFailure(int statusCode, Header[] headers, Throwable throwable, ResultObject<?> result, TypeInfoes typeInfo);
	
}

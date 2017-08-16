package com.ouchn.lib.handler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class BaseJsonResponseHandler extends JsonHttpResponseHandler {
	
	private static final String TAG = "BaseJsonResponseHandler";
	
	public TypeInfoes type = TypeInfoes.UNKNOWN;
	
	public BaseJsonResponseHandler() {
		
	}
	
	public BaseJsonResponseHandler(TypeInfoes type) {
		this.type = type;
	}
	
	public void setLoadType(TypeInfoes type) {
		this.type = type;
	}
	
	@Override
    public void onRetry(int retryNo) {
	}
	
	 @Override
	 public void onStart() {
	 }

	@Override
	protected Object parseResponse(byte[] responseBody) throws JSONException {
		return super.parseResponse(responseBody);
	}

	@Override
	public void onCancel() {
		super.onCancel();
	}

	public void onFinish() {
		super.onFinish();
	}
	
	@Override
	public abstract void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);

	@Override
	public abstract void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse);

	@Override
	public abstract void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);
	
	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, JSONArray response);

	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, String responseString);
	
	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, JSONObject response);
	
	protected void resetLoadType() {
		type = TypeInfoes.UNKNOWN;
	}
	
	public static enum TypeInfoes {

		ASYNC_LOAD_FOCUS("加载焦点图信息"),
		ASYNC_LOAD_CATEGROY("加载分类信息"),
		ASYNC_LOAD_VERSION("加载版本信息"),
		ASYNC_LOAD_CATEGROYLIST("加载分类对应的列表"),
		ASYNC_LOAD_SEARCHLIST("加载搜索的列表"),
		ASYNC_LOAD_FAVORITE_COUNT("加载搜藏个数"),
		ASYNC_LOAD_QUESTION_DEPOT("加载题库信息"),
		ASYNC_LOAD_ALLQUESTION("加载所有的试题"),
		ASYNC_SET_FAVORITE("设置收藏信息"),
		ASYNC_LOAD_FAVORITE("加载收藏信息"),
		ASYNC_LOAD_FEEDBACK("加载反馈信息"),
		SEND_LOGIN_INFO("发送登录信息"),
		SEND_REGIST_INFO("发送注册信息"),
		SEND_FOCUS_INFO("发送焦点图信息"),
		SEND_CATEGROY_INFO("发送分类信息"),
		SEND_VERSION_INFO("发送版本信息"),
		ASYNC_SUBMIT_FEEDBACK("提交反馈内容"),
		UNKNOWN("未知的操作");
		
		private String description;
		
		private TypeInfoes(String str) {
			description = str;
		}
		
		public String getDescription() {
			return description;
		}
		
	}

}

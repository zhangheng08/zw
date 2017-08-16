package com.ouchn.lib.config;

import java.util.HashMap;

import android.util.Log;

public class Config {
	
	private static final String TAG = "Config";
	
	public static final String fakeToken = "17F15623-2D37-4F4E-B034-A46C669FF7F4";
	
	public static final String BASEURL = /*"http://10.191.37.47:8080/QuestionDepotServer/data/";*/      "http://ksapi.lndx.edu.cn";
	public static final String URLTYPE_LOGIN = "/User/Login";
	public static final String URLTYPE_QUESTIONDEPOT = /*"question_depot.json";*/                       "/Exam/GetExamBanks";
	public static final String URLTYPE_QUESTIONS = /*"question_list.json";*/                            "/Exam/GetExamBankQuestions";
	
	public static String token;
	
	public static String getUrl(String type) {
		String url = BASEURL + type;
		Log.v(TAG, "request for url >>>>>>> " + url);
		return url;
	}
	
	public static String getUrl(String type, String[] keys, String[] values) {
		String url = BASEURL + type;
		
		if(keys == null || values == null || keys.length == 0 || values.length == 0 || keys.length != values.length) {
			return getUrl(type);
		}
		
		StringBuffer urlBuffer = new StringBuffer(url);
		urlBuffer.append("?");
		for(int i = 0; i < keys.length; i ++) {
			urlBuffer.append(keys[i] + "=" + values[i]);
			if(i < keys.length - 1) {
				urlBuffer.append("&");
			}
		}
		
		Log.v(TAG, "request for url >>>>>>> " + urlBuffer.toString());
		return urlBuffer.toString();
	}
	
	
	public static final String LARGE_TRANSACTION_GOBACK = "back_large_transaction_key";
	public static final String LARGE_TRANSACTION_GOTO   = "goto_large_transaction_key";
	public static HashMap<String, Object> tempCache = new HashMap<String, Object>();

}

package com.ouchn.lib;

import android.app.Application;
import android.content.Context;

public class OuchnExamApplication extends Application {

	private static OuchnExamApplication mContext;
	
	@Override
	public void onCreate() {
		mContext = this;
		super.onCreate();
	}
	
	public static Context getAppContext() {
		return mContext;
	}
	
}

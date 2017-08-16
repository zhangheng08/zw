package com.ouchn.lib.util;

public class BaseConfigUitl {

	public static final int L = 24;
	public static final int M = 21;
	public static final int S = 18;
	
	public static int Aa = S;
	
	public static long mUIThreadId;
	
	public static void setUIThreadId(long id) {
		mUIThreadId = id;
	}
	
	public static boolean isInUiThread() {
		long id = Thread.currentThread().getId();
		if(mUIThreadId == id) {
			return true;
		}
		return false;
	}
	
}

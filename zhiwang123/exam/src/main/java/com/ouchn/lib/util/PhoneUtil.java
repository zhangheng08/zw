package com.ouchn.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StatFs;
import android.util.DisplayMetrics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
	
	private static PhoneUtil self = new PhoneUtil();
	
	private PhoneUtil() {}
	
	public static PhoneUtil getInstance() {
		return self;
	}
	
	public static float getDip(Activity context) {
		DisplayMetrics dm = new DisplayMetrics(); 
        context.getWindowManager().getDefaultDisplay().getMetrics(dm); 
        dm = context.getResources().getDisplayMetrics();
        float density = dm.density; 
		return density;
	}
	
	public static float getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics(); 
        context.getWindowManager().getDefaultDisplay().getMetrics(dm); 
        dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	
	public static float[] getScreenSize(Activity context) {
		DisplayMetrics dm = new DisplayMetrics(); 
        context.getWindowManager().getDefaultDisplay().getMetrics(dm); 
        dm = context.getResources().getDisplayMetrics();
		float width = dm.widthPixels;
		float height = dm.heightPixels;
		return new float[] {width, height};
	}
	
	public static boolean getNetWorkState(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();  
        if(networkInfo == null || !networkInfo.isAvailable())  
        {  
        	return false;
        }  
        else   
        {  
            return true; 
        } 
	}
	
	public static String getAppVersionName(Context context) {
		
		String versionName = "";  
		
	    try {  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	    	e.printStackTrace();
	    }   
		
		return versionName;
	}
	
	public static int getAppVersionCode(Context context) {
		
		int versionCode = 0;  
		
	    try {  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionCode = pi.versionCode;  
	    } catch (Exception e) {  
	    	e.printStackTrace();
	    }   
		
		return versionCode;
	}
	    /**
	     * ��֤����
	     * @param email
	     * @return
	     */
	public static boolean checkEmail(String email){
	    boolean flag = false;
	    try{
	            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	            Pattern regex = Pattern.compile(check);
	            Matcher matcher = regex.matcher(email);
	            flag = matcher.matches();
	        }catch(Exception e){
	            flag = false;
	        }
	    return flag;
	}
	     

	public static boolean checkMobileNumber(String mobileNumber){
	    boolean flag = false;
	    try{
	            Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
	            Matcher matcher = regex.matcher(mobileNumber);
	            flag = matcher.matches();
	        }catch(Exception e){
	            flag = false;
	        }
	    return flag;
	}

	public static long getAvailSpace(String path){

		StatFs statfs = new StatFs(path);
		long size = statfs.getBlockSize();//获取分区的大小
		long count = statfs.getAvailableBlocks();//获取可用分区块的个数
		return size*count;
	}
	
}

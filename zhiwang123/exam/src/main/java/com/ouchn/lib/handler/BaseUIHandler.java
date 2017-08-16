package com.ouchn.lib.handler;

import android.os.Handler;
import android.os.Message;

public class BaseUIHandler extends Handler {
	
	public BaseUIHandler() {
		super();
	}
	
	public BaseUIHandler(Callback callback) {
		super(callback);
	}
	
	@Override
	public void handleMessage(Message msg) {
		BaseTask task = (BaseTask) msg.obj;
		task.post(msg);
	}
	
}

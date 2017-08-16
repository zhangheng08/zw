package com.ouchn.lib.handler;

import android.os.Message;

public class BaseTask implements Runnable {

	private int what;
	private BaseUIHandler uiHandler;
	private AsyncExecuteCallback callback;
	
	public TaskResult result;
	
	public Object param;
	
	public BaseTask(int what, AsyncExecuteCallback callback) {
		init(what, callback);
	}
	
	public BaseTask(int what, Object param, AsyncExecuteCallback callback) {
		this.param = param;
		init(what, callback);
	}
	
	public void init(int what, AsyncExecuteCallback callback) {
//		if(!BaseConfigUitl.isInUiThread()) {
//			throw new RuntimeException("this create Task operation must in UIThread!");
//		}
		this.what = what;
		this.uiHandler = new BaseUIHandler();
		this.callback = callback;
	}
	
	final public void pre(Object...objects) {
		callback.pre(objects);
	}
	
	final public TaskResult async() {
		return callback.async(what, this);
	}
	
	final public void post(Message msg) {
		callback.post(msg);
	}
	
	@Override
	final public void run() {
		result = async();
		Message msg = uiHandler.obtainMessage();
		msg.what = what;
		msg.obj = this;
		msg.sendToTarget();
	}
	
	public class TaskResult extends Object {
		
		private Object result;
		
		public TaskResult() {
			
		}
		
		public TaskResult(Object result) {
			this.result = result;
		}
		
		public void setResult(Object obj) {
			result = obj;
		}
		
		public Object getResult() {
			return result;
		}
		
	}
	
}

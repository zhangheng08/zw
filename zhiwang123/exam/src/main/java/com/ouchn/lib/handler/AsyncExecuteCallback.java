package com.ouchn.lib.handler;

import android.os.Message;

import com.ouchn.lib.handler.BaseTask.TaskResult;

public interface AsyncExecuteCallback {
	
	public void pre(Object... objs);
	public TaskResult async(int what, BaseTask baseTask);
	public void post(Message msg);
	
}

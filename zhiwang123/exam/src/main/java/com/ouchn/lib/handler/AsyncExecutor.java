package com.ouchn.lib.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AsyncExecutor {
	
	private static final AsyncExecutor self = new AsyncExecutor();
	private AsyncExecutor() {}
	public static AsyncExecutor getInstance() {
		return self;
	}
	
	public enum ExecType {concurrent, synchronous};

	private ExecutorService exec;
	
	public void execMuliteTask(BaseTask baseTask) {
		execMuliteTask(new BaseTask[] {baseTask}, ExecType.synchronous);
	}
	
	public void execMuliteTask(BaseTask[] baseTasks) {
		execMuliteTask(baseTasks, ExecType.concurrent);
	}
	
	public void execMuliteTask(BaseTask[] baseTasks, ExecType type, Object... params) {
		if(baseTasks == null) return; 
		
		switch(type) {
		case concurrent:
			exec = Executors.newCachedThreadPool();
			break;
		case synchronous:
			exec = Executors.newSingleThreadExecutor();
			break;
		}
		
		for(BaseTask task : baseTasks) {
			task.pre(params);
			exec.execute(task);
		}
	}
	
	public void cancle() {
		if(exec != null) {
			exec.shutdownNow();
		}
	}
	
}

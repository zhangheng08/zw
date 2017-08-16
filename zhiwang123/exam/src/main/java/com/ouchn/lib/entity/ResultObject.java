package com.ouchn.lib.entity;

public class ResultObject<T extends Object> {

	public T result;
	
	public ResultObject(T t) {
		result = t;
	}
	
	public void setResult(T t) {
		this.result = t;
	}
	
	public T getResult() {
		return result;
	}

}

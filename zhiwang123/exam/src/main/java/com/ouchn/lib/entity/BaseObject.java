package com.ouchn.lib.entity;

import java.io.Serializable;

import org.json.JSONObject;

public abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = -3232537009648404733L;
	
	public abstract ParseTarget<?> parseJSON(JSONObject json);
	
	protected abstract ParseTarget<?> parseJSON(BaseObject target, JSONObject json);
	
	public static ParseTarget<?> toParseJSON(BaseObject target, JSONObject json) {
		
		return target.parseJSON(target, json);
		
	}
	
	public class ParseTarget<T extends BaseObject> {
		
		public T result;
		
		public void setResult(T t) {
			this.result = t;
		}
		
		public T getResult() {
			return result;
		}
		
	}

}

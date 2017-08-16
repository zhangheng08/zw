package com.ouchn.lib.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.ouchn.lib.entity.BaseObject.ParseTarget;

import android.util.Log;

public class ResponseMessage extends BaseObject {

	private static final long serialVersionUID = -8934429003492923518L;
	private static final String USERACCESSTOKENINFO = "UserAccessTokenInfo";
	private static final String EXPIRES = "Expires";
	private static final String MESSAGE = "Message";
	private static final String STATE = "State";
	
	private String userAccessTokenInfo;
	private String expires;
	private String message;
	private boolean state;
	
	public String getUserAccessTokenInfo() {
		return userAccessTokenInfo;
	}
	public void setUserAccessTokenInfo(String userAccessTokenInfo) {
		this.userAccessTokenInfo = userAccessTokenInfo;
	}
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	
	
	@Override
	protected ParseTarget<ResponseMessage> parseJSON(BaseObject target, JSONObject json) {
		if(json == null) return null; 
		
		String userAccessTokenInfo = "none";
		String expires = "none";
		String message = "none";
		boolean state = false;
		
		ParseTarget<ResponseMessage> result = null;
		ResponseMessage response = null;
		
		try {
			if(json.has(USERACCESSTOKENINFO)) userAccessTokenInfo = json.getString(USERACCESSTOKENINFO); 
			if(json.has(EXPIRES)) expires = json.getString(EXPIRES);
			if(json.has(MESSAGE)) message = json.getString(MESSAGE);
			if(json.has(STATE)) state = json.getBoolean(STATE);
			
			response = (ResponseMessage) target;
			response.setExpires(expires);
			response.setMessage(message);
			response.setState(state);
			response.setUserAccessTokenInfo(userAccessTokenInfo);
			
			result = new ParseTarget<ResponseMessage>();
			result.setResult(response);
		} catch(JSONException e) {
			Log.e("parse error:", "JSONException");
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public ParseTarget<ResponseMessage> parseJSON(JSONObject json) {
		return parseJSON(new ResponseMessage(), json);
	}
	
}

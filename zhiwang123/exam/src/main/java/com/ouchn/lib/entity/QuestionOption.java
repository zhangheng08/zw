package com.ouchn.lib.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionOption extends BaseObject {
	
	private static final long serialVersionUID = -6009036600956728906L;

	private static final String TAG = "QuestionOption";
	
	private static final String ID = "Id";
	private static final String CONTENT = "Content";
	private static final String IFRIGHTANSWER = "IfRightAnswer";
	private static final String ORDERNUM = "OrderNum";
	private static final String EXAM_BANKQUESTIONID = "Exam_BankQuestionId";
	
	private String id;
	private String content;
	private boolean ifRightAnswer;
	private int orderNum;
	private String questionId;

	private String orderString;
	
	public static final QuestionOption I = new QuestionOption();
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public boolean isIfRightAnswer() {
		return ifRightAnswer;
	}



	public void setIfRightAnswer(boolean ifRightAnswer) {
		this.ifRightAnswer = ifRightAnswer;
	}



	public int getOrderNum() {
		return orderNum;
	}



	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}



	public String getQuestionId() {
		return questionId;
	}



	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public void setOrderString(String orderString) {
		this.orderString = orderString;
	}

	public String getOrderString() {
		return orderString;
	}

	@Override
	protected ParseTarget<QuestionOption> parseJSON(BaseObject target, JSONObject json) {
		if(json == null) return null; 
		
		String id = "none";
		String content = "none";
		boolean ifRightAnswer = false;
		int orderNum = -1;
		String questionId = "none";
		
		ParseTarget<QuestionOption> result = null;
		QuestionOption response = null;
		
		try {
			
			if(json.has(ID)) id = json.getString(ID);
			if(json.has(CONTENT)) content = json.getString(CONTENT);
			if(json.has(IFRIGHTANSWER)) ifRightAnswer = json.getBoolean(IFRIGHTANSWER);
			if(json.has(ORDERNUM)) orderNum = json.getInt(ORDERNUM);
			if(json.has(questionId)) questionId = json.getString(EXAM_BANKQUESTIONID);
			
			response = (QuestionOption) target;
			response.setId(id);
			response.setContent(content);
			response.setIfRightAnswer(ifRightAnswer);
			response.setOrderNum(orderNum);
			response.setQuestionId(questionId);
			
			result = new ParseTarget<QuestionOption>();
			result.setResult(response);
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public ParseTarget<QuestionOption> parseJSON(JSONObject json) {
		return parseJSON(new QuestionOption(), json);
	}

}

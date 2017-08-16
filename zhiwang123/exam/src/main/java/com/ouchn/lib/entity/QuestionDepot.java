package com.ouchn.lib.entity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.ouchn.lib.db.exam.ExamDatabaseHelper;

import android.util.Log;

public class QuestionDepot extends BaseObject {

	private static final long serialVersionUID = 7981370746414737084L;
	
	public static final int ANSWERING = 0x0;
	public static final int NOFINISH = 0x1;
	public static final int FINISHED = 0x2;
	public static final int IDEL = 0x3;
	
	public int currentState = 0x3;
	
	private static final String TAG = "QuestionsDepot";
	private static final String ID = "Id";
	private static final String NAME = "Name";
	private static final String DESCRIPTION = "Description";
	private static final String USE_TYPE = "UseType";
	
	private String id;
	private String name;
	private String createDate;
	private int useType;
	
	private String questionCount = "";
	private ArrayList<Question> questionList;
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCreateDate() {
		return createDate;
	}



	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}



	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}

	public String getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(String questionCount) {
		this.questionCount = questionCount;
	}
	
	public ArrayList<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(ArrayList<Question> questionList) {
		this.questionList = questionList;
	}

	@Override
	protected ParseTarget<QuestionDepot> parseJSON(BaseObject target, JSONObject json) {
		if(json == null) return null; 
		
		String id = "none";
		String name = "none";
		String reateDate = "none";
		int    useType = -1;
		
		ParseTarget<QuestionDepot> result = null;
		QuestionDepot response = null;
		
		try {
			if(json.has(ID)) id = json.getString(ID); 
			if(json.has(NAME)) name = json.getString(NAME);
			if(json.has(DESCRIPTION)) createDate = json.getString(DESCRIPTION);
			if(json.has(USE_TYPE)) useType = json.getInt(USE_TYPE);
			
			response = (QuestionDepot) target;
			response.setId(id);
			response.setCreateDate(reateDate);
			response.setName(name);
			response.setUseType(useType);
			
			result = new ParseTarget<QuestionDepot>();
			result.setResult(response);
			
		} catch(JSONException e) {
			Log.e(TAG + "parse error:", "JSONException");
			e.printStackTrace();
		}
		
		return result;
	}



	@Override
	public ParseTarget<QuestionDepot> parseJSON(JSONObject json) {
		return parseJSON(new QuestionDepot(), json);
	}

}

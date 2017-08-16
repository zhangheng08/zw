package com.ouchn.lib.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Question extends BaseObject {
	
	private static final long serialVersionUID = -8839509691093729297L;
	
	private static final String TAG = "Question";
	
	private static final String ID = "Id";
	private static final String CONTENT = "Content";
	private static final String QUESTIONTYPE = "QuestionType";
	private static final String QSCORE = "QScore";
	private static final String QTYPESTRING = "QtypeString";
	private static final String QSCORETOSTRING = "QScoreToString";
	private static final String QSERIALNUMBER = "QSerialnumber";
	private static final String QUESTIONOPTIONLIST = "QuestionOptionList";
	
	private String depotId;
	private String id;
	private String content;
	private int questionType;
	private int qScore;
	private String qTypeString;
	private String qScoreToString;
	private int qSerialNumber;
	private QuestionOption[] options;
	
	public static final String _SEPARATOR = " - ";
	public AnswerState mAnswerState = new AnswerState();
	
	public String getDeoptId() {
		return depotId;
	}
	
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	
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

	public int getqScore() {
		return qScore;
	}

	public void setqScore(int qScore) {
		this.qScore = qScore;
	}

	public String getqScoreToString() {
		return qScoreToString;
	}

	public void setqScoreToString(String qScoreToString) {
		this.qScoreToString = qScoreToString;
	}

	public int getqSerialNumber() {
		return qSerialNumber;
	}

	public void setqSerialNumber(int qSerialNumber) {
		this.qSerialNumber = qSerialNumber;
	}

	public QuestionOption[] getOptions() {
		return options;
	}

	public void setOptions(QuestionOption[] options) {
		this.options = options;
	}

	@Override
	public ParseTarget<Question> parseJSON(JSONObject json) {
		return parseJSON(new Question(), json);
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public String getqTypeString() {
		return qTypeString;
	}

	public void setqTypeString(String qTypeString) {
		this.qTypeString = qTypeString;
	}
	
	@Override
	protected ParseTarget<Question> parseJSON(BaseObject target, JSONObject json) {
		if(json == null) return null; 
				
		String id = "none";
		String content = "none";
		int questionType = -1;
		int qScore = -1;
		String qTypeString = "none";
		String qScoreToString = "none";
		int qSerialNumber = -1;
		
		QuestionOption[] options = null;
		ParseTarget<Question> result = null;
		Question response = null;
		
		try {
			if(json.has(ID)) id = json.getString(ID);
			if(json.has(CONTENT)) content = json.getString(CONTENT);
			if(json.has(QUESTIONTYPE)) questionType = json.getInt(QUESTIONTYPE);
			if(json.has(QSCORE)) qScore = json.getInt(QSCORE);
			if(json.has(QTYPESTRING)) qTypeString = json.getString(QTYPESTRING);
			if(json.has(QSCORETOSTRING)) qScoreToString = json.getString(QSCORETOSTRING);
			if(json.has(QSERIALNUMBER)) qSerialNumber = json.getInt(QSERIALNUMBER);
			
			JSONArray jsonArray = json.getJSONArray(QUESTIONOPTIONLIST);
			if(jsonArray != null && jsonArray.length() != 0) {
				options = new QuestionOption[jsonArray.length()];
				for(int i = 0; i < jsonArray.length(); i ++) {
					JSONObject jo = jsonArray.getJSONObject(i);
					options[i] = QuestionOption.I.parseJSON(jo).getResult();
				}
			}
			
			response = (Question) target;
			response.setId(id);
			response.setQuestionType(questionType);
			response.setContent(content);
			response.setOptions(options);
			response.setqScore(qScore);
			response.setqScoreToString(qScoreToString);
			response.setqSerialNumber(qSerialNumber);
			
			result = new ParseTarget<Question>();
			result.setResult(response);
			
			switch(response.getQuestionType()) {
			case QuestionType.MATCHING:
				
				QuestionOption[] tempMatchOptions = resetOrder(options);
				
				ArrayList<String> keys = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<String>();
				
				for(int i = 0; i < tempMatchOptions.length; i ++) {
					if(i % 2 == 0) {
						keys.add(tempMatchOptions[i].getContent());
					} else {
						values.add(tempMatchOptions[i].getContent());
					}
				}
				
				for(int i = 0; i < keys.size(); i ++) {
					mAnswerState.matchRightAnswers.put(keys.get(i), values.get(i));
				}
				
				Log.v(TAG, "----------------------:-)" + mAnswerState.matchRightAnswers.toString());
				break;
			case QuestionType.SORT:
				
				QuestionOption[] tempOptions = resetOrder(options);
				
				for(int i = 0; i < tempOptions.length; i ++) {
					mAnswerState.sortRightAnswers.add(new String[] {"0", tempOptions[i].getContent()});
				}
				
				tempOptions = null;
				break;
			case QuestionType.SINGLE:
			case QuestionType.JUDGMENT:
			case QuestionType.MULTIPLE:
				
				for(int i = 0; i < options.length; i ++) {
					if(options[i].isIfRightAnswer()) {
						mAnswerState.indexRightAnswers.add(options[i].getContent());
					} 
				}
				
				break;
			}

			
		} catch(JSONException e) {
			Log.e(TAG + " parse error:", "JSONException");
			e.printStackTrace();
		}
		
		return result;
	}
	
	private QuestionOption[] resetOrder(QuestionOption[] options) {
		
		QuestionOption[] tempOptions = options.clone();
		
		for(int i = 0; i < tempOptions.length - 1; i ++) {
			for(int k = 0 ; k < tempOptions.length - i - 1; k ++) {
				QuestionOption temp;
				if(tempOptions[k].getOrderNum() > tempOptions[k + 1].getOrderNum()) {
					temp = tempOptions[k];
					tempOptions[k] = tempOptions[k + 1];
					tempOptions[k + 1] = temp;
				}
			}
		}
		
		return tempOptions;
	}
	
	public class AnswerState implements Serializable {
		
		public HashSet<String> indexAnswers = new HashSet<String>();
		public HashSet<String> indexRightAnswers = new HashSet<String>();
		
		public HashMap<String, String> matchAnswers = new HashMap<String, String>();
		public HashMap<String, String> matchRightAnswers = new HashMap<String, String>();
		
		public ArrayList<String[]> sortAnswers = new ArrayList<String[]>();
		public ArrayList<String[]> sortRightAnswers = new ArrayList<String[]>();
		
		public boolean isAnswered() {
			
			//TODO
			if(questionType == QuestionType.ESSAY || questionType == QuestionType.FILL_BLANK) {
				return true;
			}
			//TODO
			
			return indexAnswers.size() != 0 || matchAnswers.size() != 0 || sortAnswers.size() != 0 ? true : false;
		}
		
		public ArrayList<String> getAnswer(boolean right) {
			ArrayList<String> ret = new ArrayList<String>();
			
			StringBuffer temp = null;
			
			switch(questionType) {
			case QuestionType.MATCHING:
				Set<String> rightAnswerKeyset = right ? matchRightAnswers.keySet() : matchAnswers.keySet();
				Iterator<String> imatch = rightAnswerKeyset.iterator();
				while(imatch.hasNext()) {
					temp = new StringBuffer("");
					String key = imatch.next();
					temp.append(key);
					temp.append(_SEPARATOR);
					String val = right ? matchRightAnswers.get(key) : matchAnswers.get(key);
					temp.append(val);
					ret.add(temp.toString());
				}
				break;
			case QuestionType.SORT:
				Iterator<String[]> isort = right ? sortRightAnswers.iterator() : sortAnswers.iterator();
				while(isort.hasNext()) {
					temp = new StringBuffer("");
					String[] arr = isort.next();
					Log.v(TAG, "before process it : " + arr[0] + "-" + arr[1]);
					temp.append(arr[0]);
					temp.append(_SEPARATOR);
					temp.append(arr[1]);
					ret.add(temp.toString());
				}
				break;
			case QuestionType.JUDGMENT:
			case QuestionType.SINGLE:
				Iterator<String> iindex = right ? indexRightAnswers.iterator() : indexAnswers.iterator();
				while(iindex.hasNext()) {
					temp = new StringBuffer("");
					String ans = iindex.next();
					temp.append(ans);
					ret.add(temp.toString());
				}
				break;
			case QuestionType.MULTIPLE:
				Iterator<String> imindex = right ? indexRightAnswers.iterator() : indexAnswers.iterator();
				while(imindex.hasNext()) {
					temp = new StringBuffer("");
					String ans = imindex.next();
					temp.append(ans);
					ret.add(temp.toString());
				}
				break;
			default:
				break;
			}
			return ret;
		}
		
		public String getDisplayAnswer(boolean right) {
			
			ArrayList<String> list = getAnswer(right);
			
			Log.v(TAG, "answer info is : --------------================" + list.toString());
			
			if(list == null || list.size() == 0) return "";
			
			StringBuffer ret = new StringBuffer("");
			
			switch(questionType) {
			case QuestionType.SORT:
				for(int i = 0; i < list.size(); i ++) {
					String str = list.get(i);
					String[] arr = str.split(_SEPARATOR);
					ret.append(arr[1]);
					if(i < list.size() - 1) ret.append(" , ");
				}
				break;
			case QuestionType.MATCHING:
			case QuestionType.JUDGMENT:
			case QuestionType.SINGLE:
			case QuestionType.MULTIPLE:
				ret.append(list.toString());
				break;
			default:
				break;
			}
			
			return ret.toString();
		}
		
		public String getCurrentAnswer() {
			
			return "";
		}
		
		public boolean isAnswerRight() {
			boolean ret = false;
			switch(questionType) {
			case QuestionType.MATCHING:
				
				Set<String> rightAnswerKeyset = matchRightAnswers.keySet();
				Iterator<String> i = rightAnswerKeyset.iterator();
				
				while(i.hasNext()) {
					String key = i.next();
					String val = matchRightAnswers.get(key);
					String val_ = matchAnswers.get(key);
					
					Log.v(TAG, val + "-----------" + val_ );
					
					if(val_ == null) {
						if(matchAnswers.containsKey(val)) {
							val_ = matchAnswers.get(val);
							if(!val_.equalsIgnoreCase(key)) {
								ret = false;
								break;
							} else {
								ret = true;
							}
						} else {
							ret = false;
							break;
						}
					} else {
						if(!val.equalsIgnoreCase(val_)) {
							ret = false;
							break;
						} else {
							ret = true;
						}
					}
				}
				
				break;
			case QuestionType.SORT:
				
				if(sortRightAnswers.size() != sortAnswers.size()) {
					ret = false;
					break;
				}
				Iterator<String[]> iRsort = sortRightAnswers.iterator();
				Iterator<String[]> isort = sortAnswers.iterator();
				
				while(iRsort.hasNext() && isort.hasNext()) {
					String[] rarr = iRsort.next();
					String[] arr = isort.next();
					if(!rarr[1].equalsIgnoreCase(arr[1])) {
						ret = false;
						break;
					} else {
						ret = true;
					}
				}

				break;
			case QuestionType.SINGLE:
			case QuestionType.JUDGMENT:
				
				Iterator<String> iindex = indexRightAnswers.iterator();
				
				while(iindex.hasNext()) {
					String content = iindex.next();
					if(indexAnswers.contains(content)) {
						ret = true;
						break;
					} else {
						ret = false;
					}
				}
				
				break;
			case QuestionType.MULTIPLE:
				
				Iterator<String> imindex = indexRightAnswers.iterator();
				
				ArrayList<Boolean> bos = new ArrayList<Boolean>();
				
				while(imindex.hasNext()) {
					String content = imindex.next();
					if(indexAnswers.contains(content)) {
						bos.add(true);
					} else {
						bos.add(false);
					}
				}
				
				ret = true;
				for(Boolean b : bos) {
					ret = ret && b;
				}
				
				break;
			case QuestionType.ESSAY:
			case QuestionType.FILL_BLANK:
				
				ret = true;
				break;
				
			}
			
			switch(questionType) {
			case QuestionType.JUDGMENT:
				if(!ret) {
					Log.e(TAG, "jugement 	x");
				} else {
					Log.i(TAG, "jugement	v");
				}
				break;
			case QuestionType.SORT:
				if(!ret) {
					Log.e(TAG, "sort 	x");
				} else {
					Log.i(TAG, "sort 	v");
				}
				break;
			case QuestionType.MATCHING:
				if(!ret) {
					Log.e(TAG, "matching 	x");
				} else {
					Log.i(TAG, "matching 	v");
				}
				break;
			case QuestionType.MULTIPLE:
				if(!ret) {
					Log.e(TAG, "multiple 	x");
				} else {
					Log.i(TAG, "multiple 	v");
				}
				break;
			case QuestionType.SINGLE:
				if(!ret) {
					Log.e(TAG, "single 	x");
				} else {
					Log.i(TAG, "single 	v");
				}
				break;
			}
			
			return ret;
		}
		
	}

}

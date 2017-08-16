package com.ouchn.lib.entity;

import android.content.Context;

import com.ouchn.lib.R;


public class QuestionType {

	public static final int SINGLE = 1;
	public static final int MULTIPLE = 2;
	public static final int JUDGMENT = 3;
	public static final int FILL_BLANK = 4;
	public static final int ESSAY = 5;
	public static final int MATCHING = 6;
	public static final int SORT = 7;
	
	public static String getQuestionType(int type, Context c) {
		String strType = "";
		switch(type) {
		case SINGLE:
			strType = c.getString(R.string.question_type_single);
			break;
		case MULTIPLE:
			strType = c.getString(R.string.question_type_multiple);
			break;
		case JUDGMENT:
			strType = c.getString(R.string.question_type_judgment);
			break;
		case FILL_BLANK:
			strType = c.getString(R.string.question_type_fillblank);
			break;
		case ESSAY:
			strType = c.getString(R.string.question_type_essay);
			break;
		case MATCHING:
			strType = c.getString(R.string.question_type_match);
			break;
		case SORT:
			strType = c.getString(R.string.question_type_sort);
			break;
		}
		return strType;
	}

	public static int getQuestionTypeInt(String type, Context c) {

		if(type.equals(c.getString(R.string.question_type_single))) {
			return SINGLE;
		} else if(type.equals(c.getString(R.string.question_type_multiple))) {
			return MULTIPLE;
		} else if(type.equals(c.getString(R.string.question_type_judgment))) {
			return JUDGMENT;
		} else if(type.equals(c.getString(R.string.question_type_fillblank))) {
			return FILL_BLANK;
		} else if(type.equals(c.getString(R.string.question_type_essay))) {
			return ESSAY;
		} else if(type.equals(c.getString(R.string.question_type_match))) {
			return MATCHING;
		} else if(type.equals(c.getString(R.string.question_type_sort))) {
			return SORT;
		}

		return  -1;

	}

}

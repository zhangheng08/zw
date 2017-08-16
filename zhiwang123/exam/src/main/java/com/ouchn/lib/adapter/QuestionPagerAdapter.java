package com.ouchn.lib.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ouchn.lib.R;
import com.ouchn.lib.adapter.holder.QuestionViewItemHolder;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionOption;
import com.ouchn.lib.entity.QuestionType;
import com.ouchn.lib.util.BaseConfigUitl;

import java.util.ArrayList;

public class QuestionPagerAdapter extends PagerAdapter {

	public static final int MAX_REUSE_NUM = 3;
	
	private Activity mContext;
	private ArrayList<Question> mQuestionList;
	private ArrayList<View> mViews;
	
	public QuestionPagerAdapter(Activity context, ArrayList<Question> data) {
		mQuestionList = data;
		mContext = context;
	}
	
	public QuestionPagerAdapter(Activity context, ArrayList<Question> data, ArrayList<View> views) {
		mQuestionList = data;
		mContext = context;
		mViews = views;
	}
	
	public void setViews(ArrayList<View> views) {
		mViews = views;
	}
	
	@Override
	public int getCount() {
		if(mQuestionList == null) return 0; 
		return mQuestionList.size();
	}
	
	

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View inner = mViews.get(position % MAX_REUSE_NUM);
		
		if(inner.getParent() != null) {
			container.removeView(inner);
		} 
		container.addView(inner);
		
		Question question = mQuestionList.get(position);
		QuestionViewItemHolder holder = (QuestionViewItemHolder) inner.getTag();
		
		String questionType = QuestionType.getQuestionType(question.getQuestionType(), mContext);
		String content = question.getContent();
		String finalContent = questionType + content;
		
		SpannableStringBuilder spannBuilder = new SpannableStringBuilder(Html.fromHtml(finalContent));
		ForegroundColorSpan span = new ForegroundColorSpan(mContext.getResources().getColor(R.color.question_text_color2));
		spannBuilder.setSpan(span, 0, questionType.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.mQuestionContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseConfigUitl.Aa);
		holder.mQuestionContent.setText(spannBuilder);
		holder.resetSelectedState();
		holder.mQuestionOptionArea.resetInnerLatyout();
		holder.mQuestionOptionArea.setQuestionEntity(question);
		
		/*if(holder.isForWrongAnswer) {
			holder.mAnswerCompareRightTx.setText(question.mAnswerState.getDisplayAnswer(true));
			holder.mAnswerCompareWrongTx.setText(question.mAnswerState.getDisplayAnswer(false));
		}*/
		
		if(question.getQuestionType() == QuestionType.SINGLE || question.getQuestionType() == QuestionType.JUDGMENT) {
			holder.mQuestionOptionArea.isMutex(true);
		} else {
			holder.mQuestionOptionArea.isMutex(false);
		}
		
		if(question.getOptions() != null && question.getOptions().length != 0) {
			QuestionOption[] questionOptions = question.getOptions();
			try {
				int type = question.getQuestionType();
				String[] arr = new String[questionOptions.length];
				for(int i = 0; i < questionOptions.length; i ++ ) {
					QuestionOption option = questionOptions[i];
					arr[i] = option.getContent();
				}
				
				if(type == QuestionType.SINGLE || type == QuestionType.MULTIPLE || type == QuestionType.JUDGMENT) {
					
//					for(int i = 0; i < questionOptions.length; i ++) {
//						holder.mQuestionOptions[i].setVisibility(View.VISIBLE);
//						holder.mQuestionOptionContexts[i].setText(questionOptions[i].getContent());
//					}
					holder.mQuestionOptionArea.setOptionIndexList(arr);
				} else if(type == QuestionType.SORT) {
					holder.mQuestionOptionArea.setOptionSortList(arr);
				} else if(type == QuestionType.MATCHING) {
					holder.mQuestionOptionArea.setOptionMatchList(arr);
				}
			} catch(Exception e) {
				e.printStackTrace();
				Toast.makeText(mContext, "more options required", Toast.LENGTH_LONG).show();
			}
		}
		
		return inner;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//((ViewPager) container).removeView(mViews.get(position % MAX_REUSE_NUM));
	}
	
}

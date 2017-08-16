package com.ouchn.lib.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.ouchn.lib.R;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.widget.QuesitonOptionContainer;
import com.ouchn.lib.widget.QuestionOptionIndex;

public class QuestionViewItemHolder implements QuesitonOptionContainer.QuestionAnsweredMonitor {

	public View mRoot;
	public TextView mQuestionContent;
	public QuesitonOptionContainer mQuestionOptionArea;
	
	public View mAnswerCompareView;
	public TextView mAnswerCompareRightTx;
	public TextView mAnswerCompareWrongTx;

	public View[] mQuestionOptions = new View[10];
	public TextView[] mQuestionOptionContexts = new TextView[10];
	public QuestionOptionIndex[] indexes = new QuestionOptionIndex[10];

	public QuestionAnsweredListener mQuestionAnsweredListener;
	
//	public WebView mQuestionContentRich;
	
	public boolean isForWrongAnswer;
	
	public QuestionViewItemHolder(View root) {
		mRoot = root;
		initView(root);
	}
	
	public QuestionViewItemHolder(View root, boolean isForWrong) {
		this(root);
		initView(root, isForWrong);
	}
	
	public void initView(View root, boolean forWrongAnswer) {
		initView(root);
		isForWrongAnswer = forWrongAnswer;
		
		mAnswerCompareView = root.findViewById(R.id.question_answer_compare_container);
		mAnswerCompareRightTx = (TextView) root.findViewById(R.id.question_answer_compare_right);
		mAnswerCompareWrongTx = (TextView) root.findViewById(R.id.question_answer_compare_error);
		
		mAnswerCompareView.setVisibility(View.VISIBLE);
	}
	
	public void initView(View root) {
		
		mQuestionContent = (TextView)root.findViewById(R.id.question_content_string);
//		mQuestionContentRich = (WebView) root.findViewById(R.id.question_content_html);
		mQuestionOptionArea = (QuesitonOptionContainer) root.findViewById(R.id.question_option_area);

		mQuestionOptionArea.setOnQuestionAnsweredMonitor(this);
		
		mQuestionOptions[0] = root.findViewById(R.id.question_option_root_1);
		mQuestionOptions[1] = root.findViewById(R.id.question_option_root_2);
		mQuestionOptions[2] = root.findViewById(R.id.question_option_root_3);
		mQuestionOptions[3] = root.findViewById(R.id.question_option_root_4);
		mQuestionOptions[4] = root.findViewById(R.id.question_option_root_5);
		mQuestionOptions[5] = root.findViewById(R.id.question_option_root_6);
		mQuestionOptions[6] = root.findViewById(R.id.question_option_root_7);
		mQuestionOptions[7] = root.findViewById(R.id.question_option_root_8);
		mQuestionOptions[8] = root.findViewById(R.id.question_option_root_9);
		mQuestionOptions[9] = root.findViewById(R.id.question_option_root_10);
		
		indexes[0] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_1);
		indexes[1] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_2);
		indexes[2] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_3);
		indexes[3] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_4);
		indexes[4] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_5);
		indexes[5] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_6);
		indexes[6] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_7);
		indexes[7] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_8);
		indexes[8] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_9);
		indexes[9] = (QuestionOptionIndex)root.findViewById(R.id.question_option_index_10);
		
		indexes[0].setOnSelectedCallback(mQuestionOptionArea);
		indexes[1].setOnSelectedCallback(mQuestionOptionArea);
		indexes[2].setOnSelectedCallback(mQuestionOptionArea);
		indexes[3].setOnSelectedCallback(mQuestionOptionArea);
		indexes[4].setOnSelectedCallback(mQuestionOptionArea);
		indexes[5].setOnSelectedCallback(mQuestionOptionArea);
		indexes[6].setOnSelectedCallback(mQuestionOptionArea);
		indexes[7].setOnSelectedCallback(mQuestionOptionArea);
		indexes[8].setOnSelectedCallback(mQuestionOptionArea);
		indexes[9].setOnSelectedCallback(mQuestionOptionArea);
		
		mQuestionOptionContexts[0] = (TextView) root.findViewById(R.id.question_option_content_1);
		mQuestionOptionContexts[1] = (TextView) root.findViewById(R.id.question_option_content_2);
		mQuestionOptionContexts[2] = (TextView) root.findViewById(R.id.question_option_content_3);
		mQuestionOptionContexts[3] = (TextView) root.findViewById(R.id.question_option_content_4);
		mQuestionOptionContexts[4] = (TextView) root.findViewById(R.id.question_option_content_5);
		mQuestionOptionContexts[5] = (TextView) root.findViewById(R.id.question_option_content_6);
		mQuestionOptionContexts[6] = (TextView) root.findViewById(R.id.question_option_content_7);
		mQuestionOptionContexts[7] = (TextView) root.findViewById(R.id.question_option_content_8);
		mQuestionOptionContexts[8] = (TextView) root.findViewById(R.id.question_option_content_9);
		mQuestionOptionContexts[9] = (TextView) root.findViewById(R.id.question_option_content_10);
		
	}
	
	public void resetSelectedState() {
		for(int i = 0; i < 10; i ++) {
			indexes[i].setSelected(false);
			mQuestionOptions[i].setVisibility(View.GONE);
		}
	}


	@Override
	public void onQuestionAnswered(Question question) {

		if(mQuestionAnsweredListener != null) mQuestionAnsweredListener.onQuestionAnswered(question);
	}

	public void setOnQuestionAnsweredListener(QuestionAnsweredListener listener) {

		mQuestionAnsweredListener = listener;
	}

	public interface QuestionAnsweredListener {

		public void onQuestionAnswered(Question question);

	}

}

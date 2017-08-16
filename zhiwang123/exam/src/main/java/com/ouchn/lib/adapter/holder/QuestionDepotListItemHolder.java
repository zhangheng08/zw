package com.ouchn.lib.adapter.holder;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ouchn.lib.R;
import com.ouchn.lib.entity.QuestionDepot;

public class QuestionDepotListItemHolder {

	public int mCurrentIndex;
	public TextView mDepotName;
	public SeekBar 	mDepotAnsweredProgress;
	public TextView mDepotAnsweredNum;
	public View 	mDepotToAnswerButton;
	
	public QuestionDepot mDepot;
	
	public QuestionDepotListItemHolder() {}
	
	public QuestionDepotListItemHolder(View root) {
		initView(root);
	}
	
	public void initView(View root) {
		mDepotName = (TextView) root.findViewById(R.id.main_depot_item_content);
		mDepotAnsweredProgress = (SeekBar) root.findViewById(R.id.depot_answered_progress);
		mDepotAnsweredNum = (TextView) root.findViewById(R.id.depot_answered_num);
		mDepotToAnswerButton = root.findViewById(R.id.depot_to_answer);
	}
	
	public void setDepot(QuestionDepot depot) {
		mDepot = depot;
	}
	
}

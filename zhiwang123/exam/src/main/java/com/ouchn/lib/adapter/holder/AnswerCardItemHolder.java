package com.ouchn.lib.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.ouchn.lib.R;

public class AnswerCardItemHolder {

	public TextView mIndex;
	
	public AnswerCardItemHolder(View root) {
		findIds(root);
	}
	
	public void findIds(View root) {
		mIndex = (TextView) root.findViewById(R.id.answer_card_item_index);
	}
	
	
}

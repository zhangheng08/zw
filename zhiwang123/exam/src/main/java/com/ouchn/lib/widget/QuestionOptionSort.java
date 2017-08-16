package com.ouchn.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouchn.lib.R;
import com.ouchn.lib.util.PhoneUtil;

public class QuestionOptionSort extends LinearLayout {
	
	private TextView mLeftIndex;
	private TextView mRightContent;
	

	public QuestionOptionSort(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout(context);
	}

	public QuestionOptionSort(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public QuestionOptionSort(Context context) {
		super(context);
		initLayout(context);
	}
	
	public QuestionOptionSort(Context context, boolean canDrag) {
		super(context);
		initLayout(context);
	}
	
	private void initLayout(Context context) {
		setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		float dip = PhoneUtil.getDip((Activity)context);
		setPadding((int)(0*dip), (int)(15*dip), (int)(0*dip), (int)(15*dip));
		LayoutInflater.from(context).inflate(R.layout.question_option_sort_layout, this);
		
		mLeftIndex = (TextView) findViewById(R.id.sort_left_content);
		mRightContent = (TextView) findViewById(R.id.sort_right_content);
	}
	
	public void setDragListener(OnDragListener onDrag) {
		setOnDragListener(onDrag);
	}
	
	public void setLongClickListener(OnLongClickListener onLongClick) {
		setOnLongClickListener(onLongClick);
	}
	
	public void setLeftIndex(String index) {
		mLeftIndex.setText(index);
	}
	
	public void setRightContent(String content) {
		mRightContent.setText(content);
	}
	
	public TextView getLeftIndex() {
		return mLeftIndex;
	}
	
	public TextView getRightContent() {
		return mRightContent;
	}
	
}

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

public class QuestionOptionMatch extends LinearLayout {
	
	public boolean isDragTarget;
	private Context mContext;
	private TextView mDragTarget;
	private TextView mDragContainer;
	private TextView mSepr;

	public QuestionOptionMatch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout(context);
	}

	public QuestionOptionMatch(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public QuestionOptionMatch(Context context) {
		super(context);
		initLayout(context);
	}
	
	public QuestionOptionMatch(Context context, boolean canDrag) {
		super(context);
		isDragTarget = canDrag;
		initLayout(context);
	}
	
	public void setIsDragTarget(boolean is) {
		isDragTarget = is;
		
		if(isDragTarget) {
			hideContainer();
		}
	}
	
	private void initLayout(Context context) {
		
		setGravity(Gravity.LEFT |Gravity.CENTER_VERTICAL);
		float dip = PhoneUtil.getDip((Activity)context);
		setPadding((int)(0*dip), (int)(15*dip), (int)(0*dip), (int)(15*dip));
		LayoutInflater.from(context).inflate(R.layout.question_option_match_layout, this);
		
		mDragTarget = (TextView) findViewById(R.id.match_left_content);
		mDragContainer = (TextView) findViewById(R.id.match_right_content);
		mSepr = (TextView) findViewById(R.id.match_center_sepr);
		
		if(isDragTarget) {
			hideContainer();
		}
		
	}
	
	public void setLongClickListener(OnLongClickListener onLongClick) {
		if(isDragTarget) mDragTarget.setOnLongClickListener(onLongClick);
	}
	
	public void setDragListener(OnDragListener dragLisener) {
		if(!isDragTarget) {
			mDragContainer.setOnDragListener(dragLisener);
		}
	}
	
	public void setRightContent(String content) {
		mDragContainer.setText(content);
	}
	
	public void setLeftContent(String content) {
		mDragTarget.setText(content);
		String leftKey = mDragTarget.getText().toString();
		mDragContainer.setTag(leftKey);
	}

	public TextView getDragTarget() {
		return mDragTarget;
	}
	
	public TextView getDragContainer() {
		return mDragContainer;
	}
	
	private void hideContainer() {
		mDragContainer.setVisibility(GONE);
		mSepr.setVisibility(GONE);
	}
	
	
	
	
	
}

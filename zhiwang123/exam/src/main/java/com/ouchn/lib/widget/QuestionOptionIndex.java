package com.ouchn.lib.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ouchn.lib.R;

public class QuestionOptionIndex extends TextView implements OnClickListener {

	public OnSelectedMonitor mOnSelectedCallback;
	
	public boolean isSelected;
	
	public Context mContext;
	
	private ColorStateList mDefaultTextColors;
	private ColorStateList mSelectedTextColors;
	private Drawable mBackground;
	private Drawable mBackgroundSelected;
	
	public int mOptionIndex;
	
	public String mOptionContent;
	
	public QuestionOptionIndex(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initLayout(context, attrs);
	}

	public QuestionOptionIndex(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initLayout(context, attrs);
	}

	private void initLayout(Context context, AttributeSet attrs) {
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.question_option_index);
		if(typedArray.hasValue(R.styleable.question_option_index_option_index_string)) {
			String text = typedArray.getString(R.styleable.question_option_index_option_index_string);
			if(text != null) setText(text);
		}
		
		if(typedArray.hasValue(R.styleable.question_option_index_option_index_text_color)) {
			mDefaultTextColors = typedArray.getColorStateList(R.styleable.question_option_index_option_index_text_color);
			mSelectedTextColors = typedArray.getColorStateList(R.styleable.question_option_index_option_index_text_color_selected);
//			if(mDefaultTextColors == null) {
//				XmlResourceParser xmlres = Resources.getSystem().getXml(R.color.black);
//				try {
//					mDefaultTextColors =  ColorStateList.createFromXml(mContext.getResources(), xmlres);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			if(mSelectedTextColors == null) {
//				XmlResourceParser xmlres = Resources.getSystem().getXml(R.color.white);
//				try {
//					mSelectedTextColors =  ColorStateList.createFromXml(mContext.getResources(), xmlres);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			setTextColor(mDefaultTextColors);
		}
		
		if(typedArray.hasValue(R.styleable.question_option_index_option_index_bg)) {
			mBackground = typedArray.getDrawable(R.styleable.question_option_index_option_index_bg);
			mBackgroundSelected = typedArray.getDrawable(R.styleable.question_option_index_option_index_selelcted_bg);
			if(mBackground == null) mBackground = mContext.getResources().getDrawable(R.drawable.question_option_bg_shape);
			if(mBackground == null) mBackgroundSelected = mContext.getResources().getDrawable(R.drawable.question_option_selected_bg_shape);
			setBackgroundDrawable(mBackground);
		}
		
		if(typedArray.hasValue(R.styleable.question_option_index_option_index)) {
			mOptionIndex = typedArray.getInteger(R.styleable.question_option_index_option_index, 0);
		}
		
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		setSelected(!isSelected);
		if(mOnSelectedCallback != null) mOnSelectedCallback.onOptionSelected(this);
	}
	
	public void setSelected(boolean is) {
		isSelected = is;
		if(is) {
			setTextColor(mSelectedTextColors);
			setBackgroundDrawable(mBackgroundSelected);
		} else {
			setTextColor(mDefaultTextColors);
			setBackgroundDrawable(mBackground);
		}
	}
	
	public void setOnSelectedCallback(OnSelectedMonitor callback) {
		mOnSelectedCallback = callback;
	}
	
	public interface OnSelectedMonitor {
		public void onOptionSelected(QuestionOptionIndex view);
	}

	public void setOptionIndexString(int index) {

		setText(String.valueOf((char)(index + 65)));

	}

	public String getOptionIndexString(int index) {

		return String.valueOf((char)(index + 65));

	}


}

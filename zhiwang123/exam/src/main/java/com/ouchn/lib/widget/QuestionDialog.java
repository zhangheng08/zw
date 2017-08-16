package com.ouchn.lib.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ouchn.lib.R;


public class QuestionDialog {

	Context mContext;
	private AlertDialog.Builder mAlertBuilder;
	private AlertDialog mCurrentDialog;
	private View mCustomView;
	private TextView mTitleTx;
	private TextView mMessageTx;
	private TextView mPButtonTx;
	private TextView mNButtonTx;
	private ImageView mCloseBtn;
	private ImageView mMessageIcon;
	
	public QuestionDialog(Context context) {
		mContext = context;
		mAlertBuilder = new AlertDialog.Builder(context);
		mCustomView = LayoutInflater.from(context).inflate(R.layout.question_dialog_layout, null);
		mCloseBtn = (ImageView) mCustomView.findViewById(R.id.dialog_close);
		mMessageIcon = (ImageView) mCustomView.findViewById(R.id.dialog_message_icon);
		mTitleTx = (TextView) mCustomView.findViewById(R.id.dialog_title);
		mMessageTx = (TextView) mCustomView.findViewById(R.id.dialog_message);
		mPButtonTx = (TextView) mCustomView.findViewById(R.id.dialog_btn_1);
		mNButtonTx = (TextView) mCustomView.findViewById(R.id.dialog_btn_2);
		setInfo("");
		
		mCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mNButtonTx.performClick();
				dismiss();
			}
		});
	}
	
	public QuestionDialog setInfo(String message) {
		
		setInfo(null, message, null, null);
		
		return this;
	}
	
	public QuestionDialog setInfo(String title, String message, String pStr, String nStr) {
		
		if(!TextUtils.isEmpty(title)) {
			mTitleTx.setText(title);
		}
		
		if(!TextUtils.isEmpty(message)) {
			mMessageTx.setText(message);
		}
		
		if(!TextUtils.isEmpty(pStr)) {
			mPButtonTx.setText(pStr);
		}
		
		if(!TextUtils.isEmpty(nStr)) {
			mNButtonTx.setText(nStr);
		}
		
		mAlertBuilder.setView(mCustomView);
		return this;
	}
	
	public QuestionDialog setInfo(int title, int message, int pStr, int nStr) {
		
		mTitleTx.setText(title);
		
		mMessageTx.setText(message);
		
		mPButtonTx.setText(pStr);
		
		mNButtonTx.setText(nStr);
		
		mAlertBuilder.setView(mCustomView);
		return this;
	}
	
	public QuestionDialog setCancelable(boolean bo) {
		mAlertBuilder.setCancelable(bo);
		return this;
	}
	
	public QuestionDialog setMessageTextResColor(int res) {
		mMessageTx.setTextColor(mContext.getResources().getColor(res));
		return this;
	}
	
	public QuestionDialog setMessageTextColor(int color) {
		mMessageTx.setTextColor(color);
		return this;
	}
	
	public QuestionDialog setMessageResIcon(int res) {
		mMessageIcon.setImageResource(res);
		return this;
	}
	
	public QuestionDialog setPClick(OnClickListener onClick) {
		mPButtonTx.setOnClickListener(onClick);
		return this;
	}
	
	public QuestionDialog setNClick(OnClickListener onClick) {
		mNButtonTx.setOnClickListener(onClick);
		return this;
	}
	
	public void dismiss() {
		if(mCurrentDialog != null) mCurrentDialog.dismiss(); 
	}
	
	public QuestionDialog show() {
		mCurrentDialog = mAlertBuilder.show();
		return this;
	}
	

}

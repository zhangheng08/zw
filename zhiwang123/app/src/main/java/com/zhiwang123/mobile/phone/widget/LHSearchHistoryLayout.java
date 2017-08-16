package com.zhiwang123.mobile.phone.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiwang123.mobile.phone.utils.MethodUtils;

import java.lang.ref.SoftReference;

/**
 * @author zhangheng
 * @since 20150616
 *
 */

public class LHSearchHistoryLayout extends LinearLayout {
	
	private static final String TAG = LHSearchHistoryLayout.class.getName();
	private TextView mInfo;
	private SoftReference<Activity> mContextReference;
	private LinearLayout mCurrentLine;
	private int mCurrentTotleWidth;
	private int mScreenWidth;
	
	private OnHistoryClickCallback mCallback;

	public LHSearchHistoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView((Activity) context);

	}

	public LHSearchHistoryLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView((Activity) context);
	}

	public LHSearchHistoryLayout(Context context) {
		super(context);
		initView((Activity) context);
	}

	private void initView(Activity context) {
		mContextReference = new SoftReference<Activity>(context);
		mScreenWidth = (int) MethodUtils.getScreenWidth(context);
	}
	
	public void setHistoryInfo(final String[] infos) {
		removeAllViews();
		mCurrentLine = addLine();
		addView(mCurrentLine);
		for(int i = 0; i < infos.length; i ++) {
			LHSearchHistoryView v = new LHSearchHistoryView(mContextReference.get() == null ? (Activity) getContext() : mContextReference.get());
			int count = infos[i].length();
			int width = 18;
			int padding = 2;
			int other = 4;
			int margin = 5;
			
			if(count == 1) {
				width = 40;
			} else if(count < 6 && count > 1) {
				width *= count;
			} else {
				width = 108;
			}
			
			float dip = MethodUtils.getDip(mContextReference.get() == null ? (Activity) getContext() : mContextReference.get());
			int widthPX = (int)((width + 2*padding + other)*dip);
			System.out.println("width: " + widthPX);
			LayoutParams lp = new LayoutParams(widthPX, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, (int)(margin * dip), 0);
//			v.setOnMeasuredCallback(this);
			v.setHistoryInfo(infos[i]);
			v.setLayoutParams(lp);
			final int index = i;
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mCallback != null) mCallback.onHistoryClick(infos[index]);
				}
			});
			
			mCurrentTotleWidth += widthPX;
			System.out.println("totle width: " + mCurrentTotleWidth);
			if(mCurrentTotleWidth < mScreenWidth - 2*(15*dip) - 3*margin * dip) {
				mCurrentLine.addView(v);
			} else {
				ViewGroup vp = (ViewGroup)(v.getParent());
				if(vp != null) vp.removeView(v);
				mCurrentTotleWidth = widthPX;
				mCurrentLine = addLine();
				mCurrentLine.addView(v);
				addView(mCurrentLine);
			}
		}
	}
	
	private LinearLayout addLine() {
		LinearLayout line = new LinearLayout(mContextReference.get() == null ? (Activity) getContext() : mContextReference.get());
		line.setOrientation(LinearLayout.HORIZONTAL);
		line.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		line.setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(60 * MethodUtils.getDip(mContextReference.get() == null ? (Activity) getContext() : mContextReference.get())));
		line.setLayoutParams(lp);
		return line;
	}
	
	public void setONHistoryClickCallback(OnHistoryClickCallback callback) {
		mCallback = callback;
	}
	
	public interface OnHistoryClickCallback {
		
		public void onHistoryClick(String keyWord);
		
	}
	
	private class UIHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			
		}
	}

}

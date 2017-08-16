package com.zhiwang123.mobile.phone.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiwang123.mobile.R;

/**
 * @author zhangheng
 * @since 20150616
 *
 */

public class LHSearchHistoryView extends FrameLayout {
	
	private TextView mInfo;
	private MeasuredCallback mCallback;

	public LHSearchHistoryView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
		
	}
	
	public LHSearchHistoryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public LHSearchHistoryView(Context context) {
		super(context);
		initView(context);
	}
	
	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.search_history_item, null);
		mInfo = (TextView )root.findViewById(R.id.search_info);
		addView(root);
	}
	
	public void setHistoryInfo(String info) {
		mInfo.setText(info);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int result = 100;
		
		result = specSize;
		
		if(mCallback != null) {
			mCallback.onMeasured(result);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}
	
	public void setOnMeasuredCallback(MeasuredCallback callback) {
		mCallback = callback;
	}
	
	public interface MeasuredCallback {
		public void onMeasured(int width);
	}
	
}

package com.ouchn.lib.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ouchn.lib.R;

public class TimerView extends RelativeLayout {

	private TextView mTimeText;
	private int mHour;
	private int mMinute;
	private int mSecond;
	private String mCurrentTimeStr;
	private boolean isPause;
	private Timer mTimer;
	private Context mContext;
	
	public TimerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout(context);
	}

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public TimerView(Context context) {
		super(context);
		initLayout(context);
	}
	
	private void initLayout(Context context) {
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.timer_layout, this);
		mTimeText = (TextView) findViewById(R.id.timer_front_time_text);
		startClock();
	}
	
	public void startClock() {
		
		mTimer = new Timer(true);
		mTimer.schedule(new MyTimerTask(), 1000, 1000);
	}
	
	public void cancelClock() {
		mTimer.cancel();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if(mTimer != null) mTimer.cancel(); 
		super.onDetachedFromWindow();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		mTimeText.setTextColor(mContext.getResources().getColor(R.color.main_deep_gray));
		super.setEnabled(enabled);
	}
	
	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			
			StringBuffer second = new StringBuffer("");
			StringBuffer minute = new StringBuffer("");
			StringBuffer hour = new StringBuffer("");
			
			StringBuffer time = new StringBuffer("");
			
			++ mSecond;
			
			if(mSecond < 10) {
				second.append("0" + mSecond);
			} else if(mSecond >= 10 && mSecond < 60) {
				second.append("" + mSecond);
			} else if(mSecond == 60) {
				mSecond = 0;
				second.append("0" + mSecond);
				++ mMinute;
			}
			
			if(mMinute < 10) {
				minute.append("0" + mMinute);
			} else if(mMinute >= 10 && mMinute < 60) {
				minute.append("" + mMinute);
			} else if(mMinute == 60) {
				mMinute = 0;
				minute.append("0" + mMinute);
				++ mHour;
			}
			
			if(mHour < 10) {
				hour.append("0" + mHour);
			} else if(mHour > 10) {
				hour.append("" + mMinute);
			}
			
			if(mHour > 0) {
				time.append(hour).append(":").append(minute).append(":").append(second);
			} else {
				time.append(minute).append(":").append(second);
			}
			
			mCurrentTimeStr = time.toString();
			mHandler.sendEmptyMessage(0);
		}
		
	};
	
	public String getTimeString() {
		return mCurrentTimeStr == null ? "00:00" : mCurrentTimeStr;
	}
	
	public void setTimeString(String timeStr) {
		mCurrentTimeStr = timeStr;
		String[] strs = timeStr.split(":");
		if(strs.length == 3) {
			mHour = Integer.valueOf(strs[0]);
			mMinute = Integer.valueOf(strs[1]);
			mSecond = Integer.valueOf(strs[2]);
		} else {
			mMinute = Integer.valueOf(strs[0]);
			mSecond = Integer.valueOf(strs[1]);
		}
		
		mTimeText.setText(timeStr);
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			mTimeText.setText(mCurrentTimeStr);
			
		}
		
	};

}

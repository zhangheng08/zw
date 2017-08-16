package com.zhiwang123.mobile.phone.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiwang123.mobile.R;


public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	private int curr_state;

	private Context mContext;

	private View mContentView;
	private TextView mHintView;
	private LinearLayout mLoad;
	private TextView mReady;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(View.GONE);
		mLoad.setVisibility(View.GONE);
		mReady.setVisibility(View.GONE);

		if (state == STATE_READY) {
			mReady.setVisibility(View.VISIBLE);
		} else if (state == STATE_LOADING) {
			mLoad.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.VISIBLE);
		}
		curr_state = state;
	}

	// get current status (add by LSC)
	public int getState() {
		return curr_state;
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mLoad.setVisibility(View.GONE);
		mReady.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mLoad.setVisibility(View.VISIBLE);
		mReady.setVisibility(View.GONE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);

		// TODO 设置隐藏 add by LSC 20131101
		mContentView.setVisibility(View.GONE);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);

		// TODO 设置隐藏 add by lsc 20131101
		mContentView.setVisibility(View.VISIBLE);

	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);

		mLoad = (LinearLayout) moreView.findViewById(R.id.xlistview_footer_load);
		mReady = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_ready);
	}

}

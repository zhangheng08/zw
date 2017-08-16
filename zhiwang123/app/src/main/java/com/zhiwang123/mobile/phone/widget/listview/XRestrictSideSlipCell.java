package com.zhiwang123.mobile.phone.widget.listview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.utils.MethodUtils;


public class XRestrictSideSlipCell extends RelativeLayout {

	private static final String TAG = "ZhListItemContainer";

	private Context mContext;
	
	private Scroller mScroller;
	
	private XRestrictListView mParentListView;
	
	public XRestrictSideSlipCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public XRestrictSideSlipCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XRestrictSideSlipCell(Context context) {
		super(context);
		init(context);
	}
	
	public void setListView(XRestrictListView listv) {
		mParentListView = listv;
	}

	private void init(Context context) {
		mContext = context;
		mScroller = new Scroller(context);
		LayoutInflater.from(context).inflate(R.layout.zh_list_item_container_layout, this);
		setOnClickListener(mOnClick);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
		super.computeScroll();
	}

	OnClickListener mOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Object tag = getTag();
			if(mCallback != null) mCallback.clickEvent();
		}
	};
	
	private float startX;
	private float startY;
	private float moveX;
	private float moveY;
	private boolean layerState = false;
	private boolean triggerScorll = false;
	private boolean interceptTouch = true;
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		switch(ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			return false;
		case MotionEvent.ACTION_MOVE:
			
			
			
			return false;
		case MotionEvent.ACTION_UP:
			
			
			return false;
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			if(mParentListView != null) mParentListView.onTouchEvent(event);
//			Log.v("z.h --", "---------onTouchEvent      DOWN     -------");
			triggerScorll = false;
			startX = event.getX();
			startY = event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
//			Log.v("z.h --", "---------onTouchEvent   MOVE     -------");
			
			moveX = event.getX();
			moveY = event.getY();
			
			if(Math.abs(startX - moveX) > 50) {
				
				if(mParentListView != null) mParentListView.requestDisallowInterceptTouchEvent(true);
				
				triggerScorll = true;
				interceptTouch = false;
//				Log.v("z.h --", "---------   SCROLL     -------");
				if((startX - moveX) > 0 && !layerState) {
					mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 200, 0);
					layerState = true;
				} else if((startX - moveX) < -0 && layerState)  {
					mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), -200, 0);
					layerState = false;
				}
//				Log.v("z.h========", "mScroller.getFinalX()" + mScroller.getFinalX());
				if(mCallback != null) mCallback.expand(layerState);
				invalidate();
			}
			
			return true;
		case MotionEvent.ACTION_UP:
//			if(mParentListView != null) mParentListView.onTouchEvent(event);
//			Log.v("z.h --", "---------onTouchEvent       UP     -------");
			if(!triggerScorll) {
				
				if(layerState && MethodUtils.getScreenWidth((Activity)mContext) - event.getX() < 200) {
					if(mCallback != null) {
						mCallback.delete(mContext);
					}
				} else {
					performClick();
				}
				
			}
			return true;
		default:
			return true;
		}
	}
	
	public void notifyLayerState(boolean isExpand) {
		if(isExpand) {
			if(mScroller.getFinalX() == 0) {
				mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), 200, 0, 0);
				layerState = true;
			}
		} else {
			if(mScroller.getFinalX() != 0) {
				mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), -200, 0, 0);
				layerState = false;
			}
		}
		invalidate();
	}
	
	public void setCallback(Callback callback) {
		mCallback = callback;
	}
	
	private Callback mCallback;
	
	public interface Callback {
		public void delete(Context context);
		public void expand(boolean isExpand);
		public void clickEvent();
	}

}

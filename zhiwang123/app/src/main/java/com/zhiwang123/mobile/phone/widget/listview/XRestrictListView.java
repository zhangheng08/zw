package com.zhiwang123.mobile.phone.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class XRestrictListView extends XListView {
	
	private float startX;
	private float startY;
	
	private float currentX;
	private float currentY;

	public XRestrictListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public XRestrictListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XRestrictListView(Context context) {
		super(context);
	}
	
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		
//		switch(ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			
//			hasDispatchWorked = false;
//			
//			startX = ev.getX();
//			startY = ev.getY();
//			
//			return false;
//		case MotionEvent.ACTION_MOVE:
//			
//			currentX = ev.getX();
//			currentY = ev.getY();
//			
//			if(Math.abs(startY - currentY) > 150 && !hasDispatchWorked) {
//				return true;
//			} else {
//				if(Math.abs(startX - currentX) > 150)  hasDispatchWorked = true;
//				return false;
//			}
//			
//		case MotionEvent.ACTION_UP:
//			
//			return false;
//		}
//		return false;
//		
//	}
	
	private boolean hasDispatchWorked;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
//		switch(ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			
//			hasDispatchWorked = false;
//			
//			startX = ev.getX();
//			startY = ev.getY();
//			
//			System.out.println("11");
//			
//			return false;
//		case MotionEvent.ACTION_MOVE:
//			
//			currentX = ev.getX();
//			currentY = ev.getY();
//			
//			System.out.println("22");
//			
////			if(Math.abs(startY - currentY) > 150 && !hasDispatchWorked) {
////				return true;
////			} else {
////				if(Math.abs(startX - currentX) > 150)  hasDispatchWorked = true;
////				return false;
////			}
//			
//			return false;
//			
//		case MotionEvent.ACTION_UP:
//			
//			System.out.println("33");
//			
//			return false;
//		}
		
		return super.onInterceptTouchEvent(ev);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			Log.i("MyListView ", "          list view action down");
			break;
		case MotionEvent.ACTION_MOVE:
//			Log.i("MyListView ", "          list view action move");
			break;
		case MotionEvent.ACTION_UP:
//			Log.i("MyListView ", "          list view action up");
			break;
		}
		return super.onTouchEvent(ev);
	}
	
}

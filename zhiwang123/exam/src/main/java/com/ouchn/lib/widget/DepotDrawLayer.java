package com.ouchn.lib.widget;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.ouchn.lib.util.PhoneUtil;

public class DepotDrawLayer extends ImageView {
	
	private static final String TAG = "DepotDrawLayer";

	private Context mContext;
	
	private Paint mCommonPaint;
	private Paint mErasePaint;
	private Paint mPaint;
	
	private float mStartX;
	private float mStartY;
	
	private float mLastX;
	private float mLastY;
	private float mNewX;
	private float mNewY;
	
	private int mWidth;
	private int mHeight;
	
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Path mPath;
	private LinkedList<Point> mPointQueue = new LinkedList<Point>();

	public enum PaintMode {erase, common};
	private PaintMode mode = PaintMode.common;
	
	public DepotDrawLayer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initLayout();
	}

	public DepotDrawLayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initLayout();
	}

	public DepotDrawLayer(Context context) {
		super(context);
		mContext = context;
		initLayout();
	}
	
	private void initLayout() {
		setBackgroundColor(0x00ff0000);
		setScaleType(ScaleType.CENTER_CROP);
		
		mPaint = new Paint();
		
		mCommonPaint = new Paint();
		mCommonPaint.setColor(Color.GRAY);
		mCommonPaint.setStrokeWidth(5);
		mCommonPaint.setStyle(Style.STROKE);  
		mCommonPaint.setStrokeCap(Cap.ROUND);
		mCommonPaint.setAntiAlias(true);
		
		mErasePaint = new Paint();
		mErasePaint.setColor(Color.BLACK);  
        mErasePaint.setAntiAlias(false);  
        mErasePaint.setStyle(Paint.Style.STROKE);  
        mErasePaint.setStrokeJoin(Paint.Join.ROUND);  
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);  
        mErasePaint.setAlpha(0);     
        mErasePaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        mErasePaint.setStrokeWidth(220);
	}
	
	private void initCanvas(int width, int height) {
		mCanvas = new Canvas();
		mPath = new Path();
//		mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
		setImageBitmap(mBitmap);
	}
	
//	@Override
//	protected void onDraw(Canvas canvas) {
//		Paint paint = null;
//		
//		switch(mode) {
//		case erase:
//			paint = mErasePaint;
//			break;
//		case common:
//		default:
//			paint = mCommonPaint;
//			break;
//		}
//		
//		canvas.drawPath(mPath, paint);
//		super.onDraw(canvas);
//	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		float[] wh = PhoneUtil.getScreenSize((Activity)mContext);
		int width = getWH(widthMeasureSpec, (int)wh[0]);
		int height = getWH(heightMeasureSpec, (int)wh[1]);
		mWidth = width;
		mHeight = height;
		Log.v(TAG, width + "---------" + height);
		initCanvas(width, height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int getWH(int measureSpec, int defaultSize) {
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		
		switch(mode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			return size;
		case MeasureSpec.UNSPECIFIED:
		default:
			return defaultSize;
		}
	}
	
	public void setPaintMode(PaintMode mode) {
		this.mode = mode;
		mPath = new Path();
	}
	
	public PaintMode getPaintMode() {
		return mode;
	}
	
	public void clearNote() {
		mBitmap.recycle();
		initCanvas(mWidth, mHeight);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.v(TAG, "DrawLayer onTouch down..................................");
//			mStartX = event.getRawX();
//			mStartY = event.getRawY();
//			
//			mPath.moveTo(mStartX, mStartY);
			
	        float startX = event.getX();  
	        float startY = event.getY();  
	          
	        mNewX = startX;  
	        mNewY = startY;  
	        //mPath绘制的绘制起点  
	        mPath.moveTo(startX, startY);
			
			break;
		case MotionEvent.ACTION_MOVE:
			
//			if(mLastX == 0f && mLastY == 0f) {
//				mLastX = event.getRawX();
//				mLastY = event.getRawY();
//			} else {
//				mLastX = mNewX;
//				mLastY = mNewY;
//			}
//			
//			mNewX = event.getRawX();
//			mNewY = event.getRawY();
			
//			Point p = new Point(mLastX, mLastY, mNewX, mNewY);
//			mPointQueue.offer(p);
			
//			if(mPointQueue.size() > 500) {
//				mPointQueue.poll();
//			}
			
//			mPath.lineTo(mNewX, mNewY);
			
//			mPath.quadTo(mLastX, mLastY, mNewX, mNewY);
//			
//			invalidate();
//			Log.v(TAG, mLastX + "<-lx  ly->" + mLastY + "  ,   " + mNewX + "<-nx  ny->" + mNewY);
			
	        final float x = event.getX();  
	        final float y = event.getY();  
	  
	        final float previousX = mNewX;  
	        final float previousY = mNewY;
	  
	        final float dx = Math.abs(x - previousX);  
	        final float dy = Math.abs(y - previousY);  
	          
	        //两点之间的距离大于等于3时，生成贝塞尔绘制曲线  
	        if (dx >= 10 || dy >= 10) {  
	            //设置贝塞尔曲线的操作点为起点和终点的一半  
	            float cX = (x + previousX) / 2;  
	            float cY = (y + previousY) / 2;  
	  
	            //二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点  
	            mPath.quadTo(previousX, previousY, cX, cY);
	            
	            Point p = new Point(previousX, previousY, cX, cY);
				mPointQueue.offer(p);
	  
	            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值  
	            mNewX = x;  
	            mNewY = y;  
	        }
	        
			switch(mode) {
			case erase:
				mPaint = mErasePaint;
				break;
			case common:
			default:
				mPaint = mCommonPaint;
				break;
			}
	        
	        mCanvas.drawPath(mPath, mPaint);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			mLastX = 0f;
			mLastY = 0f;
			break;
		}
		
		return true;
	}
	
	public void clearMark() {
		mPath.rewind();
	}
	
	private class Point {
		
		public float lx;
		public float ly;
		public float nx;
		public float ny;
		
		public Point(float lx, float ly, float nx, float ny) {
			this.lx = lx;
			this.ly = ly;
			this.nx = nx;
			this.ny = ny;
		}
		
	}
	
	private class Option {
		
		public float startX;
		public float startY;
		
		public float lastX;
		public float lastY;
		
		public float newX;
		public float newY;
		
	}
	
	

}

package com.zhiwang123.mobile.phone.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.zhiwang123.mobile.R;

/**
 * Created by ty on 2016/12/6.
 */

public class SectorProgressBar extends ImageView {

    private static final String TAG = "SectorProgressBar";

    private float mProgress;
    private Paint mPaint;
    private Context mContext;
    private RectF mDrawArea;
    private ViewTreeObserver mViewTreeObserver;


    public SectorProgressBar(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        initPaint();
        mViewTreeObserver = getViewTreeObserver();
    }

    public SectorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context.getApplicationContext();
        initPaint();
        mViewTreeObserver = getViewTreeObserver();
    }

    public SectorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context.getApplicationContext();
        initPaint();
        mViewTreeObserver = getViewTreeObserver();
    }

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary_hf_tran));
        mPaint.setAntiAlias(false);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        try {

            if(mDrawArea == null) mDrawArea = new RectF(0, 0, getWidth(), getHeight());
            canvas.drawArc(mDrawArea, -90, mProgress * 360, true, mPaint);

        } catch(Exception e) {

            e.printStackTrace();

        }


        super.onDraw(canvas);
    }

    public void setProgress(float progress) {

        mProgress = progress;
        postInvalidate();

    }

}

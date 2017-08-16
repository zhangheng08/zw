package com.zhiwang123.mobile.phone.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.TeachPlan;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/24.
 */
public class TeachPlanItemView extends RelativeLayout {

    private static final String TAG = "TeachPlanItemView";
    private static final int INNER_VIEW_ID = R.id.tachp_page_con;

    private Context mContext;
    private TextView mTitleTx;

    public TeachPlan mTeachPlan;
    public ArrayList<Course> mTeachPlanCourses;


    public TeachPlanItemView(Context context) {
        super(context);
        mContext = context;
        initContentView();
    }

    public TeachPlanItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initContentView();
    }

    public TeachPlanItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initContentView();
    }

    public void setViewData(TeachPlan entity, ArrayList<Course> subList) {

        mTeachPlan = entity;
        mTeachPlanCourses = subList;

    }

    private void initContentView() {

        LayoutInflater.from(mContext).inflate(R.layout.teach_item_inner, this);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }




}

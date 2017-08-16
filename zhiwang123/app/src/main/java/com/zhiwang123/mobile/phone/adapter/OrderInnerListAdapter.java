package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ty on 2016/6/22.
 */
public class OrderInnerListAdapter extends BaseZwAdapter {

    private static final String TAG = "CourseCenterListPageAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mCenterCourseList;

    public OrderInnerListAdapter(Context c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mCenterCourseList = new ArrayList<Course>();
        if(list != null) mCenterCourseList.addAll(list);
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<Course> dataList) {

        if(dataList == null) return;
        mCenterCourseList.clear();
        mCenterCourseList.addAll(dataList);

    }

    public void addDataList(ArrayList<Course> dataList) {

        mCenterCourseList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mCenterCourseList.size();
    }

    @Override
    public Course getItem(int position) {
        return mCenterCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course c = mCenterCourseList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.order_course_cell, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        Glide.with(mContext).load(c.picture).crossFade().into(holder.imgv);
        holder.courseId = c.courseid;
        holder.titleTv.setText(c.name);
        holder.teacherTv.setText(mContext.getString(R.string.course_teacher, c.teacherName));
        holder.studyTime.setText(mContext.getString(R.string.cell_course_time, c.courseHour));
        holder.moneyTv.setText(mContext.getString(R.string.course_price, c.money));

        return convertView;
    }

    private class CellHolder {

        public String courseId;
        public TextView titleTv;
        public TextView teacherTv;
        public TextView studyTime;
        public ImageView imgv;
        public TextView moneyTv;

        public CellHolder(View cellView) {

            titleTv = (TextView) cellView.findViewById(R.id.order_inner_title);
            teacherTv = (TextView) cellView.findViewById(R.id.order_inner_sub_teacher);
            imgv = (ImageView) cellView.findViewById(R.id.order_inner_left_img);
            studyTime = (TextView) cellView.findViewById(R.id.order_inner_sub_study_time);
            moneyTv = (TextView) cellView.findViewById(R.id.order_inner_money);

        }
    }

    private View.OnClickListener mCellBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.center_to_exam_btn:

                    break;
                case R.id.center_to_study_btn:

                    break;
            }
        }
    };


}

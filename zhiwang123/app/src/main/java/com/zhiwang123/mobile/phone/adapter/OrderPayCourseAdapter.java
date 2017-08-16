package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
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
public class OrderPayCourseAdapter extends BaseZwAdapter {

    private static final String TAG = "CourseCenterListPageAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mCenterCourseList;

    public OrderPayCourseAdapter(Activity c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mCenterCourseList = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<Course> dataList) {

        if(mCenterCourseList != null) mCenterCourseList.clear();
        else mCenterCourseList = new ArrayList<>();
        mCenterCourseList.addAll(dataList);

    }

    public void addDataList(ArrayList<Course> dataList) {

        if(mCenterCourseList == null) mCenterCourseList = new ArrayList<>();
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
            convertView = mInflater.inflate(R.layout.order_pay_course_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        Glide.with(mContext).load(c.picture).crossFade().into(holder.imgv);

        holder.title.setText(c.name);
        holder.subTitle.setText(mContext.getResources().getString(R.string.cell_course_time, c.courseHour));
        holder.teacher.setText(mContext.getString(R.string.course_teacher, c.teacherName));
        holder.rmb.setText(c.money + "");


        return convertView;
    }

    private class CellHolder {

        public ImageView imgv;
        public TextView title;
        public TextView subTitle;
        public TextView teacher;
        public TextView rmb;

        public CellHolder(View cellView) {

            imgv = (ImageView) cellView.findViewById(R.id.order_pay_c_left_img);
            title = (TextView) cellView.findViewById(R.id.order_pay_c_title);
            subTitle = (TextView) cellView.findViewById(R.id.order_pay_c_sub_title);
            teacher = (TextView) cellView.findViewById(R.id.order_pay_c_sub_teacher);
            rmb = (TextView) cellView.findViewById(R.id.order_pay_c_rmb_value);

        }

    }


}

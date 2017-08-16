package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
public class CartListAdapter extends BaseZwAdapter {

    private static final String TAG = "CartListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mCartDataList;

    public CartListAdapter(Context c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mCartDataList = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<Course> dataList) {

        if(mCartDataList != null) mCartDataList.clear();
        else mCartDataList = new ArrayList<>();
        mCartDataList.addAll(dataList);

    }

    public ArrayList<Course> getDataList() {

        return mCartDataList;
    }

    public void addDataList(ArrayList<Course> dataList) {

        if(mCartDataList == null) mCartDataList = new ArrayList<>();
        mCartDataList.addAll(dataList);

    }

    public void removeList(ArrayList<Course> subList) {

        if(mCartDataList == null || mCartDataList.size() < subList.size()) return;
        mCartDataList.removeAll(subList);

    }

    @Override
    public int getCount() {
        return  mCartDataList.size();
    }

    @Override
    public Course getItem(int position) {
        return mCartDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course c = mCartDataList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_list_cell, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        holder.courseEntity = c;
        Glide.with(mContext).load(c.picture).crossFade().into(holder.imgv);
        holder.courseId = c.courseid;
        holder.titleTv.setText(c.name);
        holder.studyTime.setText(mContext.getString(R.string.cell_course_time, c.courseHour));
        holder.teacherTv.setText(mContext.getString(R.string.course_teacher, c.teacherName));
        holder.price.setText(c.money + "");

        if(holder.courseEntity.state) holder.checkBox.setChecked(true);
        else holder.checkBox.setChecked(false);

        return convertView;
    }

    public class CellHolder {

        public String courseId;
        public TextView titleTv;
        public TextView teacherTv;
        public TextView studyTime;
        public TextView price;
        public TextView dots;
        public ImageView imgv;
        public CheckBox checkBox;
        public View checkBaxCon;

        public Course courseEntity;

        public CellHolder(View cellView) {

            titleTv = (TextView) cellView.findViewById(R.id.cart_title);
            teacherTv = (TextView) cellView.findViewById(R.id.cart_teacher);
            studyTime = (TextView) cellView.findViewById(R.id.cart_study_time);
            imgv = (ImageView) cellView.findViewById(R.id.cart_left_img);
            price = (TextView) cellView.findViewById(R.id.cart_rmb_value);
            dots = (TextView) cellView.findViewById(R.id.cart_rmb_dot);
            checkBox = (CheckBox) cellView.findViewById(R.id.cart_cell_ck);
            checkBaxCon = cellView.findViewById(R.id.cart_cell_ck_con);

        }

    }


}

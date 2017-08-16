package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;
import com.zhiwang123.mobile.phone.bean.ExamECategory;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ty on 2016/6/22.
 */
public class ExamEListAdpater extends BaseZwAdapter {

    private static final String TAG = "ExamEListAdpater";

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<ExamECategory> mDataList;

    public ExamEListAdpater(Context c, ArrayList<ExamECategory> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mDataList = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<ExamECategory> dataList) {

        if(mDataList != null) mDataList.clear();
        else mDataList = new ArrayList<>();
        mDataList.addAll(dataList);

    }

    public ArrayList<ExamECategory> getDataList() {

        return mDataList;
    }

    public void addDataList(ArrayList<ExamECategory> dataList) {

        if(mDataList == null) mDataList = new ArrayList<>();
        mDataList.addAll(dataList);

    }

    public void removeList(ArrayList<Course> subList) {

        if(mDataList == null || mDataList.size() < subList.size()) return;
        mDataList.removeAll(subList);

    }

    @Override
    public int getCount() {
        return  mDataList.size();
    }

    @Override
    public ExamECategory getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExamECategory ec = mDataList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.exam_e_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        holder.examPaperId = ec.haveCourseId;
        holder.titleTv.setText(ec.title);
        holder.dateTv.setText(ec.startDate + " " + mContext.getString(R.string.tachp_zhi) + " " + ec.endDate);
        holder.durationTv.setText(mContext.getString(R.string.exam_e_list_cm, ec.hour));
        holder.timesTv.setText(mContext.getString(R.string.exam_e_list_ti, ec.leaveExamNum));

        switch (ec.stateInt) {

            case StaticConfigs.EXAM_E_STATE_LE:

                holder.stateTv.setText(R.string.exam_e_list_state_le);
                holder.stateTv.setBackgroundResource(R.drawable.exam_e_list_shape_1);
                holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                holder.btn.setText(R.string.exam_e_list_start);
                holder.btn.setBackgroundResource(R.drawable.exam_e_list_shape_1);
                holder.btn.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                holder.btn.setVisibility(View.VISIBLE);

                break;
            case StaticConfigs.EXAM_E_STATE_IN:

                holder.stateTv.setText(R.string.exam_e_list_state_in);
                holder.stateTv.setBackgroundResource(R.drawable.exam_e_list_shape_2);
                holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary_red));

                holder.btn.setText(R.string.exam_e_list_goon);
                holder.btn.setBackgroundResource(R.drawable.exam_e_list_shape_2);
                holder.btn.setTextColor(mContext.getResources().getColor(R.color.colorPrimary_red));

                holder.btn.setVisibility(View.VISIBLE);

                break;
            case StaticConfigs.EXAM_E_STATE_RI:

                holder.stateTv.setText(R.string.exam_e_list_state_ri);
                holder.stateTv.setBackgroundResource(R.drawable.exam_e_list_shape_3);
                holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.zw_green));

                holder.btn.setVisibility(View.INVISIBLE);

                break;
        }

        return convertView;
    }

    public class CellHolder {

        public String examPaperId;
        public TextView titleTv;
        public TextView dateTv;
        public TextView durationTv;
        public TextView timesTv;
        public TextView stateTv;
        public TextView btn;

        public CellHolder(View cellView) {

            titleTv = (TextView) cellView.findViewById(R.id.exam_e_item_name);
            dateTv = (TextView) cellView.findViewById(R.id.exam_e_item_date);
            durationTv = (TextView) cellView.findViewById(R.id.exam_e_item_duration);
            timesTv = (TextView) cellView.findViewById(R.id.exam_e_item_count);
            stateTv = (TextView) cellView.findViewById(R.id.exam_e_item_status);
            btn = (TextView) cellView.findViewById(R.id.exam_e_item_btn);

        }

    }


}

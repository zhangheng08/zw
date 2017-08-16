package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRoot;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class ChapterListAdapter extends BaseZwAdapter {

    private static final String TAG = "ChapterListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private CourseRoot mCourseRoot;
    private ArrayList<Course.CourseChild> mChapterList;


    public ChapterListAdapter(Context c, CourseRoot cr) {
        super(c);
        mContext = c;
        mCourseRoot = cr;
        mInflater = LayoutInflater.from(c);
    }

    public ChapterListAdapter(Context c, ArrayList<Course.CourseChild> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mChapterList = list;
    }

    @Override
    public int getCount() {
        return  mChapterList.size();
    }

    @Override
    public Course.CourseChild getItem(int position) {
        return mChapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course.CourseChild ch = mChapterList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.chapter_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        if(position == 0) {
            holder.sepTopv.setVisibility(View.INVISIBLE);
        } else if(position == mChapterList.size() - 1) {
            holder.sepBtmv.setVisibility(View.INVISIBLE);
        } else {
            holder.sepTopv.setVisibility(View.VISIBLE);
            holder.sepBtmv.setVisibility(View.VISIBLE);
        }

        holder.courseId = ch.courseid;
        holder.nameTxv.setText(ch.name);
        if(ch.money != 0) holder.moneyTxv.setText("￥" + ch.money);
        else holder.moneyTxv.setText("");

        return convertView;
    }

    public class CellHolder {

        public TextView nameTxv;
        public String courseId;
        public TextView moneyTxv;
        public View sepTopv;
        public View sepBtmv;

        public CellHolder(View cellView) {

            nameTxv = (TextView) cellView.findViewById(R.id.chapter_name);
            sepTopv = cellView.findViewById(R.id.sep_top);
            sepBtmv = cellView.findViewById(R.id.sep_btm);
            moneyTxv = (TextView) cellView.findViewById(R.id.chapter_money);

//            nameTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        }

    }


}

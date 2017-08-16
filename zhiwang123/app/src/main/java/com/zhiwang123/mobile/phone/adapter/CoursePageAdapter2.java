package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.CourseListActivity;
import com.zhiwang123.mobile.phone.activity.MainActivity;
import com.zhiwang123.mobile.phone.bean.Course;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class CoursePageAdapter2 extends BaseZwAdapter {

    private static final String TAG = "CoursePageAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Course> mDataList;


    public CoursePageAdapter2(Context c, ArrayList<Course> list) {
        super(c);
        mContext = c;
        mDataList = list;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return  mDataList.size();
    }

    @Override
    public Course getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(TAG, "getView............");

        Course c = mDataList.get(position);

        CellHolder holder = null;

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.course_fragment_page_cell_reuse, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (CellHolder) convertView.getTag();

        }

        if(holder.subAdapter == null) {

            holder.subAdapter = new CourseSubAdapter(mContext, c.recmList);
            holder.recyclerView.setAdapter(holder.subAdapter);

        } else {

            holder.subAdapter.setDataset(c.recmList);

        }

        holder.subAdapter.notifyDataSetChanged();
        holder.courseNameTxv.setText(c.name);
        holder.toMore.setTag(R.id.category_id, c.pkId);
        holder.toMore.setTag(R.id.category_name, c.name);
        holder.toMore.setOnClickListener(mOnToMoreClick);

        return convertView;
    }

    private class CellHolder {

        public TextView courseNameTxv;
        public TextView toMore;
        public RecyclerView recyclerView;
        public CourseSubAdapter subAdapter;

        public CellHolder(View cellView) {

            courseNameTxv = (TextView) cellView.findViewById(R.id.cell_course_root);
            toMore = (TextView) cellView.findViewById(R.id.cell_to_more);
            recyclerView = (RecyclerView) cellView.findViewById(R.id.cell_recommand_container);

//            toMore.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            courseNameTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

            LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
            recyclerViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(recyclerViewLayoutManager);
            recyclerView.setHasFixedSize(true);


        }

    }

    @Override
    public void notifyDataSetChanged() {
        Log.i(TAG, "notifyDataSetChanged..........");
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        Log.i(TAG, "notifyDataSetInvalidated..........");
        super.notifyDataSetInvalidated();
    }

    private View.OnClickListener mOnToMoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int pkid = (int) v.getTag(R.id.category_id);
            String name = (String) v.getTag(R.id.category_name);
            Intent intent = new Intent(mContext, CourseListActivity.class);
            intent.putExtra(CourseListActivity.EXTRA_PKID, pkid);
            intent.putExtra(CourseListActivity.EXTRA_NAME, name);
            ((MainActivity)mContext).startActivity(intent);

        }
    };

}

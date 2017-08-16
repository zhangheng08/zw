package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Topic;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/29.
 */

public class CategrayGridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Topic> mList;

    public CategrayGridAdapter(Context context, ArrayList<Topic> dataList) {
        mContext = context;
        mList = dataList;
    }


    @Override
    public int getCount() {
        int count = 0;
        if(mList != null) {
            count = mList.size();
        }
        return count;
    }

    @Override
    public Topic getItem(int position) {
        return (mList == null ? null : mList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Topic tc = mList.get(position);

        if(convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.topic_grid_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pkid = tc.pkId;
        holder.pkids = tc.pkIds;
        holder.title = tc.title;
        holder.name = tc.name;
        holder.txtv.setText(tc.title);
        holder.img.setImageResource(tc.imgResId);

        return convertView;
    }

    public class ViewHolder {

        public int pkid;
        public String pkids;
        public String title;
        public String name;
        public ImageView img;
        public TextView txtv;

        public ViewHolder(View cv) {
            img = (ImageView) cv.findViewById(R.id.topic_grid_icon);
            txtv = (TextView) cv.findViewById(R.id.topic_grid_name);
        }

    }

}

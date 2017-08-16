package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Organ;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class OrganListAdapter extends BaseZwAdapter {

    private static final String TAG = "OrganListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Organ> mChapterList;


    public OrganListAdapter(Context c, ArrayList<Organ> list) {

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
    public Organ getItem(int position) {
        return mChapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Organ organ = mChapterList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.organ_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }


        holder.entity = organ;

        holder.nameTxv.setText(organ.name);

        return convertView;
    }

    public class CellHolder {

        public Organ entity;

        public TextView nameTxv;

        public CellHolder(View cellView) {

            nameTxv = (TextView) cellView.findViewById(R.id.organ_cell_name);

        }

    }


}

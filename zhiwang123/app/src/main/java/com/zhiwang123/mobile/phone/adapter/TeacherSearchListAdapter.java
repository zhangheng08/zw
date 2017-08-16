package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.Teacher;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;

import java.util.ArrayList;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

/**
 * Created by ty on 2016/6/22.
 */
public class TeacherSearchListAdapter extends BaseZwAdapter {

    private static final String TAG = "CourseCenterListPageAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<Teacher> mTeacherList;
    private XRestrictListView mHostListView;

    public TeacherSearchListAdapter(Activity c, ArrayList<Teacher> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mTeacherList = list;
    }

    public void setDataList(ArrayList<Teacher> dataList) {

        if(mTeacherList != null) mTeacherList.clear();
        else mTeacherList = new ArrayList<>();
        mTeacherList.addAll(dataList);

    }

    public void addDataList(ArrayList<Teacher> dataList) {

        if(mTeacherList == null) mTeacherList = new ArrayList<>();
        mTeacherList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mTeacherList.size();
    }

    @Override
    public Teacher getItem(int position) {
        return mTeacherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Teacher t = mTeacherList.get(position);
        CellHolder cellholder = null;
        View contentView = null;

        if(convertView == null) {

            convertView = mContext.getLayoutInflater().inflate(R.layout.search_teacher_item, null);
            cellholder = new CellHolder(convertView);
            convertView.setTag(cellholder);

        } else {

            cellholder = (CellHolder) convertView.getTag();

        }

        cellholder.t = t;
        Glide.with(mContext).load(t.avatar).crossFade().into(cellholder.avatarImg);
        cellholder.nameTx.setText(t.name);
        cellholder.descTx.setText(Html.fromHtml(t.introduction));

        return convertView;
    }

    public class CellHolder {

        public Teacher t;


        public TextView nameTx;
        public ImageView avatarImg;
        public TextView descTx;


        public CellHolder(View cellview) {

            nameTx = (TextView) cellview.findViewById(R.id.cell_st_name);
            avatarImg = (ImageView) cellview.findViewById(R.id.cell_st_ava);
            descTx = (TextView) cellview.findViewById(R.id.cell_st_desc);

        }
    }

    @Override
    protected void onRemFav() {

        notifyDataSetChanged();

    }

}

package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Teacher;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class TeacherListAdapter extends Adapter<TeacherListAdapter.CourseIntrViewHolder> {


    public OnItemClickMonitor mOnItemClickMonitor;
    public ArrayList<Teacher> mDataset;
    public Context mContext;


    public TeacherListAdapter(Context context, ArrayList<Teacher> data) {
        mContext = context;
        mDataset = new ArrayList<>();
        mDataset.addAll(data);
    }

    public void setDataset(ArrayList<Teacher> newDataset) {
        if(mDataset != null) mDataset.clear();
        mDataset.addAll(newDataset);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();

    }

    @Override
    public void onBindViewHolder(CourseIntrViewHolder holder, int position) {
        holder.fillData(mDataset.get(position));
    }



    @Override
    public CourseIntrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.teacher_list_item, null);
        CourseIntrViewHolder holder = new CourseIntrViewHolder(view);
        return holder;
    }


    public class CourseIntrViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImgv;
        public TextView nameTxv;


        public CourseIntrViewHolder(View itemLayout) {
            super(itemLayout);
            avatarImgv = (ImageView) itemLayout.findViewById(R.id.cell_teacher_avatar);
            nameTxv = (TextView) itemLayout.findViewById(R.id.cell_teacher_name);

//            nameTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        }

        public void fillData(final Teacher t) {

            Glide.with(mContext).load(t.getAvatar()).placeholder(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(mContext)).crossFade().into(avatarImgv);

            nameTxv.setText(t.name);
        }
    }

    public void setOnItemClickMonitor(OnItemClickMonitor monitor) {
        mOnItemClickMonitor = monitor;
    }

    public interface OnItemClickMonitor {

        public void onClick(CourseIntrViewHolder viewHolder, int position, View cellView);

    }


}
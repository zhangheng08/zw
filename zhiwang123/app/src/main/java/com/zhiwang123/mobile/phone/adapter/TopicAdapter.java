package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.CourseListActivity;
import com.zhiwang123.mobile.phone.bean.Topic;

import java.util.ArrayList;


public class TopicAdapter extends Adapter<TopicAdapter.RecommandViewHolder> {


    private static final String TAG = "TopicAdapter";

    public OnItemClickMonitor mOnItemClickMonitor;
    public ArrayList<Topic> mDataset;
    public Context mContext;

    public TopicAdapter(Context context, ArrayList<Topic> data) {
        mContext = context;
        mDataset = new ArrayList<>();
        mDataset.addAll(data);
    }

    public void setDataset(ArrayList<Topic> newDataset) {
        if(mDataset != null) mDataset.clear();
        mDataset.addAll(newDataset);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();

    }

    @Override
    public void onBindViewHolder(RecommandViewHolder holder, int position) {
        holder.fillData(mDataset.get(position));
        if(position == mDataset.size() - 1) {
            holder.leftv.setVisibility(View.VISIBLE);
            holder.rightv.setVisibility(View.VISIBLE);
        } else {
            holder.leftv.setVisibility(View.VISIBLE);
            holder.rightv.setVisibility(View.GONE);
        }
    }



    @Override
    public RecommandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.topic_item_layout, null);
        RecommandViewHolder holder = new RecommandViewHolder(view);
        return holder;
    }

    public class RecommandViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView  name;
        public View leftv;
        public View rightv;
        public View itemRoot;


        public RecommandViewHolder(View itemLayout) {

            super(itemLayout);
            itemRoot = itemLayout.findViewById(R.id.topic_item_root);
            img = (ImageView) itemLayout.findViewById(R.id.topic_icon);
            name = (TextView) itemLayout.findViewById(R.id.topic_name);
            leftv = itemLayout.findViewById(R.id.topic_left_mar);
            rightv = itemLayout.findViewById(R.id.topic_right_mar);

//            name.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
        }

        public void fillData(final Topic c) {

            name.setText(c.title);
            img.setImageResource(c.imgResId);

            itemRoot.setOnClickListener(mOnClk);

            itemRoot.setTag(c);

//            try {
//                Field field = Class.forName("com.zhiwang123.mobile.R$drawable").getField("t" + c.pkId);
//                Thread.currentThread().join();
//                int resId = field.getInt(field);
//                Log.e(TAG, "topic " + c.pkId + "icon resId:" + resId);
//                img.setImageResource(resId);
//            } catch (Exception e) {
//                Log.e(TAG, "" + e.getMessage());
//                e.printStackTrace();
//            }

        }
    }

    public void setOnItemClickMonitor(OnItemClickMonitor monitor) {

        mOnItemClickMonitor = monitor;

    }

    public interface OnItemClickMonitor {

        public void onClick(RecommandViewHolder viewHolder, int position, View cellView);

    }

    public View.OnClickListener mOnClk = new View.OnClickListener() {


        @Override
        public void onClick(View v) {

            Topic c = (Topic) v.getTag();
            Intent intent = new Intent(mContext, CourseListActivity.class);
            intent.putExtra(CourseListActivity.EXTRA_SWITCH_FRAGMENT, CourseListActivity.FRAGMENT_COURSE_LIST);
            intent.putExtra(CourseListActivity.EXTRA_PKIDS, c.pkIds);
            intent.putExtra(CourseListActivity.EXTRA_NAME, c.title);
            mContext.startActivity(intent);

        }
    };


}
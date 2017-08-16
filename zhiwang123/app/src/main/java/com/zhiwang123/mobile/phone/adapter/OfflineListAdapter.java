package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.service.DownLoadTaskWrapper;
import com.zhiwang123.mobile.phone.widget.SectorProgressBar;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class OfflineListAdapter extends BaseZwAdapter {

    private static final String TAG = "OfflineListAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<DownloadTask> mOfflineDataList;

    public OfflineListAdapter(Activity c) {
        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
    }

    public OfflineListAdapter(Activity c, ArrayList<DownloadTask> list) {

        this(c);
        mOfflineDataList = list;
    }

    public void setDataList(ArrayList<DownloadTask> dataList) {

        if(mOfflineDataList != null) mOfflineDataList.clear();
        else mOfflineDataList = new ArrayList<>();
        mOfflineDataList.addAll(dataList);

    }

    public void addDataList(ArrayList<DownloadTask> dataList) {

        if(mOfflineDataList == null) mOfflineDataList = new ArrayList<>();
        mOfflineDataList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mOfflineDataList.size();
    }

    @Override
    public DownloadTask getItem(int position) {
        return mOfflineDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DownloadTask task = mOfflineDataList.get(position);
        CellHolder holder = null;

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.offline_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (CellHolder) convertView.getTag();
        }

        if(!task.isRun) {

            new Thread(task).start();

        }

        holder.task = task;
//        Log.i(TAG, "use img : " + task.cover);
        Glide.with(mContext).load(task.cover).crossFade().into(holder.imageView);
        holder.titleTv.setText(task.title);
        int precent = (int) (100 * ((float) task.currentBits / (float) task.totalBits));
        holder.progressTv.setText(mContext.getString(R.string.offline_item_loading, precent));
        holder.itemLoadingProgress.setProgress((float) task.currentBits / (float) task.totalBits);

        if(task.status == DownLoadTaskWrapper.PAUSE) {

            holder.pauseHint.setText(R.string.offline_item_pause);

        } else {

            holder.pauseHint.setText("");

        }

        return convertView;
    }

    public class CellHolder {

        public DownloadTask task;
        public TextView titleTv;
        public TextView pauseHint;
        public TextView progressTv;
        public ImageView imageView;
        public SectorProgressBar itemLoadingProgress;

        public CellHolder(View cellView) {

            titleTv = (TextView) cellView.findViewById(R.id.offline_item_title);
            pauseHint = (TextView) cellView.findViewById(R.id.offline_pause_hint);
            progressTv = (TextView) cellView.findViewById(R.id.offline_item_sub_title);
            imageView = (ImageView) cellView.findViewById(R.id.offline_item_left_img);
            itemLoadingProgress = (SectorProgressBar) cellView.findViewById(R.id.offline_item_progress);

        }

    }

    private View.OnClickListener mCellBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent intent = null;

            switch(v.getId()) {



            }
        }
    };

}

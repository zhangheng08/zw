package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.LocalVideoListActivity;
import com.zhiwang123.mobile.phone.activity.OffLineListActivity;
import com.zhiwang123.mobile.phone.activity.VideoPlayActivity;
import com.zhiwang123.mobile.phone.bean.LocalVideo;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictSideSlipCell;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class LocalListAdapter extends BaseZwAdapter {

    private static final String TAG = "OfflineListAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<LocalVideo> mOfflineDataList;
    private View mLoadDirItem;

    private XRestrictListView mHostListView;

    public boolean isExpandAll = false;


    public LocalListAdapter(Activity c) {
        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mLoadDirItem = mInflater.inflate(R.layout.offline_list_item_loading, null);
    }

    public LocalListAdapter(Activity c, ArrayList<LocalVideo> list, XRestrictListView hostlistView) {

        this(c);
        mOfflineDataList = list;
        mHostListView = hostlistView;

    }

    public void addDataList(ArrayList<LocalVideo> dataList) {

        if(mOfflineDataList == null) mOfflineDataList = new ArrayList<>();
        mOfflineDataList.addAll(dataList);

    }

    public void setDataList(ArrayList<LocalVideo> dataList) {

        if(mOfflineDataList != null) mOfflineDataList.clear();
        else mOfflineDataList = new ArrayList<>();
        mOfflineDataList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mOfflineDataList.size();
    }

    @Override
    public LocalVideo getItem(int position) {
        return mOfflineDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return mOfflineDataList.get(position).type;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        LocalVideo lv = mOfflineDataList.get(position);

        if(LocalVideo.DIR == type) {

            TextView tx = (TextView) mLoadDirItem.findViewById(R.id.offline_loading_sub_title);
            tx.setText(mContext.getString(R.string.offline_loading_num, lv.taskNum));
            convertView = mLoadDirItem;
            convertView.setTag(new Integer(0x0));

            convertView.setOnClickListener(onClickListener);

        } else if(LocalVideo.FIL == type) {

            CellHolder holder = null;
            View contentView = null;

            if(convertView == null || convertView.getTag() instanceof Integer) {

                contentView = mContext.getLayoutInflater().inflate(R.layout.offline_local_list_item, null);
                convertView = mContext.getLayoutInflater().inflate(R.layout.zh_list_item_layout, null);
                ((ViewGroup)convertView.findViewById(R.id.content_layer)).addView(contentView);

                holder = new CellHolder(convertView);
                convertView.setTag(holder);

            } else {

                holder = (CellHolder) convertView.getTag();

            }

            holder.mVideo = lv;
            holder.titleTv.setText(lv.title);
            DecimalFormat df = new DecimalFormat("#.0");
            Double size = (lv.currentBits / 1024.0) / 1024.0;

            holder.subTv.setText(df.format(size) + "MB");
//            holder.imageView.setImageBitmap(lv.fileImg);
            Glide.with(mContext).load(Uri.fromFile( new File( lv.filePath ) )).crossFade().into(holder.imageView);

        }

        return convertView;
    }

    private class CellHolder implements XRestrictSideSlipCell.Callback {

        public LocalVideo mVideo;

        public TextView titleTv;
        public TextView subTv;
        public ImageView imageView;

        public XRestrictSideSlipCell scrollContainer;
        public TextView mDeleteTriggerView;

        public CellHolder(View cellView) {

            titleTv = (TextView) cellView.findViewById(R.id.local_item_title);
            subTv = (TextView) cellView.findViewById(R.id.local_item_sub_title);
            imageView = (ImageView) cellView.findViewById(R.id.local_item_left_img);

            scrollContainer = (XRestrictSideSlipCell) cellView.findViewById(R.id.scroll_container);
            mDeleteTriggerView = (TextView) cellView.findViewById(R.id.click_event_trigger);
            scrollContainer.setListView(mHostListView);
            scrollContainer.setCallback(this);
            scrollContainer.notifyLayerState(isExpandAll);

        }

        @Override
        public void delete(Context context) {

            AsyncDBHelper.deleteVideoCacheDB(mContext, mVideo, null);
            mOfflineDataList.remove(mVideo);
            notifyDataSetChanged();

        }

        @Override
        public void expand(boolean isExpand) {

        }

        @Override
        public void clickEvent() {

            Intent intent = new Intent(mContext, VideoPlayActivity.class);
            intent.putExtra(VideoPlayActivity.EXTRA_KEY_VNAME, mVideo.title);
            intent.putExtra(VideoPlayActivity.EXTRA_KEY_VIMG, mVideo.cover);
            intent.putExtra(VideoPlayActivity.EXTRA_KEY_VID, mVideo.videoId);
            intent.putExtra(VideoPlayActivity.EXTRA_KEY_LOCPLY, true);
            mContext.startActivity(intent);

        }

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(v.getTag() instanceof Integer) {

                Intent intent = new Intent(mContext, OffLineListActivity.class);
                mContext.startActivityForResult(intent, LocalVideoListActivity.REQUEST_CODE_FOR_REFRESH);

            }

        }
    };

}

package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.CmsModel;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class CmsListAdapter extends BaseZwAdapter {

    private static final String TAG = "CmsListAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<CmsModel> mCmsDataList;

    public CmsListAdapter(Activity c) {
        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
    }

    public CmsListAdapter(Activity c, ArrayList<CmsModel> list) {

        this(c);
        mCmsDataList = list;

    }

    public void addDataList(ArrayList<CmsModel> dataList) {

        if(mCmsDataList == null) mCmsDataList = new ArrayList<>();
        mCmsDataList.addAll(dataList);

    }

    public void setDataList(ArrayList<CmsModel> dataList) {

        if(mCmsDataList != null) mCmsDataList.clear();
        else mCmsDataList = new ArrayList<>();
        mCmsDataList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mCmsDataList.size();
    }

    @Override
    public CmsModel getItem(int position) {
        return mCmsDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CmsModel cms = mCmsDataList.get(position);

        CellHolder holder = null;

        if(convertView == null) {

            convertView = mContext.getLayoutInflater().inflate(R.layout.activity_cms_list, null);

            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (CellHolder) convertView.getTag();

        }

        holder.entity = cms;
        holder.cmsSummaryTx.setText(cms.title);

        if(TextUtils.isEmpty(cms.picture)) holder.cmsRightImg.setVisibility(View.GONE);
        else Glide.with(mContext).load(cms.picture).crossFade().into(holder.cmsRightImg);

        return convertView;
    }

    public class CellHolder {

        public CmsModel entity;
        public TextView cmsSummaryTx;
        public ImageView cmsRightImg;
        public TextView cmsDateTx;

        public CellHolder(View cellView) {

            cmsSummaryTx = (TextView) cellView.findViewById(R.id.cms_news_summary);
            cmsRightImg = (ImageView) cellView.findViewById(R.id.right_cms_img);
            cmsDateTx = (TextView) cellView.findViewById(R.id.cms_date);

        }



    }

}

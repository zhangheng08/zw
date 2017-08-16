package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.CourseRoot;
import com.zhiwang123.mobile.phone.bean.PayType;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class PayTypeListAdapter extends BaseZwAdapter {

    private static final String TAG = "ChapterListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private CourseRoot mCourseRoot;
    private ArrayList<PayType> mPayTypeList;

    public PayTypeListAdapter(Context c, ArrayList<PayType> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mPayTypeList = list;
    }

    public ArrayList<PayType> getDataList() {

        return mPayTypeList;

    }

    @Override
    public int getCount() {
        return  mPayTypeList.size();
    }

    @Override
    public PayType getItem(int position) {
        return mPayTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PayType pt = mPayTypeList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.order_pay_type_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CellHolder) convertView.getTag();
        }

        if(pt.state) {

            holder.check.setChecked(true);

        } else {

            holder.check.setChecked(false);

        }

        switch(pt.key) {

            case StaticConfigs.PAY_TYPE_AL:

                holder.imgv.setImageResource(R.drawable.pay_ali);
                holder.nameTv.setText(R.string.order_pay_ali_str);
                holder.descTv.setText(R.string.order_pay_al_sub);
                holder.descTv.setTextColor(mContext.getResources().getColor(R.color.zw_gray_main_cell_sub_txt));

                break;

            case StaticConfigs.PAY_TYPE_ME:

                holder.imgv.setImageResource(R.drawable.pay_user);
                holder.nameTv.setText(R.string.order_pay_user_str);
                if(StaticConfigs.mLoginResult != null && StaticConfigs.mLoginResult.dataobject.get(0) != null) {
                    holder.descTv.setText(mContext.getString(R.string.order_pay_user_rmb, StaticConfigs.mLoginResult.dataobject.get(0).money));
                }
                holder.descTv.setTextColor(mContext.getResources().getColor(R.color.zw_orange_price));

                break;

            case StaticConfigs.PAY_TYPE_WX:

                holder.imgv.setImageResource(R.drawable.pay_wechat);
                holder.nameTv.setText(R.string.order_pay_wch_str);
                holder.descTv.setText(R.string.order_pay_wx_sub);
                holder.descTv.setTextColor(mContext.getResources().getColor(R.color.zw_gray_main_cell_sub_txt));

                break;

        }

        return convertView;
    }

    private class CellHolder {

        public ImageView imgv;
        public TextView nameTv;
        public TextView descTv;
        public CheckBox check;

        public CellHolder(View cellView) {

            imgv = (ImageView) cellView.findViewById(R.id.order_pay_item_img);
            nameTv = (TextView) cellView.findViewById(R.id.order_pay_item_name);
            descTv = (TextView) cellView.findViewById(R.id.order_pay_item_desc);
            check = (CheckBox) cellView.findViewById(R.id.order_pay_item_check);
            check.setClickable(false);

        }

    }


}

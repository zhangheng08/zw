package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.OrderActivity;
import com.zhiwang123.mobile.phone.activity.OrderPayActivity;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;
import com.zhiwang123.mobile.phone.bean.Order;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/6/22.
 */
public class OrderPageAdapter extends BaseZwAdapter {

    private static final String TAG = "OrderPageAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Order> mCenterCourseList;
    private float dip;

    public OrderPageAdapter(Context c, ArrayList<Order> list, float dp) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mCenterCourseList = list;
        dip = dp;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<Order> dataList) {

        if(mCenterCourseList != null) mCenterCourseList.clear();
        else mCenterCourseList = new ArrayList<>();
        mCenterCourseList.addAll(dataList);

    }

    public void addDataList(ArrayList<Order> dataList) {

        if(mCenterCourseList == null) mCenterCourseList = new ArrayList<>();
        mCenterCourseList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mCenterCourseList.size();
    }

    @Override
    public Order getItem(int position) {
        return mCenterCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Order order = mCenterCourseList.get(position);
        CellHolder holder = null;

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.order_list_cell, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (CellHolder) convertView.getTag();

        }

        holder.orderNumTv.setText(mContext.getString(R.string.order_numb, order.orderNum));
        holder.orderCreateTv.setText(mContext.getString(R.string.order_create, order.createDate));
        holder.orderStatuesTv.setText(order.statusName);
        holder.orderTotalPriceTv.setText(order.money + "");

        switch(order.status) {

            case Order.ORDER_STATUS_PAY_YES:

                holder.orderStatuesTv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary_red));

                holder.orderBtnCon.setVisibility(View.GONE);
                holder.orderPayNowBtn.setVisibility(View.INVISIBLE);
                holder.orderCancelBtn.setVisibility(View.INVISIBLE);

                break;

            case Order.ORDER_STATUS_PAY_NO:

                holder.orderStatuesTv.setTextColor(mContext.getResources().getColor(R.color.zw_orange_price));
                holder.orderBtnCon.setVisibility(View.VISIBLE);
                holder.orderPayNowBtn.setVisibility(View.VISIBLE);
                holder.orderCancelBtn.setVisibility(View.VISIBLE);

                break;

            case Order.ORDER_STATUS_PAY_CLOSE:

                holder.orderStatuesTv.setTextColor(mContext.getResources().getColor(R.color.zw_gray_main_cell_txt));
                holder.orderBtnCon.setVisibility(View.VISIBLE);
                holder.orderPayNowBtn.setVisibility(View.INVISIBLE);
                holder.orderCancelBtn.setVisibility(View.VISIBLE);

                break;

        }

        int totalCount = order.orderItems == null ? 0 : order.orderItems.size();
        holder.orderTotalCountTv.setText(mContext.getString(R.string.order_item_inner_num, totalCount));

        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(dip * 105 * totalCount));
        holder.orderCellList.setLayoutParams(llp);

        if(holder.innerAdapter == null) {
            holder.innerAdapter = new OrderInnerListAdapter(mContext, order.orderItems);
            holder.orderCellList.setAdapter(holder.innerAdapter);
        }
        else holder.innerAdapter.setDataList(order.orderItems);

        holder.orderCancelBtn.setTag(order.id);
        holder.orderPayNowBtn.setTag(order.orderItems);

        holder.innerAdapter.notifyDataSetChanged();

        return convertView;
    }

    private class CellHolder {

        public TextView orderNumTv;
        public TextView orderCreateTv;
        public ListView orderCellList;
        public TextView orderStatuesTv;
        public TextView orderTotalPriceTv;
        public TextView orderTotalCountTv;
        public TextView orderPayNowBtn;
        public TextView orderCancelBtn;
        public View orderBtnCon;

        public OrderInnerListAdapter innerAdapter;

        public CellHolder(View cellView) {

            orderNumTv = (TextView) cellView.findViewById(R.id.order_cell_num);
            orderCreateTv = (TextView) cellView.findViewById(R.id.order_cell_create);
            orderCellList = (ListView) cellView.findViewById(R.id.order_cell_list);
            orderStatuesTv = (TextView) cellView.findViewById(R.id.order_cell_state);
            orderTotalPriceTv = (TextView) cellView.findViewById(R.id.order_item_money);
            orderTotalCountTv = (TextView) cellView.findViewById(R.id.order_cell_total_count);
            orderPayNowBtn = (TextView) cellView.findViewById(R.id.order_paynow_btn);
            orderCancelBtn = (TextView) cellView.findViewById(R.id.order_cancel_btn);
            orderBtnCon = cellView.findViewById(R.id.order_btn_con);

            orderPayNowBtn.setOnClickListener(mCellBtnClick);
            orderCancelBtn.setOnClickListener(mCellBtnClick);

        }

    }

    private View.OnClickListener mCellBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.order_paynow_btn:

                    ArrayList<Course> orderItems = (ArrayList<Course>) v.getTag();
                    Intent intent = new Intent(mContext, OrderPayActivity.class);
                    intent.putExtra(OrderPayActivity.KEY_PAY_COURSE_LIST, orderItems);
                    mContext.startActivity(intent);

                    break;
                case R.id.order_cancel_btn:

                    String orderId = (String) v.getTag();
                    cancelOrder(StaticConfigs.mLoginResult.accessToken, orderId);

                    break;

            }
        }
    };

    protected void cancelOrder(String token, String orderId) {

        Map<String, Object> kvs = new HashMap<String, Object>();

        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
        kvs.put("id", orderId);

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCancelOrdre(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                boolean state = false;
                String message = "";

                try {

                    Log.i(TAG, response.toString());

                    if(response.has("State")) state = response.getBoolean("State");
                    if(response.has("Message")) message = response.getString("Message");

                    if(state) {

                        ((OrderActivity) mContext).loadCartList();

                    } else {

                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                    }


                } catch (Exception e) {

                    Log.e(TAG, e.getMessage() + "");

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, error.getMessage() + "");

            }

        });

        mRequestQueue.add(jreq);

    }


}

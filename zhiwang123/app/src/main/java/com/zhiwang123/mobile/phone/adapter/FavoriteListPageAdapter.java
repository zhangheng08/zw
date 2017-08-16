package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictSideSlipCell;

import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/6/22.
 */
public class FavoriteListPageAdapter extends BaseZwAdapter {

    private static final String TAG = "CourseCenterListPageAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mFavoriteList;
    private XRestrictListView mHostListView;

    public FavoriteListPageAdapter(Activity c, ArrayList<Course> list, XRestrictListView hostlistView) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mFavoriteList = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
        mHostListView = hostlistView;
    }

    public void setDataList(ArrayList<Course> dataList) {

        if(mFavoriteList != null) mFavoriteList.clear();
        else mFavoriteList = new ArrayList<>();
        mFavoriteList.addAll(dataList);

    }

    public void addDataList(ArrayList<Course> dataList) {

        if(mFavoriteList == null) mFavoriteList = new ArrayList<>();
        mFavoriteList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mFavoriteList.size();
    }

    @Override
    public Course getItem(int position) {
        return mFavoriteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course c = mFavoriteList.get(position);
        CellHolder cellholder = null;
        View contentView = null;

        if(convertView == null) {

            contentView = mContext.getLayoutInflater().inflate(R.layout.main_fragment_page_cell_reuse, null);
            convertView = mContext.getLayoutInflater().inflate(R.layout.zh_list_item_layout, null);
            ((ViewGroup)convertView.findViewById(R.id.content_layer)).addView(contentView);

            cellholder = new CellHolder(convertView);
            convertView.setTag(cellholder);
        } else {
            cellholder = (CellHolder) convertView.getTag();
        }

        cellholder.c = c;
        Glide.with(mContext).load(c.picture).crossFade().into(cellholder.imgv);
        cellholder.titleTxv.setText(c.name);
        cellholder.studyTimeTxv.setText(mContext.getString(R.string.cell_course_time, c.courseHour));
        cellholder.teacherTxv.setText(mContext.getString(R.string.course_teacher, c.teacherName));
        cellholder.priceTxv.setText(c.money + "");

        cellholder.imgvFav.setTag(c);

        cellholder.addCart.setTag(c);

//        if(c.isInFavorite) {
//            cellholder.imgvFav.setImageResource(R.drawable.v_dis_fav);
//        } else {
//            cellholder.imgvFav.setImageResource(R.drawable.v_favorite);
//        }

        if(c.isInCart) {
            cellholder.imgvCart.setImageResource(R.drawable.cart_list);
        } else {
            cellholder.imgvCart.setImageResource(R.drawable.cart_list_dis);
        }

        cellholder.scrollContainer.notifyLayerState(false);
        cellholder.mDeleteTriggerView.setTag(null);

        return convertView;
    }

    public class CellHolder implements XRestrictSideSlipCell.Callback {

        public Course c;
        public TextView titleTxv;
        public TextView studyTimeTxv;
        public TextView teacherTxv;
        public TextView priceTxv;
        public ImageView imgv;
        public ImageView imgvFav;
        public View imgFavCon;
        public ImageView imgvCart;
        public View addCart;

        public XRestrictSideSlipCell scrollContainer;
        public TextView mDeleteTriggerView;

        public CellHolder(View cellview) {

            titleTxv = (TextView) cellview.findViewById(R.id.cell_title);
            studyTimeTxv = (TextView) cellview.findViewById(R.id.cell_sub_title);
            teacherTxv = (TextView) cellview.findViewById(R.id.cell_sub_teacher);
            priceTxv = (TextView) cellview.findViewById(R.id.nc_rmb_value);
            imgv = (ImageView) cellview.findViewById(R.id.cell_left_img);
            imgvFav = (ImageView) cellview.findViewById(R.id.cell_img_fav);
            imgvCart = (ImageView) cellview.findViewById(R.id.nc_cell_cart_img);
            addCart = cellview.findViewById(R.id.nc_cell_add_to);
            imgFavCon = cellview.findViewById(R.id.cell_img_fav_con);

//            titleTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            studyTimeTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            teacherTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            priceTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

            //imgvFav.setOnClickListener(onClickListener);
            imgFavCon.setVisibility(View.INVISIBLE);
            addCart.setOnClickListener(onClickListener);

            scrollContainer = (XRestrictSideSlipCell) cellview.findViewById(R.id.scroll_container);
            mDeleteTriggerView = (TextView) cellview.findViewById(R.id.click_event_trigger);
            scrollContainer.setListView(mHostListView);
            scrollContainer.setCallback(this);
            scrollContainer.notifyLayerState(false);

        }

        @Override
        public void delete(Context context) {

            removedFavorite(c);
            mFavoriteList.remove(c);
            notifyDataSetChanged();

        }

        @Override
        public void expand(boolean isExpand) {

        }

        @Override
        public void clickEvent() {

        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Course c = (Course) v.getTag();

                switch(v.getId()) {

//                case R.id.cell_img_fav:
//
//
//                    ImageView img = (ImageView) v;
//
//                    if(c.isInFavorite) {
//
//                        img.setImageResource(R.drawable.v_favorite);
//                        c.isInFavorite = false;
//                        removedFavorite(c.favoriteId);
//
//                    } else {
//
//                        img.setImageResource(R.drawable.v_dis_fav);
//                        c.isInFavorite = true;
//                        addFavorite(c.courseid, c.courseType);
//
//                    }
//
//                    break;

                    case R.id.nc_cell_add_to:

                        ImageView cartImg = (ImageView) v.findViewById(R.id.nc_cell_cart_img);

                        if(c.isInCart) {

                            removeCart(c.cartId);

                        } else {

                            toAddCart();

                        }

                        break;
                }

            }
        };

        protected void toAddCart() {

            Map<String, Object> kvs = new HashMap<String, Object>();

            kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
            kvs.put("CourseId", c.courseid);
            kvs.put("CourseType", c.courseType);

            JSONObject params = new JSONObject(kvs);

            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCartAdd(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    boolean state = false;
                    String message = "";
                    String cartId = "";

                    try {


                        if(response.has("State")) state = response.getBoolean("State");
                        if(response.has("Message")) message = response.getString("Message");
                        if(response.has("DataObject")) cartId = response.getString("DataObject");

                        if(state) {

                            Toast.makeText(mContext, "成功加入购物车", Toast.LENGTH_LONG).show();
                            c.cartId = cartId;
                            c.isInCart = true;

                            SharedPreferences sp = mContext.getSharedPreferences("cart_kv", 0);
                            sp.edit().putString(c.courseid, c.cartId).commit();

                            imgvCart.setImageResource(R.drawable.cart_list);

                        } else {

                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    error.printStackTrace();

                }

            });

            mRequestQueue.add(jreq);

        }

        private void removeCart(String ids) {

            Map<String, String> kvs = new HashMap<String, String>();

            kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
            kvs.put("Ids", ids);

            JSONObject params = new JSONObject(kvs);

            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCartRemove(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    boolean state = false;
                    String message = "";

                    try {


                        if(response.has("State")) state = response.getBoolean("State");
                        if(response.has("Message")) message = response.getString("Message");

                        if(state) {

                            Toast.makeText(mContext, "从购物车移除成功", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = mContext.getSharedPreferences("cart_kv", 0);
                            sp.edit().remove(c.courseid).commit();
                            c.cartId = null;
                            c.isInCart = false;
                            imgvCart.setImageResource(R.drawable.cart_list_dis);

                        } else {

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }

            });

            mRequestQueue.add(jreq);


        }

    }

    @Override
    protected void onRemFav() {

        notifyDataSetChanged();

    }

    protected void removedFavorite(final Course c) {

        if(TextUtils.isEmpty(c.id)) return;

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
        kvs.put("Id", c.id);

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlRemovedFav(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean state = false;

                    String message = "";

                    if(response.has("State")) {
                        state = response.getBoolean("State");
                    }

                    if(state) {

                        Toast.makeText(mContext, R.string.fav_del_sucs, Toast.LENGTH_LONG).show();
                        SharedPreferences sp = mContext.getSharedPreferences("favorite_kv", 0);
                        sp.edit().remove(c.courseid).commit();


                    } else {

                        if(response.has("Message")) message = response.getString("Message");

                        Toast.makeText(mContext, message + "---", Toast.LENGTH_LONG).show();

                    }

                } catch(Exception e) {

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, error.getMessage() + "+++", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(jreq);

    }


}

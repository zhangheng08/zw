package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.zhiwang123.mobile.phone.activity.LoginActivity;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseCart;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/6/22.
 */
public class CourseListPageAdapter extends BaseZwAdapter {

    private static final String TAG = "CourseCenterListPageAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<Course> mDataList;
    private XRestrictListView mHostListView;

    public CourseListPageAdapter(Activity c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mDataList = list;
        fetchCart();

    }

    public void setDataList(ArrayList<Course> dataList) {

        if(mDataList != null) mDataList.clear();
        else mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        fetchCart();

    }

    public void addDataList(ArrayList<Course> dataList) {

        if(mDataList == null) mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        fetchCart();

    }

    @Override
    public int getCount() {
        return  mDataList.size();
    }

    @Override
    public Course getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course c = mDataList.get(position);
        CellHolder cellholder = null;
        View contentView = null;

        if(convertView == null) {

            convertView = mContext.getLayoutInflater().inflate(R.layout.main_fragment_page_cell_reuse, null);
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

        if(c.money == 0) {

            cellholder.buyBarFree.setVisibility(View.VISIBLE);
            cellholder.buyBar.setVisibility(View.GONE);

        } else {

            cellholder.buyBar.setVisibility(View.VISIBLE);
            cellholder.buyBarFree.setVisibility(View.GONE);
            cellholder.priceTxv.setText(c.money + "");

        }

        if(c.courseType == 1) {

            cellholder.studyCountTxv.setVisibility(View.VISIBLE);
            cellholder.studyCountTxv.setText(mContext.getString(R.string.cell_course_count, c.childCourseNum));

        } else {

            cellholder.studyCountTxv.setVisibility(View.INVISIBLE);

        }

        cellholder.imgvFav.setTag(c);

        cellholder.addCart.setTag(c);

        if(c.isInFavorite) {
            cellholder.imgvFav.setImageResource(R.drawable.v_dis_fav);
        } else {
            cellholder.imgvFav.setImageResource(R.drawable.v_favorite);
        }

        if(c.isInCart) {
            cellholder.imgvCart.setImageResource(R.drawable.cart_list);
        } else {
            cellholder.imgvCart.setImageResource(R.drawable.cart_list_dis);
        }

        return convertView;
    }

    public class CellHolder {

        public Course c;
        public TextView titleTxv;

        public TextView studyTimeTxv;
        public TextView studyCountTxv;

        public TextView teacherTxv;
        public TextView priceTxv;
        public ImageView imgv;
        public ImageView imgvFav;
        public View imgFavCon;
        public ImageView imgvCart;
        public View addCart;

        public View buyBar;
        public View buyBarFree;

        public CellHolder(View cellview) {

            titleTxv = (TextView) cellview.findViewById(R.id.cell_title);
            studyTimeTxv = (TextView) cellview.findViewById(R.id.cell_sub_title);
            studyCountTxv = (TextView) cellview.findViewById(R.id.cell_sub_title2);
            teacherTxv = (TextView) cellview.findViewById(R.id.cell_sub_teacher);
            priceTxv = (TextView) cellview.findViewById(R.id.nc_rmb_value);
            imgv = (ImageView) cellview.findViewById(R.id.cell_left_img);
            imgvFav = (ImageView) cellview.findViewById(R.id.cell_img_fav);
            imgvCart = (ImageView) cellview.findViewById(R.id.nc_cell_cart_img);
            addCart = cellview.findViewById(R.id.nc_cell_add_to);
            imgFavCon = cellview.findViewById(R.id.cell_img_fav_con);

            buyBar = cellview.findViewById(R.id.nc_buy_bar);
            buyBarFree = cellview.findViewById(R.id.nc_buy_free_bar);

//            titleTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            studyTimeTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            teacherTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            priceTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

            imgvFav.setOnClickListener(onClickListener);
            addCart.setOnClickListener(onClickListener);

        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {

                    case R.id.cell_img_fav:


                        ImageView img = (ImageView) v;

                        if(c.isInFavorite) {

                            removedFavorite(c.favoriteId);

                        } else {

                            addFavorite();

                        }

                        break;

                    case R.id.nc_cell_add_to:

                        if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) {

                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivityForResult(intent, StaticConfigs.REQUEST_CODE_LOGIN);
                            return;
                        }

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

        protected void addFavorite() {

            Map<String, String> kvs = new HashMap<String, String>();
            kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
            kvs.put("CourseId", c.courseid);
            kvs.put("CourseType", c.courseType + "");

            JSONObject params = new JSONObject(kvs);

            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlAddFavorite(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        boolean state = false;
                        String message = "";

                        boolean bo = false;

                        if(response.has("State")) {
                            bo = response.getBoolean("State");
                        }

                        if(bo) {

                            Toast.makeText(mContext, R.string.fav_add_sucs, Toast.LENGTH_SHORT).show();

                            String favId = response.getString("DataObject");

                            SharedPreferences sp = mContext.getSharedPreferences("favorite_kv", 0);
                            sp.edit().putString(c.courseid, favId).commit();
                            c.favoriteId = favId;
                            imgvFav.setImageResource(R.drawable.v_dis_fav);
                            c.isInFavorite = true;

                        } else {

                            if(response.has("Message")) message = response.getString("Message");
                            Toast.makeText(mContext, message + "", Toast.LENGTH_SHORT).show();

                        }

                    } catch(Exception e) {

                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mContext, error.getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            });

            mRequestQueue.add(jreq);

        }

        protected void removedFavorite(String favId) {

            if(TextUtils.isEmpty(favId)) return;

            Map<String, String> kvs = new HashMap<String, String>();
            kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
            kvs.put("Id", favId);

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

                            Toast.makeText(mContext, R.string.fav_del_sucs, Toast.LENGTH_SHORT).show();
                            c.favoriteId = null;
                            SharedPreferences sp = mContext.getSharedPreferences("favorite_kv", 0);
                            sp.edit().remove(c.courseid).commit();
                            imgvFav.setImageResource(R.drawable.v_favorite);
                            c.isInFavorite = false;


                        } else {

                            if(response.has("Message")) message = response.getString("Message");

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                        }

                    } catch(Exception e) {

                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            mRequestQueue.add(jreq);

        }

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

                            c.cardId = null;
                            c.isInCart = false;
                            SharedPreferences sp = mContext.getSharedPreferences("cart_kv", 0);
                            sp.edit().remove(c.courseid).commit();
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


    private void fetchCart() {

        if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) return;

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCartlist(StaticConfigs.mLoginResult.accessToken), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseCart courseCart = new CourseParser<CourseCart>(new CourseCart()).parse(response);
                        ArrayList<Course> cList = courseCart.dataobject;

                        for(Course c : cList) {

                            for(Course nc : mDataList) {

                                if(nc.courseid.equals(c.courseid)) {

                                    nc.isInCart = true;
                                    break;

                                }

                            }

                        }

                        notifyDataSetChanged();


                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error.getMessage());

                    }
                });

        mRequestQueue.add(jsonRequest);
    }


}

package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.CourseIntroduceActivity;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CourseSubAdapter extends Adapter<CourseSubAdapter.RecommandViewHolder> {

    public static final String TAG = "CourseSubAdapter";

    public OnItemClickMonitor mOnItemClickMonitor;
    public ArrayList<Course> mDataset;
    public Context mContext;

    private RequestQueue mRequestQueue;

    public CourseSubAdapter(Context context, ArrayList<Course> data) {
        mContext = context;
        mDataset = new ArrayList<>();
        mDataset.addAll(data);
        mRequestQueue = Volley.newRequestQueue(context);
//        fetchFavorite();
//        fetchCart();
    }

    public void setDataset(ArrayList<Course> newDataset) {
        if(mDataset != null) mDataset.clear();
        mDataset.addAll(newDataset);
    }

    public void clearDataset() {

        if(mDataset != null) mDataset.clear();

    }

    @Override
    public int getItemCount() {
        return mDataset.size();

    }

    @Override
    public void onBindViewHolder(RecommandViewHolder holder, int position) {
        Course c = mDataset.get(position);
        holder.fillData(c);
        if(position == mDataset.size() - 1) holder.rightMargin.setVisibility(View.VISIBLE);
        else holder.rightMargin.setVisibility(View.GONE);
    }



    @Override
    public RecommandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.course_fragment_page_cell_sub, null);
        RecommandViewHolder holder = new RecommandViewHolder(view);
        return holder;
    }


    public class RecommandViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public View favoriteImgCon;
        public ImageView favoriteImg;
        public TextView cellTitleTxv;
        public TextView rmbValue;
        public View rightMargin;
        public ImageView cellAddto;
        public View rootView;

        public Course c;


        public RecommandViewHolder(View itemLayout) {

            super(itemLayout);
            rootView = itemLayout.findViewById(R.id.course_line_sub_root);
            img = (ImageView) itemLayout.findViewById(R.id.cell_recommand_img);
            favoriteImgCon = itemLayout.findViewById(R.id.cell_recommand_favorite_con);
            favoriteImg = (ImageView) itemLayout.findViewById(R.id.cell_recommand_fav);
            cellTitleTxv = (TextView) itemLayout.findViewById(R.id.cell_recommand_name);
            rmbValue = (TextView) itemLayout.findViewById(R.id.rmb_value);
            rightMargin = itemLayout.findViewById(R.id.right_margin);
            cellAddto = (ImageView) itemLayout.findViewById(R.id.cell_add_to);

//            cellTitleTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            rmbValue.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

            rootView.setOnClickListener(onClickListener);
            favoriteImg.setOnClickListener(onClickListener);
            cellAddto.setOnClickListener(onClickListener);
        }

        public void fillData(final Course c) {

            this.c = c;

            Glide.with(mContext).load(c.picture).crossFade().into(img);
            cellTitleTxv.setText(c.name);
            rmbValue.setText(c.money + "");

            favoriteImg.setTag(c);
            favoriteImg.setImageResource(c.isInFavorite ? R.drawable.v_dis_fav : R.drawable.v_favorite);

            cellAddto.setTag(c);
            cellAddto.setImageResource(c.isInCart ? R.drawable.cart_list : R.drawable.cart_list_dis);

        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StaticConfigs.loginCheck(mContext)) return;

                switch(v.getId()) {

                    case R.id.course_line_sub_root:

                        String courseId = c.courseid;
                        boolean isFav = c.isInFavorite;
                        Intent intent = new Intent(mContext, CourseIntroduceActivity.class);
                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_CID, courseId);
                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_COURSE_TYPE, c.courseType);
                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_FAVSTATE, isFav);
                        mContext.startActivity(intent);

                        break;

                    case R.id.cell_recommand_fav:


                        ImageView img = (ImageView) v;

                        if(c.isInFavorite) {

                            removedFavorite(c.favoriteId);

                        } else {

                            addFavorite();

                        }

                        break;

                    case R.id.cell_add_to:

                        ImageView cartImg = (ImageView) v;

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
                            favoriteImg.setImageResource(R.drawable.v_dis_fav);
                            c.isInFavorite = true;

                        } else {

                            if(response.has("Message")) message = response.getString("Message");

                            Toast.makeText(mContext, message + "", Toast.LENGTH_SHORT).show();

                        }

                    } catch(Exception e) {

                        Log.i(TAG, e.getMessage() + "");

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
                            favoriteImg.setImageResource(R.drawable.v_favorite);
                            c.isInFavorite = false;


                        } else {

                            if(response.has("Message")) message = response.getString("Message");

                            Toast.makeText(mContext, message + "---", Toast.LENGTH_SHORT).show();

                        }

                    } catch(Exception e) {

                        Log.i(TAG, e.getMessage() + "");

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(mContext, error.getMessage() + "+++", Toast.LENGTH_SHORT).show();
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

                        Log.i(TAG, response.toString());

                        if(response.has("State")) state = response.getBoolean("State");
                        if(response.has("Message")) message = response.getString("Message");
                        if(response.has("DataObject")) cartId = response.getString("DataObject");

                        if(state) {

                            Toast.makeText(mContext, "成功加入购物车", Toast.LENGTH_LONG).show();
                            c.cartId = cartId;
                            c.isInCart = true;

                            SharedPreferences sp = mContext.getSharedPreferences("cart_kv", 0);
                            sp.edit().putString(c.courseid, c.cartId).commit();

                            cellAddto.setImageResource(R.drawable.cart_list);

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

                        Log.i(TAG, response.toString());

                        if(response.has("State")) state = response.getBoolean("State");
                        if(response.has("Message")) message = response.getString("Message");

                        if(state) {

                            Toast.makeText(mContext, "从购物车移除成功", Toast.LENGTH_SHORT).show();

                            c.cardId = null;
                            c.isInCart = false;
                            SharedPreferences sp = mContext.getSharedPreferences("cart_kv", 0);
                            sp.edit().remove(c.courseid).commit();
                            cellAddto.setImageResource(R.drawable.cart_list_dis);

                        } else {

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

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

    public void setOnItemClickMonitor(OnItemClickMonitor monitor) {
        mOnItemClickMonitor = monitor;
    }

    public interface OnItemClickMonitor {

        public void onClick(RecommandViewHolder viewHolder, int position, View cellView);

    }


//    protected void addFavorite(String courseId, int courseType) {
//
//        Map<String, String> kvs = new HashMap<String, String>();
//        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
//        kvs.put("CourseId", courseId);
//        kvs.put("CourseType", courseType + "");
//
//        JSONObject params = new JSONObject(kvs);
//
//        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlAddFavorite(), params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    boolean state = false;
//                    String message = "";
//
//                    if(response.has("State")) {
//
//                        Toast.makeText(mContext, R.string.fav_add_sucs, Toast.LENGTH_LONG).show();
//
//                    } else {
//
//                        if(response.has("Message")) message = response.getString("Message");
//
//                        Toast.makeText(mContext, message + "", Toast.LENGTH_LONG).show();
//
//                    }
//
//                } catch(Exception e) {
//
//                    Log.i(TAG, e.getMessage() + "");
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(mContext, error.getMessage() + "", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        mRequestQueue.add(jreq);
//
//    }
//
//    protected void removedFavorite(String favId) {
//
//        if(TextUtils.isEmpty(favId)) return;
//
//        Map<String, String> kvs = new HashMap<String, String>();
//        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
//        kvs.put("Id", favId);
//
//        JSONObject params = new JSONObject(kvs);
//
//        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlRemovedFav(), params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    boolean state = false;
//                    String message = "";
//
//                    if(response.has("State")) {
//
//                        Toast.makeText(mContext, R.string.fav_del_sucs, Toast.LENGTH_LONG).show();
//
//                    } else {
//
//                        if(response.has("Message")) message = response.getString("Message");
//
//                        Toast.makeText(mContext, message + "---", Toast.LENGTH_LONG).show();
//
//                    }
//
//                } catch(Exception e) {
//
//                    Log.i(TAG, e.getMessage() + "");
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(mContext, error.getMessage() + "+++", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        mRequestQueue.add(jreq);
//
//    }
//
//    protected void toAddCart(String courseId, int courseType) {
//
//        Map<String, Object> kvs = new HashMap<String, Object>();
//
//        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
//        kvs.put("CourseId", courseId);
//        kvs.put("CourseType", courseType);
//
//        JSONObject params = new JSONObject(kvs);
//
//        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCartAdd(), params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                boolean state = false;
//                String message = "";
//
//                try {
//
//                    Log.i(TAG, response.toString());
//
//                    if(response.has("State")) state = response.getBoolean("State");
//                    if(response.has("Message")) message = response.getString("Message");
//
//                    if(state) {
//
//
//                    } else {
//
//                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
//
//                    }
//
//
//                } catch (Exception e) {
//
//                    Log.e(TAG, e.getMessage() + "");
//
//                }
//
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.e(TAG, error.getMessage() + "");
//
//            }
//
//        });
//
//        mRequestQueue.add(jreq);
//
//    }

//    public void fetchFavorite() {
//
//        if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) return;
//
//        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlListFav(StaticConfigs.mLoginResult.accessToken, "360*270", 1, 10), null,
//
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        CourseFavorite cv = new CourseParser<CourseFavorite>(new CourseFavorite()).parse(response);
//                        ArrayList<Course> flist = cv.dataobject;
//
//                        for(Course c : flist) {
//
//                            for(Course nc : mDataset) {
//
//                                if(nc.courseid.equals(c.courseid)) {
//
//                                    nc.isInFavorite = true;
//                                    break;
//
//                                }
//
//                            }
//
//                        }
//
//                        notifyDataSetChanged();
//
//                    }
//                },
//                new Response.ErrorListener(){
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//        mRequestQueue.add(jsonRequest);
//    }
//
//    private void fetchCart() {
//
//        if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) return;
//
//        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCartlist(StaticConfigs.mLoginResult.accessToken), null,
//
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        CourseCart courseCart = new CourseParser<CourseCart>(new CourseCart()).parse(response);
//                        ArrayList<Course> cList = courseCart.dataobject;
//
//                        for(Course c : cList) {
//
//                            for(Course nc : mDataset) {
//
//                                if(nc.courseid.equals(c.courseid)) {
//
//                                    nc.isInCart = true;
//                                    break;
//
//                                }
//
//                            }
//
//                        }
//
//                        notifyDataSetChanged();
//
//
//                    }
//                },
//                new Response.ErrorListener(){
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, error.getMessage());
//                    }
//                });
//
//        mRequestQueue.add(jsonRequest);
//    }



}
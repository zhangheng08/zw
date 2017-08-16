package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.ouchn.flipper.Animations.DescriptionAnimation;
import com.ouchn.flipper.Indicators.PagerIndicator;
import com.ouchn.flipper.SliderLayout;
import com.ouchn.flipper.SliderTypes.BaseSliderView;
import com.ouchn.flipper.SliderTypes.TextSliderView;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.TopicWebPageActivity;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseNewest;
import com.zhiwang123.mobile.phone.bean.CourseRoot;
import com.zhiwang123.mobile.phone.bean.Snippet;
import com.zhiwang123.mobile.phone.bean.Topic;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.SilderViewContainer;
import com.zhiwang123.mobile.phone.widget.TopicViewContainer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/6/22.
 */
public class FirstPageAdapter extends BaseZwAdapter {

    private static final String TAG = "FirstPageAdapter";

    private CourseNewest mNewestCourse;
    private SliderLayout mSlider;
    private PagerIndicator mIndicator;
    private LayoutInflater mInflater;
    private Snippet mFocuses;
    private CourseRoot mCourseRoot;
    private SilderViewContainer mFilpperContainer;
    private TopicViewContainer mTopicContainer;
    private RecyclerView mTopicRecv;

    public FirstPageAdapter(Context c, CourseNewest newest, Snippet focuses) {
        super(c);
        mNewestCourse = newest;
        mFocuses = focuses;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFilpperContainer = (SilderViewContainer) mInflater.inflate(R.layout.main_fragment_page_cell_flipper, null);
        mSlider = (SliderLayout) mFilpperContainer.findViewById(R.id.slider);
        mIndicator = (PagerIndicator) mFilpperContainer.findViewById(R.id.custom_indicator);
        initFocusList(mFocuses.focusPics);
        initTopicView();
//        fetchFavorite();
//        fetchCart();
    }

    public void setRecommandList(CourseNewest recommand) {
        mNewestCourse = recommand;
    }

    private void initTopicView() {

        mTopicContainer = (TopicViewContainer) mInflater.inflate(R.layout.main_fragment_page_cell_topic, null);
        mTopicRecv = (RecyclerView) mTopicContainer.findViewById(R.id.topic_list);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTopicRecv.setLayoutManager(recyclerViewLayoutManager);
        mTopicRecv.setHasFixedSize(true);
        loadCourseRoot();

        View fcCon = mTopicContainer.findViewById(R.id.free_con);
        TextView fcTv = (TextView) mTopicContainer.findViewById(R.id.free_course);
        TextView fcsubTv = (TextView) mTopicContainer.findViewById(R.id.free_course_sub);

        View barginCon = mTopicContainer.findViewById(R.id.bargin_con);
        TextView bcTv = (TextView) mTopicContainer.findViewById(R.id.bargain_course);
        TextView bcsubTv = (TextView) mTopicContainer.findViewById(R.id.bargain_course_sub);

        View teacherCon = mTopicContainer.findViewById(R.id.teacher_con);
        TextView trTv = (TextView) mTopicContainer.findViewById(R.id.teacher_rank);
        TextView trsubTv = (TextView) mTopicContainer.findViewById(R.id.teacher_rank_sub);

        View faceCon = mTopicContainer.findViewById(R.id.face_con);
        TextView facecTv = (TextView) mTopicContainer.findViewById(R.id.face_course);
        TextView facecsubTv = (TextView) mTopicContainer.findViewById(R.id.face_course_sub);

        View newsCon = mTopicContainer.findViewById(R.id.news_con);
        TextView naTv = (TextView) mTopicContainer.findViewById(R.id.news_action);
        TextView nasubTv = (TextView) mTopicContainer.findViewById(R.id.news_action_sub);

        TextView newsListTitltTv = (TextView) mTopicContainer.findViewById(R.id.cell_course_root);

        fcCon.setOnClickListener(mTopicClick);
        barginCon.setOnClickListener(mTopicClick);
        teacherCon.setOnClickListener(mTopicClick);
        faceCon.setOnClickListener(mTopicClick);
        newsCon.setOnClickListener(mTopicClick);

    }

    private void loadCourseRoot() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getCourseCustom(), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mCourseRoot = new CourseParser<CourseRoot>(new CourseRoot()).parse(response);
                        if(mCourseRoot != null && mCourseRoot.dataobject != null && mCourseRoot.dataobject.size() != 0) {
                            ArrayList<Topic> topics = new ArrayList<>();
                            for(Course c : mCourseRoot.dataobject) {
                                Topic topic = new Topic();
//                                topic.name = c.name;
//                                topic.pkId = c.pkId;

                                topic.title = c.title;
                                topic.pkIds = c.categoryPkIds;

                                if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_1_398))) {
                                    topic.imgResId = R.drawable.t398;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_2_414))) {
                                    topic.imgResId = R.drawable.t414;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_3_423))) {
                                    topic.imgResId = R.drawable.t423;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_4_620))) {
                                    topic.imgResId = R.drawable.t620;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_5_356))) {
                                    topic.imgResId = R.drawable.t356;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_6_367))) {
                                    topic.imgResId = R.drawable.t367;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_7_446))) {
                                    topic.imgResId = R.drawable.t446;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_8_382))) {
                                    topic.imgResId = R.drawable.t382;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_9_491))) {
                                    topic.imgResId = R.drawable.t491;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_10_505))) {
                                    topic.imgResId = R.drawable.t505;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_11_504))) {
                                    topic.imgResId = R.drawable.t504;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_12_465))) {
                                    topic.imgResId = R.drawable.t465;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_13_490))) {
                                    topic.imgResId = R.drawable.t490;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_14_604))) {
                                    topic.imgResId = R.drawable.t604;
                                } else {
                                    topic.imgResId = R.drawable.tmp_sit;
                                }
                                topics.add(topic);
                            }
                            TopicAdapter adapter = new TopicAdapter(mContext, topics);
                            mTopicRecv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });

        mRequestQueue.add(jsonRequest);
    }


    private void initFocusList(ArrayList<Snippet.FocusPic> data) {

        if(data == null) return;

        for (int i = 0; i < data.size(); i++) {
            final Snippet.FocusPic focusPic = data.get(i);

            TextSliderView textSliderView = new TextSliderView(mContext);
            textSliderView
                    .description(focusPic.title)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .image(focusPic.picture).empty(R.drawable.tmp_sit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", focusPic.title);
            mSlider.addSlider(textSliderView);
        }

        mSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mSlider.setCustomIndicator(mIndicator);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(8000);
        mSlider.setCurrentPosition(1);
        mSlider.addOnPageChangeListener(null);

    }

    @Override
    public int getCount() {
        return mNewestCourse.dataobject.size() + 2;
    }

    @Override
    public Course getItem(int position) {
        return mNewestCourse.dataobject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position == 0) {

            convertView = mFilpperContainer;

        } else if(position == 1) {

            convertView = mTopicContainer;

        } else {

            Course c = mNewestCourse.dataobject.get(position - 2);

            CellHolder cellholder = null;

            if(convertView != null) {
                if(convertView instanceof SilderViewContainer || convertView instanceof TopicViewContainer) {
                    convertView = null;
                    convertView = mInflater.inflate(R.layout.main_fragment_page_cell_reuse, null);
                    cellholder = new CellHolder(convertView);
                    convertView.setTag(cellholder);
                } else {
                    cellholder = (CellHolder) convertView.getTag();
                }
            } else {
                convertView = mInflater.inflate(R.layout.main_fragment_page_cell_reuse, null);
                cellholder = new CellHolder(convertView);
                convertView.setTag(cellholder);
            }

            cellholder.c = c;
            cellholder.courseId = c.courseid;
            Glide.with(mContext).load(c.picture).crossFade().into(cellholder.imgv);
            cellholder.titleTxv.setText(c.name);
            cellholder.studyTimeTxv.setText(mContext.getString(R.string.cell_course_time, c.courseHour));
            cellholder.teacherTxv.setText(mContext.getString(R.string.course_teacher, c.teacherName));

            if(c.money == 0) {

                cellholder.buyBarFree.setVisibility(View.VISIBLE);
                cellholder.buyBar.setVisibility(View.GONE);

            } else {

                cellholder.buyBar.setVisibility(View.VISIBLE);
                cellholder.buyBarFree.setVisibility(View.GONE);
                cellholder.priceTxv.setText(c.money + "");

            }

            if(c.money == 0) cellholder.imgvCart.setVisibility(View.INVISIBLE);
            else cellholder.imgvCart.setVisibility(View.VISIBLE);

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

            if(c.courseType == 1) {

                cellholder.studyCountTxv.setVisibility(View.VISIBLE);
                cellholder.studyCountTxv.setText(mContext.getString(R.string.cell_course_count, c.childCourseNum));

            } else {

                cellholder.studyCountTxv.setVisibility(View.INVISIBLE);

            }

            /*
            * Crop

                CropTransformation, CropCircleTransformation, CropSquareTransformation, RoundedCornersTransformation

            Color

                ColorFilterTransformation, GrayscaleTransformation

            Blur

                BlurTransformation

            Mask

                MaskTransformation

            GPU Filter (use GPUImage) Will require add dependencies for GPUImage.

                ToonFilterTransformation, SepiaFilterTransformation, ContrastFilterTransformation
                InvertFilterTransformation, PixelationFilterTransformation, SketchFilterTransformation
                SwirlFilterTransformation, BrightnessFilterTransformation, KuwaharaFilterTransformation VignetteFilterTransformation
            * */

//            Glide.with(mContext).load("").bitmapTransform(null).into(new ImageView(mContext));

        }

        return convertView;
    }

    public class CellHolder {

        public Course c;

        public String courseId;
        public TextView titleTxv;
        public TextView studyTimeTxv;
        public TextView studyCountTxv;
        public TextView teacherTxv;
        public TextView priceTxv;
        public ImageView imgv;
        public ImageView imgvFav;
        public ImageView imgvCart;
        public View addCart;

        public View buyBar;
        public View buyBarFree;

        public CellHolder(View cellview) {

            titleTxv = (TextView) cellview.findViewById(R.id.cell_title);
            studyTimeTxv = (TextView) cellview.findViewById(R.id.cell_sub_title);
            studyCountTxv = (TextView) cellview.findViewById(R.id.cell_sub_title2);
            teacherTxv = (TextView) cellview.findViewById(R.id.cell_sub_teacher);

            buyBar = cellview.findViewById(R.id.nc_buy_bar);
            buyBarFree = cellview.findViewById(R.id.nc_buy_free_bar);
            priceTxv = (TextView) cellview.findViewById(R.id.nc_rmb_value);

            imgv = (ImageView) cellview.findViewById(R.id.cell_left_img);
            imgvFav = (ImageView) cellview.findViewById(R.id.cell_img_fav);
            imgvCart = (ImageView) cellview.findViewById(R.id.nc_cell_cart_img);
            addCart = cellview.findViewById(R.id.nc_cell_add_to);

//            titleTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            studyTimeTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            teacherTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            priceTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

            imgvFav.setOnClickListener(onClickListener);
            addCart.setOnClickListener(onClickListener);

        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StaticConfigs.loginCheck(mContext)) return;

                switch(v.getId()) {

                    case R.id.cell_img_fav:

                        if(c.isInFavorite) {

                            removedFavorite(c.favoriteId);

                        } else {


                            addFavorite();

                        }

                        break;

                    case R.id.nc_cell_add_to:

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
                            imgvFav.setImageResource(R.drawable.v_favorite);
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
            kvs.put("CourseId", courseId);
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

                            imgvCart.setImageResource(R.drawable.cart_list);

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
                            imgvCart.setImageResource(R.drawable.cart_list_dis);

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
//                            for(Course nc : mNewestCourse.dataobject) {
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
//                            for(Course nc : mNewestCourse.dataobject) {
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
	
	private View.OnClickListener mTopicClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext, TopicWebPageActivity.class);

            switch (v.getId()) {

                case R.id.free_con:

                    intent.putExtra(TopicWebPageActivity.KEY_URL, "http://www.zhiwang123.com/ThrCourse/Index");
                    intent.putExtra(TopicWebPageActivity.KEY_NAME, "免费课程");

                    break;
                case R.id.bargin_con:

                    intent.putExtra(TopicWebPageActivity.KEY_URL, "http://www.zhiwang123.com/ThrCourse/Index");
                    intent.putExtra(TopicWebPageActivity.KEY_NAME, "超值套餐");

                    break;
                case R.id.teacher_con:

                    intent.putExtra(TopicWebPageActivity.KEY_URL, "http://www.zhiwang123.com/ThrExpert/List");
                    intent.putExtra(TopicWebPageActivity.KEY_NAME, "优秀讲师");

                    break;
                case R.id.face_con:

                    intent.putExtra(TopicWebPageActivity.KEY_URL, "http://www.zhiwang123.com/Pages/FaceTeach/");
                    intent.putExtra(TopicWebPageActivity.KEY_NAME, "面授课程");

                    break;
                case R.id.news_con:

                    //intent = new Intent(mContext, CmsListActivity.class);
                    intent.putExtra(TopicWebPageActivity.KEY_URL, "http://www.zhiwang123.com/news/2012-05-04/3ff43dd6-8e10-44ed-9266-749319467ab1.htm");
                    intent.putExtra(TopicWebPageActivity.KEY_NAME, "新闻动态");

                    break;
            }

            mContext.startActivity(intent);

        }

    };


}

package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.ChapterListAdapter;
import com.zhiwang123.mobile.phone.adapter.TeacherListAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseIntr;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.MethodUtils;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.wxapi.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/10/27.
 */

public class CourseIntroduceActivity extends BaseActivity {

    private static final String TAG = "CourseIntroduceActivity";

    private Course mEntity;

    private TextView mTitxtTx;
    private TextView mPriceTx;
    private TextView mStudyHoursTx;
    private TextView mIfTestTx;
    private RecyclerView mTeachersRecv;
    private TextView mDescTx;
    private TextView mDescTxExp;
    private ListView mChildCourseListView;

    private TextView mCartTx;
    private TextView mAskTx;
    private TextView mCartCountTx;
    private ImageView mFavImg;

    private ImageView mMainImagev;

    private View mDescExpBtn;
    private TextView mBuyNowBtn;
    private View mAddCartBtn;
    private View mAskBtn;
    private View mCartBtn;
    private View mGoBackBtn;
    private View mFavBtn;
    private View mShareBtn;
    private View mChildListBolck;

    private CourseIntr mCourseIntr;

    private Context mContext;

    private String mCourseId;

    private boolean isFav;

    private int courseType;

    private String mFavoriteId;

    public static final String EXTRA_KEY_CID = "courseId";
    public static final String EXTRA_KEY_FAVSTATE = "favorite_state";
    public static final String EXTRA_KEY_COURSE_TYPE = "course_type";

    private View progressLayer;

    private View shareLayer;
    private View shareBlank;
    private View shareActionWxcs;
    private View shareActionWxtl;
    private View shareActionQQ;
    private View shareActionSina;


    private boolean is0MoneyToBuy = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.course_intro_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.zw_trans);
        }

        mCourseId = getIntent().getStringExtra(EXTRA_KEY_CID);
        isFav = getIntent().getBooleanExtra(EXTRA_KEY_FAVSTATE, false);
        courseType = getIntent().getIntExtra(EXTRA_KEY_COURSE_TYPE, -1);

        initLayout();

        loadCourseIntroduce();
    }

    private void loadCourseIntroduce() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseIntr(mCourseId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mCourseIntr = new CourseParser<CourseIntr>(new CourseIntr()).parse(response);
                        Course c = mCourseIntr.dataobject.get(0);
                        mEntity = c;

                        Glide.with(mContext).load(c.picture).crossFade().into(mMainImagev);
                        mTitxtTx.setText(c.name);
                        if(c.money != 0) mPriceTx.setText(mContext.getString(R.string.course_price, c.money));
                        else mPriceTx.setText("免费");
                        mStudyHoursTx.setText(mContext.getString(R.string.course_hourse, c.courseHour));

                        is0MoneyToBuy = c.money == 0 ? true : false;

                        if(is0MoneyToBuy) {

                            mAddCartBtn.setVisibility(View.INVISIBLE);
                            mCartBtn.setVisibility(View.INVISIBLE);
                            mBuyNowBtn.setText("立即学习");
                            is0MoneyToBuy = true;

                        }

                        if(c.isHaveTestPaper && c.isHaveExamNum && c.judgeWhetherExam != StaticConfigs.ISNULL) {

                            mIfTestTx.setVisibility(View.VISIBLE);

                            switch (c.judgeWhetherExam) {
                                case StaticConfigs.ISEXAM:
                                    mIfTestTx.setText("含考试");
                                    break;
                                case StaticConfigs.ISTEST:
                                    mIfTestTx.setText("含测试");
                                    break;
                            }

                        } else {

                            mIfTestTx.setVisibility(View.INVISIBLE);

                        }

//                        mTitxtTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//                        mPriceTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//                        mStudyHoursTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

                        TeacherListAdapter teacherListAdapter = new TeacherListAdapter(mContext, c.teachers);
                        mTeachersRecv.setAdapter(teacherListAdapter);
                        teacherListAdapter.notifyDataSetChanged();

                        mDescTx.setText(Html.fromHtml(c.courseDescription));
                        mDescTxExp.setText(Html.fromHtml(c.courseDescription));

//                        mDescTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//                        mDescTxExp.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

                        if(c.childCourses != null && c.childCourses.size() > 1) {

                            int itemCount = c.childCourses.size();
                            float itemNeedH = MethodUtils.getDip(CourseIntroduceActivity.this) * 50;
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) itemNeedH * itemCount);
                            mChildCourseListView.setLayoutParams(llp);
                            mChildCourseListView.setDividerHeight(0);
                            mChildCourseListView.setCacheColorHint(getResources().getColor(R.color.zw_trans));
                            ChapterListAdapter chapterListAdapter = new ChapterListAdapter(mContext, c.childCourses);
                            mChildCourseListView.setAdapter(chapterListAdapter);
                            chapterListAdapter.notifyDataSetChanged();

                            if(c.money != 0) {

                                mChildCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        ChapterListAdapter.CellHolder holder = (ChapterListAdapter.CellHolder) view.getTag();
                                        String courseId = holder.courseId;
                                        Intent intent = new Intent(mContext, CourseIntroduceActivity.class);
                                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_CID, courseId);
                                        startActivity(intent);

                                    }
                                });

                            }

                        } else {

                            mChildListBolck.setVisibility(View.INVISIBLE);

                        }


                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage() + "");
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);
    }

    private void loadCartNum() {

        if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) return;

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCartNum(StaticConfigs.mLoginResult.accessToken), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        int num = 0;

                        if(response.has("Message")) try {
                            num = response.getInt("Message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mCartCountTx.setText(num + "");

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private void toAddCart(String courseId, int courseType) {

        if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) {

            Toast.makeText(mContext, "您还没有登录哦~", Toast.LENGTH_SHORT).show();
            return;

        }

        Map<String, Object> kvs = new HashMap<String, Object>();

        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
        kvs.put("CourseId", courseId);
        kvs.put("CourseType", courseType);

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCartAdd(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                boolean state = false;
                String message = "";

                try {

                    Log.i(TAG, response.toString());

                    if(response.has("State")) state = response.getBoolean("State");
                    if(response.has("Message")) message = response.getString("Message");

                    if(state) {

                        loadCartNum();

                        sendBroadcast(new Intent(StaticConfigs.CART_CHANGE_ACITON));

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

        mVolleyRequestQueue.add(jreq);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCartNum();
    }


    public void initLayout() {

        mGoBackBtn = findViewById(R.id.c_intr_go_back);
        mTitxtTx = (TextView) findViewById(R.id.c_intr_course_title);
        mPriceTx = (TextView) findViewById(R.id.c_intr_course_rmb);
        mStudyHoursTx = (TextView) findViewById(R.id.c_intr_course_hours);
        mIfTestTx = (TextView) findViewById(R.id.c_intr_iftest);
        mDescTx = (TextView) findViewById(R.id.c_intr_course_desc);
        mDescTxExp = (TextView) findViewById(R.id.c_intr_course_desc_exp);
        mDescExpBtn = findViewById(R.id.c_intr_desc_exp_btn);
        mDescExpBtn.setOnClickListener(onViewClick);
        mMainImagev = (ImageView) findViewById(R.id.c_intr_top_img);
        mCartTx = (TextView) findViewById(R.id.c_intr_cart_tx);
        mAskTx = (TextView) findViewById(R.id.c_intr_ask_tx);
        mCartCountTx = (TextView) findViewById(R.id.c_intr_cart_count);
        mFavBtn = findViewById(R.id.c_intr_favorite);
        mFavImg = (ImageView) findViewById(R.id.c_intr_fav_img);
        if(isFav) mFavImg.setImageResource(R.drawable.v_dis_fav);
        mShareBtn = findViewById(R.id.c_intr_share);
        progressLayer = findViewById(R.id.c_intr_prog);
        mChildListBolck = findViewById(R.id.c_intr_children_list);

        mTeachersRecv = (RecyclerView) findViewById(R.id.c_intr_teacher_list);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTeachersRecv.setLayoutManager(recyclerViewLayoutManager);
        mTeachersRecv.setHasFixedSize(true);

        mBuyNowBtn = (TextView) findViewById(R.id.c_intr_buynow);
        mAddCartBtn = findViewById(R.id.c_intr_addcart);
        mAskBtn = findViewById(R.id.c_intr_ask_why);
        mCartBtn = findViewById(R.id.c_intr_my_cart);

        mCartBtn.setOnClickListener(onViewClick);
        mAddCartBtn.setOnClickListener(onViewClick);
        mGoBackBtn.setOnClickListener(onViewClick);
        mFavBtn.setOnClickListener(onViewClick);
        mShareBtn.setOnClickListener(onViewClick);
        mBuyNowBtn.setOnClickListener(onViewClick);
        mAskBtn.setOnClickListener(onViewClick);

        shareLayer = findViewById(R.id.share_layer);
        shareBlank = findViewById(R.id.share_blank);
        shareActionWxcs = findViewById(R.id.share_action_wxcs);
        shareActionWxtl = findViewById(R.id.share_action_wxtl);
        shareActionQQ = findViewById(R.id.share_action_qq);
        shareActionSina = findViewById(R.id.share_action_sina);

        mShareBtn.setOnClickListener(mShareClick);
        shareBlank.setOnClickListener(mShareClick);
        shareActionWxcs.setOnClickListener(mShareClick);
        shareActionWxtl.setOnClickListener(mShareClick);
        shareActionQQ.setOnClickListener(mShareClick);
        shareActionSina.setOnClickListener(mShareClick);


//        mCartTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD_BLOD));
//        mAskTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD_BLOD));
        mChildCourseListView = (ListView) findViewById(R.id.c_intr_chapters);
    }

    private View.OnClickListener onViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.c_intr_desc_exp_btn:

                    TextView stateTv = (TextView) mDescExpBtn.findViewById(R.id.c_intr_desc_state);

                    if(mDescTxExp.getVisibility() == View.VISIBLE) {
                        mDescTxExp.setVisibility(View.GONE);
                        mDescTx.setVisibility(View.VISIBLE);
                        stateTv.setText(R.string.c_intr_to_expan_desc);
                    } else {
                        mDescTxExp.setVisibility(View.VISIBLE);
                        mDescTx.setVisibility(View.GONE);
                        stateTv.setText(R.string.c_intr_to_hiden_desc);
                    }
                    break;
                case R.id.c_intr_my_cart:

                    Intent intent = null;

                    if(StaticConfigs.mLoginResult == null || TextUtils.isEmpty(StaticConfigs.mLoginResult.account)) {
//                        intent = new Intent(mContext, LoginActivity.class);
//                        startActivityForResult(intent, StaticConfigs.REQUEST_CODE_LOGIN);
                        Toast.makeText(mContext, "请登陆后再学习", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent = new Intent(mContext, CartActivity.class);
                    CourseIntroduceActivity.this.startActivity(intent);

                    break;
                case R.id.c_intr_addcart:

                    if(mEntity != null) {
                        Log.i(TAG, "mEntity.courseid :" + mEntity.courseid + ", mEntity.courseType " + mEntity.courseType);
                        toAddCart(mEntity.courseid, mEntity.courseType);
                    }

                    break;

                case R.id.c_intr_go_back:

                    onBackPressed();

                    break;

                case R.id.c_intr_favorite:

                    isFav = !isFav;

                    if(isFav) {

                        addFavorite();

                    } else {

                        removedFavorite(mFavoriteId);

                    }

                    break;

                case R.id.c_intr_ask_why:

                    Intent adviceIntent = new Intent(CourseIntroduceActivity.this, TopicWebPageActivity.class);
                    adviceIntent.putExtra(TopicWebPageActivity.KEY_URL, StaticConfigs.URL_ADVICE);
                    adviceIntent.putExtra(TopicWebPageActivity.KEY_NAME, "咨询");
                    startActivity(adviceIntent);

                    break;

                case R.id.c_intr_buynow:

                    if(is0MoneyToBuy) {

                        if(StaticConfigs.mLoginResult == null || TextUtils.isEmpty(StaticConfigs.mLoginResult.accessToken)) {

                            Toast.makeText(mContext, "您还没有登录哦~", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        to0MoneyRequest(StaticConfigs.mLoginResult.accessToken, mEntity.configid);

                    } else {

                        ArrayList<Course> checkedList = new ArrayList<Course>();
                        checkedList.add(mEntity);
                        Intent buyIntent = new Intent(mContext, OrderPayActivity.class);
                        buyIntent.putExtra(OrderPayActivity.KEY_PAY_COURSE_LIST, checkedList);
                        startActivity(buyIntent);

                    }

                    break;

            }
        }
    };

    public void to0MoneyRequest(String token, String configId) {

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("AccessToken", token);
        kvs.put("ConfigId", configId);
        JSONObject params = new JSONObject(kvs);

        Log.i(TAG, "start - 0 money to buy");

        progressLayer.setVisibility(View.VISIBLE);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrl0MoneyBuy(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean state = false;

                    String message = "";

                    boolean bo = false;

                    if(response.has("State")) {
                        bo = response.getBoolean("State") && response.has("DataObject");
                    }

                    if(bo) {

                        progressLayer.setVisibility(View.GONE);

                        String buyCourseId = response.getString("DataObject");
                        Intent intent = new Intent(mContext, VideoPlayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(VideoPlayActivity.EXTRA_KEY_VNAME, mEntity.name);
                        intent.putExtra(VideoPlayActivity.EXTRA_KEY_VIMG, mEntity.picture);
                        intent.putExtra(VideoPlayActivity.EXTRA_KEY_BUYCOURSEID, buyCourseId);
                        mContext.startActivity(intent);

                        Log.i(TAG, "end - 0 money to buy");

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
                System.out.println(error.getMessage());

            }
        });

        mVolleyRequestQueue.add(jreq);

    }

    protected void addFavorite() {

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
        kvs.put("CourseId", mCourseId);
        kvs.put("CourseType", courseType + "");

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

                        mFavoriteId = response.getString("DataObject");

                        SharedPreferences sp = mContext.getSharedPreferences("favorite_kv", 0);
                        sp.edit().putString(mCourseId, mFavoriteId).commit();
                        mFavImg.setImageResource(R.drawable.v_dis_fav);
                        isFav = true;

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

        mVolleyRequestQueue.add(jreq);

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
                        SharedPreferences sp = mContext.getSharedPreferences("favorite_kv", 0);
                        sp.edit().remove(mCourseId).commit();
                        mFavImg.setImageResource(R.drawable.v_favorite);
                        isFav = false;

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

        mVolleyRequestQueue.add(jreq);

    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.c_intr_share:

                    shareLayer.setVisibility(shareLayer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    break;
                case R.id.share_action_wxcs:

                    sharewx(SendMessageToWX.Req.WXSceneSession);

                    break;
                case R.id.share_action_wxtl:

                    sharewx(SendMessageToWX.Req.WXSceneTimeline);

                    break;
                case R.id.share_action_qq:

                    shareqq();

                    break;
                case R.id.share_action_sina:

                    sharesina();

                    break;
                case R.id.share_blank:

                    shareLayer.setVisibility(View.GONE);

                    break;
            }

        }
    };


    //-----share-------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode,resultCode,data, null);
        }
    }

    public void shareqq()
    {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "智网新闻");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.zhiwang123.com/");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mEntity.courseDescription);
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                Tencent.createInstance("101361700", mContext).shareToQQ(CourseIntroduceActivity.this, params, qqShareListener);
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

        }
        @Override
        public void onComplete(Object response) {

        }


        @Override
        public void onError(UiError e) {

        }
    };

    public void sharewx(int scene) {

        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = "http://www.zhiwang123.com/";
        WXMediaMessage mediaMessage = new WXMediaMessage(wxWebpageObject);
        mediaMessage.title = mEntity.courseName;
        mediaMessage.description = mEntity.courseDescription;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.app_share_icon);
        mediaMessage.thumbData = Util.bmpToByteArray(bm, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webPage");
        req.message = mediaMessage;
        req.scene = scene;

        WXAPIFactory.createWXAPI(this, "wx5dbdd6555a391027").sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void sharesina() {

        UMWeb web = new UMWeb("http://www.zhiwang123.com/");
        web.setTitle("智网新闻");//标题
        web.setThumb(new UMImage(mContext, R.drawable.app_share_icon));  //缩略图
        web.setDescription("my description");

        new ShareAction(CourseIntroduceActivity.this)
                .withMedia(web)
                .setPlatform(SHARE_MEDIA.SINA)
                .share();

    }

}

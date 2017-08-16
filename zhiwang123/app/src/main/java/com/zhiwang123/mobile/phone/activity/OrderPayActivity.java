package com.zhiwang123.mobile.phone.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.OrderPayCourseAdapter;
import com.zhiwang123.mobile.phone.adapter.PayTypeListAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.PayType;
import com.zhiwang123.mobile.phone.bean.PayTypeEntity;
import com.zhiwang123.mobile.phone.bean.parser.PayTypeParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ty on 2016/11/2.
 */

public class OrderPayActivity extends BaseActivity {

    public static final String TAG = "OrderPayActivity";
    public static final String KEY_PAY_COURSE_LIST = "pay_course_list";

    public static final String ACITON_WX_PAY_ERRCODE = "com.zhiwang123.mobile.wxPay_errCode";


    private View mGobackBtn;
    private TextView mTitleTxt;

    private TextView mToPayBtn;

    private Context mContext;

    private ListView mOrderListView;
    private ListView mPayTypeListView;

    private ArrayList<Course> mOrderItems;
    private ArrayList<PayType> mPayTypeList;

    private PayTypeListAdapter mPayTypesAdapter;
    private OrderPayCourseAdapter mPayCourseList;

    private int totalSum;
    private String configIds;

    private String mCurrentPayType;

    private ApplicationInfo mAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_pay_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mContext = getApplicationContext();

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_order_layout);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            mOrderItems = (ArrayList<Course>) bundle.getSerializable(KEY_PAY_COURSE_LIST);

        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACITON_WX_PAY_ERRCODE);
        registerReceiver(mWxReceiver, filter);

        initLayout();

        loadPayTypes();

    }

    @Override
    protected void initLayout() {

        View actRt = mActionBar.getCustomView().findViewById(R.id.order_layout_root);
        mGobackBtn = actRt.findViewById(R.id.order_go_back);
        mTitleTxt = (TextView) actRt.findViewById(R.id.order_title);
        mGobackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToPayBtn = (TextView) findViewById(R.id.order_pay_for_btn);

        mPayTypeListView = (ListView) findViewById(R.id.order_pay_types);
        mOrderListView = (ListView) findViewById(R.id.order_list);


        if(mOrderItems != null && mOrderItems.size() != 0) {

            mPayCourseList = new OrderPayCourseAdapter(this, mOrderItems);
            mOrderListView.setAdapter(mPayCourseList);
            mPayCourseList.notifyDataSetChanged();

            if(mOrderItems.size() == 1) {

                configIds = mOrderItems.get(0).configid;
                totalSum = mOrderItems.get(0).money;

            } else if (mOrderItems.size() > 1) {

                StringBuffer strb = new StringBuffer("");

                for(Course c : mOrderItems) {

                    totalSum += c.money;
                    if(strb.length() == 0) strb.append(c.configid);
                    else strb.append("," + c.configid);

                }

                configIds = strb.toString();

            }


            mToPayBtn.setText(getString(R.string.order_confirm_to_pay, totalSum));

            if(totalSum != 0) {

                mToPayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(TextUtils.isEmpty(mCurrentPayType)) {

                            Toast.makeText(mContext, R.string.order_pay_type_no_sel, Toast.LENGTH_LONG).show();
                            return;

                        }

                        sendPayBuyReqestion(StaticConfigs.mLoginResult.accessToken, mCurrentPayType, configIds, true);

                    }
                });
            }
        }

    }

    private void sendPayBuyReqestion(String token, String key, String configIds, boolean fromShoppingCart) {

        Log.i(TAG, "token : " + token + "\n key :" + key + "\n configIds : " + configIds + "\n fromShoppingCart: " + fromShoppingCart);

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("AccessToken", token);
        kvs.put("Key", key);
        kvs.put("ConfigIds", configIds);
        kvs.put("FromShoppingCart", fromShoppingCart ? "true" : "false");
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlPayBuy(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, response.toString());

                boolean bo = false;

                try {

                    if(response.has("State")) bo = response.getBoolean("State");

                    if(bo) {

                        if(response.has("Message")) {

                            String resultStr = response.getString("Message");

                            switch(mCurrentPayType) {

                                case StaticConfigs.PAY_TYPE_AL:

                                    final String orderInfo = resultStr;

                                    Log.i(TAG, "order info : " + orderInfo);

                                    Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {

                                            PayTask alipay = new PayTask(OrderPayActivity.this);
                                            String result = alipay.pay(orderInfo, true);

                                            Message msg = mHandler.obtainMessage();
                                            msg.obj = result;
                                            mHandler.sendMessageAtTime(msg, 0);

                                        }
                                    };
                                    Thread payThread = new Thread(payRunnable);
                                    payThread.start();

                                    break;

                                case StaticConfigs.PAY_TYPE_ME:

                                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                                    finish();


                                    break;

                                case StaticConfigs.PAY_TYPE_WX:

                                    JSONObject wxResp = new JSONObject(resultStr);

                                    String appid = "";
                                    String noncestr = "";
                                    String packageStr = "";
                                    String partnerid = "";
                                    String prepayid = "";
                                    String sign = "";
                                    String timestamp = "";

                                    if(wxResp.has("appid")) appid = wxResp.getString("appid");
                                    if(wxResp.has("noncestr")) noncestr = wxResp.getString("noncestr");
                                    if(wxResp.has("package")) packageStr = wxResp.getString("package");
                                    if(wxResp.has("prepayid")) prepayid = wxResp.getString("prepayid");
                                    if(wxResp.has("partnerid")) partnerid = wxResp.getString("partnerid");
                                    if(wxResp.has("sign")) sign = wxResp.getString("sign");
                                    if(wxResp.has("timestamp")) timestamp = wxResp.getString("timestamp");

                                    Log.e(TAG, "appid: " + appid + "\n noncestr :" + noncestr + "\n package :"
                                            + packageStr + "\n prepayid :" + prepayid + "\n partenerid :" + partnerid + "\n sign :" + sign + "\n teimstamp :" + timestamp);

                                    IWXAPI api = WXAPIFactory.createWXAPI(mContext, null);
                                    api.registerApp(appid);

                                    PayReq request = new PayReq();
                                    request.appId = appid;
                                    request.partnerId = partnerid;
                                    request.prepayId= prepayid;
                                    request.packageValue = packageStr;
                                    request.nonceStr= noncestr;
                                    request.timeStamp= timestamp;
                                    request.sign = sign;
                                    request.extData = "app data";

                                    api.sendReq(request);

                                    break;

                            }

                        }

                    }

                } catch(Exception e) {

                    e.printStackTrace();

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

    private void loadPayTypes() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlPayList(), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i(TAG, response.toString());
                        PayTypeEntity pte = new PayTypeParser().parse(response);
                        mPayTypeList = pte.dataobject;
                        mPayTypesAdapter = new PayTypeListAdapter(OrderPayActivity.this, mPayTypeList);
                        mPayTypeListView.setAdapter(mPayTypesAdapter);
                        mPayTypesAdapter.notifyDataSetChanged();
                        mPayTypeListView.setOnItemClickListener(onItemClickListener);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWxReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ArrayList<PayType> list = mPayTypesAdapter.getDataList();

            for(int i = 0; i < list.size(); i++) {

                PayType pt = list.get(i);

                if(i == position) {

                    pt.state = true;
                    mCurrentPayType = pt.key;

                } else {

                    list.get(i).state = false;

                }
            }


            mPayTypesAdapter.notifyDataSetChanged();
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            String result = (String) msg.obj;

            Log.e(TAG, "ali pay return : " + result);

            String codeStr = result.substring(result.indexOf("{") + 1, result.indexOf("}"));

            int code = Integer.parseInt(codeStr);

            switch(code) {

                case 9000:

                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();

                    onBackPressed();

                    break;
                case 6001:
                default:

                    String[] resultInfos = result.split(";");

                    Toast.makeText(mContext, resultInfos[1], Toast.LENGTH_SHORT).show();

                    break;

            }

        }
    };

    private BroadcastReceiver mWxReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();

            int code = bundle.getInt("wx_errCode", -2);

            switch(code) {

                case 0:
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -2:
                default:
                    Toast.makeText(mContext, "未完成支付，请到订单页面查看", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


}

package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.OrderFragmentAdapter;
import com.zhiwang123.mobile.phone.bean.Order;
import com.zhiwang123.mobile.phone.bean.OrderCourse;
import com.zhiwang123.mobile.phone.bean.parser.OrderParser;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.OrderPageFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ty on 2016/11/2.
 */

public class OrderActivity extends BaseActivity {

    private static final String TAG = "OrderActivity";

    private Context mContext;
    private View mGobackBtn;
    private TextView mTitleTv;

    private ViewPager mPager;
    private OrderFragmentAdapter mPagerAdapter;
    private OrderPageFragment mFragmentAll;
    private OrderPageFragment mFragmentPayY;
    private OrderPageFragment mFragmentPayN;

    private View pagerTap0;
    private View pagerTap1;
    private View pagerTap2;

    private View topBtn;
    private View topBtn1;
    private View topBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_layout);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_order_layout);

        Bundle bundle = getIntent().getExtras();

        initLayout();

        //loadCartList();
    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.order_layout_root);
        mGobackBtn = actionView.findViewById(R.id.order_go_back);
        mTitleTv = (TextView) actionView.findViewById(R.id.order_title);
        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<BaseFragment> pagerList = initSubPagers();
        mPager = (ViewPager) findViewById(R.id.order_type_pagers);
        mPagerAdapter = new OrderFragmentAdapter(getSupportFragmentManager(), pagerList);
        mPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mPager.setOffscreenPageLimit(2);
        mPager.setCurrentItem(0);
        mPager.addOnPageChangeListener(onPageChangeListener);

        pagerTap0 = findViewById(R.id.order_top_silder);
        pagerTap1 = findViewById(R.id.order_top_silder1);
        pagerTap2 = findViewById(R.id.order_top_silder2);

        topBtn = findViewById(R.id.top_btn);
        topBtn1 = findViewById(R.id.top_btn1);
        topBtn2 = findViewById(R.id.top_btn2);

        topBtn.setOnClickListener(onClickListener);
        topBtn1.setOnClickListener(onClickListener);
        topBtn2.setOnClickListener(onClickListener);

    }

    public void loadCartList() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlOrderList(StaticConfigs.mLoginResult.accessToken,
                StaticConfigs.OrderStatus.STATUS_ALL, 1, 1000), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        OrderCourse oc = new OrderParser<OrderCourse>(new OrderCourse()).parse(response);
                        ArrayList<Order> orderList = oc.dataobject;

                        if(orderList != null && orderList.size() != 0) {

                            ArrayList<Order> listPayY = new ArrayList<>();
                            ArrayList<Order> listPayN = new ArrayList<>();

                            for(Order order : orderList) {

                                if(order.status == Order.ORDER_STATUS_PAY_YES) {

                                    listPayY.add(order);

                                } else if(order.status == Order.ORDER_STATUS_PAY_NO) {

                                    listPayN.add(order);

                                }

                            }

                            mFragmentAll.setPageList(orderList, OrderActivity.this);
                            mFragmentPayY.setPageList(listPayY, OrderActivity.this);
                            mFragmentPayN.setPageList(listPayN, OrderActivity.this);

                        }

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

    private ArrayList<BaseFragment> initSubPagers() {

        ArrayList<BaseFragment> pagers = new ArrayList<>();

        mFragmentAll = new OrderPageFragment();
        mFragmentPayY = new OrderPageFragment();
        mFragmentPayN = new OrderPageFragment();

        pagers.add(mFragmentAll);
        pagers.add(mFragmentPayN);
        pagers.add(mFragmentPayY);

        return pagers;

    }

    @Override
    protected void onResume() {

        super.onResume();

        loadCartList();

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {

                case 0:
                    pagerTap0.setVisibility(View.VISIBLE);
                    pagerTap1.setVisibility(View.INVISIBLE);
                    pagerTap2.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    pagerTap0.setVisibility(View.INVISIBLE);
                    pagerTap1.setVisibility(View.VISIBLE);
                    pagerTap2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    pagerTap0.setVisibility(View.INVISIBLE);
                    pagerTap1.setVisibility(View.INVISIBLE);
                    pagerTap2.setVisibility(View.VISIBLE);
                    break;

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.top_btn:

                    mPager.setCurrentItem(0, true);

                    break;

                case R.id.top_btn1:

                    mPager.setCurrentItem(1, true);

                    break;

                case R.id.top_btn2:

                    mPager.setCurrentItem(2, true);

                    break;
            }

        }
    };


}

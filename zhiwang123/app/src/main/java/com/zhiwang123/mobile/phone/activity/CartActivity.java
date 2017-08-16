package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.BaseTask;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.CartListAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseCart;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ty on 2016/11/2.
 */

public class CartActivity extends BaseActivity implements AsyncExecuteCallback {

    private static final String TAG = "CartActivity";

    private Context mContext;
    private View mGobackBtn;
    private TextView mEdtBtn;

    private TextView mTitleTv;
    private TextView mTotalCostTv;
    private TextView mTotalSlectedTv;
    private View mCheckAllCon;
    private View mCountConv;
    private View mMoneyConv;
    private CheckBox mCheckAllCbx;
    private BaseFragment mCurrentFragment;
    private ListView mCartListView;
    private CartListAdapter mCartListAdapter;

    private ArrayList<Course> mCheckedCourse;


    private int totalSum;
    private int totalCount;

    private enum EditState {idle, editing};

    private EditState mCurrentEdtState = EditState.idle;
    private boolean setCheckAllOther = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cart_layout);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_cart_layout);

        Bundle bundle = getIntent().getExtras();

        mCheckedCourse = new ArrayList<>();

        initLayout();

//        loadCartList();
    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.cart_action_layout_root);
        mTitleTv = (TextView) actionView.findViewById(R.id.cart_title);
        mGobackBtn = actionView.findViewById(R.id.cart_go_back);
        mEdtBtn = (TextView) actionView.findViewById(R.id.cart_to_edit);
        mEdtBtn.setOnClickListener(onClickListener);
        mCountConv = findViewById(R.id.cart_total_count_con);
        mMoneyConv = findViewById(R.id.cart_total_money_con);

        mCartListView = (ListView) findViewById(R.id.cart_page_list);
        mTotalCostTv = (TextView) findViewById(R.id.cart_total_money);
        mTotalSlectedTv = (TextView) findViewById(R.id.cart_total_count);
        mTotalCostTv.setText(getString(R.string.course_price, new Integer(0)));
        mTotalSlectedTv.setText(getString(R.string.cart_count_num, new Integer(0)));
        mCheckAllCbx = (CheckBox) findViewById(R.id.cart_check_all);
        mCheckAllCon = findViewById(R.id.cart_check_all_con);
        mCheckAllCon.setOnClickListener(onClickListener);
        mCountConv.setOnClickListener(onClickListener);
        mCheckAllCbx.setOnCheckedChangeListener(onCheckedChangeListener);
        mCountConv.setOnClickListener(onClickListener);

        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadCartList() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCartlist(StaticConfigs.mLoginResult.accessToken), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseCart courseCart = new CourseParser<CourseCart>(new CourseCart()).parse(response);
                        ArrayList<Course> dataList = courseCart.dataobject;

                        mCartListAdapter = new CartListAdapter(mContext, dataList);
                        mCartListView.setAdapter(mCartListAdapter);
                        mCartListAdapter.notifyDataSetChanged();

                        mCartListView.setOnItemClickListener(onItemClickListener);

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

    private void removeCarts(String ids, final int removedCount) {

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

                        loadCartList();

                        totalCount -= removedCount;

                        mTotalSlectedTv.setText(getString(R.string.cart_delete_num, totalCount));

                        sendBroadcast(new Intent(StaticConfigs.CART_CHANGE_ACITON));


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

        mVolleyRequestQueue.add(jreq);


    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(mCheckAllCbx.isChecked()){
                setCheckAllOther = true;
                mCheckAllCbx.setChecked(false);
            }

            CartListAdapter.CellHolder cellHolder = (CartListAdapter.CellHolder) view.getTag();
            if(cellHolder.courseEntity.state) {
                cellHolder.courseEntity.state = false;
                cellHolder.checkBox.setChecked(false);
                totalSum -= cellHolder.courseEntity.money;
                totalCount -= 1;
            } else {
                cellHolder.courseEntity.state = true;
                cellHolder.checkBox.setChecked(true);
                totalSum += cellHolder.courseEntity.money;
                totalCount += 1;
            }

            if(mCurrentEdtState == EditState.idle) {
                mTotalSlectedTv.setText(getString(R.string.cart_count_num, totalCount));
                mTotalCostTv.setText(getString(R.string.course_price, totalSum));
            } else if(mCurrentEdtState == EditState.editing) {
                mTotalSlectedTv.setText(getString(R.string.cart_delete_num, totalCount));
            }

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.cart_check_all_con:

                    if(mCheckAllCbx.isChecked()) mCheckAllCbx.setChecked(false);
                    else mCheckAllCbx.setChecked(true);

                    break;
                case R.id.cart_to_edit:

                    mTotalCostTv.setText(getString(R.string.course_price, 0));
                    mCheckAllCbx.setChecked(false);
                    totalCount = 0;
                    totalSum = 0;

                    for(Course c : mCartListAdapter.getDataList()) {
                        c.state = false;
                    }

                    if(mCurrentEdtState == EditState.idle) {
                        mCurrentEdtState = EditState.editing;
                        mEdtBtn.setText(R.string.cart_to_cancel_edt);
                        mCountConv.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary_red));
                        mTotalSlectedTv.setText(getString(R.string.cart_delete_num, 0));
                        mMoneyConv.setVisibility(View.INVISIBLE);
                    } else if(mCurrentEdtState == EditState.editing) {
                        mCurrentEdtState = EditState.idle;
                        mEdtBtn.setText(R.string.cart_to_edt);
                        mCountConv.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                        mTotalSlectedTv.setText(getString(R.string.cart_count_num, 0));
                        mMoneyConv.setVisibility(View.VISIBLE);
                    }

                    mCartListAdapter.notifyDataSetChanged();
                    break;

                case R.id.cart_total_count_con:


                    if(mCurrentEdtState == EditState.idle) {

                        if(totalCount == 0 || totalSum == 0) return;

//                        Intent intent = new Intent(mContext, OrderPayActivity.class);
//                        CartActivity.this.startActivity(intent);

                        BaseTask task = new BaseTask(0, CartActivity.this);
                        AsyncExecutor.getInstance().execMuliteTask(task);

                    } else if(mCurrentEdtState == EditState.editing) {

                        if(mCartListAdapter.getDataList() != null) {

                            StringBuffer cartIds = new StringBuffer("");

                            int count = 0;

                            for(Course c : mCartListAdapter.getDataList()) {

                                if(c.state) {

                                    cartIds.append("," + c.id);
                                    count ++;

                                }

                            }

                            removeCarts(cartIds.toString(), count);

                        }

                    }

                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(setCheckAllOther) {
                setCheckAllOther = false;
                return;
            }

            totalSum = 0;
            totalCount = 0;

            for(Course c : mCartListAdapter.getDataList()) {
                c.state = isChecked;
                if(isChecked) totalSum += c.money;
            }

            if(isChecked) {
                totalCount = mCartListAdapter.getDataList().size();
            }

            if(mCurrentEdtState == EditState.idle) {
                mTotalSlectedTv.setText(getString(R.string.cart_count_num, totalCount));
                mTotalCostTv.setText(getString(R.string.course_price, totalSum));
            } else if(mCurrentEdtState == EditState.editing) {
                mTotalSlectedTv.setText(getString(R.string.cart_delete_num, totalCount));
            }

            mCartListAdapter.notifyDataSetChanged();

        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        loadCartList();

        totalSum = 0;
        totalCount = 0;
        mCheckAllCbx.setChecked(false);
        mTotalSlectedTv.setText(getString(R.string.cart_count_num, totalCount));

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

    @Override
    public void pre(Object... objs) {



    }

    @Override
    public BaseTask.TaskResult async(int what, BaseTask baseTask) {

        switch(what) {

            case 0:

                ArrayList<Course> cs = mCartListAdapter.getDataList();

                ArrayList<Course> checkedCs = new ArrayList<Course>();

                for(Course c : cs) {

                    if(c.state) {

                        checkedCs.add(c);

                    }

                }

                BaseTask.TaskResult result = baseTask.new TaskResult();
                result.setResult(checkedCs);

                return result;

        }

        return null;
    }

    @Override
    public void post(Message msg) {


        BaseTask baseTask = (BaseTask) msg.obj;

        switch (msg.what) {
            case 0:

                ArrayList<Course> checkedList = (ArrayList<Course>) baseTask.result.getResult();
                Intent intent = new Intent(mContext, OrderPayActivity.class);
                intent.putExtra(OrderPayActivity.KEY_PAY_COURSE_LIST, checkedList);
                startActivity(intent);

                break;
        }


    }

}


package com.zhiwang123.mobile.phone.activity;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.UserInfo;
import com.zhiwang123.mobile.phone.utils.MethodUtils;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ty on 2016/11/2.
 */

public class BindRegisterActivity extends BaseActivity {

    private static final String TAG = "BindRegisterActivity";

    private EditText mPhoneInput;
    private EditText mCodeInput;
    private TextView mGetCodeBtn;
    private EditText mPassInput;
    private EditText mRePassInput;
    private TextView mBindRegistBtn;

    private TextView mBindRegHint;

    private View mGobackbtn;
    private TextView mTitle;

    private Drawable mErrorDrawable;

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bind_register_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_bind_regist_layout);

        registerMsgObserver();

        initLayout();

    }

    @Override
    protected void initLayout() {

        View view = mActionBar.getCustomView().findViewById(R.id.login_action_layout_root);
        mGobackbtn = view.findViewById(R.id.bind_go_back);
        mTitle = (TextView) view.findViewById(R.id.bind_title);

        mErrorDrawable = getResources().getDrawable(R.drawable.input_error);
        mErrorDrawable.setBounds(new Rect(0, 0, mErrorDrawable.getIntrinsicWidth()/2 , mErrorDrawable.getIntrinsicHeight() / 2));

        mPhoneInput = (EditText) findViewById(R.id.bind_register_phone_input);
        mCodeInput = (EditText) findViewById(R.id.bind_register_code_input);
        mGetCodeBtn = (TextView) findViewById(R.id.bind_register_code_btn);
        mPassInput = (EditText) findViewById(R.id.bind_register_pass_input);
        mRePassInput = (EditText) findViewById(R.id.bind_register_pass_reinput);
        mBindRegistBtn = (TextView) findViewById(R.id.bind_register_btn);

        mBindRegHint = (TextView) findViewById(R.id.bind_regist_hint);

        mGetCodeBtn.setOnClickListener(mOnClickListener);
        mBindRegistBtn.setOnClickListener(mOnClickListener);

        UserInfo ui = StaticConfigs.mLoginResult.dataobject.get(0);

        switch(ui.type) {

            case UserInfo.TYPE_QQ:
                mBindRegHint.setText("注册后，您的智网账户和QQ账户都可以登录");
                break;
            case UserInfo.TYPE_WX:
                mBindRegHint.setText("注册后，您的智网账户和微信账户都可以登录");
                break;

        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.bind_register_btn:

                    if (TextUtils.isEmpty(mPhoneInput.getText())) {
                        mPhoneInput.requestFocus();
                        mPhoneInput.setError(getResources().getString(R.string.phone_empty_error), mErrorDrawable);
                        return;
                    }

                    if(!MethodUtils.checkPhoneNum(mPhoneInput.getText().toString())) {
                        mPhoneInput.requestFocus();
                        mPhoneInput.setError(getResources().getString(R.string.phone_num_format_error), mErrorDrawable);
                        return;
                    }

                    if (TextUtils.isEmpty(mCodeInput.getText())) {
                        mCodeInput.requestFocus();
                        mCodeInput.setError(getResources().getString(R.string.code_empty_error), mErrorDrawable);
                        return;
                    }

                    if (TextUtils.isEmpty(mPassInput.getText())) {
                        mPassInput.requestFocus();
                        mPassInput.setError(getResources().getString(R.string.password_empty_error), mErrorDrawable);
                        return;
                    }

                    if (TextUtils.isEmpty(mRePassInput.getText())) {
                        mRePassInput.requestFocus();
                        mRePassInput.setError(getResources().getString(R.string.repassword_empty_error), mErrorDrawable);
                        return;
                    }

                    if(!mPassInput.getText().toString().equals(mRePassInput.getText().toString())) {
                        mRePassInput.requestFocus();
                        mRePassInput.setError(getResources().getString(R.string.password_not_thesame_error), mErrorDrawable);
                        return;
                    }

                    toBindRegist(mPhoneInput.getText().toString(), mCodeInput.getText().toString(), mPassInput.getText().toString());

                    break;
                case R.id.bind_register_code_btn:

                    if (TextUtils.isEmpty(mPhoneInput.getText())) {
                        mPhoneInput.requestFocus();
                        mPhoneInput.setError(getResources().getString(R.string.phone_empty_error), mErrorDrawable);
                        return;
                    }

                    if(!MethodUtils.checkPhoneNum(mPhoneInput.getText().toString())) {
                        mPhoneInput.requestFocus();
                        mPhoneInput.setError(getResources().getString(R.string.phone_num_format_error), mErrorDrawable);
                        return;
                    }

                    toFetchCode(mPhoneInput.getText().toString());

                    break;
            }
        }
    };

    public void toFetchCode(final String phoneNum) {

        mGetCodeBtn.setOnClickListener(null);
        mBindRegistBtn.setTextColor(Color.GRAY);
        mBindRegistBtn.setEnabled(false);

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("Phone", phoneNum);
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlSendCode(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                boolean state = false;

                try {

                    if(response.has("State")) state = response.getBoolean("State");

                    if(state) {

                        mTimer = new Timer();

                        mTimer.schedule(new Tk(), 0, 1000);

                        Toast.makeText(BindRegisterActivity.this, R.string.register_sms_sending, Toast.LENGTH_LONG).show();

                    }

                } catch(JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mVolleyRequestQueue.add(jreq);

    }

    private void toBindRegist(final String phoneNum, final String code, final String password) {

        UserInfo userInfo = StaticConfigs.mLoginResult.dataobject.get(0);

        if(userInfo == null) return;

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("Phone", phoneNum);
        kvs.put("Password", password);
        kvs.put("Code", code);
        kvs.put("ThirdLoginKey", userInfo.thirdLoginKey);
        kvs.put("ThirdLoginOpenId", userInfo.thirdLoginOpenId);
        kvs.put("Name", userInfo.name);
        kvs.put("Avatar", userInfo.avatar);

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getURlBindRegist(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                boolean state = false;
                String message = "";

                try {

                    if (response.has("State")) state = response.getBoolean("State");
                    if (response.has("Message")) message = response.getString("Message");

                    if (state) {

                        Toast.makeText(BindRegisterActivity.this, R.string.register_result_success, Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(BindRegisterActivity.this, message, Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mVolleyRequestQueue.add(jreq);
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

        unRegisterMsgObserver();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    private class Tk extends TimerTask {

        int count = 0;

        @Override
        public void run() {

            Message msg = mHandler.obtainMessage();
            msg.what = 0;
            msg.obj = count ++;
            msg.sendToTarget();

        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch(msg.what) {

                case 0:

                    int count = (int) msg.obj;

                    if(count > 59) {

                        mTimer.cancel();

                        mGetCodeBtn.setText(R.string.register_get_code);

                        mGetCodeBtn.setOnClickListener(mOnClickListener);

                        mBindRegistBtn.setTextColor(Color.WHITE);
                        mBindRegistBtn.setEnabled(true);

                        break;

                    }

                    mGetCodeBtn.setText(getString(R.string.register_get_code_waiting, count));

                    break;
                case 1:

                    break;

            }

        }
    };

    private ContentObserver observer = new ContentObserver(mHandler) {

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            Uri uri = Uri.parse("content://sms/inbox");
            String[] projection = { "_id", "address", "person", "body", "date", "type", "read" };
            String selection = "read = ?";
            String[] selectionArgs = { "0" };
            String sortOrder = "date desc";
            Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            if (!cursor.moveToFirst()) {
                return;
            }

            String name = cursor.getString(cursor.getColumnIndex("person"));
            String number = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));

            Log.i(TAG, name + "-" + number + "-" + body);

            if(body.contains(getResources().getString(R.string.sms_info))) {

                mTimer.cancel();

                mGetCodeBtn.setText(R.string.register_get_code);
                mGetCodeBtn.setOnClickListener(mOnClickListener);
                mBindRegistBtn.setTextColor(Color.WHITE);
                mBindRegistBtn.setEnabled(true);

            }

        }
    };

    public void registerMsgObserver() {

        Uri uri = Uri.parse("content://sms/");// inbox");
        getContentResolver().registerContentObserver(uri, true, observer);

    }

    public void unRegisterMsgObserver() {

        getContentResolver().unregisterContentObserver(observer);

    }

}

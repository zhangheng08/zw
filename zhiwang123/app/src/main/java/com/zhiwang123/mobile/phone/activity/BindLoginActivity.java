
package com.zhiwang123.mobile.phone.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.UserInfo;
import com.zhiwang123.mobile.phone.bean.parser.LoginParser;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ty on 2016/11/2.
 */

public class BindLoginActivity extends BaseActivity {

    private static final String TAG = "BindLoginActivity";

    public static final int REQUEST_CODE_ORGAN_KEY = 0x1;
    public static final int RESULT_CODE_ORGAN_KEY = 0x2;
    public static final String RESULT_KEY = "organ_key";

    private View mGobackBtn;

    private TextView mTitleTxt;
    private EditText mNameEdt;
    private EditText mPasswordEdt;
    private View mELoginSelectBtn;
    private View mLoginBtn;
    private TextView mBindHint;

    private Drawable mErrorDrawable;

    private LoginResult mLoginResult;

    private String mOrganKey;

    private int loginMode = StaticConfigs.LOGIN_MODE_PB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bind_login_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_bind_regist_layout);

        initLayout();

        mErrorDrawable = getResources().getDrawable(R.drawable.input_error);
        mErrorDrawable.setBounds(new Rect(0, 0, mErrorDrawable.getIntrinsicWidth()/2 , mErrorDrawable.getIntrinsicHeight() / 2));

    }

    @Override
    protected void initLayout() {

        View view = mActionBar.getCustomView().findViewById(R.id.login_action_layout_root);
        mTitleTxt = (TextView) view.findViewById(R.id.bind_title);
        mTitleTxt.setText(R.string.bind_login_tt);
        mGobackBtn = view.findViewById(R.id.bind_go_back);

        mNameEdt = (EditText) findViewById(R.id.bind_lg_name_input);
        mPasswordEdt = (EditText) findViewById(R.id.bind_lg_pass_input);
        mLoginBtn = findViewById(R.id.bind_login_btn);
        mELoginSelectBtn = findViewById(R.id.bind_login_e);

        mLoginBtn.setOnClickListener(mOnClickListener);
        mELoginSelectBtn.setOnClickListener(mOnClickListener);

        mBindHint = (TextView) findViewById(R.id.bind_lg_hint);

        UserInfo ui = StaticConfigs.mLoginResult.dataobject.get(0);

        switch(ui.type) {

            case UserInfo.TYPE_QQ:
                mBindHint.setText("关联后您的智网账户和QQ账户都可以登录");
                break;
            case UserInfo.TYPE_WX:
                mBindHint.setText("关联后您的智网账户和微信账户都可以登录");
                break;

        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.bind_login_btn:

                    if (TextUtils.isEmpty(mNameEdt.getText())) {
                        mNameEdt.requestFocus();
                        mNameEdt.setError(BindLoginActivity.this.getResources().getString(R.string.name_empty_error), mErrorDrawable);
                        return;
                    }

                    if (TextUtils.isEmpty(mPasswordEdt.getText())) {
                        mPasswordEdt.requestFocus();
                        mPasswordEdt.setError(BindLoginActivity.this.getResources().getString(R.string.password_empty_error), mErrorDrawable);
                        return;
                    }

                    if(loginMode == StaticConfigs.LOGIN_MODE_PB) toBindLoginPB(mNameEdt.getText().toString(), mPasswordEdt.getText().toString());
                    else if(loginMode == StaticConfigs.LOGIN_MODE_E) toBindLoginOrgan(mOrganKey, mNameEdt.getText().toString(), mPasswordEdt.getText().toString());

                    break;

                case R.id.bind_login_e:
                    Intent intent = new Intent(BindLoginActivity.this, OrganDialogActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ORGAN_KEY);
//					getActivity().startActivity(intent);
                    BindLoginActivity.this.overridePendingTransition(R.anim.translate_in_zw_v, 0);
                    break;

            }

        }
    };

    private void toBindLoginPB(final String userName, final String password) {

        UserInfo userInfo = StaticConfigs.mLoginResult.dataobject.get(0);

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("UserName", userName);
        kvs.put("Password", password);
        kvs.put("ThirdLoginKey", userInfo.thirdLoginKey);
        kvs.put("ThirdLoginOpenId", userInfo.thirdLoginOpenId);
        kvs.put("Name", userInfo.name);
        kvs.put("Avatar", userInfo.avatar);

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlBindLogin(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                LoginResult lr = new LoginParser<LoginResult>(new LoginResult()).parse(response);
                lr.account = userName;
                lr.loginMode = StaticConfigs.LOGIN_MODE_PB;

                if(lr.state) {
                    mLoginResult = lr;
                    fetchUserInfo(lr.accessToken);
                } else {
                    Toast.makeText(BindLoginActivity.this, ellipsizeMessage(lr.message), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Log.e(TAG, error.getMessage());
                String errorInfo = ellipsizeMessage(error.getMessage());
                Toast.makeText(BindLoginActivity.this, errorInfo, Toast.LENGTH_LONG).show();
            }
        });

        mVolleyRequestQueue.add(jreq);

    }

    private void toBindLoginOrgan(final String organKey, final String name, final String pass) {

        UserInfo userInfo = StaticConfigs.mLoginResult.dataobject.get(0);

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("Key", organKey);
        kvs.put("UserName", name);
        kvs.put("Password", pass);
        kvs.put("ThirdLoginKey", userInfo.thirdLoginKey);
        kvs.put("ThirdLoginOpenId", userInfo.thirdLoginOpenId);
        kvs.put("Name", userInfo.name);
        kvs.put("Avatar", userInfo.avatar);
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlEloginBind(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                LoginResult lr = new LoginParser<LoginResult>(new LoginResult()).parse(response);
                lr.account = name;
                lr.loginMode = StaticConfigs.LOGIN_MODE_E;

                if(lr.state) {
                    mLoginResult = lr;
                    fetchUserInfo(lr.accessToken);
                } else {
                    Toast.makeText(BindLoginActivity.this, ellipsizeMessage(lr.message), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                Log.e(TAG, error.getMessage());
                String errorInfo = ellipsizeMessage(error.getMessage());
                Toast.makeText(BindLoginActivity.this, errorInfo, Toast.LENGTH_LONG).show();
            }
        });

        mVolleyRequestQueue.add(jreq);

    }

    private String ellipsizeMessage(String message) {

        if(TextUtils.isEmpty(message)) return "";

        return message.length() <= 10 ? message : message.substring(0, 9) + "...";

    }

    public void fetchUserInfo(final String token) {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlUseInfo(token), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        LoginResult lr =  new LoginParser<LoginResult>(new LoginResult()).parse(response);
                        mLoginResult.dataobject = lr.dataobject;
                        AsyncDBHelper.saveLoginInfoDB(BindLoginActivity.this, mLoginResult, uiHandler);
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

    private AsyncDBHelper.UIHandler<LoginResult> uiHandler = new AsyncDBHelper.UIHandler<LoginResult>() {

        @Override
        public void processMessage(int what, LoginResult loginResult) {
            switch(what) {
                case SEND_SAVE_USERINFO_OK:

                    Intent data = new Intent();
                    data.putExtra(StaticConfigs.RESULT_CODE_LOGIN_KEY, loginResult);
                    setResult(StaticConfigs.RESULT_CODE_LOGIN_OK, data);
                    finish();

                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_ORGAN_KEY) {

            switch (resultCode) {

                case RESULT_CODE_ORGAN_KEY:

                    mOrganKey = data.getStringExtra(RESULT_KEY);

                    if(!TextUtils.isEmpty(mOrganKey)) {

                        loginMode = StaticConfigs.LOGIN_MODE_E;

                    } else {

                        loginMode = StaticConfigs.LOGIN_MODE_PB;

                    }

                    break;

            }

        }

        super.onActivityResult(requestCode, resultCode, data);

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
    public void onBackPressed() {

        super.onBackPressed();

    }

}

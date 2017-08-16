
package com.zhiwang123.mobile.phone.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.UserInfo;
import com.zhiwang123.mobile.phone.bean.parser.LoginParser;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.LoginFragment;
import com.zhiwang123.mobile.phone.fragment.RegisterFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.qqlisnteners.QQUiListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by ty on 2016/11/2.
 */

public class LoginActivity extends BaseActivity implements QQUiListener {

    private static final String TAG = "LoginActivity";

    public static final int CONTENT_EARE = R.id.login_content_layout;

    private View mRootView;
    private View mGobackBtn;
    private TextView mTitleTxt;
    private View mToRegist;
    private BaseFragment mCurrentFragment;
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegistFragment;


    protected Tencent mTencent;
    protected ApplicationInfo mAppInfo;
    protected String qq_app_id;
    protected String wx_app_id;

    private ProgressBar thirdFetchPro;
    private TextView mUserName;
    private ImageView mUserAva;
    private View mAllowLoginBtn;
    private View mQQCallbackPage;

    private View mBindRegistBtn;
    private View mBindLoginBtn;

    private LoginResult mLoginResult;

    private TextView mThridNameLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_login_layout);

        try {
            mAppInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            wx_app_id = mAppInfo.metaData.getString("WEIXIN_APPID");
            qq_app_id = mAppInfo.metaData.getInt("QQ_APPID") + "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTencent = Tencent.createInstance(qq_app_id, getApplicationContext());

        initLayout();

        mLoginFragment = LoginFragment.getInstance();
        mRegistFragment = RegisterFragment.getInstance();
        mRegistFragment.setArguments(new Bundle());


        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            String code = bundle.getString("wx_code");
            if(!TextUtils.isEmpty(code)) fetchThirdInfoWX(code);

        }

        choosePage(mLoginFragment);

    }

    @Override
    protected void initLayout() {

        View view = mActionBar.getCustomView().findViewById(R.id.login_action_layout_root);
        mTitleTxt = (TextView) view.findViewById(R.id.login_title);
        mGobackBtn = view.findViewById(R.id.login_go_back);
        mToRegist = view.findViewById(R.id.login_to_reg);

        mRootView = findViewById(CONTENT_EARE);
        mThridNameLeft = (TextView) findViewById(R.id.name_left);

        thirdFetchPro = (ProgressBar) findViewById(R.id.qq_third_progress);
        mUserName = (TextView) findViewById(R.id.qq_third_name);
        mUserAva = (ImageView) findViewById(R.id.qq_third_user_ava);
//        mAllowLoginBtn = findViewById(R.id.qq_third_btn);
        mQQCallbackPage = findViewById(R.id.qq_login_app_callback);

        mBindRegistBtn = findViewById(R.id.register_bind_btn);
        mBindLoginBtn = findViewById(R.id.login_bind_btn);

        mGobackBtn.setOnClickListener(mOnClickListener);
        mToRegist.setOnClickListener(mOnClickListener);

    }

    private void choosePage(BaseFragment fragment) {

        final FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if(fragment.isAdded()) {
            transaction.hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        } else {
            if(mCurrentFragment != null) {
                transaction.addToBackStack(mCurrentFragment.getName());
                transaction.hide(mCurrentFragment).add(CONTENT_EARE, fragment).commitAllowingStateLoss();
            } else {
                transaction.add(CONTENT_EARE, fragment).commitAllowingStateLoss();
            }
        }

        mCurrentFragment = fragment;
        mRootView.setFitsSystemWindows(true);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.login_go_back:
                    onBackPressed();
                    break;
                case R.id.login_to_reg:
                    choosePage(mRegistFragment);
                    mTitleTxt.setText(R.string.register_page_title);
                    mToRegist.setVisibility(View.INVISIBLE);
                    break;
                case R.id.login_bind_btn:

                    Intent inten = new Intent(LoginActivity.this, BindLoginActivity.class);
                    startActivityForResult(inten, 0x2123);

                    break;
                case R.id.register_bind_btn:

                    Intent intent = new Intent(LoginActivity.this, BindRegisterActivity.class);
                    startActivity(intent);

                    break;

            }

        }
    };

    public void forgetPassword() {

        mRegistFragment.isForgetPass = true;
        choosePage(mRegistFragment);
        mTitleTxt.setText(R.string.forget_pass_word);
        mToRegist.setVisibility(View.INVISIBLE);

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

    public void wxSignIn() {

        SendAuth.Req req = new SendAuth.Req();
        req.scope = StaticConfigs.WXLOGIN_SCOPE;
        req.state = StaticConfigs.WXREQ_STATE;
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, wx_app_id, true);
        wxapi.sendReq(req);
    }

    public void qqSignIn() {

        if (!mTencent.isSessionValid()) {
            mTencent.login(this, StaticConfigs.QQLOGIN_SCOPE, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        mTencent.onActivityResultData(requestCode, resultCode, data, this);

        if(requestCode == 0x2123) {

            if(resultCode == StaticConfigs.RESULT_CODE_LOGIN_OK) {
                setResult(StaticConfigs.RESULT_CODE_LOGIN_OK, data);
            }
            finish();

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);



    }

    @Override
    public void onComplete(Object o) {

        Log.i(TAG, "qq_uilistener : onComplete");

        JSONObject jo = (JSONObject) o;

        Log.i(TAG, "return info : " + jo.toString());

        String extraData = jo.toString();

        if(!TextUtils.isEmpty(extraData)) {

            fetchThirdInfoQQ(extraData);

        } else {

            Toast.makeText(this, "授权数据异常", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onError(UiError uiError) {
        Log.i(TAG, "qq_uilistener : UiError");

    }

    @Override
    public void onCancel() {
        Log.i(TAG, "qq_uilistener : onCancel");

    }

    @Override
    public void onBackPressed() {

        if(mCurrentFragment == null || mCurrentFragment instanceof LoginFragment) {
            super.onBackPressed();
        } else if(mCurrentFragment instanceof RegisterFragment)  {
            mTitleTxt.setText(R.string.login_title_login);
            mToRegist.setVisibility(View.VISIBLE);
            mFragmentManager.popBackStack();
            mCurrentFragment = mLoginFragment;
        }
    }

    private void fetchThirdInfoQQ(String extendData) {

        mQQCallbackPage.setVisibility(View.VISIBLE);
        thirdFetchPro.setVisibility(View.VISIBLE);

        mThridNameLeft.setText("尊敬的QQ用户：");

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("Key", "QQLogin");
        kvs.put("ExtendData", extendData);
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlQQLogin(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "qq login result info : " + response.toString());

                mLoginResult = new LoginParser<LoginResult>(new LoginResult()).parse(response);

                if(mLoginResult.dataobject == null || mLoginResult.dataobject.size() == 0) {

                    mQQCallbackPage.setVisibility(View.GONE);
                    thirdFetchPro.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, mLoginResult.message, Toast.LENGTH_SHORT).show();
                    return;

                }

                mLoginResult.loginMode = StaticConfigs.LOGIN_MODE_QQ;
                UserInfo ui = mLoginResult.dataobject.get(0);
                ui.type = UserInfo.TYPE_QQ;

                if(!mLoginResult.state) {

                    Glide.with(LoginActivity.this)
                            .load(mLoginResult.dataobject.get(0).avatar)
                            .placeholder(R.drawable.default_avatar)
                            .bitmapTransform(new CropCircleTransformation(LoginActivity.this))
                            .crossFade().into(mUserAva);

                    mUserName.setText(mLoginResult.dataobject.get(0).name);

                    thirdFetchPro.setVisibility(View.GONE);

                    mBindLoginBtn.setOnClickListener(mOnClickListener);
                    mBindRegistBtn.setOnClickListener(mOnClickListener);

                    StaticConfigs.mLoginResult = mLoginResult;

                } else {

                    mBindLoginBtn.setVisibility(View.INVISIBLE);
                    mBindRegistBtn.setVisibility(View.INVISIBLE);
                    AsyncDBHelper.saveLoginInfoDB(LoginActivity.this, mLoginResult, uiHandler);

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

    private void fetchThirdInfoWX(String code) {

        mQQCallbackPage.setVisibility(View.VISIBLE);
        thirdFetchPro.setVisibility(View.VISIBLE);

        mThridNameLeft.setText("尊敬的微信用户：");

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("Key", "WeiXinLogin");
        kvs.put("AuthorizationCode", code);
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlWxLogin(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "result info : " + response.toString());
                mLoginResult = new LoginParser<LoginResult>(new LoginResult()).parse(response);

                if(mLoginResult.dataobject == null || mLoginResult.dataobject.size() == 0) {

                    mQQCallbackPage.setVisibility(View.GONE);
                    thirdFetchPro.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, mLoginResult.message, Toast.LENGTH_SHORT).show();
                    return;

                }

                mLoginResult.loginMode = StaticConfigs.LOGIN_MODE_WX;
                UserInfo ui = mLoginResult.dataobject.get(0);
                ui.type = UserInfo.TYPE_WX;

                if(!mLoginResult.state) {

                    Glide.with(LoginActivity.this)
                            .load(mLoginResult.dataobject.get(0).avatar)
                            .placeholder(R.drawable.default_avatar)
                            .bitmapTransform(new CropCircleTransformation(LoginActivity.this))
                            .crossFade().into(mUserAva);

                    mUserName.setText(mLoginResult.dataobject.get(0).name);

                    thirdFetchPro.setVisibility(View.GONE);

                    mBindLoginBtn.setOnClickListener(mOnClickListener);
                    mBindRegistBtn.setOnClickListener(mOnClickListener);

                    StaticConfigs.mLoginResult = mLoginResult;

                } else {

                    mBindLoginBtn.setVisibility(View.INVISIBLE);
                    mBindRegistBtn.setVisibility(View.INVISIBLE);
                    AsyncDBHelper.saveLoginInfoDB(LoginActivity.this, mLoginResult, uiHandler);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("wx fetch info error : " + error.getMessage());
            }
        });

        mVolleyRequestQueue.add(jreq);

    }

    private AsyncDBHelper.UIHandler<LoginResult> uiHandler = new AsyncDBHelper.UIHandler<LoginResult>() {

        @Override
        public void processMessage(int what, LoginResult loginResult) {
            switch(what) {
                case SEND_SAVE_USERINFO_OK:

                    StaticConfigs.mLoginResult = loginResult;
                    Intent data = new Intent(MainActivity.ACTION_REFRESH_UI);
                    data.putExtra(StaticConfigs.RESULT_CODE_LOGIN_KEY, loginResult);
                    LoginActivity.this.sendBroadcast(data);
                    finish();

                    break;
            }
        }
    };

}

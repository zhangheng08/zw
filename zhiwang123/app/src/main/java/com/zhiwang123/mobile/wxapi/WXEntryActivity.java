package com.zhiwang123.mobile.wxapi;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.ZWApplication;
import com.zhiwang123.mobile.phone.activity.BaseActivity;
import com.zhiwang123.mobile.phone.activity.LoginActivity;
import com.zhiwang123.mobile.phone.activity.MainActivity;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.parser.LoginParser;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    public static final String TAG = "WXEntryActivity";

    private ProgressBar thirdFetchPro;
    private TextView mUserName;
    private ImageView mUserAva;
    private View mAllowLoginBtn;

    private LoginResult mLoginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxapi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_login_wx);

        ApplicationInfo appInfo = null;
        String wx_app_id = "-1";

        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        wx_app_id = appInfo.metaData.getString("WEIXIN_APPID");
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, wx_app_id, true);
        wxapi.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {


        SendAuth.Resp resp =  (SendAuth.Resp) baseResp;

        Log.i("123 ------- ", resp.code  + "-" + resp.country + "-" + resp.state);

        String respCode = resp.code;

        if(!TextUtils.isEmpty(respCode)) {

//            thirdFetchPro = (ProgressBar) findViewById(R.id.fetch_third_progress);
//            mUserName = (TextView) findViewById(R.id.login_third_name);
//            mUserAva = (ImageView) findViewById(R.id.login_third_user_ava);
//            mAllowLoginBtn = findViewById(R.id.login_third_btn);
//            fetchThirdInfo(respCode);

            Intent intent = new Intent(ZWApplication.getAppContext(), LoginActivity.class);
            intent.putExtra("wx_code", respCode);
            startActivity(intent);

        }

        finish();

    }

    private void fetchThirdInfo(String code) {

        thirdFetchPro.setVisibility(View.VISIBLE);

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("Key", "WeiXinLogin");
        kvs.put("AuthorizationCode", code);
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlWxLogin(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                mLoginResult = new LoginParser<LoginResult>(new LoginResult()).parse(response);
                mLoginResult.loginMode = StaticConfigs.LOGIN_MODE_WX;
                mLoginResult.accessToken = mLoginResult.dataobject.get(0).token;
                mLoginResult.account = mLoginResult.dataobject.get(0).userName;

                Glide.with(WXEntryActivity.this)
                        .load(mLoginResult.dataobject.get(0).avatar)
                        .placeholder(R.drawable.default_avatar)
                        .bitmapTransform(new CropCircleTransformation(WXEntryActivity.this))
                        .crossFade().into(mUserAva);

                mUserName.setText(mLoginResult.dataobject.get(0).name);

                thirdFetchPro.setVisibility(View.GONE);

                mAllowLoginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AsyncDBHelper.saveLoginInfoDB(WXEntryActivity.this, mLoginResult, uiHandler);

                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
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
                    WXEntryActivity.this.sendBroadcast(data);
                    finish();

                    break;
            }
        }
    };

}

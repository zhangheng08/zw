package com.zhiwang123.mobile.phone.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.message.PushAgent;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.utils.SystemBarTintManager;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected RequestQueue mVolleyRequestQueue;
    protected FragmentManager mFragmentManager;
    protected ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mVolleyRequestQueue = Volley.newRequestQueue(this);
        mFragmentManager = getSupportFragmentManager();
        mActionBar = getSupportActionBar();
        mActionBar.hide();

        PushAgent.getInstance(this).onAppStart();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "reslult reqCode:" + requestCode + "-resCode:" + resultCode);

        switch(requestCode) {
            case StaticConfigs.REQUEST_CODE_LOGIN:
                if(resultCode == StaticConfigs.RESULT_CODE_LOGIN_OK) {
                    LoginResult lr = (LoginResult) data.getSerializableExtra(StaticConfigs.RESULT_CODE_LOGIN_KEY);
                    StaticConfigs.mLoginResult = lr;
                }
                break;
        }

    }

    protected void setTranslucentStatus(boolean on) {
        setTranslucentStatus(on, R.color.colorPrimary);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on, int drawableOrColorResId) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(drawableOrColorResId);
    }

    protected void initLayout() {

    }

}

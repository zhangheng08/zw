package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.VersionModel;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ty on 2016/10/27.
 */

public class AboutActivity extends BaseActivity {

    private Context mContext;

    private View versionCheckBtn;
    private TextView versionName;
    private ProgressBar checkProgress;

    private View mGobackBtn;
    private TextView mTitleTv;

    String desc = "";
    String versionNumber = "";
    String hash = "";
    String remotePath = "";
    long fileSize = 0l;
    long downloadId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_custom_layout);
        initLayout();

    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.login_action_layout_root);
        mGobackBtn = actionView.findViewById(R.id.go_back_btn);
        mTitleTv = (TextView) actionView.findViewById(R.id.title);
        mTitleTv.setText("设置");
        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        versionName = (TextView) findViewById(R.id.version_name);
        versionCheckBtn = findViewById(R.id.v_check_btn);
        checkProgress = (ProgressBar) findViewById(R.id.v_check_pro);

        versionName.setText(StaticConfigs.getAppVersionName(this));

        versionCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                versionCheck();

            }
        });

    }


    private void versionCheck() {

        checkProgress.setVisibility(View.VISIBLE);

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.VERSION_CHECK_URL, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.has("Description")) desc = response.getString("Description");
                            if(response.has("VersionNumber")) versionNumber = response.getString("VersionNumber");
                            if(response.has("Hash")) hash = response.getString("Hash");
                            if(response.has("FilePath")) remotePath = response.getString("FilePath");
                            if(response.has("FileSize")) fileSize = response.getLong("FileSize");

                            VersionModel vm = new VersionModel();
                            vm.desc = desc;
                            vm.versionNumber = versionNumber;
                            vm.HashStr = hash;
                            vm.filePath = remotePath;
                            vm.fileSize = fileSize;

                            boolean ifUpdate = versionCompare(vm.versionNumber);

                            if(ifUpdate) {

//                                Intent intent = new Intent(AboutActivity.this, LHUpdateService.class);
//                                intent.putExtra("update_path", vm.filePath);
//                                startService(intent);

                                Intent intent = new Intent(AboutActivity.this, UpdateDialogActivity.class);
                                intent.putExtra(UpdateDialogActivity.MESSAEG, desc);
                                intent.putExtra(UpdateDialogActivity.VERSION_STR, versionNumber);
                                intent.putExtra(UpdateDialogActivity.UPDATE_PATH, remotePath);
                                intent.putExtra(UpdateDialogActivity.REMOTE_LENGTH, fileSize);
                                startActivity(intent);
                                overridePendingTransition(R.anim.translate_in_zw_v, 0);

                            } else {

                                Toast.makeText(AboutActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();

                            }


                        } catch(JSONException e) {

                            e.printStackTrace();

                        } finally {

                            checkProgress.setVisibility(View.GONE);

                        }



                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private boolean versionCompare(String serverVersion) {

        boolean bo = false;

        String currentVersion = StaticConfigs.getAppVersionName(this);

        Log.i("server v string: ", serverVersion);
        Log.i("current v string: ", currentVersion);


        int serverVersionInt = versionStrToInt(serverVersion);
        int currentVersionInt = versionStrToInt(currentVersion);

        Log.i("server version int: ", serverVersionInt + "");
        Log.i("current version int: ", currentVersionInt + "");

        if(serverVersionInt > 0 && currentVersionInt > 0) {

            bo = serverVersionInt > currentVersionInt ? true : false;

        } else {

            bo = false;

        }

        return bo;

    }

    private int versionStrToInt(String versionStr) {

        int serverVersionInt = 0;

        try {

            String[] strs = versionStr.split("\\.");

            for(int i = 0; i < strs.length; i ++) {

                int zeros = 1;

                if(i == 0) {

                    zeros *= 1000;

                } else if(i == 1) {

                    zeros *= 100;

                } else if(i == 2) {

                    zeros *= 10;

                }

                serverVersionInt += Integer.valueOf(strs[i]) * zeros;

            }

        } catch (Exception e) {

            serverVersionInt = -1;

        }

        return serverVersionInt;


    }


}

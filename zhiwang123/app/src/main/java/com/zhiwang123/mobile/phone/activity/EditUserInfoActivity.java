package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.avatar.AvatarPickWindow;
import com.zhiwang123.mobile.phone.widget.cacheimage.NetWorkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/10/27.
 */

public class EditUserInfoActivity extends BaseActivity {

    public static final String TAG = "EditUserInfoActivity";

    public static final String KEY_NM = "key_name";
    public static final String KEY_AV = "key_avatar";

    private Context mContext;

    private View mToEditAvatar;
    private View mToEidtName;
    private View mToBindPhone;

    private TextView mTitle;
    private View mEditOk;
    private View mGoBack;

    private NetWorkImageView mAvatarimgv;
    private EditText mNameInput;

    private String base64String;
    private String avatarUrl;
    private String avatarUrl_;
    private String mName;

    private AvatarPickWindow mAvatarPickWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_use_layout);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_edit_layout);

        initLayout();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            avatarUrl = bundle.getString(KEY_AV);
            avatarUrl_ = avatarUrl;
            mName = bundle.getString(KEY_NM);
            mAvatarimgv.loadBitmap(avatarUrl, R.drawable.default_avatar);
            mNameInput.setText(mName);

        }

    }

    @Override
    protected void initLayout() {

        View av = mActionBar.getCustomView();
        mTitle = (TextView) av.findViewById(R.id.eidt_title);
        mEditOk = av.findViewById(R.id.eidt_ok);
        mGoBack = av.findViewById(R.id.edit_go_back);

        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToEditAvatar = findViewById(R.id.edit_to_avatar);
        mToEidtName = findViewById(R.id.edit_to_name);
        mToBindPhone = findViewById(R.id.edit_to_bind);

        mNameInput = (EditText) findViewById(R.id.edit_name);

        mAvatarimgv = (NetWorkImageView) findViewById(R.id.edit_avatar);
        mAvatarimgv.isShowRoundCorner = true;
        mAvatarimgv.xRadius = 90;
        mAvatarimgv.yRadius = 90;

        initAvatarPickDialog(mAvatarimgv);

        mEditOk.setOnClickListener(onClickListener);
        mToEditAvatar.setOnClickListener(onClickListener);
        mToBindPhone.setOnClickListener(onClickListener);

    }

    public void initAvatarPickDialog(ImageView avatar) {
        mAvatarPickWindow = new AvatarPickWindow(this, avatar, avatarItemsOnClick);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.edit_to_avatar:

                    if(mAvatarPickWindow == null) return;
                    mAvatarPickWindow.showAtLocation(EditUserInfoActivity.this.findViewById(R.id.edit_page_root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                    break;
                case R.id.edit_to_name:

                    break;

                case R.id.edit_to_bind:

                    break;
                case R.id.eidt_ok:

                    if(!avatarUrl.equals(avatarUrl_) || !mName.equals(mNameInput.getText().toString())) {


                        updateNickName();


                    } else {

                        onBackPressed();

                    }

                    break;
            }

        }
    };


    public View.OnClickListener avatarItemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            mAvatarPickWindow.dismiss();

            Intent intent = null;
            int requestCode = 0;

            switch (v.getId()) {
                case R.id.btn_take_photo:
                    intent = mAvatarPickWindow.getStartCamearPicCutIntent();
                    requestCode = AvatarPickWindow.PHOTO_REQUEST_TAKEPHOTO;
                    break;
                case R.id.btn_pick_photo:
                    intent = mAvatarPickWindow.getStartImageCaptrueIntent();
                    requestCode = AvatarPickWindow.PHOTO_REQUEST_GALLERY;
                    break;
                default:
                    break;
            }

            startActivityForResult(intent, requestCode);
        }

    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 33:
                startPhotoZoom(Uri.fromFile(mAvatarPickWindow.tempFile), 150);
                break;
            case 32:
                if (data != null) {
                    startPhotoZoom(data.getData(), 150);
                }
                break;
            case 34:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, AvatarPickWindow.PHOTO_REQUEST_CUT);
    }

    private void setPicToView(Intent picdata) {

        Bundle bundle = picdata.getExtras();

        if (bundle != null) {

            final Bitmap photo = bundle.getParcelable("data");

            base64String = AvatarPickWindow.bitmapToString(photo);

            Log.v("avatar 64 value: ", base64String);

            getSharedPreferences("avatar_base64string", 0).edit().putString("avatar_base64string_key", base64String).commit();

            mAvatarimgv.setImageBitmap(photo);

            Map<String, String> kvs = new HashMap<String, String>();
            kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
            kvs.put("Avatar", base64String);
            kvs.put("FileExt", "png");
            JSONObject params = new JSONObject(kvs);

            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlUpAvatar(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    boolean state = false;

                    if(response.has("State")) try {

                        state = response.getBoolean("State");

                        if(state) {

                            if(response.has("Avatar")) {

                                avatarUrl = response.getString("Avatar");

                                Log.i(TAG, "avatar url : " + avatarUrl);

                            }

                        }

                    } catch (JSONException e) {
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
    }

    private void updateNickName() {

        Map<String, String> kvs = new HashMap<String, String>();
        kvs.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
        kvs.put("Name", mNameInput.getText().toString());
        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlSetNick(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                boolean state = false;

                Log.i(TAG, response.toString());

                if(response.has("State")) try {

                    state = response.getBoolean("State");

                    if(state) {

                        Intent data = new Intent();

                        data.putExtra(KEY_AV, avatarUrl);
                        data.putExtra(KEY_NM, mNameInput.getText().toString());
                        setResult(0x1, data);

                        finish();

                    }

                } catch (JSONException e) {
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

}

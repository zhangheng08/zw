package com.zhiwang123.mobile.phone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ty on 2016/10/27.
 */

public class VideoHintDialogActivity extends Activity {

    private static final String TAG = "OrganDialogActivity";

    public static final String KEY_CARD_ID = "card_id";
    public static final String KEY_VIDEO_ID = "video_id";
    public static final String KEY_VIDEO_NAME = "video_name";

    private Context mContext;
    private RequestQueue mVolleyRequestQueue;

    private View mConfirmBtn;
    private View mCancelBtn;

    private View mHintCon;
    private View mActiveCon;

    private String cardId;
    private String videoName;
    private String videoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_hint);
        mContext = this;

        mVolleyRequestQueue = Volley.newRequestQueue(this);

        mConfirmBtn = findViewById(R.id.hint_confirm);
        mCancelBtn = findViewById(R.id.hint_cancel);

        mHintCon = findViewById(R.id.hint_con);
        mActiveCon = findViewById(R.id.hint_active_con);

        mConfirmBtn.setOnClickListener(mOnClick);
        mCancelBtn.setOnClickListener(mOnClick);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            cardId = bundle.getString(KEY_CARD_ID);
            videoName = bundle.getString(KEY_VIDEO_NAME);
            videoId = bundle.getString(KEY_VIDEO_ID);

        }

    }

    private View.OnClickListener mOnClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.hint_confirm:

                    activeCard(cardId);

                    break;
                case R.id.hint_cancel:

                    onBackPressed();

                    break;
            }
        }
    };


    private void activeCard(String cardId) {

        Map<String, String> kvs = new HashMap<String, String>();

        kvs.put("CardId", cardId);

        JSONObject params = new JSONObject(kvs);

        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlActiveCard(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {

                    if(response.getBoolean("State")) {

                        Intent data = new Intent();

                        data.putExtra(KEY_VIDEO_NAME, videoName);
                        data.putExtra(KEY_CARD_ID, videoId);

                        setResult(2, data);


                    } else {

                        Toast.makeText(mContext, response.getString("Message"), Toast.LENGTH_SHORT).show();

                    }

                } catch(JSONException e) {

                    e.printStackTrace();

                }

                onBackPressed();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        mVolleyRequestQueue.add(jreq);

    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}

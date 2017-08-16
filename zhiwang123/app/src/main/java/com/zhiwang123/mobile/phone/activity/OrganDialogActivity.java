package com.zhiwang123.mobile.phone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.OrganListAdapter;
import com.zhiwang123.mobile.phone.bean.Organ;
import com.zhiwang123.mobile.phone.bean.OrganLogin;
import com.zhiwang123.mobile.phone.bean.parser.OrganParser;
import com.zhiwang123.mobile.phone.fragment.LoginFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

/**
 * Created by ty on 2016/10/27.
 */

public class OrganDialogActivity extends Activity {

    private static final String TAG = "OrganDialogActivity";

    private RequestQueue mVolleyRequestQueue;
    private EditText mOrganInputEdt;
    private ListView mOrganListView;
    private OrganListAdapter mOrganListAdapter;
    private ProgressBar mOrganWaiting;
    private Context mCotnext;
    private View mCloseBtn;
    private Organ mCurrentOrgan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.organ_dialog);
        mCotnext = getApplication();

        mVolleyRequestQueue = Volley.newRequestQueue(this);
        mOrganInputEdt = (EditText) findViewById(R.id.organ_input);
        mOrganListView = (ListView) findViewById(R.id.organ_list);
        mOrganWaiting = (ProgressBar) findViewById(R.id.organ_list_waiting);
        mCloseBtn = findViewById(R.id.organ_close_btn);

        mOrganListView.setOnItemClickListener(onItemClickListener);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mOrganInputEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mOrganWaiting.setVisibility(View.VISIBLE);
                mOrganListView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

                loadOrganList(s.toString());

            }
        });

        //loadOrganList("");

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            OrganListAdapter.CellHolder holder = (OrganListAdapter.CellHolder) view.getTag();

            String organName = holder.nameTxv.getText().toString();

            mCurrentOrgan = holder.entity;

            mOrganInputEdt.setText(organName);

        }
    };

    private void loadOrganList(final String name) {

        mOrganWaiting.setVisibility(View.VISIBLE);

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getOrgans(name), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        OrganLogin organLogin = new OrganParser<OrganLogin>(new OrganLogin()).parse(response);
                        mOrganListAdapter = new OrganListAdapter(mCotnext, organLogin.dataobject);
                        mOrganListView.setAdapter(mOrganListAdapter);
                        mOrganListAdapter.notifyDataSetChanged();

                        mOrganWaiting.setVisibility(View.INVISIBLE);
                        mOrganListView.setVisibility(View.VISIBLE);

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

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.umeng_socialize_slide_out_from_bottom, 0);
    }

    @Override
    public void onBackPressed() {

        if(mCurrentOrgan != null) {

            Intent data = new Intent();

            String key = mCurrentOrgan.key;
            String name = mCurrentOrgan.name;

            data.putExtra(LoginFragment.RESULT_KEY, key);
            data.putExtra(LoginFragment.RESULT_NAME, name);
            setResult(LoginFragment.RESULT_CODE_ORGAN_KEY, data);
            finish();

        }

        super.onBackPressed();
    }
}

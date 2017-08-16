package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.CmsListAdapter;
import com.zhiwang123.mobile.phone.bean.CmsModel;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CmsListActivity extends BaseActivity {

    private static final String TAG = "CmsListActivity";

    private Context mContext;

    private View mGoback;
    private TextView mTitle_txv;
    private TextView mSubBtn_txv;

    private XListView mListView;
    private CmsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.cms_list_item);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_custom_layout);
        initLayout();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {



        }

        loadCmslist();

    }

    protected void initLayout() {

        View view = mActionBar.getCustomView();

        mTitle_txv = (TextView) view.findViewById(R.id.title);
        mSubBtn_txv = (TextView) view.findViewById(R.id.sub_btn);
        mSubBtn_txv.setVisibility(View.GONE);
        mGoback = view.findViewById(R.id.go_back_btn);

        mListView = (XListView) findViewById(R.id.cms_list_view);

        mGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle_txv.setText("新闻动态");

    }

    private void loadCmslist() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCmsList("abfcd939-dfdf-4d83-8b3f-74f285d381db", 1), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        ArrayList<CmsModel> list = new ArrayList<CmsModel>();

                        try {

                            JSONArray jsonArr = response.getJSONArray("DataObject");

                            for(int i = 0; i < jsonArr.length(); i ++) {

                                JSONObject job = jsonArr.getJSONObject(i);

                                CmsModel model = new CmsModel();

                                model.id = job.getString(CmsModel.ID);
                                model.title = job.getString(CmsModel.Title);
                                model.picture = job.getString(CmsModel.PICTURE);
                                model.summary = job.getString(CmsModel.SUMMARY);

                                list.add(model);

                            }

                        } catch(JSONException ex) {

                            ex.printStackTrace();

                        }

                        if(list.size() != 0) {

                            mAdapter = new CmsListAdapter(CmsListActivity.this, list);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                            mListView.setOnItemClickListener(mOnItemClick);

                        }

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, error.getMessage() + "");

                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            CmsListAdapter.CellHolder holder = (CmsListAdapter.CellHolder) view.getTag();
            String cmsId = holder.entity.id;

            Intent intent = new Intent(mContext, TopicWebPageActivity.class);
            intent.putExtra(TopicWebPageActivity.KEY_CMS_ID, cmsId);
            startActivity(intent);

        }
    };

}

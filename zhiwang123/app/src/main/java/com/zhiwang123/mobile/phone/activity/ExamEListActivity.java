package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.ExamEListAdpater;
import com.zhiwang123.mobile.phone.bean.ExamECEntity;
import com.zhiwang123.mobile.phone.bean.ExamECategory;
import com.zhiwang123.mobile.phone.bean.parser.ExamECategroyParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ty on 2016/11/2.
 */

public class ExamEListActivity extends BaseActivity {

    private static final String TAG = "CourseListActivity";

    private View mRootView;
    private View mGobackBtn;
    private TextView mTitleTxt;

    private Context mContext;

    private ListView mListView;
    private ExamEListAdpater mAdapter;
    private ArrayList<ExamECategory> mDateList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exam_e_list_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mContext = getApplicationContext();

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_exam_e_list);

        initLayout();

    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.exam_e_action_root);
        mTitleTxt = (TextView) actionView.findViewById(R.id.exam_e_title);
        mGobackBtn = actionView.findViewById(R.id.exam_e_go_back);
//        mTitleTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        mListView = (ListView) findViewById(R.id.exam_e_list_list);

        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadExamList();

    }

    private void loadExamList() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlExamEList(StaticConfigs.mLoginResult.accessToken, 1, 10), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        ExamECEntity eec = new ExamECategroyParser().parse(response);
                        mDateList = eec.dataobject;
                        mAdapter = new ExamEListAdpater(ExamEListActivity.this, mDateList);
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        mListView.setOnItemClickListener(onItemClickListener);

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ExamEListAdpater.CellHolder holder = (ExamEListAdpater.CellHolder) view.getTag();

            Intent intent = new Intent(mContext, ExamPaperActivity.class);

            intent.putExtra(StaticConfigs.KEY_EXAM_PAPER, holder.examPaperId);

            intent.putExtra(ExamPaperActivity.KEY_E_TPYE, ExamPaperActivity.E_TYPE_EXAM_CENTER);

            ExamEListActivity.this.startActivity(intent);

        }
    };

}

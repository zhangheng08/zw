package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.BaseTask;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.TeachCoursesAdapter;
import com.zhiwang123.mobile.phone.adapter.TeachPlanPagerAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseTeachPlan;
import com.zhiwang123.mobile.phone.bean.TeachPlan;
import com.zhiwang123.mobile.phone.bean.TeachPlanEntity;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.bean.parser.TeachPlanParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.ZWScrollTransformer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by ty on 2016/11/2.
 */

public class TeachPlanActivity extends BaseActivity implements AsyncExecuteCallback {

    private static final String TAG = "TeachPlanActivity";

    private Context mContext;

    private View mRootView;
    private View mGobackBtn;
    private TextView mTitleTxt;
    private ViewPager mPager;
    private TeachPlanPagerAdapter mPagerAdapter;

    private ArrayList<TeachPlan> mTeachPlanTopics;
    private ArrayList<Course> mTeachPlanCourses;

    private HashMap<String, ArrayList<Course>> mTeachPlanIdCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.teach_plan_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.zw_trans);
        }

        mActionBar.setElevation(0);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setBackgroundDrawable(null);

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_teach_plan_layout);

        Bundle bundle = getIntent().getExtras();

        mContext = getApplicationContext();

        initLayout();

        loadTeachPlanTopics();

    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.course_list_action_layout_root);
        mTitleTxt = (TextView) actionView.findViewById(R.id.tachp_list_title);
        mGobackBtn = actionView.findViewById(R.id.tachp_list_go_back);
//        mTitleTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
        mTitleTxt.setText(R.string.page_course_center);

        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void initItemViews() {

        BaseTask task = new BaseTask(0, this);
        AsyncExecutor.getInstance().execMuliteTask(task);

    }

    public void loadTeachPlanTopics() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlTeachPlan(StaticConfigs.mLoginResult.accessToken), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        TeachPlanEntity teachPlanEntity = new  TeachPlanParser<TeachPlanEntity>(new TeachPlanEntity()).parse(response);
                        mTeachPlanTopics = teachPlanEntity.dataobject;

                        if(mTeachPlanTopics != null && mTeachPlanTopics.size() != 0) {

                            mTeachPlanIdCourses = new HashMap<>();

                            initItemViews();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, error.getMessage());

                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    public void loadTeachPlanCourses(final String teachPlanId, final ListView listView) {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlTeachPlanCourses(StaticConfigs.mLoginResult.accessToken, teachPlanId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseTeachPlan ct = new CourseParser<CourseTeachPlan>(new CourseTeachPlan()).parse(response);
                        mTeachPlanCourses = ct.dataobject;

                        if(mTeachPlanCourses != null && mTeachPlanCourses.size() != 0) {

                            mTeachPlanIdCourses.put(teachPlanId, mTeachPlanCourses);
                            TeachCoursesAdapter adapter = new TeachCoursesAdapter(TeachPlanActivity.this, teachPlanId, mTeachPlanCourses);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

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

    @Override
    public void pre(Object... objs) {

    }

    @Override
    public BaseTask.TaskResult async(int what, BaseTask baseTask) {

        switch (what) {
            case 0:

                List<View> views = new ArrayList<View>();

                for(int i = 0; i < mTeachPlanTopics.size(); i ++) {

                    View v = LayoutInflater.from(this).inflate(R.layout.teach_plan_item, null);
                    views.add(v);

                }

                BaseTask.TaskResult result = baseTask.new TaskResult();
                result.setResult(views);

                return  result;

        }

        return null;
    }

    @Override
    public void post(Message msg) {

        BaseTask task = (BaseTask) msg.obj;

        switch (msg.what) {

            case 0:

                ArrayList<View> views = (ArrayList<View>) task.result.getResult();
                ViewPager viewPager = (ViewPager) findViewById(R.id.tachp_view_pager);
                viewPager.setOffscreenPageLimit(4);
                viewPager.setAdapter(new TeachPlanPagerAdapter(views));
                viewPager.setPageTransformer(true, new ZWScrollTransformer());

                for(int i = 0; i < mTeachPlanTopics.size(); i ++) {

                    TeachPlan tp = mTeachPlanTopics.get(i);
                    View v = views.get(i);

                    TextView teachPlanNameTv = (TextView) v.findViewById(R.id.tachp_name);
                    TextView teachPlanTargetTv = (TextView) v.findViewById(R.id.tachp_target);
                    TextView teachPlanPersonTv = (TextView) v.findViewById(R.id.tachp_person);
                    TextView teachPlanDateTv = (TextView) v.findViewById(R.id.tachp_date);

                    teachPlanNameTv.setText(tp.name);
                    teachPlanTargetTv.setText(tp.description);
                    teachPlanDateTv.setText(tp.startDate + mContext.getString(R.string.tachp_zhi) + tp.endDate);
                    teachPlanPersonTv.setText(mContext.getString(R.string.tachp_study_p, tp.studyPersons));

                    loadTeachPlanCourses(tp.id, (ListView)(views.get(i).findViewById(R.id.tachp_sub_clistv)) );

                }

                break;

        }

    }
}

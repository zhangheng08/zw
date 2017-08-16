package com.zhiwang123.mobile.phone.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.CourseListPageAdapter;
import com.zhiwang123.mobile.phone.bean.CourseCenter;
import com.zhiwang123.mobile.phone.bean.Teacher;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by ty on 2016/10/27.
 */

public class TeacherIntroActivity extends BaseActivity {

    private static final String TAG = "TeacherIntroActivity";

    public static final String EXTRA_TEACHER_KEY = "key_teacher";

    private Context mContext;

    private Teacher mTeacher;

    private TextView mNameTx;
    private TextView mDescTx;
    private ImageView mAvatarImg;
    private ListView mCourseListV;
    private CourseListPageAdapter mCourseAdapter;

    private TextView mTitleTxt;
    private View mGobackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        setContentView(R.layout.teacher_intro_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        System.out.println("123123");

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_bind_regist_layout);

        initLayout();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            mTeacher = (Teacher) bundle.getSerializable(EXTRA_TEACHER_KEY);

            if(mTeacher != null) {

                mNameTx.setText(mTeacher.name);
//                Glide.with(mContext).load(mTeacher.avatar).crossFade().into(mAvatarImg);
                Glide.with(mContext).load(mTeacher.avatar).placeholder(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(mContext)).crossFade().into(mAvatarImg);

                mDescTx.setText(Html.fromHtml(mTeacher.introduction));

                mTitleTxt.setText(mTeacher.name);

                loadTeacherCourseList();

            }

        }

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(StaticConfigs.CART_CHANGE_ACITON);
        filter2.setPriority(Integer.MAX_VALUE);
        registerReceiver(mCartChangeRecv, filter2);

    }

    private void loadTeacherCourseList() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseList(0, "", mTeacher.name, "360*270", 1, 100), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseCenter ce = new CourseParser<CourseCenter>(new CourseCenter()).parse(response);

                        mCourseAdapter = new CourseListPageAdapter(TeacherIntroActivity.this, ce.dataobject);
                        mCourseListV.setAdapter(mCourseAdapter);

                        mCourseAdapter.notifyDataSetChanged();

                        mCourseListV.setOnItemClickListener(onItemClickListener);

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        mVolleyRequestQueue.add(jsonRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mCartChangeRecv);
    }

    public void initLayout() {

        View view = mActionBar.getCustomView().findViewById(R.id.login_action_layout_root);
        mTitleTxt = (TextView) view.findViewById(R.id.bind_title);
        mGobackBtn = view.findViewById(R.id.bind_go_back);

        mNameTx = (TextView) findViewById(R.id.teacher_detail_name);
        mAvatarImg = (ImageView) findViewById(R.id.teacher_detail_avatar);
        mDescTx = (TextView) findViewById(R.id.teacher_detail_desc);
        mCourseListV = (ListView) findViewById(R.id.teacher_course_list);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = null;

			if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) {

				intent = new Intent(mContext, LoginActivity.class);
				startActivityForResult(intent, StaticConfigs.REQUEST_CODE_LOGIN);
				return;
			}

            CourseListPageAdapter.CellHolder holder = (CourseListPageAdapter.CellHolder) view.getTag();
            String courseId = holder.c.courseid;
            intent = new Intent(mContext, CourseIntroduceActivity.class);
            intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_CID, courseId);
            startActivity(intent);

        }
    };

    private BroadcastReceiver mCartChangeRecv = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(StaticConfigs.CART_CHANGE_ACITON)) {

                if(mTeacher != null) loadTeacherCourseList();

            }

        }
    };

}

package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.ExamPaper;
import com.zhiwang123.mobile.phone.bean.ExamPublic;
import com.zhiwang123.mobile.phone.bean.parser.ExamParser;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.ExamGuideFragment;
import com.zhiwang123.mobile.phone.fragment.ExamResultFragment;
import com.zhiwang123.mobile.phone.fragment.QuestionPagerFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ty on 2016/11/2.
 */

public class ExamPaperActivity extends BaseActivity {

    private static final String TAG = "ExamPaperActivity";

    public static final int CONTENT_EARE = R.id.paper_content;

    public static final int FRAGMENT_GUIDE = 0;
    public static final int FRAGMETN_ANSWER_QUESTION = 1;
    public static final int FRAGMENT_RESULT = 2;

    public static final String KEY_E_TPYE = "e_type";

    public static final int E_TYPE_EXAM_CENTER =  0;
    public static final int E_TYPE_TEACHPLAN =  1;

    public int mEtype = E_TYPE_EXAM_CENTER;

    private View mRootView;
    private TextView mTitleTimer;
    private View mExamGoback;

    private BaseFragment mCurrentFragment;

    private ExamPublic mExamPublic;
    private ExamPaper mExamPaper;
    private String mExamPaperId;
    private Context mContext;


    private Timer mTimer;

    private int hour;
    private int minute;
    private int second;

    private String mCurrentTimerStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exam_paper_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mContext = getApplicationContext();

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_exam);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            mExamPaperId = bundle.getString(StaticConfigs.KEY_EXAM_PAPER);
            mEtype = bundle.getInt(KEY_E_TPYE, E_TYPE_EXAM_CENTER);
        }

        initLayout();

        if(StaticConfigs.mLoginResult != null) {

            switch(StaticConfigs.mLoginResult.loginMode) {

                case StaticConfigs.LOGIN_MODE_PB:

                    loadExamPaper();

                    break;
                case StaticConfigs.LOGIN_MODE_E:

                    loadExamPaperE();

                    break;
            }

        }
    }

    @Override
    protected void initLayout() {

        View actv = mActionBar.getCustomView().findViewById(R.id.course_list_action_layout_root);
        mTitleTimer = (TextView) actv.findViewById(R.id.exam_title_timer);
        mExamGoback = actv.findViewById(R.id.exam_go_back);
        mRootView = findViewById(CONTENT_EARE);

        mExamGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadExamPaper() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlExamPublic(StaticConfigs.mLoginResult.accessToken, mExamPaperId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        processData(response);

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage() + "");
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private void loadExamPaperE() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlExamE(StaticConfigs.mLoginResult.accessToken, mExamPaperId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        processData(response);

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(error.getMessage() + "");

                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private void processData(JSONObject response) {

        mExamPublic = new ExamParser<ExamPublic>(new ExamPublic()).parse(response);
        mExamPaper  = mExamPublic.dataobject == null || mExamPublic.dataobject.size() == 0 ? null : mExamPublic.dataobject.get(0);

        if(mExamPaper != null) {

            choosePage(FRAGMENT_GUIDE);

        }

    }

    public void choosePage(int page) {

        BaseFragment fragment = null;
        Bundle bundle = null;

        switch(page) {
            case FRAGMENT_GUIDE:

                fragment = ExamGuideFragment.getInstance();

                bundle = new Bundle();
                bundle.putInt(ExamGuideFragment.KEY_T, mExamPaper.timeLong);
                bundle.putInt(ExamGuideFragment.KEY_S, mExamPaper.totalScore);
                bundle.putInt(ExamGuideFragment.KEY_QC, mExamPaper.questionNum);
                bundle.putInt(ExamGuideFragment.KEY_PS, mExamPaper.passScore);
                bundle.putString(ExamGuideFragment.KEY_D, mExamPaper.description);
                fragment.setArguments(bundle);

                break;
            case FRAGMETN_ANSWER_QUESTION:

                startTimer();

                fragment = QuestionPagerFragment.getInstance();
                bundle = new Bundle();
                bundle.putSerializable(QuestionPagerFragment.KEY_CONTENT, mExamPublic);
                fragment.setArguments(bundle);

                break;
            case FRAGMENT_RESULT:

                mTimer.cancel();
                mTimer = null;

                fragment = ExamResultFragment.getInstance();
                bundle = new Bundle();

                if(StaticConfigs.LOGIN_MODE_E != StaticConfigs.mLoginResult.loginMode) {

                    Log.i(TAG, "buy id: " + mExamPaperId + " examId: " + mExamPaper.id);
                    bundle.putString(ExamResultFragment.KEY_EXAMID, mExamPaper.id);
                    bundle.putString(ExamResultFragment.KEY_BUYCID, mExamPaperId);
                    bundle.putString(ExamResultFragment.KEY_TIMER, mCurrentTimerStr);


                } else  {

                    if(mEtype == E_TYPE_EXAM_CENTER) {

                        Log.i(TAG, "examId: " + mExamPaper.id);
                        bundle.putString(ExamResultFragment.KEY_EXAMID, mExamPaper.id);

                    } else if(mEtype == E_TYPE_TEACHPLAN) {



                    }

                }

                fragment.setArguments(bundle);

                break;
        }

        final FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if(mCurrentFragment != null) {
            if(!mCurrentFragment.isAdded()) transaction.add(CONTENT_EARE, fragment).commitAllowingStateLoss();
            else transaction.replace(CONTENT_EARE, fragment).commitAllowingStateLoss();
        } else {
            transaction.add(CONTENT_EARE, fragment).commitAllowingStateLoss();
        }

        mCurrentFragment = fragment;
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

        if(mCurrentFragment != null) {

            if(mCurrentFragment.backPressed()) super.onBackPressed();

        } else {

            super.onBackPressed();

        }
    }

    public void startTimer() {

        mTimer = new Timer();

        mTimer.schedule(timerTask, 1000, 1000);

    }

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {

            ++ second;

            if(second > 59) {

                ++ minute;

                if(minute > 59) {

                    ++ hour;

                }

            }

            String secondStr = second > 10 ? "" + second : "0" + second;
            String minuteStr = minute > 10 ? "" + minute : "0" + minute;
            String hourStr = hour > 10 ? "" + hour : "0" + hour;

            String timerStr = hourStr + ":" + minuteStr + ":" + secondStr;

            Message msg = mHandler.obtainMessage();
            msg.obj = timerStr;
            msg.sendToTarget();

        }
    };

    private Handler mHandler =  new Handler () {

        @Override
        public void handleMessage(Message msg) {

            String timerStr = (String) msg.obj;

            mTitleTimer.setText("计时：" + timerStr);
            mCurrentTimerStr = timerStr;

        }
    };

}

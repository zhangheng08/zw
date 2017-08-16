package com.zhiwang123.mobile.phone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.CourseCenterListFragment;


/**
 * Created by ty on 2016/11/2.
 */

public class FavoriteListActivity extends BaseActivity {

    private static final String TAG = "CourseListActivity";

    public static final int CONTENT_EARE = R.id.course_list_content;

    public static final String EXTRA_SWITCH_FRAGMENT = "switch_fragment";

    public static final int FRAGMENT_COURSE_LIST   = 0;
    public static final int FRAGMENT_COURSE_CENTER = 1;

    private View mRootView;
    private View mGobackBtn;
    private TextView mTitleTxt;
    private BaseFragment mCurrentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.course_list_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_course_list_layout);

        Bundle bundle = getIntent().getExtras();

        initLayout();

    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.course_list_action_layout_root);
        mTitleTxt = (TextView) actionView.findViewById(R.id.course_list_title);
        mGobackBtn = actionView.findViewById(R.id.course_list_go_back);
        mRootView = findViewById(CONTENT_EARE);

        mTitleTxt.setText(R.string.fav_page_tit);
//        mTitleTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        mGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void choosePage(int flag) {

        BaseFragment fragment = null;

        switch(flag) {
            case FRAGMENT_COURSE_CENTER:

                fragment = new CourseCenterListFragment();
                mTitleTxt.setText(R.string.page_course_center);

                break;
            case FRAGMENT_COURSE_LIST:



                break;
        }

        final FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if(fragment.isAdded()) {
            transaction.hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        } else {
            if(mCurrentFragment != null) {
//                transaction.addToBackStack(mCurrentFragment.getName());
                transaction.hide(mCurrentFragment).add(CONTENT_EARE, fragment).commitAllowingStateLoss();
            } else {
                transaction.add(CONTENT_EARE, fragment).commitAllowingStateLoss();
            }
        }

        mCurrentFragment = fragment;
        mRootView.setFitsSystemWindows(true);
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
}

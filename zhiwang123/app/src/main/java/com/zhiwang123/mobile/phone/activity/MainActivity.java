
package com.zhiwang123.mobile.phone.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.VersionModel;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.CoursePageFragment;
import com.zhiwang123.mobile.phone.fragment.FirstPageFragment;
import com.zhiwang123.mobile.phone.fragment.MeFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    public static final String ACTION_REFRESH_UI = "com.zhiwang123.mobile.refresh_ui";
    public static final String ACTION_HIDDEN_SPLASH = "com.zhiwang123.mobile.hidden_splash";

    private static final int CONTENT_VIEW_ID = R.id.main_body;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonMainPage;
    private RadioButton mRadioButtonCourse;
    private RadioButton mRadioButtonMe;

    private ImageView mMainPageImage;
    private ImageView mCourseImage;
    private ImageView mMeImage;

    private TextView mMainText;
    private TextView mCourseText;
    private TextView mMeText;

    private BaseFragment mCurrentFragment;

    private Menu mMenu;

    private View mRootView;
    private View mMainBody;

    private float movement = 50;

    private long exitTime;

    private FirstPageFragment mFirstPageFragment;
    private CoursePageFragment mCoursePageFragment;
    private MeFragment mMePageFragment;

    private ImageView mSplashImgv;

    private ViewPager mGuidePager;

    private SharedPreferences sp;

    private String desc = "";
    private String versionNumber = "";
    private String hash = "";
    private String remotePath = "";
    private long fileSize = 0l;
    private long downloadId;

    private int[] imgs = new int[] {R.drawable.guide0, R.drawable.guide2, R.drawable.guide1, R.drawable.guide3};
    private View[] views = new View[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.zw_trans);
        }

//        mActionBar.setElevation(0);
//        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.setBackgroundDrawable(null);
//        mActionBar.setCustomView(R.layout.actionbar_layout);

//        mActionBar.show();
//        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.setCustomView(R.layout.actionbar_main_actv_layout);

        sp = getSharedPreferences("splash_guid", 0);

        mFirstPageFragment = FirstPageFragment.getInstance();
        mCoursePageFragment = CoursePageFragment.getInstance();
        mMePageFragment = MeFragment.getInstance();

//        PushAgent.getInstance(context).onAppStart();

        PushAgent mPushAgent = PushAgent.getInstance(this);

        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        Log.d("device_token", registrationId);
                    }
                });
            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {

                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();

            }
        };

        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        AsyncDBHelper.getUserInfoDB(this, uiHandler);

        initLayout();


        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_REFRESH_UI);
        registerReceiver(refreshLoginStatusReceiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(ACTION_HIDDEN_SPLASH);
        registerReceiver(hiddenSplashImgReceiver, filter2);

        versionCheck();

    }

    @Override
    protected void initLayout() {

        mRootView = findViewById(R.id.main_root);
        mMainBody = findViewById(R.id.main_body);

        mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        mRadioButtonMainPage = (RadioButton) findViewById(R.id.btn1);
        mRadioButtonCourse = (RadioButton) findViewById(R.id.btn2);
        mRadioButtonMe = (RadioButton) findViewById(R.id.btn3);

        mRadioButtonMainPage.setOnTouchListener(mOnTouchListener);
        mRadioButtonCourse.setOnTouchListener(mOnTouchListener);
        mRadioButtonMe.setOnTouchListener(mOnTouchListener);

        mMainPageImage = (ImageView) findViewById(R.id.btn1_image);
        mCourseImage = (ImageView) findViewById(R.id.btn2_image);
        mMeImage = (ImageView) findViewById(R.id.btn3_image);

        mMainText = (TextView) findViewById(R.id.btn1_text);
        mCourseText = (TextView) findViewById(R.id.btn2_text);
        mMeText = (TextView) findViewById(R.id.btn3_text);

        mSplashImgv = (ImageView) findViewById(R.id.splash_imgv);
        mGuidePager = (ViewPager) findViewById(R.id.guide_pager);

        mGuidePager.setAdapter(pagerAdapter);

//        mMainText.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD_BLOD));
//        mCourseText.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD_BLOD));
//        mMeText.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD_BLOD));

        mRadioGroup.setOnCheckedChangeListener(mRadioGroupOnCheckChange);

//        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(vgl);

        navigatorToCheck(R.id.btn3, false);
        navigatorToCheck(R.id.btn2, false);
        navigatorToCheck(R.id.btn1, false);

        if(sp.getBoolean("is_first_guide", true)) {
            mGuidePager.setVisibility(View.VISIBLE);
            mGuidePager.setOffscreenPageLimit(4);
        }

        for(int i = 0; i < 4; i ++) {

            views[i] = LayoutInflater.from(MainActivity.this).inflate(R.layout.guide_item, null);

        }

        super.initLayout();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                setSelector(v.getId());

//				Log.v(TAG, "--- down ---");
//				if(v.getId() == R.id.btn2) {
//					if(!ConfigInfo.isUserLogined) {
//						startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_FOR_LOGIN);
//						return true;
//					}
//				} else {
//					setSelector(v.getId());
//				}

            }
            return false;
        }

    };

    android.widget.RadioGroup.OnCheckedChangeListener mRadioGroupOnCheckChange = new android.widget.RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//			Log.v(TAG, "" + checkedId);
            navigatorToCheck(checkedId, true);
//			MeFragment.getInstance().notifCountUpdate();
        }
    };



    private void navigatorToCheck(int checkedId, boolean anim) {

        BaseFragment fragment = setSelector(checkedId);

        if(fragment != null) {
            navigatorToPage(fragment, anim);
        }
    }



    private BaseFragment setSelector(int viewId) {

        BaseFragment fragment = null;

        switch(viewId) {
            case R.id.btn1:
                fragment = mFirstPageFragment;
                mMainPageImage.setImageResource(R.drawable.main_icon);
                mMainText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mMeImage.setImageResource(R.drawable.mine_icon_dis);
                mMeText.setTextColor(getResources().getColor(R.color.zw_gray_main_bottom_bar_text));
                mCourseImage.setImageResource(R.drawable.course_icon_dis);
                mCourseText.setTextColor(getResources().getColor(R.color.zw_gray_main_bottom_bar_text));
                break;
            case R.id.btn2:

                fragment = mCoursePageFragment;
                mMainPageImage.setImageResource(R.drawable.main_icon_dis);
                mMainText.setTextColor(getResources().getColor(R.color.zw_gray_main_bottom_bar_text));
                mCourseImage.setImageResource(R.drawable.course_icon);
                mCourseText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mMeImage.setImageResource(R.drawable.mine_icon_dis);
                mMeText.setTextColor(getResources().getColor(R.color.zw_gray_main_bottom_bar_text));
                break;
            case R.id.btn3:

                fragment = mMePageFragment;
                mMainPageImage.setImageResource(R.drawable.main_icon_dis);
                mMainText.setTextColor(getResources().getColor(R.color.zw_gray_main_bottom_bar_text));
                mCourseImage.setImageResource(R.drawable.course_icon_dis);
                mCourseText.setTextColor(getResources().getColor(R.color.zw_gray_main_bottom_bar_text));
                mMeImage.setImageResource(R.drawable.mine_icon);
                mMeText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
//            case R.id.btn4:
//                isShowMenuItem(false);
//                //getActionBar().setTitle(R.string.main_actionbar_title_setting);
//                mCustomActionTitle.setText(R.string.main_actionbar_title_publish);
//                mCustomActionBack.setVisibility(View.GONE);
//                mCustomActionR1.setVisibility(View.VISIBLE);
//                mCustomActionR2_.setVisibility(View.VISIBLE);
//                mCustomActionR3.setVisibility(View.GONE);
//                mCustomActionR4.setVisibility(View.VISIBLE);
//                //fragment = SettingsFragment.getInstance();
//                fragment = PublishFragment.getInstance();
//                mPublishImage.setImageResource(R.drawable.first_publish);
//                mPublishText.setTextColor(0xff30b7a1);
//                mMainPageImage.setImageResource(R.drawable.first_page_disable);
//                mMainText.setTextColor(0xff999999);
//                mMeImage.setImageResource(R.drawable.first_category_dis);
//                mMeText.setTextColor(0xff999999);
//                mSettingImage.setImageResource(R.drawable.me_disable);
//                mSettingText.setTextColor(0xff999999);
//                break;
        }

        return fragment;
    }

    private void navigatorToPage(BaseFragment fragment, boolean userAnim) {

        if(mCurrentFragment == fragment) return;

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if(userAnim) {

            if(mCurrentFragment instanceof FirstPageFragment) {
                transaction.setCustomAnimations(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
            } else if(mCurrentFragment instanceof MeFragment) {
                transaction.setCustomAnimations(R.anim.translate_in_zw, R.anim.translate_out_zw);
            } else if(mCurrentFragment instanceof CoursePageFragment ) {
                if(fragment instanceof FirstPageFragment) {
                    transaction.setCustomAnimations(R.anim.translate_in_zw, R.anim.translate_out_zw);
                } else if(fragment instanceof MeFragment) {
                    transaction.setCustomAnimations(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
                }
            }

        }

        if(fragment.isAdded()) {
            transaction.hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
        } else {
            if(mCurrentFragment != null) {
                transaction.hide(mCurrentFragment).add(CONTENT_VIEW_ID, fragment).commitAllowingStateLoss();
            } else {
                transaction.add(CONTENT_VIEW_ID, fragment).commitAllowingStateLoss();
            }
        }

        mCurrentFragment = fragment;
    }

    public void navigatorToPage(BaseFragment fragment) {

        navigatorToPage(fragment, true);

    }


    @Override
    public void onBackPressed() {

        /*if(mCurrentFragment instanceof CategoryFragment) {

            CategoryFragment fragment = (CategoryFragment) mCurrentFragment;
            Log.v(TAG, "backStack count: " + getFragmentManager().getBackStackEntryCount() + "");
            if(getFragmentManager().getBackStackEntryCount() == 1 && fragment.getCategroyBodyVisitable()) {
                getFragmentManager().popBackStack();
                fragment.hideCategroyBody();
                mCustomActionBack.setVisibility(View.GONE);
                mCustomActionTitle.setText(R.string.app_name);
                mCustomActionR1.setVisibility(View.VISIBLE);
                mCustomActionR2_.setVisibility(View.VISIBLE);
                mCustomActionR4.setVisibility(View.VISIBLE);
                return;
            } else if(getFragmentManager().getBackStackEntryCount() > 1 && fragment.getCategroyBodyVisitable()) {
                getFragmentManager().popBackStack();
                int index = getFragmentManager().getBackStackEntryCount();
                BackStackEntry entry = null;

                try {
                    entry = getFragmentManager().getBackStackEntryAt(index - 2);
                    String categoryPageTitle = entry.getName();
                    mCustomActionTitle.setText(categoryPageTitle);
                    fragment.setDeepTitle(categoryPageTitle);
                } catch(IndexOutOfBoundsException e) {
                    Log.e(TAG, "---------------");
                    e.printStackTrace();
                    Log.e(TAG, "---------------");
                }

                return;
            }
        } else if(mCurrentFragment instanceof SettingsFragment) {
            SettingsFragment fragment = (SettingsFragment) mCurrentFragment;
            if(getFragmentManager().getBackStackEntryCount() == 1) {
                mCustomActionTitle.setText(R.string.main_actionbar_title_setting);
                fragment.enableGetFeedbackBtn();
            }
        } */
        exitApp();
    }

/*    ViewTreeObserver.OnGlobalLayoutListener vgl = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();

            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

            int statusBarHeight = frame.top;
            int actionBarHeight = getActionBar().getHeight();

//			llp.setMargins(0, getActivity().getActionBar().getHeight() + (screenHeight - rect.height()), 0, 0);
//			mListView.setLayoutParams(llp);
            mRootView.setPadding(0, getActionBar().getHeight() + (screenHeight - rect.height()), 0, 0);
            mHandler.sendEmptyMessage(0);
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(vgl);
        }
    };*/


    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.exit_notice, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
//        MeFragment.getInstance().notifFavoriteCountUpdate();
//        MeFragment.getInstance().notifUserInfoCheck();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(refreshLoginStatusReceiver);
        unregisterReceiver(hiddenSplashImgReceiver);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "reslult reqCode:" + requestCode + "-resCode:" + resultCode);

        switch(requestCode) {
            case StaticConfigs.REQUEST_CODE_LOGIN:
                if(resultCode == StaticConfigs.RESULT_CODE_LOGIN_OK) {
                    LoginResult lr = (LoginResult) data.getSerializableExtra(StaticConfigs.RESULT_CODE_LOGIN_KEY);
                    MeFragment.getInstance().setLoginInfo(lr);
                    MeFragment.getInstance().resetUI();
                }
                break;
        }

    }

    AsyncDBHelper.UIHandler uiHandler = new AsyncDBHelper.UIHandler<LoginResult>() {

        @Override
        public void processMessage(int what, LoginResult loginResult) {

            StaticConfigs.mLoginResult = loginResult;
            MeFragment.getInstance().setLoginInfo(loginResult);
            MeFragment.getInstance().resetUI();

        }
    };

    private BroadcastReceiver refreshLoginStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            LoginResult lr = (LoginResult) intent.getSerializableExtra(StaticConfigs.RESULT_CODE_LOGIN_KEY);
            MeFragment.getInstance().setLoginInfo(lr);
            MeFragment.getInstance().resetUI();

        }
    };

    private BroadcastReceiver hiddenSplashImgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startPropertyAnim(mSplashImgv);

                }
            }, (int)(Math.random() * 5) * 1000);


        }
    };

    private void startPropertyAnim(final View v) {

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);

        anim.setDuration(1000);// 动画持续时间

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                if(value == 0) {
                    v.setVisibility(View.GONE);
                }
            }
        });

        anim.start();
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View v = views[position];

            ImageView im = ((ImageView) v.findViewById(R.id.guide_item_img));

            View btn = v.findViewById(R.id.to_use_btn);

            im.setImageResource(imgs[position]);

            container.addView(v);

            if(position == 3) {

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sp.edit().putBoolean("is_first_guide", false).commit();
                        startPropertyAnim(mGuidePager);

                    }
                });

            } else {

                btn.setOnClickListener(null);

            }

            return v;
        }
    };

    private void versionCheck() {


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

//                                Intent intent = new Intent(MainActivity.this, LHUpdateService.class);
//                                intent.putExtra("update_path", vm.filePath);
//                                startService(intent);

//                                Intent intent = new Intent(MainActivity.this, UpdateDialogActivity.class);
//                                intent.putExtra(UpdateDialogActivity.MESSAEG, desc);
//                                intent.putExtra(UpdateDialogActivity.VERSION_STR, versionNumber);
//                                intent.putExtra(UpdateDialogActivity.UPDATE_PATH, remotePath);
//                                intent.putExtra(UpdateDialogActivity.REMOTE_LENGTH, fileSize);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.translate_in_zw_v, 0);


                                AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.drawable.update_alert)
                                        .setTitle(getString(R.string.update_alert_dialog_str, versionNumber))
                                        .setMessage(TextUtils.isEmpty(desc) ? "fix some bugs" : desc)
                                        .setPositiveButton(R.string.update_confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if(TextUtils.isEmpty(remotePath)) {

                                                    Toast.makeText(MainActivity.this, "download path error!", Toast.LENGTH_SHORT).show();

                                                    return;
                                                }


                                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                                Uri uri = Uri.parse(remotePath);
                                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                                request.setDestinationInExternalFilesDir(MainActivity.this,"zhiwang_download","release_"+ versionNumber +".apk");
                                                downloadId = downloadManager.enqueue(request);

                                                SharedPreferences sp = MainActivity.this.getSharedPreferences("download_ids", MODE_PRIVATE);
                                                sp.edit().putLong("load_apk_id", downloadId).commit();
                                                sp.edit().putString("load_apk_version", versionNumber).commit();

                                                Toast.makeText(MainActivity.this, "已开始下载", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .setNegativeButton(R.string.cart_to_cancel_edt, null)
                                        .show();


                            } else {

                                //Toast.makeText(MainActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();

                            }


                        } catch(JSONException e) {

                            e.printStackTrace();

                        } finally {


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

    private void startDownload2() {



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

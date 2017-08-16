package com.zhiwang123.mobile.phone.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bokecc.sdk.mobile.download.DownloadListener;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.drm.DRMServer;
import com.bokecc.sdk.mobile.exception.DreamwinException;
import com.bokecc.sdk.mobile.exception.ErrorCode;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.cc.CCDataSet;
import com.zhiwang123.mobile.cc.ConfigUtil;
import com.zhiwang123.mobile.cc.ParamsUtil;
import com.zhiwang123.mobile.cc.PopMenu;
import com.zhiwang123.mobile.cc.Subtitle;
import com.zhiwang123.mobile.cc.VerticalSeekBar;
import com.zhiwang123.mobile.phone.adapter.VideoSubFragmentAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseCenterSub;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.ChapterFragment;
import com.zhiwang123.mobile.phone.fragment.NoteFragment;
import com.zhiwang123.mobile.phone.service.ZWDownLoaderService;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 视频播放界面
 *
 * @author CC视频
 */
public class VideoPlayActivity extends BaseActivity implements
        DWMediaPlayer.OnBufferingUpdateListener,
        DWMediaPlayer.OnInfoListener,
        DWMediaPlayer.OnPreparedListener, DWMediaPlayer.OnErrorListener,
        MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, SensorEventListener {

    private static final String TAG = "VideoPlayActivity";

    public static final String EXTRA_KEY_VID = "videoId";
    public static final String EXTRA_KEY_LOCPLY = "isLocalPlay";
    public static final String EXTRA_KEY_VNAME = "videoName";
    public static final String EXTRA_KEY_VIMG = "videoImg";
    public static final String EXTRA_KEY_BUYCOURSEID = "videoBuyCourseId";

    public static final String EXTRA_KEY_TEACHERPLANID = "teacher_plan_id";
    public static final String EXTRA_KEY_COURSEID = "course_id";

    public static final String EXTRA_KEY_ACTIVE_TIMES = "active_times";

    private boolean networkConnected = true;
    private DWMediaPlayer player;
    private Subtitle subtitle;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ProgressBar bufferProgressBar;
    private SeekBar skbProgress;
    private ImageView playOp, backPlayList;
    private TextView videoIdText, playDuration, videoDuration;
    private Button screenSizeBtn, definitionBtn, subtitleBtn;
    private PopMenu screenSizeMenu, definitionMenu, subtitleMenu;
    private LinearLayout playerTopLayout, volumeLayout;
    private LinearLayout playerBottomLayout;
    private AudioManager audioManager;
    private VerticalSeekBar volumeSeekBar;
    private int currentVolume;
    private int maxVolume;
    private TextView subtitleText;

    private View mVshareBtn;
    private View mVdownloadBtn;
    private View mVcollectionBtn;

    private boolean isLocalPlay;
    private boolean isPrepared;
    private Map<String, Integer> definitionMap;

    private Context mContext;
    private Handler playerHandler;
    private Timer timer = new Timer();
    private TimerTask timerTask, networkInfoTimerTask;

    private int currentScreenSizeFlag = 1;
    private int currrentSubtitleSwitchFlag = 0;
    private int currentDefinition = 0;

    private boolean firstInitDefinition = true;

    private String path;

    private Boolean isPlaying;
    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze = false;
    private boolean isSurfaceDestroy = false;

    int currentPosition;
    private Dialog dialog;

    private String[] definitionArray;
    private final String[] screenSizeArray = new String[]{"满屏", "100%", "75%", "50%"};
    private final String[] subtitleSwitchArray = new String[]{"开启", "关闭"};
    private final String subtitleExampleURL = "http://dev.bokecc.com/static/font/example.utf8.srt";

    private GestureDetector detector;
    private float scrollTotalDistance, scrollCurrentPosition;
    private int lastPlayPosition, currentPlayPosition;

    public String videoId;
    public String videoName;
//    public long activeTime;

    private String videoImgCover;
    private String videoBuyCourseId;

    private String organCourseId;
    private String organTeacherPlanId;

    private RelativeLayout rlBelow, rlPlay;
    private WindowManager wm;
    private ImageView ivFullscreen;

    private VideoSubFragmentAdapter mPagerAdapter;
    private ViewPager mSubPager;
    private TextView mTitleTx;

    private View mChapterBtn;
    private View mNoteBtn;
    private View mAdviceBtn;
    private View mShareCloseBtn;

    private ImageView mChpImg;
    private TextView mChpTxt;
    private ImageView mNoteImg;
    private TextView mNoteTxt;
    private ImageView mAskImg;
    private TextView mAskTxt;
    private ImageView mImageIcon;

    private ArrayList<Course> mSubCourseList;

    private NoteFragment mNoteFragment;
    private ChapterFragment mChapterFragment;

    private View mShareView;

    private  DRMServer drmServer;

    public int currentPlayIndex = 0;


    // 隐藏界面的线程
    private Runnable hidePlayRunnable = new Runnable() {

        @Override
        public void run() {
            setLayoutVisibility(View.GONE, false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.cc_video_layout);

        mContext = this;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        detector = new GestureDetector(this, new MyGesture());

        Bundle bundle = getIntent().getExtras();

//        long size = PhoneUtil.getAvailSpace(Environment.getDataDirectory().getAbsolutePath());

//        Log.i(TAG, "内部存储空间：" + (size / 1024) / 1024);

        if (bundle != null) {

            videoId = bundle.getString(EXTRA_KEY_VID);
            videoName = bundle.getString(EXTRA_KEY_VNAME);
            videoImgCover = bundle.getString(EXTRA_KEY_VIMG);
            isLocalPlay = bundle.getBoolean(EXTRA_KEY_LOCPLY, false);
            videoBuyCourseId = bundle.getString(EXTRA_KEY_BUYCOURSEID);

            organCourseId = bundle.getString(EXTRA_KEY_COURSEID);
            organTeacherPlanId = bundle.getString(EXTRA_KEY_TEACHERPLANID);

        }

        initView();

        initPlayHander();

        initScreenSizeMenu();


        if(!TextUtils.isEmpty(videoBuyCourseId) || isLocalPlay) {

            if (!isLocalPlay) {

                initNetworkTimerTask();

                initPlayInfo("", "");

                mTitleTx.setText(videoName);

                loadSubCourse(videoBuyCourseId);

            } else {

                mChapterBtn.setVisibility(View.INVISIBLE);
                initPlayInfo(videoId, videoName);

            }

        } else {

            initNetworkTimerTask();

            initPlayInfo("", "");

            mTitleTx.setText(videoName);

            loadOrganSubCourse(organTeacherPlanId, organCourseId);

        }

    }

    private void initNetworkTimerTask() {

        networkInfoTimerTask = new TimerTask() {
            @Override
            public void run() {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    if (!networkConnected) {
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {

                                if (!isPrepared) {
                                    return;
                                }

                                playerHandler.sendEmptyMessage(0);
                            }
                        };
                        timer.schedule(timerTask, 0, 1000);
                    }
                    networkConnected = true;
                } else {
                    networkConnected = false;
                    timerTask.cancel();
                }

            }
        };

        timer.schedule(networkInfoTimerTask, 0, 600);
    }

    private void initView() {

        rlBelow = (RelativeLayout) findViewById(R.id.rl_below_info);
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);

        rlPlay.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPrepared) {
                    return true;
                }

                resetHideDelayed();

                // 事件监听交给手势类来处理
                detector.onTouchEvent(event);
                return true;
            }
        });

        rlPlay.setClickable(true);
        rlPlay.setLongClickable(true);
        rlPlay.setFocusable(true);

        surfaceView = (SurfaceView) findViewById(R.id.playerSurfaceView);
        playOp = (ImageView) findViewById(R.id.btnPlay);
        backPlayList = (ImageView) findViewById(R.id.backPlayList);
        bufferProgressBar = (ProgressBar) findViewById(R.id.bufferProgressBar);

        mImageIcon = (ImageView) findViewById(R.id.video_icon_imgv);

        videoIdText = (TextView) findViewById(R.id.videoIdText);
        playDuration = (TextView) findViewById(R.id.playDuration);
        videoDuration = (TextView) findViewById(R.id.videoDuration);
        playDuration.setText(ParamsUtil.millsecondsToStr(0));
        videoDuration.setText(ParamsUtil.millsecondsToStr(0));

        screenSizeBtn = (Button) findViewById(R.id.playScreenSizeBtn);
        definitionBtn = (Button) findViewById(R.id.definitionBtn);
        subtitleBtn = (Button) findViewById(R.id.subtitleBtn);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar = (VerticalSeekBar) findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setThumbOffset(2);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        skbProgress = (SeekBar) findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);

        playerTopLayout = (LinearLayout) findViewById(R.id.playerTopLayout);
        volumeLayout = (LinearLayout) findViewById(R.id.volumeLayout);
        playerBottomLayout = (LinearLayout) findViewById(R.id.playerBottomLayout);

        ivFullscreen = (ImageView) findViewById(R.id.iv_fullscreen);

        mVshareBtn = findViewById(R.id.video_to_share);
        mVdownloadBtn = findViewById(R.id.video_to_download);
        mVcollectionBtn = findViewById(R.id.video_to_collection);

        mChapterBtn = findViewById(R.id.video_chp_btn);
        mChpImg = (ImageView) findViewById(R.id.video_chp_img);
        mChpTxt = (TextView) findViewById(R.id.video_chp_txt);

        mShareView = findViewById(R.id.share_view_root);
        mShareCloseBtn = findViewById(R.id.share_close_btn);

        mNoteBtn = findViewById(R.id.video_note_btn);
        mNoteImg = (ImageView) findViewById(R.id.video_note_img);
        mNoteTxt = (TextView) findViewById(R.id.video_note_txt);
//        mNoteTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        mAdviceBtn = findViewById(R.id.video_ask_btn);
        mAskImg = (ImageView) findViewById(R.id.video_ask_img);
        mAskTxt = (TextView) findViewById(R.id.video_ask_txt);
//        mAskTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        ivFullscreen.setOnClickListener(onClickListener);
        playOp.setOnClickListener(onClickListener);
        backPlayList.setOnClickListener(onClickListener);
        screenSizeBtn.setOnClickListener(onClickListener);
        definitionBtn.setOnClickListener(onClickListener);
        subtitleBtn.setOnClickListener(onClickListener);
        mChapterBtn.setOnClickListener(onClickListener);
        mNoteBtn.setOnClickListener(onClickListener);
        mAdviceBtn.setOnClickListener(onClickListener);
        mShareCloseBtn.setOnClickListener(onClickListener);

        mVdownloadBtn.setOnClickListener(onClickListener);
        mVshareBtn.setOnClickListener(onClickListener);

        mImageIcon.setOnClickListener(onClickListener);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //2.3及以下使用，不然出现只有声音没有图像的问题
        surfaceHolder.addCallback(this);


        mTitleTx = (TextView) findViewById(R.id.video_title_txt);
        subtitleText = (TextView) findViewById(R.id.subtitleText);

//        mTitleTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//        subtitleText.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//        videoIdText.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

        mSubPager = (ViewPager) findViewById(R.id.video_page_sub_pager);
        mPagerAdapter = new VideoSubFragmentAdapter(getSupportFragmentManager(), initSubPagers());
        mSubPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mSubPager.setCurrentItem(0);
        mSubPager.addOnPageChangeListener(onPageChangeListener);

    }

    private ArrayList<BaseFragment> initSubPagers() {

        ArrayList<BaseFragment> pagers = new ArrayList<>();

        mNoteFragment = NoteFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString(NoteFragment.KEY_COURSE_ID_KEY, videoId);
        mNoteFragment.setArguments(bundle);


        if(!isLocalPlay) {
            mChapterFragment = ChapterFragment.getInstance();
            pagers.add(mChapterFragment);
        }

        pagers.add(mNoteFragment);

        return pagers;

    }

    private void loadSubCourse(final String buyCourseId) {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlChildrenCourse(buyCourseId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseCenterSub ccs = new CourseParser<CourseCenterSub>(new CourseCenterSub()).parse(response);
                        mSubCourseList = ccs.dataobject;

                        if (mSubCourseList == null || mSubCourseList.size() == 0) {
                            Toast.makeText(mContext, R.string.video_no_video, Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        if (mChapterFragment != null)
                            mChapterFragment.initPageList(VideoPlayActivity.this, mSubCourseList, false, videoImgCover);

//                        videoId = mSubCourseList.get(0).ccKey;
//                        videoName = mSubCourseList.get(0).courseName;
//                        initPlayInfo(videoId, videoName);


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage() + "");
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);
    }

    private void loadOrganSubCourse(String organTeacherPlanId, String organCourseId) {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlOrganChildcourse(organTeacherPlanId, organCourseId), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseCenterSub ccs = new CourseParser<CourseCenterSub>(new CourseCenterSub()).parse(response);
                        mSubCourseList = ccs.dataobject;

                        if (mSubCourseList == null || mSubCourseList.size() == 0) {
                            Toast.makeText(mContext, R.string.video_no_video, Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }

                        if (mChapterFragment != null)
                            mChapterFragment.initPageList(VideoPlayActivity.this, mSubCourseList, true, videoImgCover);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage() + "");
                    }
                });

        mVolleyRequestQueue.add(jsonRequest);

    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:

                    mChpImg.setImageResource(R.drawable.v_ch);
                    mChpTxt.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                    mNoteImg.setImageResource(R.drawable.v_note_d);
                    mNoteTxt.setTextColor(mContext.getResources().getColor(R.color.zw_gray_main_bottom_bar_text));

                    break;
                case 1:

                    mChpImg.setImageResource(R.drawable.v_ch_d);
                    mChpTxt.setTextColor(mContext.getResources().getColor(R.color.zw_gray_main_bottom_bar_text));

                    mNoteImg.setImageResource(R.drawable.v_note);
                    mNoteTxt.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

                    break;
                case 2:

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    private void initPlayHander() {

        playerHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (player == null) {
                    return;
                }

                // 刷新字幕
                subtitleText.setText(subtitle.getSubtitleByTime(player
                        .getCurrentPosition()));

                // 更新播放进度
                currentPlayPosition = player.getCurrentPosition();
                int duration = player.getDuration();

                if (duration > 0) {
                    long pos = skbProgress.getMax() * currentPlayPosition / duration;
                    playDuration.setText(ParamsUtil.millsecondsToStr(player
                            .getCurrentPosition()));
                    skbProgress.setProgress((int) pos);
                }
            }

            ;
        };

        // 通过定时器和Handler来更新进度
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!isPrepared) {
                    return;
                }

                playerHandler.sendEmptyMessage(0);
            }
        };

    }

    public void initPlayInfo(String vid, String vname) {

        if(!TextUtils.isEmpty(vid)) {
            bufferProgressBar.setVisibility(View.VISIBLE);
            mImageIcon.setVisibility(View.GONE);
        }

        if (timerTask != null) {

            timer.cancel();

            timer = new Timer();

            timerTask = new TimerTask() {
                @Override
                public void run() {

                    if (!isPrepared) {
                        return;
                    }

                    playerHandler.sendEmptyMessage(0);
                }
            };

            timer.schedule(timerTask, 0, 1000);
        }

        isPrepared = false;

        if (player == null) {

            player = new DWMediaPlayer();
            player.setOnErrorListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnInfoListener(this);

        }

        if(drmServer == null) drmServer = new DRMServer();

        try {

            drmServer.start();

        } catch(IOException e) {


        }

        int drmServerPort = drmServer.getPort();

        player.setDRMServerPort(drmServerPort);

        if(TextUtils.isEmpty(vid) || TextUtils.isEmpty(vname)) {

            return;

        }

        videoId = vid;
        videoName = vname;

        if (player.isPlaying()) player.stop();

        player.reset();

        videoIdText.setText(vname);
        mTitleTx.setText(vname);

        if (TextUtils.isEmpty(vid)) return;

        try {

            if (!isLocalPlay) {// 播放线上视频
                lastPlayPosition = CCDataSet.getVideoPosition(vid);
                player.setVideoPlayInfo(vid, ConfigUtil.USERID, ConfigUtil.API_KEY, this);
                // 设置默认清晰度
                player.setDefaultDefinition(DWMediaPlayer.NORMAL_DEFINITION);

            } else {// 播放本地已下载视频

                SharedPreferences sp = getSharedPreferences(EXTRA_KEY_ACTIVE_TIMES, 0);
                long activeTime = sp.getLong(vid, 0l);

                long currentTime = new Date().getTime();

//                if(activeTime == 0l) {
//                    Toast.makeText(this, "缓存视频未检测到激活信息", Toast.LENGTH_SHORT).show();
//                    bufferProgressBar.setVisibility(View.GONE);
//                    mImageIcon.setVisibility(View.VISIBLE);
//                    return;
//                } else if((currentTime - activeTime) > 48 * 3600 * 1000) {
//                    Toast.makeText(this, "视频已经过期", Toast.LENGTH_SHORT).show();
//                    bufferProgressBar.setVisibility(View.GONE);
//                    mImageIcon.setVisibility(View.VISIBLE);
//                    return;
//                }

                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    path = Environment.getExternalStorageDirectory()
                            + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/")
                            .concat(vname).concat(".pcm");

                    Log.i(TAG, "local path : " + path);

                    if (!new File(path).exists()) {
                        return;
                    }

                    player.setDRMVideoPath(path, mContext);
                    //player.setDataSource(path);
                }
            }

            player.prepareAsync();

        } catch (IllegalArgumentException e) {
            Log.e("player error", e.getMessage() + "");
        } catch (SecurityException e) {
            Log.e("player error", e.getMessage() + "");
        } catch (IllegalStateException e) {
            Log.e("player error", e.getMessage() + "");
        } catch (IOException e) {
            Log.e("player error", e.getMessage() + "");
        }

        // 设置视频字幕
        subtitle = new Subtitle(new Subtitle.OnSubtitleInitedListener() {

            @Override
            public void onInited(Subtitle subtitle) {
                // 初始化字幕控制菜单
                initSubtitleSwitchpMenu(subtitle);
            }
        });
        subtitle.initSubtitleResource(subtitleExampleURL, mVolleyRequestQueue);

    }

    private void initScreenSizeMenu() {
        screenSizeMenu = new PopMenu(this, R.drawable.popdown,
                currentScreenSizeFlag);
        screenSizeMenu.addItems(screenSizeArray);
        screenSizeMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                // 提示已选择的屏幕尺寸
                Toast.makeText(getApplicationContext(),
                        screenSizeArray[position], Toast.LENGTH_SHORT)
                        .show();

                LayoutParams params = getScreenSizeParams(position);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                surfaceView.setLayoutParams(params);
            }
        });
    }

    private LayoutParams getScreenSizeParams(int position) {
        currentScreenSizeFlag = position;
        int width = 600;
        int height = 400;
        if (isPortrait()) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight() * 2 / 5; //TODO 根据当前布局更改
        } else {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }


        String screenSizeStr = screenSizeArray[position];
        if (screenSizeStr.indexOf("%") > 0) {// 按比例缩放
            int vWidth = player.getVideoWidth();
            if (vWidth == 0) {
                vWidth = 600;
            }

            int vHeight = player.getVideoHeight();
            if (vHeight == 0) {
                vHeight = 400;
            }

            if (vWidth > width || vHeight > height) {
                float wRatio = (float) vWidth / (float) width;
                float hRatio = (float) vHeight / (float) height;
                float ratio = Math.max(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth / ratio);
                height = (int) Math.ceil((float) vHeight / ratio);
            } else {
                float wRatio = (float) width / (float) vWidth;
                float hRatio = (float) height / (float) vHeight;
                float ratio = Math.min(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth * ratio);
                height = (int) Math.ceil((float) vHeight * ratio);
            }


            int screenSize = ParamsUtil.getInt(screenSizeStr.substring(0, screenSizeStr.indexOf("%")));
            width = (width * screenSize) / 100;
            height = (height * screenSize) / 100;
        }

        LayoutParams params = new LayoutParams(width, height);
        return params;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setDisplay(holder);
            player.setScreenOnWhilePlaying(true);
            if (isSurfaceDestroy) {
                if (isLocalPlay) {
                    player.setDataSource(path);
                }
                player.prepareAsync();
            }
        } catch (Exception e) {
            Log.e("videoPlayer", "error", e);
        }
        Log.i("videoPlayer", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        holder.setFixedSize(width, height);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        if (isPrepared) {
            currentPosition = player.getCurrentPosition();
        }

        isPrepared = false;
        isSurfaceDestroy = true;

        player.stop();
        player.reset();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (!isFreeze) {
            if (isPlaying == null || isPlaying.booleanValue()) {
                player.start();
                playOp.setImageResource(R.drawable.btn_pause);
            }
        }

        if (currentPosition > 0) {
            player.seekTo(currentPosition);
        } else {
            if (lastPlayPosition > 0) {
                player.seekTo(lastPlayPosition);
            }
        }

        definitionMap = player.getDefinitions();
        if (!isLocalPlay) {
            initDefinitionPopMenu();
        }

        bufferProgressBar.setVisibility(View.GONE);
        setSurfaceViewLayout();
        videoDuration.setText(ParamsUtil.millsecondsToStr(player.getDuration()));
    }

    // 设置surfaceview的布局
    private void setSurfaceViewLayout() {
        LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        surfaceView.setLayoutParams(params);
        surfaceView.setBackgroundColor(getResources().getColor(R.color.zw_trans));
    }

    private void initDefinitionPopMenu() {

        if (definitionMap.size() > 1 && firstInitDefinition) {
            currentDefinition = 1;
            firstInitDefinition = false;
        }

        definitionMenu = new PopMenu(this, R.drawable.popup, currentDefinition);
        // 设置清晰度列表
        definitionArray = new String[]{};
        definitionArray = definitionMap.keySet().toArray(definitionArray);

        definitionMenu.addItems(definitionArray);
        definitionMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                try {

                    currentDefinition = position;
                    int definitionCode = definitionMap
                            .get(definitionArray[position]);

                    if (isPrepared) {
                        currentPosition = player.getCurrentPosition();
                        if (player.isPlaying()) {
                            isPlaying = true;
                        } else {
                            isPlaying = false;
                        }
                    }

                    setLayoutVisibility(View.GONE, false);
                    bufferProgressBar.setVisibility(View.VISIBLE);

                    player.reset();

                    player.setDefinition(getApplicationContext(),
                            definitionCode);

                } catch (IOException e) {
                    Log.e("player error", e.getMessage() + "");
                }

            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        skbProgress.setSecondaryProgress(percent);
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            resetHideDelayed();

            switch (v.getId()) {
                case R.id.btnPlay:
                    if (!isPrepared) {
                        return;
                    }
                    changePlayStatus();
                    break;

                case R.id.backPlayList:
                    if (isPortrait()) {
                        finish();
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case R.id.iv_fullscreen:
                    if (isPortrait()) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case R.id.playScreenSizeBtn:
                    screenSizeMenu.showAsDropDown(v);
                    break;
                case R.id.subtitleBtn:
                    subtitleMenu.showAsDropDown(v);
                    break;
                case R.id.definitionBtn:
                    definitionMenu.showAsDropDown(v);
                    break;
                case R.id.video_chp_btn:
                    mSubPager.setCurrentItem(0);
                    break;
                case R.id.video_note_btn:
                    mSubPager.setCurrentItem(1);
                    break;
                case R.id.video_to_download:

                    startLoadingVideo();

                    break;
                case R.id.video_to_share:

                    if (mShareView.getVisibility() == View.GONE)
                        mShareView.setVisibility(View.VISIBLE);
                    else mShareView.setVisibility(View.GONE);

                    break;
                case R.id.share_close_btn:

                    mShareView.setVisibility(View.GONE);

                    break;

                case R.id.video_ask_btn:

                    Intent adviceIntent = new Intent(VideoPlayActivity.this, TopicWebPageActivity.class);
                    adviceIntent.putExtra(TopicWebPageActivity.KEY_URL, StaticConfigs.URL_ADVICE);
                    adviceIntent.putExtra(TopicWebPageActivity.KEY_NAME, "咨询");
                    startActivity(adviceIntent);

                    break;

                case R.id.video_icon_imgv:

                    //initPlayInfo(videoId, videoName);

                    if(mSubCourseList == null || mSubCourseList.size() == 0) {

                        Toast.makeText(mContext, "没有可播放的视频", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    Course c = mSubCourseList.get(currentPlayIndex);

                    if(c.status == 0) {

                        Toast.makeText(mContext, "请先激活课程", Toast.LENGTH_SHORT).show();
                        return;

                    } else if (c.status == 2) {

                        Toast.makeText(mContext, "视频已过期", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    initPlayInfo(c.ccKey, c.courseName);

                    break;

            }
        }
    };

    public void startLoadingVideo() {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            path = Environment.getExternalStorageDirectory() + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/");
            File savePath = new File(path);
            if (!savePath.exists()) savePath.mkdirs();
            String filePath = path.concat(videoName).concat(".pcm");
            File file = new File(filePath);

//            if(file.exists()) return;

            Downloader downloader = new Downloader(file, videoId, ConfigUtil.USERID, ConfigUtil.API_KEY);
            DownloadTask taskItem = new DownloadTask();
            taskItem.videoId = videoId;
            taskItem.title = videoName;
            taskItem.filePath = filePath;
            taskItem.cover = videoImgCover;
            taskItem.downloader = downloader;
            //taskItem.buyTime =

            AsyncDBHelper.saveVideoCacheDB(mContext, taskItem, new AsyncDBHelper.UIHandler<DownloadTask>() {
                @Override
                public void processMessage(int what, DownloadTask cache) {

                    switch (what) {
                        case SEND_SAVE_OK:

                            ZWDownLoaderService.mTaskItems.put(videoId, cache);
                            Intent intent = new Intent(mContext, ZWDownLoaderService.class);
                            intent.putExtra(ZWDownLoaderService.VID, videoId);
                            startService(intent);

                            break;
                    }

                }
            });

        }
    }

    OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (networkConnected || isLocalPlay) {
                player.seekTo(progress);
                playerHandler.postDelayed(hidePlayRunnable, 5000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (networkConnected || isLocalPlay) {
                this.progress = progress * player.getDuration() / seekBar.getMax();
            }
        }
    };

    VerticalSeekBar.OnSeekBarChangeListener seekBarChangeListener = new VerticalSeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            currentVolume = progress;
            volumeSeekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

    };

    // 控制播放器面板显示
    private boolean isDisplay = false;

    private void initSubtitleSwitchpMenu(Subtitle subtitle) {
        this.subtitle = subtitle;
        subtitleMenu = new PopMenu(this, R.drawable.popup, currrentSubtitleSwitchFlag);
        subtitleMenu.addItems(subtitleSwitchArray);
        subtitleMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:// 开启字幕
                        currentScreenSizeFlag = 0;
                        //subtitleText.setVisibility(View.VISIBLE);
                        break;
                    case 1:// 关闭字幕
                        currentScreenSizeFlag = 1;
                        //subtitleText.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 监测音量变化
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
                || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {

            int volume = audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume != volume) {
                currentVolume = volume;
                volumeSeekBar.setProgress(currentVolume);
            }

            if (isPrepared) {
                setLayoutVisibility(View.VISIBLE, true);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void setLayoutVisibility(int visibility, boolean isDisplay) {
        if (player == null) {
            return;
        }

        if (player.getDuration() <= 0) {
            return;
        }

        playerHandler.removeCallbacks(hidePlayRunnable);

        this.isDisplay = isDisplay;

        if (isDisplay) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

        playerTopLayout.setVisibility(visibility);
        playerBottomLayout.setVisibility(visibility);

        if (isPortrait()) {
            volumeLayout.setVisibility(View.GONE);
            screenSizeBtn.setVisibility(View.GONE);
            definitionBtn.setVisibility(View.GONE);
            subtitleBtn.setVisibility(View.GONE);
        } else {
            volumeLayout.setVisibility(visibility);
            screenSizeBtn.setVisibility(visibility);
            definitionBtn.setVisibility(visibility);
            subtitleBtn.setVisibility(visibility);
        }
    }

    private Handler alertHandler = new Handler() {

        AlertDialog.Builder builder;
        AlertDialog.OnClickListener onClickListener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                bufferProgressBar.setVisibility(View.GONE);
            }

        };

        @Override
        public void handleMessage(Message msg) {

            mImageIcon.setVisibility(View.VISIBLE);
            bufferProgressBar.setVisibility(View.GONE);

            String message = "";
            boolean isSystemError = false;
            if (ErrorCode.INVALID_REQUEST.Value() == msg.what) {
                message = "无法播放此视频，请检查视频状态";
            } else if (ErrorCode.NETWORK_ERROR.Value() == msg.what) {
                message = "无法播放此视频，请检查网络状态";
            } else if (ErrorCode.PROCESS_FAIL.Value() == msg.what) {
                message = "无法播放此视频，请检查帐户信息";
            } else if(-15 == msg.what) {

                message = "播放未授权！";

            }else {
                isSystemError = true;
            }

            if (!isSystemError) {
                builder = new AlertDialog.Builder(mContext);
                dialog = builder.setTitle("提示").setMessage(message)
                        .setPositiveButton("OK", onClickListener)
                        .setCancelable(false).show();
            }

            super.handleMessage(msg);
        }

    };

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Message msg = new Message();
        msg.what = what;
        Log.e(TAG, "error code : " + msg.what + "");
        if (alertHandler != null) {
            alertHandler.sendMessage(msg);
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        setSurfaceViewLayout();
    }


    // 重置隐藏界面组件的延迟时间
    private void resetHideDelayed() {
        playerHandler.removeCallbacks(hidePlayRunnable);
        playerHandler.postDelayed(hidePlayRunnable, 5000);
    }

    // 手势监听器类
    private class MyGesture extends SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            scrollTotalDistance += distanceX;

            float duration = (float) player.getDuration();

            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            float width = wm.getDefaultDisplay().getWidth() * 0.75f; // 设定总长度是多少，此处根据实际调整

            float currentPosition = scrollCurrentPosition - (float) duration
                    * scrollTotalDistance / width;

            if (currentPosition < 0) {
                currentPosition = 0;
            } else if (currentPosition > duration) {
                currentPosition = duration;
            }

            player.seekTo((int) currentPosition);

            playDuration.setText(ParamsUtil
                    .millsecondsToStr((int) currentPosition));
            int pos = (int) (skbProgress.getMax() * currentPosition / duration);
            skbProgress.setProgress(pos);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {

            scrollTotalDistance = 0f;

            scrollCurrentPosition = (float) player.getCurrentPosition();

            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            changePlayStatus();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isDisplay) {
                setLayoutVisibility(View.GONE, false);
            } else {
                setLayoutVisibility(View.VISIBLE, true);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void changePlayStatus() {
        if (player.isPlaying()) {
            player.pause();
            playOp.setImageResource(R.drawable.btn_play);

        } else {
            player.start();
            playOp.setImageResource(R.drawable.btn_pause);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (player.isPlaying()) {
                    bufferProgressBar.setVisibility(View.VISIBLE);
                }
                break;
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_END:
                bufferProgressBar.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    // 获得当前屏幕的方向
    private boolean isPortrait() {
        int mOrientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    private int mX, mY, mZ;
    private long lastTimeStamp = 0;
    private Calendar mCalendar;
    private SensorManager sensorManager;

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis() / 1000l;

            int second = mCalendar.get(Calendar.SECOND);// 53

            int px = Math.abs(mX - x);
            int py = Math.abs(mY - y);
            int pz = Math.abs(mZ - z);

            int maxvalue = getMaxValue(px, py, pz);
            if (maxvalue > 2 && (stamp - lastTimeStamp) > 1) {
                lastTimeStamp = stamp;
                Log.d("demo", " sensor isMoveorchanged....");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
            mX = x;
            mY = y;
            mZ = z;
        }*/
    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    private int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }

        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onBackPressed() {

        if (mShareView.getVisibility() == View.VISIBLE) {

            mShareView.setVisibility(View.GONE);
            return;

        }

        if (isPortrait()) {
            surfaceView.setBackgroundColor(getResources().getColor(R.color.black));
            super.onBackPressed();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onResume() {
        if (isFreeze) {
            isFreeze = false;
            if (isPrepared) {
                player.start();
            }
        } else {
            if (isPlaying != null && isPlaying.booleanValue() && isPrepared) {
                player.start();
            }
        }
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        if (isPrepared) {
            // 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
            if (player.isPlaying()) {
                isPlaying = true;
            } else {
                isPlaying = false;
            }
            player.pause();
        } else {
            // 如果播放器没有prepare完成，则设置isFreeze为true
            isFreeze = true;
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        timerTask.cancel();

        playerHandler.removeCallbacksAndMessages(null);
        playerHandler = null;

        alertHandler.removeCallbacksAndMessages(null);
        alertHandler = null;

        if (currentPlayPosition > 0) {
            if (lastPlayPosition > 0) {
                CCDataSet.updateVideoPosition(videoId, currentPlayPosition);
            } else {
                CCDataSet.insertVideoPosition(videoId, currentPlayPosition);
            }
        }

        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (!isLocalPlay) {
            networkInfoTimerTask.cancel();
        }

        if(drmServer != null) {

            drmServer.disconnectCurrentStream();
            drmServer.stop();

        }

        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (isPrepared) {
            // 刷新界面
            setLayoutVisibility(View.GONE, false);
            setLayoutVisibility(View.VISIBLE, true);
        }

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            rlBelow.setVisibility(View.VISIBLE);
            ivFullscreen.setImageResource(R.drawable.fullscreen_close);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            rlBelow.setVisibility(View.GONE);
            ivFullscreen.setImageResource(R.drawable.fullscreen_open);

        }

        setSurfaceViewLayout();
    }

    private DownloadListener downloadListener = new DownloadListener() {

        @Override
        public void handleProcess(long start, long end, String videoId) {
            // TODO 处理下载进度
            Log.i(TAG, start + "-" + end + "-" + videoId);
        }

        @Override
        public void handleException(DreamwinException exception, int status) {
            // TODO 处理下载过程中发生的异常信息
        }

        @Override
        public void handleStatus(String videoId, int status) {
            // TODO 处理视频下载信息及相应状态
            Log.i(TAG, videoId + "-" + status);
        }

        @Override
        public void handleCancel(String videoId) {
            // TODO 处理取消下载的后续操作
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0) {

            if(resultCode == 2) {

                loadSubCourse(videoBuyCourseId);

                Log.i(TAG, "-=========================-" + videoId);

                initPlayInfo(videoId, videoName);

                long activeTime = new Date().getTime();

                SharedPreferences sp = getSharedPreferences(EXTRA_KEY_ACTIVE_TIMES, 0);
                sp.edit().putLong(videoId, activeTime).commit();

            }


        }


    }

}

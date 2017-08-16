package com.zhiwang123.mobile.phone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.CategrayGridAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseHotkey;
import com.zhiwang123.mobile.phone.bean.CourseRoot;
import com.zhiwang123.mobile.phone.bean.Topic;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.fragment.BaseFragment;
import com.zhiwang123.mobile.phone.fragment.CourseListFragment;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.LHSearchHistoryLayout;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ty on 2016/11/2.
 */

public class SearchActivity extends BaseActivity implements LHSearchHistoryLayout.OnHistoryClickCallback {

    public static final String TAG = "LoginActivity";

    public static final int CONTENT_EARE = R.id.search_content;

    public static final String KEY_SHOW_ALL = "is_show_category";

    private Context mContext;
    private View mRootView;
    private TextView mGobackBtn;
    private TextView mTtSwitch;
    private View mTtSearchBtnCon;
    private BaseFragment mCurrentFragment;
    private EditText mSearchInput;
    private GridView mGridView;
    private View mClearHistoryBtn;
    private View mClearInputBtn;
    private View mSearchTypeBtnCourse;
    private View mSearchTypeBtnTeacher;
    private LHSearchHistoryLayout mHotSearchHistory;
    private LHSearchHistoryLayout mPastSearchHsitroy;
    private boolean mSearchBtnState = false;
    private boolean isTiNoTe = true;

    private boolean isShowAllCate;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_activity_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, R.color.colorPrimary);
        }

        mActionBar.show();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_search_actv_layout);

        mContext = getApplicationContext();

        //choosePage(null);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            isShowAllCate = bundle.getBoolean(KEY_SHOW_ALL, false);

        }

        initLayout();
        loadCourseRoot();
        loadHots();
        loadHistoryTag();
    }

    @Override
    protected void initLayout() {

        View actionView = mActionBar.getCustomView().findViewById(R.id.search_action_layout_root);
        mGobackBtn = (TextView) actionView.findViewById(R.id.search_page_go_back);
        mTtSwitch = (TextView) actionView.findViewById(R.id.search_action_switch);
        mSearchInput = (EditText) actionView.findViewById(R.id.search_page_search);
        mTtSearchBtnCon = actionView.findViewById(R.id.search_btn_con);

        mRootView = findViewById(CONTENT_EARE);
        mGridView = (GridView) findViewById(R.id.search_categray_list);
        mClearHistoryBtn = findViewById(R.id.search_clear_history_btn);
        mClearInputBtn = findViewById(R.id.search_clear_input_btn);
        mHotSearchHistory = (LHSearchHistoryLayout) findViewById(R.id.search_info_hot_view);
        mPastSearchHsitroy = (LHSearchHistoryLayout) findViewById(R.id.search_info_history_view);

        mHotSearchHistory.setONHistoryClickCallback(this);
        mPastSearchHsitroy.setONHistoryClickCallback(this);

        mSearchInput.clearFocus();
        mSearchInput.addTextChangedListener(textWatcher);
        mGobackBtn.setOnClickListener(mOnClickListener);
        mTtSearchBtnCon.setOnClickListener(mOnClickListener);
        mClearHistoryBtn.setOnClickListener(mOnClickListener);
        mClearInputBtn.setOnClickListener(mOnClickListener);

        mGridView.setVisibility(isShowAllCate ? View.VISIBLE : View.GONE);

        mSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)  {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||(event != null&&event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    toSearch(mSearchInput.getText().toString());

                    return true;
                }

                return false;
            }

        });

    }

    private void loadHots() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlHotkeys(), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        CourseHotkey courseHotkey = new CourseParser<CourseHotkey>(new CourseHotkey()).parse(response);
                        String[] hots = courseHotkey.data;
                        if(hots != null && hots.length != 0) {
                            mHotSearchHistory.setHistoryInfo(hots);
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

    private void loadHistoryTag() {

        SharedPreferences sp = getSharedPreferences(StaticConfigs.SP_HISTORY_XML_FILENAME, 0);
        String str = sp.getString(StaticConfigs.SP_HISTORY_XML_KEY, "");
        mPastSearchHsitroy.setHistoryInfo(str.split("-"));

    }

    private void choosePage(BaseFragment fragment) {

        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(CONTENT_EARE, fragment).commit();

    }


    boolean tmpBo = true;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.search_page_go_back:
                    if(!mSearchBtnState) onBackPressed();
                    else toSearch(mSearchInput.getText().toString());
                    break;
                case R.id.search_btn_con:

                    tmpBo = true;

                    View popupWindow_view = getLayoutInflater().inflate(R.layout.pop_window_layout, null, false);

                    popupWindow = new PopupWindow(popupWindow_view, 350, 700, true);

                    popupWindow.showAsDropDown(mTtSwitch);

                    mSearchTypeBtnCourse = popupWindow_view.findViewById(R.id.search_type_btn_title);
                    mSearchTypeBtnTeacher = popupWindow_view.findViewById(R.id.searcher_type_btn_teacher);


//                    final String[] item = new String[] {mContext.getString(R.string.search_type_ti), mContext.getString(R.string.search_type_te)};
//
//                    new AlertDialog.Builder(SearchActivity.this)
//                            .setTitle(mContext.getString(R.string.search_type))
//                            .setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    switch (which) {
//                                        case 0:
//                                            tmpBo = true;
//                                            break;
//                                        case 1:
//                                            tmpBo = false;
//                                            break;
//                                    }
//                                }
//                            })
//                            .setPositiveButton(mContext.getString(R.string.search_yes), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    isTiNoTe = tmpBo;
//
//                                    if(isTiNoTe) {
//                                        mTtSwitch.setText(mContext.getString(R.string.search_type_title));
//                                    } else {
//                                        mTtSwitch.setText(mContext.getString(R.string.search_type_teacher));
//                                    }
//                                    dialog.dismiss();
//                                }
//                            })
//                            .setNegativeButton(mContext.getString(R.string.search_cancel), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    tmpBo = true;
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();

                    mSearchTypeBtnCourse.setOnClickListener(mOnClickListener);
                    mSearchTypeBtnTeacher.setOnClickListener(mOnClickListener);

                    break;
                case R.id.search_clear_history_btn:

                    SharedPreferences sp = getSharedPreferences(StaticConfigs.SP_HISTORY_XML_FILENAME, 0);
                    String str = sp.getString(StaticConfigs.SP_HISTORY_XML_KEY, "");
                    sp.edit().remove(StaticConfigs.SP_HISTORY_XML_KEY).commit();
                    mPastSearchHsitroy.removeAllViews();

                    break;

                case R.id.search_clear_input_btn:

                    mSearchInput.setText("");

                    break;
                case R.id.search_type_btn_title:

                    mTtSwitch.setText(R.string.search_type_title);
                    tmpBo = true;
                    isTiNoTe = true;
                    popupWindow.dismiss();

                    break;
                case R.id.searcher_type_btn_teacher:

                    mTtSwitch.setText(R.string.search_type_teacher);
                    tmpBo = false;
                    isTiNoTe = false;
                    popupWindow.dismiss();

                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            CategrayGridAdapter.ViewHolder viewHolder = (CategrayGridAdapter.ViewHolder) view.getTag();
            String pkIds = viewHolder.pkids;
            String name = viewHolder.title;
            Intent intent = new Intent(mContext, CourseListActivity.class);
            intent.putExtra(CourseListActivity.EXTRA_PKIDS, pkIds);
            intent.putExtra(CourseListActivity.EXTRA_NAME, name);
            startActivity(intent);

        }
    };

    private void toSearch(String keyword) {

        if(TextUtils.isEmpty(keyword)) return;

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(),0);

        SharedPreferences sp = getSharedPreferences(StaticConfigs.SP_HISTORY_XML_FILENAME, 0);

        String str = sp.getString(StaticConfigs.SP_HISTORY_XML_KEY, "");
        StringBuffer stringBuffer = new StringBuffer(str);

        if(!str.contains(keyword)) {

            if(stringBuffer.length() != 0) stringBuffer.append("-" + keyword);
            else stringBuffer.append(keyword);
            sp.edit().putString(StaticConfigs.SP_HISTORY_XML_KEY, stringBuffer.toString()).commit();

        }


        CourseListFragment fragment = new CourseListFragment();

        Bundle bundle = new Bundle();

        if(isTiNoTe) {

            bundle.putString(CourseListFragment.EXTRA_KEY_TITLE, keyword);

        } else {

            bundle.putString(CourseListFragment.EXTRA_KEY_TEACHER, keyword);

        }

        fragment.setArguments(bundle);

        choosePage(fragment);

        mPastSearchHsitroy.setHistoryInfo(stringBuffer.toString().split("-"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
    }

    private void loadCourseRoot() {

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getCourseCustom(), null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        CourseRoot courseRoot = new CourseParser<CourseRoot>(new CourseRoot()).parse(response);
                        if(courseRoot != null && courseRoot.dataobject != null && courseRoot.dataobject.size() != 0) {
                            ArrayList<Topic> topics = new ArrayList<>();
                            for(Course c : courseRoot.dataobject) {
                                Topic topic = new Topic();

                                topic.title = c.title;
                                topic.pkIds = c.categoryPkIds;

                                if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_1_398))) {
                                    topic.imgResId = R.drawable.t398;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_2_414))) {
                                    topic.imgResId = R.drawable.t414;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_3_423))) {
                                    topic.imgResId = R.drawable.t423;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_4_620))) {
                                    topic.imgResId = R.drawable.t620;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_5_356))) {
                                    topic.imgResId = R.drawable.t356;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_6_367))) {
                                    topic.imgResId = R.drawable.t367;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_7_446))) {
                                    topic.imgResId = R.drawable.t446;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_8_382))) {
                                    topic.imgResId = R.drawable.t382;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_9_491))) {
                                    topic.imgResId = R.drawable.t491;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_10_505))) {
                                    topic.imgResId = R.drawable.t505;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_11_504))) {
                                    topic.imgResId = R.drawable.t504;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_12_465))) {
                                    topic.imgResId = R.drawable.t465;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_13_490))) {
                                    topic.imgResId = R.drawable.t490;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_14_604))) {
                                    topic.imgResId = R.drawable.t604;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_15_681))) {
                                    topic.imgResId = R.drawable.t681;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_16_682))) {
                                    topic.imgResId = R.drawable.t682;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_17_683))) {
                                    topic.imgResId = R.drawable.t683;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_18_684))) {
                                    topic.imgResId = R.drawable.t684;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_19_685))) {
                                    topic.imgResId = R.drawable.t685;
                                } else if(c.categoryPkIds.contains(String.valueOf(StaticConfigs.T_20_686))) {
                                    topic.imgResId = R.drawable.t686;
                                } else {
                                    topic.imgResId = R.drawable.tmp_sit;
                                }
                                topics.add(topic);
                            }

                            CategrayGridAdapter adapter = new CategrayGridAdapter(mContext, topics);
                            mGridView.setAdapter(adapter);
                            mGridView.setOnItemClickListener(onItemClickListener);
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

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if(TextUtils.isEmpty(s.toString())) {
                mGobackBtn.setText(R.string.search_cancel);
                mSearchBtnState = false;
            } else {
                if(s.toString().contains("-")) {
                    String str = s.toString().replace("-", "");
                    s.clear();
                    s.append(str);
                }
                mGobackBtn.setText(R.string.search_go);
                mSearchBtnState = true;
            }

        }
    };

    @Override
    public void onHistoryClick(String keyWord) {

        toSearch(keyWord);

    }
}

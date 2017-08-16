package com.zhiwang123.mobile.phone.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.CourseIntroduceActivity;
import com.zhiwang123.mobile.phone.activity.MainActivity;
import com.zhiwang123.mobile.phone.activity.SearchActivity;
import com.zhiwang123.mobile.phone.adapter.FirstPageAdapter;
import com.zhiwang123.mobile.phone.bean.CourseNewest;
import com.zhiwang123.mobile.phone.bean.Snippet;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.SilderViewContainer;
import com.zhiwang123.mobile.phone.widget.TopicViewContainer;
import com.zhiwang123.mobile.phone.widget.listview.XListView;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;

import org.json.JSONObject;

/**
 * @author wz
 */
public class FirstPageFragment extends BaseFragment {

	private static final String TAG = "FirstPageFragment";

	private View mRootView;
	private Context mContext;
	private XRestrictListView mListView;
	private FirstPageAdapter mAdapter;
	private Snippet mFocuses;
	private CourseNewest mNewestCourse;
	private View mToAllTopicBtn;
	private TextView mTopBarLeftTv;
	private TextView mSearchEdt;
	private View mTran;
	private View mFackStatue;
	private View mFackAction;

	private static final float alphaSeed = 255f / 375f;

	private static FirstPageFragment self = new FirstPageFragment();
	public FirstPageFragment() {}

	public static FirstPageFragment getInstance() {
		return self;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.first_fragment_layout, null);
		//mRootView.getViewTreeObserver().addOnGlobalLayoutListener(vgl);
		mListView = (XRestrictListView) mRootView.findViewById(R.id.first_page_list);
		mListView.setDividerHeight(0);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(mXlistViewListener);
		mListView.setOnHeaderUpateChangeListener(callback);

		mToAllTopicBtn = mRootView.findViewById(R.id.first_page_to_all);
		mSearchEdt = (TextView) mRootView.findViewById(R.id.first_page_search);
		mTopBarLeftTv = (TextView) mRootView.findViewById(R.id.first_top_bar_left_tv);
		mTran = mRootView.findViewById(R.id.tran_area);
		mFackAction = mRootView.findViewById(R.id.first_page_fack_actionbar);
		mFackStatue = mRootView.findViewById(R.id.first_page_fack_statusbar);

		mFackAction.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		mFackStatue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		mFackAction.getBackground().setAlpha(0);
		mFackStatue.getBackground().setAlpha(0);

		mToAllTopicBtn.setOnClickListener(mOnClick);
		mSearchEdt.setOnClickListener(mOnClick);

//		mTopBarLeftTv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mSearchEdt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mContext = getActivity().getApplicationContext();

		IntentFilter filter = new IntentFilter();
		filter.addAction(StaticConfigs.FAVORITE_CHANGE_ACITON);
		filter.setPriority(Integer.MAX_VALUE);
		getActivity().registerReceiver(mFavoriteChangeRecv, filter);

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(StaticConfigs.CART_CHANGE_ACITON);
		filter2.setPriority(Integer.MAX_VALUE);
		getActivity().registerReceiver(mCartChangeRecv, filter2);


		Bundle bundle = getArguments();

		loadFocusInfo();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mFavoriteChangeRecv);
		getActivity().unregisterReceiver(mCartChangeRecv);
	}

	private XListView.IXListViewListener mXlistViewListener = new XListView.IXListViewListener() {

		@Override
		public void onRefresh() {

			refreshInfo();

		}

		@Override
		public void onLoadMore() {


		}

	};

	private XListView.OnHeaderHeightUpdateCallback callback = new XListView.OnHeaderHeightUpdateCallback() {

		@Override
		public void onChanged(float height) {

//			Log.i(TAG, "height------" + height);

		}
	};

	public void refreshInfo() {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlFocus(5), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Snippet snippet = new Snippet().parse(response);
						mFocuses = snippet;
						loadCourseNew();
					}
				},
				new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, error.getMessage());
						loadCourseNew();
					}
				});

		mVolleyRequestQueue.add(jsonRequest);

	}

	private void loadFocusInfo() {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlFocus(5), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Snippet snippet = new Snippet().parse(response);
						mFocuses = snippet;
						loadCourseNew();
					}
				},
				new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, error.getMessage() + "");
						loadCourseNew();
					}
				});

		mVolleyRequestQueue.add(jsonRequest);
	}

	private void loadCourseNew() {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseNewest(10, 1), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						mNewestCourse = new CourseParser<CourseNewest>(new CourseNewest()).parse(response);
						mAdapter = new FirstPageAdapter(getActivity(), mNewestCourse, mFocuses);
						mListView.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
						mListView.setOnScrollListener(onScrollListener);
						mListView.setOnItemClickListener(onItemClickListener);
						mSearchEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if(hasFocus) mSearchEdt.setCursorVisible(true);
								else mSearchEdt.setCursorVisible(false);
							}
						});
						mSearchEdt.clearFocus();

						mListView.stopRefresh();

						mContext.sendBroadcast(new Intent(MainActivity.ACTION_HIDDEN_SPLASH));

					}
				},
				new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {

						System.out.println("" + error.getMessage());

					}
		});

		mVolleyRequestQueue.add(jsonRequest);
	}

//	ViewTreeObserver.OnGlobalLayoutListener vgl = new ViewTreeObserver.OnGlobalLayoutListener() {
//
//		@Override
//		public void onGlobalLayout() {
//			RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//			Rect rect = new Rect();
//			getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//			int screenHeight = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
//			llp.setMargins(0, getActivity().getActionBar().getHeight() + (screenHeight - rect.height()), 0, 0);
//			mListView.setLayoutParams(llp);
//			mRootView.setPadding(0, getActivity().getActionBar().getHeight() + (screenHeight - rect.height()), 0, 0);
//			mHandler.sendEmptyMessage(0);
//		}
//	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(vgl);
		}
	};

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	private AbsListView.OnScrollListener  onScrollListener = new AbsListView.OnScrollListener() {

		private int scrollAlpha = 0;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			switch(scrollState) {

				case SCROLL_STATE_IDLE:

					if(view.getChildAt(0) == null) return;
					View slider = view.getChildAt(1);
					View v = view.getChildAt(2);

					if(slider instanceof SilderViewContainer && v instanceof TopicViewContainer) {

						float offset = Math.abs(slider.getY());

						Log.i(TAG, offset + "----------(---");

						int alpha = (int)(offset * alphaSeed);
						if(alpha < 10) alpha = 0;

						if(alpha == 0) {

							mFackAction.getBackground().setAlpha(0);
							mFackStatue.getBackground().setAlpha(0);

						}


					}

					break;
				case SCROLL_STATE_FLING:



					break;
				case SCROLL_STATE_TOUCH_SCROLL:



					break;
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			if(view.getChildAt(0) == null) return;
			View slider = view.getChildAt(0);
			View v = view.getChildAt(1);

			if(slider instanceof SilderViewContainer && v instanceof TopicViewContainer) {

				float offset = Math.abs(slider.getY());

				Log.i(TAG, offset + "------------------");

				scrollAlpha = (int)(offset * alphaSeed);
				if(scrollAlpha < 10) scrollAlpha = 0;
				else if(scrollAlpha > 255) scrollAlpha = 255;

				mFackAction.getBackground().setAlpha(scrollAlpha);
				mFackStatue.getBackground().setAlpha(scrollAlpha);

			} /*else {

				mFackAction.getBackground().setAlpha(255);
				mFackStatue.getBackground().setAlpha(255);

			}*/

		}

//		public void startAlphaAnimation(View v, float before, float after){
//			/**
//			 * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
//			 * @param toAlpha 结束的透明度，同上
//			 */
//			AlphaAnimation alphaAnimation = new AlphaAnimation(before, after);
//			//设置动画持续时长
//			alphaAnimation.setDuration(2000);
//			//设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
//			alphaAnimation.setFillAfter(true);
//			//设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
//			alphaAnimation.setFillBefore(true);
//			//设置动画的重复模式：反转REVERSE和重新开始RESTART
//			alphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);
//			//设置动画播放次数
//			alphaAnimation.setRepeatCount(AlphaAnimation.INFINITE);
//			//开始动画
//			v.startAnimation(alphaAnimation);
////			//清除动画
////			v.clearAnimation();
////			//同样cancel()也能取消掉动画
////			alphaAnimation.cancel();
//		}

	};

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			Intent intent = null;

//			if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) {
//
//				intent = new Intent(mContext, LoginActivity.class);
//				getActivity().startActivityForResult(intent, StaticConfigs.REQUEST_CODE_LOGIN);
//				return;
//			}

			FirstPageAdapter.CellHolder holder = (FirstPageAdapter.CellHolder) view.getTag();
			String courseId = holder.courseId;
			boolean isFav = holder.c.isInFavorite;
			intent = new Intent(mContext, CourseIntroduceActivity.class);
			intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_CID, courseId);
			intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_COURSE_TYPE, holder.c.courseType);
			intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_FAVSTATE, isFav);
			startActivity(intent);

		}
	};

	private View.OnClickListener mOnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = null;

			switch(v.getId()) {
				case R.id.first_page_to_all:

					intent = new Intent(mContext, SearchActivity.class);

					intent.putExtra(SearchActivity.KEY_SHOW_ALL, true);

					getActivity().startActivity(intent);

					break;

				case R.id.first_page_search:

					intent = new Intent(mContext, SearchActivity.class);

					intent.putExtra(SearchActivity.KEY_SHOW_ALL, false);

					getActivity().startActivity(intent);

					break;
			}

		}
	};


	private BroadcastReceiver mFavoriteChangeRecv = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals(StaticConfigs.FAVORITE_CHANGE_ACITON)) {

				refreshInfo();

			}

		}
	};

	private BroadcastReceiver mCartChangeRecv = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals(StaticConfigs.CART_CHANGE_ACITON)) {

				refreshInfo();

			}

		}
	};

}

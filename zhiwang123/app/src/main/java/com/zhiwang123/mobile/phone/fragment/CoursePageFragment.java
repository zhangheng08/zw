package com.zhiwang123.mobile.phone.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.SearchActivity;
import com.zhiwang123.mobile.phone.adapter.CoursePageAdapter;
import com.zhiwang123.mobile.phone.adapter.CoursePageAdapter2;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecmd;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;
import com.zhiwang123.mobile.phone.bean.CourseRoot;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XListView;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author wz
 */
public class CoursePageFragment extends BaseFragment {

	private static final String TAG = "CoursePageFragment";

	private View mRootView;
	private Context mContext;
	private XRestrictListView mListView;
	private CoursePageAdapter mAdapter;
	private CoursePageAdapter2 mAdapter2;
	private CourseRoot mCourseRoot;
	private CourseRecmd mCourseRecmd;
	private ArrayList<CourseRecommand> recommandsList;
	private ArrayList<View> views;
	private ImageView mToAllCateBtn;

	private static CoursePageFragment self = new CoursePageFragment();
	public CoursePageFragment() {}

	public static CoursePageFragment getInstance() {
		return self;
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.course_fragment_layout, null);
		mListView = (XRestrictListView) mRootView.findViewById(R.id.course_page_list);

		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(mXlistViewListener);

		mToAllCateBtn = (ImageView) mRootView.findViewById(R.id.course_page_to_all);

		mToAllCateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(mContext, SearchActivity.class);

				intent.putExtra(SearchActivity.KEY_SHOW_ALL, true);

				getActivity().startActivity(intent);

			}
		});

		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		mContext = getActivity().getApplicationContext();
//		loadCourseRoot();

		IntentFilter filter = new IntentFilter();
		filter.addAction(StaticConfigs.FAVORITE_CHANGE_ACITON);
		filter.setPriority(Integer.MAX_VALUE);
		getActivity().registerReceiver(mFavoriteChangeRecv, filter);

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(StaticConfigs.CART_CHANGE_ACITON);
		filter2.setPriority(Integer.MAX_VALUE);
		getActivity().registerReceiver(mCartChangeRecv, filter2);

		loadCourseRecmd();

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

	private void loadCourseRecmd() {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlRecmdCourse("360*270", 4), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseRecmd = new CourseParser<CourseRecmd>(new CourseRecmd()).parse(response);
						mAdapter2 = new CoursePageAdapter2(getActivity(), mCourseRecmd.dataobject);
						mListView.setAdapter(mAdapter2);

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

	private XListView.IXListViewListener mXlistViewListener = new XListView.IXListViewListener() {

		@Override
		public void onRefresh() {

			refreshCourseRecommand();

		}

		@Override
		public void onLoadMore() {


		}

	};

	private void refreshCourseRecommand() {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlRecmdCourse("360*270", 4), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseRecmd = new CourseParser<CourseRecmd>(new CourseRecmd()).parse(response);
						mAdapter2 = new CoursePageAdapter2(getActivity(), mCourseRecmd.dataobject);
						mListView.setAdapter(mAdapter2);

						mListView.stopRefresh();

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

	private void loadCourseRoot() {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getCourseRoot(), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						mCourseRoot = new CourseParser<CourseRoot>(new CourseRoot()).parse(response);
						mAdapter = new CoursePageAdapter(getActivity(), mCourseRoot.dataobject);
						mListView.setAdapter(mAdapter);

//						if(mCourseRoot.dataobject.size() != 0) {
//							recommandsList = new ArrayList<>();
//							views = new ArrayList<>();
//							for(Course c : mCourseRoot.dataobject) {
//								View v = LayoutInflater.from(mContext).inflate(R.layout.course_fragment_page_cell_reuse, null);
//								views.add(v);
//							}
//							for(Course c : mCourseRoot.dataobject) {
//								loadRecommandCourse(c);
//							}
//						}
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

	private void loadRecommandCourse(final Course parentCourse) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseList(parentCourse.pkId, "120*90", 1, 6), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
//						CourseRecommand recommandCourse = new CourseParser<CourseRecommand>(new CourseRecommand()).parse(response);
//						recommandCourse.parentCourse = parentCourse;
//						recommandsList.add(recommandCourse);

//						RecyclerView recyclerView = new RecyclerView(mContext);
//						RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//						recyclerView.setLayoutParams(llp);
//						LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
//			            recyclerViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            			recyclerView.setLayoutManager(recyclerViewLayoutManager);
//						CourseSubAdapter subAdapter = new CourseSubAdapter(mContext, recommandCourse.dataobject);
//			            recyclerView.setAdapter(subAdapter);
//						subAdapter.notifyDataSetChanged();
//						recommandCourse.viewList = recyclerView;

//						if(mAdapter == null) {
//							mAdapter = new CoursePageAdapter(mContext, views, recommandsList);
//							mListView.setAdapter(mAdapter);
//						} else {
//							mAdapter.appendDataList(recommandCourse);
//						}
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
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


	private BroadcastReceiver mFavoriteChangeRecv = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals(StaticConfigs.FAVORITE_CHANGE_ACITON)) {

				refreshCourseRecommand();

			}

		}
	};

	private BroadcastReceiver mCartChangeRecv = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals(StaticConfigs.CART_CHANGE_ACITON)) {

				refreshCourseRecommand();

			}

		}
	};

}

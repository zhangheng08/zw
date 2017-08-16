package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.CourseCenterListPageAdapter;
import com.zhiwang123.mobile.phone.bean.CourseCenter;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XListView;

import org.json.JSONObject;

/**
 * @author wz
 */
public class CourseCenterListFragment extends BaseFragment {

	private static final String TAG = "CourseCenterListFragment";

	private View mRootView;
	private Context mContext;
	private XListView mListView;
	private CourseCenterListPageAdapter mAdapter;
	private CourseCenter mCourseCenter;

	private int mCurrentPage;

	private ProgressBar mWaiting;


	private static CourseCenterListFragment self = new CourseCenterListFragment();
	public CourseCenterListFragment() {}

	public static CourseCenterListFragment getInstance() {
		return self;
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.course_center_fragm_layout, null);
		mListView = (XListView) mRootView.findViewById(R.id.course_center_list);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(mXlistViewListener);

		mWaiting = (ProgressBar) mRootView.findViewById(R.id.course_center_waiting);

		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		mCurrentPage = 1;
		refreshCourseCenter();
	}

	private XListView.IXListViewListener mXlistViewListener = new XListView.IXListViewListener() {

		@Override
		public void onRefresh() {

			refreshCourseCenter();

		}

		@Override
		public void onLoadMore() {

			loadMoreCourseCenter(++ mCurrentPage);

		}

	};

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void refreshCourseCenter() {

		mWaiting.setVisibility(View.VISIBLE);

 		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCenterCoursePublic(StaticConfigs.mLoginResult.accessToken, 1, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseCenter = new CourseParser<CourseCenter>(new CourseCenter()).parse(response);

						if(mAdapter == null) {
							mAdapter = new CourseCenterListPageAdapter(getActivity(), mCourseCenter.dataobject);
							mListView.setAdapter(mAdapter);
						} else {
							mAdapter.setDataList(mCourseCenter.dataobject);
						}

						mAdapter.notifyDataSetChanged();
						mListView.stopRefresh();

						mCurrentPage = 1;
						mListView.setPullLoadEnable(true);

						mWaiting.setVisibility(View.GONE);

					}
				},
				new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

		mVolleyRequestQueue.add(jsonRequest);
	}

	private void loadMoreCourseCenter(int pageNum) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCenterCoursePublic(StaticConfigs.mLoginResult.accessToken, pageNum, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseCenter = new CourseParser<CourseCenter>(new CourseCenter()).parse(response);

						if(mCourseCenter.dataobject == null || mCourseCenter.dataobject.size() == 0) {
							mListView.setPullLoadEnable(false);
							mListView.stopLoadMore();
							Toast.makeText(mContext, R.string.center_no_more, Toast.LENGTH_SHORT).show();
							return;
						} else {
							mListView.setPullLoadEnable(true);
						}

						if(mAdapter == null) {
							mAdapter = new CourseCenterListPageAdapter(getActivity(), mCourseCenter.dataobject);
							mListView.setAdapter(mAdapter);
						} else {
							mAdapter.addDataList(mCourseCenter.dataobject);
						}

						mAdapter.notifyDataSetChanged();
						mListView.stopLoadMore();

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
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}

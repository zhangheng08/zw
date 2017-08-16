package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.adapter.FavoriteListPageAdapter;
import com.zhiwang123.mobile.phone.bean.CourseFavorite;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XListView;
import com.zhiwang123.mobile.phone.widget.listview.XRestrictListView;

import org.json.JSONObject;

/**
 * @author wz
 */
public class FavoriteListFragment extends BaseFragment {

	public static final String TAG = FavoriteListFragment.class.getName();

	private XRestrictListView mListView;

	private View mRootView;
	private Context mContext;
	private FavoriteListPageAdapter mAdapter;
	private CourseFavorite mCourseFavorite;

	private int mCurrentPage;


	private static FavoriteListFragment self = new FavoriteListFragment();
	public FavoriteListFragment() {}

	public static FavoriteListFragment getInstance() {
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
		mListView = (XRestrictListView) mRootView.findViewById(R.id.course_center_list);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(mXlistViewListener);
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {

		}
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

			loadMoreCourseCenter(mCurrentPage ++);

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

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlListFav(StaticConfigs.mLoginResult.accessToken, "360*270", 1, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseFavorite = new CourseParser<CourseFavorite>(new CourseFavorite()).parse(response);

						if(mAdapter == null) {
							mAdapter = new FavoriteListPageAdapter(getActivity(), mCourseFavorite.dataobject, mListView);
							mListView.setAdapter(mAdapter);
						} else {
							mAdapter.setDataList(mCourseFavorite.dataobject);
						}

						mAdapter.notifyDataSetChanged();
						mListView.stopRefresh();

						mCurrentPage = 1;
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

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlListFav(StaticConfigs.mLoginResult.accessToken, "360*270", pageNum, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseFavorite = new CourseParser<CourseFavorite>(new CourseFavorite()).parse(response);

						if(mAdapter == null) {
							mAdapter = new FavoriteListPageAdapter(getActivity(), mCourseFavorite.dataobject, mListView);
							mListView.setAdapter(mAdapter);
						} else {
							mAdapter.addDataList(mCourseFavorite.dataobject);
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

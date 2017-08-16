package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.CourseIntroduceActivity;
import com.zhiwang123.mobile.phone.activity.LoginActivity;
import com.zhiwang123.mobile.phone.activity.TeacherIntroActivity;
import com.zhiwang123.mobile.phone.adapter.CourseListPageAdapter;
import com.zhiwang123.mobile.phone.adapter.TeacherSearchListAdapter;
import com.zhiwang123.mobile.phone.bean.CourseCenter;
import com.zhiwang123.mobile.phone.bean.Teacher;
import com.zhiwang123.mobile.phone.bean.TeacherEntity;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.bean.parser.TeacherParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;
import com.zhiwang123.mobile.phone.widget.listview.XListView;

import org.json.JSONObject;

/**
 * @author wz
 */
public class CourseListFragment extends BaseFragment {

	private static final String TAG = CourseListFragment.class.getName();

	public static final String EXTRA_KEY_PKID = "extra_key_pkid";
	public static final String EXTRA_KEY_TEACHER = "extra_key_te";
	public static final String EXTRA_KEY_TITLE = "extra_key_ti";

	private View mRootView;
	private Context mContext;
	private XListView mListView;
	private CourseListPageAdapter mAdapter;
	private CourseCenter mCourseCenter;

	private TeacherEntity mTeacherEntity;
	private TeacherSearchListAdapter mTeacherListAdapter;

	private int mCurrentPage;

	//private int pkId = 0;
	private String pkIds;
	private String keyword_title;
	private String keyword_teacher;

	private boolean isCourseList = true;

	private ProgressBar mWaiting;


	private static CourseListFragment self = new CourseListFragment();
	public CourseListFragment() {}

	public static CourseListFragment getInstance() {
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
		if(bundle != null) {
			pkIds = bundle.getString(EXTRA_KEY_PKID);
			keyword_title = bundle.getString(EXTRA_KEY_TITLE);
			keyword_teacher = bundle.getString(EXTRA_KEY_TEACHER);
		}
		mCurrentPage = 1;

		if(!TextUtils.isEmpty(pkIds)) {//if(pkId != 0) {

			keyword_teacher = "";
			keyword_title = "";
			isCourseList = true;

		} else {

			if(!TextUtils.isEmpty(keyword_title)) {
				isCourseList = true;
			} else {
				isCourseList = false;
			}

		}

		refreshCourseCenter();

	}

	private XListView.IXListViewListener mXlistViewListener = new XListView.IXListViewListener() {

		@Override
		public void onRefresh() {

			if(isCourseList) {
				refreshCourseCenter();
			} else {
				refreshTeacherList();
			}

		}

		@Override
		public void onLoadMore() {

			if(isCourseList) {
				loadMoreCourseCenter( ++ mCurrentPage);
			} else {
				loadMoreTeacher(++ mCurrentPage);
			}

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

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseList_2(pkIds, keyword_title, keyword_teacher, "360*270", 1, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseCenter = new CourseParser<CourseCenter>(new CourseCenter()).parse(response);

						if(mAdapter == null) {
							mAdapter = new CourseListPageAdapter(getActivity(), mCourseCenter.dataobject);
							mListView.setAdapter(mAdapter);
						} else {
							mAdapter.setDataList(mCourseCenter.dataobject);
						}

						mAdapter.notifyDataSetChanged();
						mListView.stopRefresh();

						mListView.setOnItemClickListener(onItemClickListener);

						mCurrentPage = 1;

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

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseList_2(pkIds, keyword_title, "", "360*270", pageNum, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mCourseCenter = new CourseParser<CourseCenter>(new CourseCenter()).parse(response);

						if(mAdapter == null) {
							mAdapter = new CourseListPageAdapter(getActivity(), mCourseCenter.dataobject);
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

	private void refreshTeacherList() {

		mWaiting.setVisibility(View.VISIBLE);

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getURLSt(keyword_teacher, "360*270", 1, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						Log.i("response : ", response.toString());

						mTeacherEntity = new TeacherParser().parse(response);

						if(mTeacherListAdapter == null) {
							mTeacherListAdapter = new TeacherSearchListAdapter(getActivity(), mTeacherEntity.dataobject);
							mListView.setAdapter(mTeacherListAdapter);
						} else {
							mTeacherListAdapter.setDataList(mTeacherEntity.dataobject);
						}

						mTeacherListAdapter.notifyDataSetChanged();
						mListView.stopRefresh();

						mListView.setOnItemClickListener(onTeacherItemClickListener);

						mCurrentPage = 1;

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

	private void loadMoreTeacher(int pageNum) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getURLSt(keyword_teacher, "720*720", pageNum, 10), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						mTeacherEntity = new TeacherParser().parse(response);

						if(mTeacherListAdapter == null) {
							mTeacherListAdapter = new TeacherSearchListAdapter(getActivity(), mTeacherEntity.dataobject);
							mListView.setAdapter(mTeacherListAdapter);
						} else {
							mTeacherListAdapter.setDataList(mTeacherEntity.dataobject);
						}

						mTeacherListAdapter.notifyDataSetChanged();

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

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			Intent intent = null;

			if(StaticConfigs.mLoginResult == null || StaticConfigs.mLoginResult.accessToken == null) {

				intent = new Intent(mContext, LoginActivity.class);
				getActivity().startActivityForResult(intent, StaticConfigs.REQUEST_CODE_LOGIN);
				return;
			}

			CourseListPageAdapter.CellHolder holder = (CourseListPageAdapter.CellHolder) view.getTag();
			String courseId = holder.c.courseid;
			intent = new Intent(mContext, CourseIntroduceActivity.class);
			intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_CID, courseId);
			startActivity(intent);

		}
	};

	private AdapterView.OnItemClickListener onTeacherItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			TeacherSearchListAdapter.CellHolder holder = (TeacherSearchListAdapter.CellHolder) view.getTag();
			Teacher t = holder.t;
			Intent intent = new Intent(mContext, TeacherIntroActivity.class);
			intent.putExtra(TeacherIntroActivity.EXTRA_TEACHER_KEY, t);
			startActivity(intent);

		}
	};


}

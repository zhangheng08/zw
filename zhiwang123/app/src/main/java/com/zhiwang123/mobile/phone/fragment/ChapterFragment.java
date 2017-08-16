package com.zhiwang123.mobile.phone.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.VideoHintDialogActivity;
import com.zhiwang123.mobile.phone.activity.VideoPlayActivity;
import com.zhiwang123.mobile.phone.adapter.CourseCenterCubCAdapter;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.service.ZWDownLoaderService;

import java.util.ArrayList;

/**
 * @author wz
 */
public class ChapterFragment extends BaseFragment {

	public static final String TAG = "ChapterFragment";
	public static final String EXTA_KEY_BUYCOURSEID = "ChapterFragment_buy_course_id";

	private View mRootView;

	private ListView mListView;

	ArrayList<Course> mDataliist;

	private boolean isOrganPlay;

	private CourseCenterCubCAdapter mAdapter;

	private VideoPlayActivity mActivity;

	private ProgressBar mProgress;

	private static ChapterFragment self = new ChapterFragment();

	public ChapterFragment() {
		super();
		name = TAG;
	}

	public static ChapterFragment getInstance() {
		return self;
	}
	
	@Override
	public void onAttach(Context context) {

		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.chapter_fragment_page, null);
		initViews();
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		IntentFilter filter = new IntentFilter();
		filter.addAction(ZWDownLoaderService.BROADCAST_REFRESH_ACTION);
		getActivity().registerReceiver(refreshReceiver, filter);

	}


	public void initPageList(Activity activity, ArrayList<Course> dataList, boolean isOrganPlay, String cover) {

		mProgress.setVisibility(View.GONE);

		mActivity = (VideoPlayActivity) activity;

		mDataliist = dataList;

		if(mDataliist != null && mDataliist.size() != 0) {

			mActivity.videoName = mDataliist.get(0).courseName;
			mActivity.videoId = mDataliist.get(0).ccKey;

		}

		this.isOrganPlay = isOrganPlay;

		mAdapter = new CourseCenterCubCAdapter(activity, mDataliist);
		mAdapter.picUrl = cover;

		mListView.setAdapter(mAdapter);

		mAdapter.notifyDataSetChanged();

		mListView.setOnItemClickListener(onItemClickListener);

	}

	@Override
	protected void initViews() {

		mListView = (ListView) mRootView.findViewById(R.id.chapter_course_list);
		mProgress = (ProgressBar) mRootView.findViewById(R.id.chapter_course_pro);

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
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {

		getActivity().unregisterReceiver(refreshReceiver);
		mActivity = null;

		super.onDestroy();
	}

	private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if(mDataliist == null || mDataliist.size() == 0) return;

			String finishedVid = intent.getStringExtra(ZWDownLoaderService.VID);

			for(Course c : mDataliist) {

				if(c.downloadState != Course.IN_LOCAL && finishedVid.equals(c.ccKey)) {
					c.downloadState = Course.IN_LOCAL;
					break;
				}

			}

			mAdapter.notifyDataSetChanged();
		}
	};

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			mActivity.currentPlayIndex = position;
			Course c = mDataliist.get(position);

			if(isOrganPlay) {

				mActivity.initPlayInfo(c.ccKey, c.courseName);
				return;

			}

			if(c.status == 0) {

				mActivity.videoName = c.courseName;
				mActivity.videoId = c.ccKey;

				Intent intent = new Intent(getActivity(), VideoHintDialogActivity.class);
				intent.putExtra(VideoHintDialogActivity.KEY_CARD_ID, c.cardId);
				getActivity().startActivityForResult(intent, 0);
				getActivity().overridePendingTransition(R.anim.translate_in_zw_v, 0);

			} else if(c.status == 1) {

				mActivity.initPlayInfo(c.ccKey, c.courseName);

			} else if(c.status == 2) {

				Toast.makeText(mActivity, "课程已经过期", Toast.LENGTH_SHORT).show();

			}

		}
	};



}

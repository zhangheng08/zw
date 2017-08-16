package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.OrderActivity;
import com.zhiwang123.mobile.phone.adapter.OrderPageAdapter;
import com.zhiwang123.mobile.phone.bean.Order;
import com.zhiwang123.mobile.phone.utils.MethodUtils;

import java.util.ArrayList;

/**
 * @author wz
 */
public class OrderPageFragment extends BaseFragment {

	public static final String TAG = "RegisterFragment";
	public static final String EXTA_KEY_BUYCOURSEID = "ChapterFragment_buy_course_id";

	private View mRootView;

	private ListView mListView;

	private ArrayList<Order> mDatalist;

	private OrderPageAdapter mAdapter;

	private OrderActivity mActivity;

	private static OrderPageFragment self = new OrderPageFragment();

	public OrderPageFragment() {
		super();
		name = TAG;
	}

	public static OrderPageFragment getInstance() {
		return self;
	}
	
	@Override
	public void onAttach(Context context) {

		super.onAttach(context);
	}

	@Override
	public void onDetach() {

		super.onDetach();

	}

	public void setPageList(ArrayList<Order> list, OrderActivity activity) {

		mActivity = activity;
		mDatalist = list;
		if(mAdapter == null) {

			mAdapter = new OrderPageAdapter(mActivity, mDatalist, MethodUtils.getDip(mActivity));
			mListView.setAdapter(mAdapter);

		} else {

			mAdapter.setDataList(list);

		}

		mListView.setOnItemClickListener(onItemClickListener);
		mAdapter.notifyDataSetChanged();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.order_fragment_page, null);

		initViews();

		return mRootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}


	@Override
	protected void initViews() {

		mListView = (ListView) mRootView.findViewById(R.id.order_type_list);

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

		mActivity = null;

		super.onDestroy();
	}


	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



		}
	};



}

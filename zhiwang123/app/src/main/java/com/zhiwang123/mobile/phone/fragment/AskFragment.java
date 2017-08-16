package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiwang123.mobile.R;

/**
 * @author wz
 */
public class AskFragment extends BaseFragment {

	public static final String TAG = "RegisterFragment";

	private View mRootView;

	private static AskFragment self = new AskFragment();

	public AskFragment() {
		super();
		name = TAG;
	}

	public static AskFragment getInstance() {
		return self;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();

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
		mRootView = inflater.inflate(R.layout.me_fragment_layout, null);
		initViews();
		return mRootView;
	}

	@Override
	protected void initViews() {


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

}

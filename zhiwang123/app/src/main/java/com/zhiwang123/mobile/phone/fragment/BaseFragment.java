package com.zhiwang123.mobile.phone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseFragment extends Fragment {

	private static final String TAG = "BaseFragment";

	protected String name;

	protected RequestQueue mVolleyRequestQueue;

	public BaseFragment() {
		name = TAG;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mVolleyRequestQueue = Volley.newRequestQueue(getActivity());
	}

	protected void initViews() {

	}

	public String getName() {
		return name;
	}

	public boolean backPressed() {

		return true;

	}

	
}

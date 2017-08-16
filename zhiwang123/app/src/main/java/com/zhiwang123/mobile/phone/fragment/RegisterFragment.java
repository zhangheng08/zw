package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.utils.MethodUtils;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wz
 */
public class RegisterFragment extends BaseFragment {

	public static final String TAG = "RegisterFragment";

	private View mRootView;
	private EditText mPhoneEdt;
	private EditText mCodeEdt;
	private EditText mPasswordEdt;
	private EditText mRePasswordEdt;
	private View mRegisterBtn;
	private TextView mGetCodeBtn;

	private Drawable mErrorDrawable;

	private Timer mSmsCodeTimer;
	private MyTimerTask myTimerTask;
	private int timerCount;

	public Boolean isForgetPass = false;

	private static RegisterFragment self = new RegisterFragment();

	public RegisterFragment() {
		super();
		name = TAG;
	}

	public static RegisterFragment getInstance() {
		return self;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		mRootView = inflater.inflate(R.layout.register_fragment_layout, null);
		initViews();

		mErrorDrawable = getResources().getDrawable(R.drawable.input_error);
		mErrorDrawable.setBounds(new Rect(0, 0, mErrorDrawable.getIntrinsicWidth()/2 , mErrorDrawable.getIntrinsicHeight() / 2));

		return mRootView;
	}

	@Override
	protected void initViews() {

		mPhoneEdt = (EditText) mRootView.findViewById(R.id.register_phone_input);
		mPasswordEdt = (EditText) mRootView.findViewById(R.id.register_pass_input);
		mRePasswordEdt = (EditText) mRootView.findViewById(R.id.register_pass_reinput);
		mCodeEdt = (EditText) mRootView.findViewById(R.id.register_code_input);
		mRegisterBtn = mRootView.findViewById(R.id.register_btn);
		mGetCodeBtn = (TextView) mRootView.findViewById(R.id.register_code_btn);

		mRegisterBtn.setOnClickListener(mOnClickListener);
		mGetCodeBtn.setOnClickListener(mOnClickListener);

	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch(v.getId()) {
				case R.id.register_btn:

					if (TextUtils.isEmpty(mPhoneEdt.getText())) {
						mPhoneEdt.requestFocus();
						mPhoneEdt.setError(getActivity().getResources().getString(R.string.phone_empty_error), mErrorDrawable);
						return;
					}

					if(!MethodUtils.checkPhoneNum(mPhoneEdt.getText().toString())) {
						mPhoneEdt.requestFocus();
						mPhoneEdt.setError(getActivity().getResources().getString(R.string.phone_num_format_error), mErrorDrawable);
						return;
					}

					if (TextUtils.isEmpty(mCodeEdt.getText())) {
						mCodeEdt.requestFocus();
						mCodeEdt.setError(getActivity().getResources().getString(R.string.code_empty_error), mErrorDrawable);
						return;
					}

					if (TextUtils.isEmpty(mPasswordEdt.getText())) {
						mPasswordEdt.requestFocus();
						mPasswordEdt.setError(getActivity().getResources().getString(R.string.password_empty_error), mErrorDrawable);
						return;
					}

					if (TextUtils.isEmpty(mRePasswordEdt.getText())) {
						mRePasswordEdt.requestFocus();
						mRePasswordEdt.setError(getActivity().getResources().getString(R.string.repassword_empty_error), mErrorDrawable);
						return;
					}

					if(!mPasswordEdt.getText().toString().equals(mRePasswordEdt.getText().toString())) {
						mRePasswordEdt.requestFocus();
						mRePasswordEdt.setError(getActivity().getResources().getString(R.string.password_not_thesame_error), mErrorDrawable);
						return;
					}

					toRegistPhone(mPhoneEdt.getText().toString(), mCodeEdt.getText().toString(), mPasswordEdt.getText().toString());

					break;
				case R.id.register_code_btn:

					if(mSmsCodeTimer != null || myTimerTask != null) {
						break;
					}

					if (TextUtils.isEmpty(mPhoneEdt.getText())) {
						mPhoneEdt.requestFocus();
						mPhoneEdt.setError(getActivity().getResources().getString(R.string.phone_empty_error), mErrorDrawable);
						return;
					}

					if(!MethodUtils.checkPhoneNum(mPhoneEdt.getText().toString())) {
						mPhoneEdt.requestFocus();
						mPhoneEdt.setError(getActivity().getResources().getString(R.string.phone_num_format_error), mErrorDrawable);
						return;
					}

					toLoginPB(mPhoneEdt.getText().toString());

					break;
			}
		}
	};

	public void toLoginPB(final String phoneNum) {

		Map<String, String> kvs = new HashMap<String, String>();
		kvs.put("Phone", phoneNum);
		JSONObject params = new JSONObject(kvs);

		JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlSendCode(), params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				boolean state = false;

				try {

					if(response.has("State")) state = response.getBoolean("State");

					if(state) {

						Toast.makeText(getActivity(), R.string.register_sms_sending, Toast.LENGTH_LONG).show();

						timerCount = 60;
						mSmsCodeTimer = new Timer();
						myTimerTask = new MyTimerTask();
						mSmsCodeTimer.schedule(myTimerTask, 0, 1000);

					}

				} catch(JSONException e) {

				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});

		mVolleyRequestQueue.add(jreq);

	}


	public void toRegistPhone(final String phoneNum, final String code, final String password) {

		String key = "";
		String url = "";

		if(isForgetPass) {

			url = StaticConfigs.getUrlForgetPhone();
			key = "NewPassword";

		} else {

			url = StaticConfigs.getUrlRegistPnone();
			key = "Password";

		}

		Map<String, String> kvs = new HashMap<String, String>();
		kvs.put("Phone", phoneNum);
		kvs.put(key, password);
		kvs.put("Code", code);

		JSONObject params = new JSONObject(kvs);

		JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				boolean state = false;
				String message = "";

				try {

					if(response.has("State")) state = response.getBoolean("State");
					if(response.has("Message")) message = response.getString("Message");

					if(state) {

						Toast.makeText(getActivity(), R.string.register_result_success, Toast.LENGTH_LONG).show();

					} else {

						Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

					}

				} catch(JSONException e) {

				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});

		mVolleyRequestQueue.add(jreq);

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		if(isForgetPass) {
			mPasswordEdt.setHint("新密码");
		} else {
			mPasswordEdt.setHint("设置密码");
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {

			timerCount --;
			mUIHandler.sendEmptyMessage(0);

		}
	};

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			mGetCodeBtn.setText("(" + timerCount + ")s 后重新发送");
			if(timerCount == 0) {

				mSmsCodeTimer.cancel();
				mGetCodeBtn.setText("获取验证码");

				myTimerTask.cancel();
				mSmsCodeTimer.cancel();

				myTimerTask = null;
				mSmsCodeTimer = null;

			}

		}
	};

}

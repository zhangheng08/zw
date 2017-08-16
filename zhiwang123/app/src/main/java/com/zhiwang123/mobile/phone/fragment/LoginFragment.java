package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.zhiwang123.mobile.phone.activity.LoginActivity;
import com.zhiwang123.mobile.phone.activity.OrganDialogActivity;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.parser.LoginParser;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wz
 */
public class LoginFragment extends BaseFragment {

	public static final String TAG = "LoginFragment";

	public static final int REQUEST_CODE_ORGAN_KEY = 0x1;
	public static final int RESULT_CODE_ORGAN_KEY = 0x2;
	public static final String RESULT_KEY = "organ_key";
	public static final String RESULT_NAME = "organ_name";

	private Context mContext;
	private View mRootView;
	private EditText mNameEdt;
	private EditText mPasswordEdt;
	private View mELoginSelectBtn;
	private View mLoginBtn;
	private View mWXLoginBtn;
	private View mQQLoginBtn;
	private View mClearOrganBtn;

	private View mFindPassword;

	private TextView mOrganInfoTxt;

	private Drawable mErrorDrawable;

	private LoginResult mLoginResult;

	private String mOrganKey;
	private String mOrganName;

	private int loginMode = StaticConfigs.LOGIN_MODE_PB;

	private static LoginFragment self = new LoginFragment();


	public LoginFragment() {
		super();
		name = TAG;
	}

	public static LoginFragment getInstance() {
		return self;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		mContext = getActivity().getApplicationContext();
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
		mRootView = inflater.inflate(R.layout.login_fragment_layout, null);
		initViews();

		mErrorDrawable = getResources().getDrawable(R.drawable.input_error);
		mErrorDrawable.setBounds(new Rect(0, 0, mErrorDrawable.getIntrinsicWidth()/2 , mErrorDrawable.getIntrinsicHeight() / 2));

		return mRootView;
	}

	@Override
	protected void initViews() {

		mNameEdt = (EditText) mRootView.findViewById(R.id.login_name_input);
		mPasswordEdt = (EditText) mRootView.findViewById(R.id.login_pass_input);
		mLoginBtn = mRootView.findViewById(R.id.login_btn);
		mWXLoginBtn = mRootView.findViewById(R.id.login_wx);
		mQQLoginBtn = mRootView.findViewById(R.id.login_qq);
		mELoginSelectBtn = mRootView.findViewById(R.id.login_e);
		mOrganInfoTxt = (TextView) mRootView.findViewById(R.id.login_organ_text);
		mClearOrganBtn = mRootView.findViewById(R.id.login_clear_organ_btn);
		mFindPassword = mRootView.findViewById(R.id.login_forget_pw);

		mLoginBtn.setOnClickListener(mOnClickListener);
		mWXLoginBtn.setOnClickListener(mOnClickListener);
		mQQLoginBtn.setOnClickListener(mOnClickListener);
		mELoginSelectBtn.setOnClickListener(mOnClickListener);
		mClearOrganBtn.setOnClickListener(mOnClickListener);
		mFindPassword.setOnClickListener(mOnClickListener);

	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch(v.getId()) {

				case R.id.login_btn:

					if (TextUtils.isEmpty(mNameEdt.getText())) {
						mNameEdt.requestFocus();
						mNameEdt.setError(getActivity().getResources().getString(R.string.name_empty_error), mErrorDrawable);
						return;
					}

					if (TextUtils.isEmpty(mPasswordEdt.getText())) {
						mPasswordEdt.requestFocus();
						mPasswordEdt.setError(getActivity().getResources().getString(R.string.password_empty_error), mErrorDrawable);
						return;
					}

					if(loginMode == StaticConfigs.LOGIN_MODE_PB) toLoginPB(mNameEdt.getText().toString(), mPasswordEdt.getText().toString());
					else if(loginMode == StaticConfigs.LOGIN_MODE_E) toLoginOrgan(mOrganKey, mNameEdt.getText().toString(), mPasswordEdt.getText().toString());

					break;
				case R.id.login_wx:
					((LoginActivity) getActivity()).wxSignIn();
					getActivity().finish();
					break;

				case R.id.login_qq:
					((LoginActivity) getActivity()).qqSignIn();
					break;

				case R.id.login_e:
					Intent intent = new Intent(mContext, OrganDialogActivity.class);
					startActivityForResult(intent, REQUEST_CODE_ORGAN_KEY);
//					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(R.anim.translate_in_zw_v, 0);
					break;

				case R.id.login_clear_organ_btn:

					mOrganKey = null;
					mOrganInfoTxt.setText("选择机构");
					mClearOrganBtn.setVisibility(View.GONE);
					loginMode = StaticConfigs.LOGIN_MODE_PB;

					break;

				case R.id.login_forget_pw:

					((LoginActivity) getActivity()).forgetPassword();

					break;

			}
		}
	};

	public void toLoginPB(final String userName, String password) {

		Map<String, String> kvs = new HashMap<String, String>();
		kvs.put("UserName", userName);
		kvs.put("Password", password);
		JSONObject params = new JSONObject(kvs);

		JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlLogin(), params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				Log.d(TAG, response.toString());
				LoginResult lr = new LoginParser<LoginResult>(new LoginResult()).parse(response);
				lr.account = userName;
				lr.loginMode = StaticConfigs.LOGIN_MODE_PB;

				if(lr.state) {
					mLoginResult = lr;
					fetchUserInfo(lr.accessToken);
				} else {
					Toast.makeText(mContext, ellipsizeMessage(lr.message), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error.getMessage());
				Log.e(TAG, error.getMessage());
				String errorInfo = ellipsizeMessage(error.getMessage());
				Toast.makeText(mContext, errorInfo, Toast.LENGTH_LONG).show();
			}
		});

		mVolleyRequestQueue.add(jreq);

	}

	public void toLoginOrgan(final String organkey, final String userName, String password) {

		Map<String, String> kvs = new HashMap<String, String>();
		kvs.put("Key", organkey);
		kvs.put("UserName", userName);
		kvs.put("Password", password);
		JSONObject params = new JSONObject(kvs);

		JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlELogin(), params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				Log.d(TAG, response.toString());
				LoginResult lr = new LoginParser<LoginResult>(new LoginResult()).parse(response);
				lr.account = userName;
				lr.loginMode = StaticConfigs.LOGIN_MODE_E;

				if(lr.state) {
					mLoginResult = lr;
					fetchUserInfo(lr.accessToken);
				} else {
					Toast.makeText(mContext, ellipsizeMessage(lr.message), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error.getMessage());
				Log.e(TAG, error.getMessage());
				String errorInfo = ellipsizeMessage(error.getMessage());
				Toast.makeText(mContext, errorInfo, Toast.LENGTH_LONG).show();
			}
		});

		mVolleyRequestQueue.add(jreq);

	}


	public void fetchUserInfo(final String token) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlUseInfo(token), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						LoginResult lr =  new LoginParser<LoginResult>(new LoginResult()).parse(response);
						mLoginResult.dataobject = lr.dataobject;
						AsyncDBHelper.saveLoginInfoDB(mContext, mLoginResult, uiHandler);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == REQUEST_CODE_ORGAN_KEY) {

			switch (resultCode) {

				case RESULT_CODE_ORGAN_KEY:

					mOrganKey = data.getStringExtra(RESULT_KEY);
					mOrganName = data.getStringExtra(RESULT_NAME);

					if(!TextUtils.isEmpty(mOrganKey)) {

						loginMode = StaticConfigs.LOGIN_MODE_E;

						mOrganInfoTxt.setText("已选机构：" + mOrganName);
						mClearOrganBtn.setVisibility(View.VISIBLE);

					} else {

						loginMode = StaticConfigs.LOGIN_MODE_PB;

					}

					break;

			}

		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	private String ellipsizeMessage(String message) {

		if(TextUtils.isEmpty(message)) return "";

		return message.length() <= 10 ? message : message.substring(0, 9) + "...";

	}

	private AsyncDBHelper.UIHandler<LoginResult> uiHandler = new AsyncDBHelper.UIHandler<LoginResult>() {

		@Override
		public void processMessage(int what, LoginResult loginResult) {
			switch(what) {

				case SEND_SAVE_USERINFO_OK:

					Intent data = new Intent();
					data.putExtra(StaticConfigs.RESULT_CODE_LOGIN_KEY, loginResult);
					getActivity().setResult(StaticConfigs.RESULT_CODE_LOGIN_OK, data);
					getActivity().finish();

					break;
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
	
	@Override
	public void onPause() {
		super.onPause();
	}

}

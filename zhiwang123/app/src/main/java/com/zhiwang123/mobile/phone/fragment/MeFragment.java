
package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.AboutActivity;
import com.zhiwang123.mobile.phone.activity.CartActivity;
import com.zhiwang123.mobile.phone.activity.CourseListActivity;
import com.zhiwang123.mobile.phone.activity.EditUserInfoActivity;
import com.zhiwang123.mobile.phone.activity.ExamEListActivity;
import com.zhiwang123.mobile.phone.activity.LocalVideoListActivity;
import com.zhiwang123.mobile.phone.activity.LoginActivity;
import com.zhiwang123.mobile.phone.activity.OrderActivity;
import com.zhiwang123.mobile.phone.activity.TeachPlanActivity;
import com.zhiwang123.mobile.phone.activity.TopicWebPageActivity;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.ResultEntity;
import com.zhiwang123.mobile.phone.bean.parser.LoginParser;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author wz
 */
public class MeFragment extends BaseFragment {

	public static final String TAG = "RegisterFragment";

	private View mRootView;
	private TextView mToLoginTx;
	private TextView mToEdtTx;
	private TextView mToCourseCenterTx;
	private TextView mToExamCenterTxt;
	private TextView mToCacheCoursesTx;
	private TextView mToFavoriteTx;
	private TextView mToCartTx;
	private TextView mToOrderTx;
	private TextView mToAsk;
	private TextView mToHotline;
	private TextView mHotNumTx;
	private TextView mTokickUs;
	private TextView mToFeedBack;
	private TextView mToSettings;

	private View mToLoginView;

	private ImageView avatarImgv;

	private Context mContext;

	private View mStockLine1;
	private View mSepLine1;
	private View mStockLine2;
	private View mSepLine2;
	private View mStockLine3;
	private View mSepLine3;

	private View mSignOutBtn;

	private boolean isViewInited;

	private LoginResult mLoginInfo;

	private static MeFragment self = new MeFragment();

	public MeFragment() {
		super();
		name = TAG;
	}

	public static MeFragment getInstance() {
		return self;
	}

	@Override
	public void onAttach(Context context) {
		mContext = context;
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.me_fragment_layout, null);
		initViews();
		return mRootView;
	}

	@Override
	protected void initViews() {

		mToCourseCenterTx = (TextView) mRootView.findViewById(R.id.me_to_course_center);
		mToExamCenterTxt = (TextView) mRootView.findViewById(R.id.me_to_exam_center);
		mToLoginTx = (TextView) mRootView.findViewById(R.id.me_to_login_btn);
		mToEdtTx = (TextView) mRootView.findViewById(R.id.me_to_edit_btn);
		mToCacheCoursesTx = (TextView) mRootView.findViewById(R.id.me_to_cache_course);
		mToLoginView = mRootView.findViewById(R.id.me_to_login_btn_layer);

		mStockLine1 = mRootView.findViewById(R.id.me_stock_line_1);
		mStockLine2 = mRootView.findViewById(R.id.me_stock_line_2);
		mSepLine1 = mRootView.findViewById(R.id.me_sep_line1);
		mSepLine2 = mRootView.findViewById(R.id.me_sep_line2);

		mToFavoriteTx = (TextView) mRootView.findViewById(R.id.me_to_favorite);
		mToCartTx = (TextView) mRootView.findViewById(R.id.me_to_cart);
		mToOrderTx = (TextView) mRootView.findViewById(R.id.me_to_my_order);
		mToAsk = (TextView) mRootView.findViewById(R.id.me_to_ask);
		mToHotline = (TextView) mRootView.findViewById(R.id.me_to_hotline);
		mHotNumTx = (TextView) mRootView.findViewById(R.id.me_to_hotnumber);
		mTokickUs = (TextView) mRootView.findViewById(R.id.me_to_say);
		mToFeedBack = (TextView) mRootView.findViewById(R.id.me_to_feedback);
		mToSettings = (TextView) mRootView.findViewById(R.id.me_to_settings);

		avatarImgv = (ImageView) mRootView.findViewById(R.id.me_avatar_img);


//		mToCourseCenterTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToExamCenterTxt.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToLoginTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToEdtTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToCacheCoursesTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToFavoriteTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToCartTx .setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToOrderTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToAsk.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToHotline.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mHotNumTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mTokickUs.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToFeedBack .setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//		mToSettings.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

		mSignOutBtn = mRootView.findViewById(R.id.me_to_signout_btn);
//		((TextView)mSignOutBtn.findViewById(R.id.me_to_signout_tx)).setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

		mToLoginTx.setOnClickListener(onClickListener);
		mToCourseCenterTx.setOnClickListener(onClickListener);
		mToCacheCoursesTx.setOnClickListener(onClickListener);
		mSignOutBtn.setOnClickListener(onClickListener);
		mToExamCenterTxt.setOnClickListener(onClickListener);
		mToFavoriteTx.setOnClickListener(onClickListener);
		mToCartTx.setOnClickListener(onClickListener);
		mToOrderTx.setOnClickListener(onClickListener);
		mToEdtTx.setOnClickListener(onClickListener);
		mToAsk.setOnClickListener(onClickListener);
		mToSettings.setOnClickListener(onClickListener);
		mToLoginView.setOnClickListener(onClickListener);

		isViewInited = true;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		mContext = getActivity().getApplicationContext();
		if(mLoginInfo != null) resetUI();
	}

	public void setLoginInfo(LoginResult loginResult) {
		mLoginInfo = loginResult;
	}

	public void resetUI() {

		if(mLoginInfo != null && isViewInited) {

			mToLoginTx.setText(mLoginInfo.dataobject.get(0).name);

			Glide.with(getActivity())
					.load(mLoginInfo.dataobject.get(0).avatar)
					.placeholder(R.drawable.default_avatar)
					.bitmapTransform(new CropCircleTransformation(getActivity()))
					.crossFade().into(avatarImgv);

			mToLoginTx.setClickable(false);
			mToEdtTx.setVisibility(View.VISIBLE);
			mToLoginView.setVisibility(View.GONE);

			switch(mLoginInfo.loginMode) {

				case StaticConfigs.LOGIN_MODE_PB:
				case StaticConfigs.LOGIN_MODE_WX:
				case StaticConfigs.LOGIN_MODE_QQ:
					mStockLine1.setVisibility(View.VISIBLE);
					mStockLine2.setVisibility(View.VISIBLE);
					mToExamCenterTxt.setVisibility(View.GONE);
					mSepLine1.setVisibility(View.VISIBLE);
					mSepLine2.setVisibility(View.VISIBLE);
					mSignOutBtn.setVisibility(View.VISIBLE);
					break;

				case StaticConfigs.LOGIN_MODE_E:
					mStockLine1.setVisibility(View.VISIBLE);
					mStockLine2.setVisibility(View.VISIBLE);
					mToExamCenterTxt.setVisibility(View.VISIBLE);
					mSepLine1.setVisibility(View.VISIBLE);
					mSepLine2.setVisibility(View.VISIBLE);
					mSignOutBtn.setVisibility(View.VISIBLE);
					break;

			}


		} else {

			mToLoginTx.setText(R.string.me_to_login_tx);
			mToLoginTx.setClickable(true);
			mToLoginView.setVisibility(View.VISIBLE);
			mToEdtTx.setVisibility(View.GONE);
			mStockLine1.setVisibility(View.GONE);
			mStockLine2.setVisibility(View.GONE);
			mSepLine1.setVisibility(View.GONE);
			mSepLine2.setVisibility(View.GONE);
			mSignOutBtn.setVisibility(View.GONE);
			Glide.with(getActivity())
					.load("")
					.placeholder(R.drawable.default_avatar)
					.bitmapTransform(new CropCircleTransformation(getActivity()))
					.crossFade().into(avatarImgv);

		}

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
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = null;

			switch (v.getId()) {
				case R.id.me_to_edit_btn:

					intent = new Intent(mContext, EditUserInfoActivity.class);

					intent.putExtra(EditUserInfoActivity.KEY_AV, mLoginInfo.dataobject.get(0).avatar);
					intent.putExtra(EditUserInfoActivity.KEY_NM, mLoginInfo.dataobject.get(0).name);

					MeFragment.this.startActivityForResult(intent, 0x1);

					break;
				case R.id.me_to_course_center:

					if(mLoginInfo.dataobject.get(0).isOrganRole) {

						intent = new Intent(mContext, TeachPlanActivity.class);

					} else {

						intent = new Intent(mContext, CourseListActivity.class);
						intent.putExtra(CourseListActivity.EXTRA_SWITCH_FRAGMENT, CourseListActivity.FRAGMENT_COURSE_CENTER);

					}

					startActivity(intent);

					break;
				case R.id.me_to_login_btn:
				case R.id.me_to_login_btn_layer:

					intent = new Intent(mContext, LoginActivity.class);
					getActivity().startActivityForResult(intent, StaticConfigs.REQUEST_CODE_LOGIN);

					break;
				case R.id.me_to_signout_btn:

					AsyncDBHelper.deleteUserInfoDB(mContext, uiHandler);

					break;
				case R.id.me_to_cache_course:

					intent = new Intent(mContext, LocalVideoListActivity.class);
					startActivity(intent);

					break;
				case R.id.me_to_exam_center:

					if(mLoginInfo.dataobject.get(0).isOrganRole) {

						intent = new Intent(mContext, ExamEListActivity.class);

					} else {



					}

					startActivity(intent);

					break;
				case R.id.me_to_favorite:

					intent = new Intent(mContext, CourseListActivity.class);
					intent.putExtra(CourseListActivity.EXTRA_SWITCH_FRAGMENT, CourseListActivity.FRAGMENT_COURSE_FAVORITE);
					startActivity(intent);

					break;
				case R.id.me_to_cart:

					intent = new Intent(mContext, CartActivity.class);
					startActivity(intent);

					break;
				case R.id.me_to_my_order:

					intent = new Intent(mContext, OrderActivity.class);
					startActivity(intent);

					break;
				case R.id.me_to_ask:

					Intent adviceIntent = new Intent(mContext, TopicWebPageActivity.class);
					adviceIntent.putExtra(TopicWebPageActivity.KEY_URL, StaticConfigs.URL_ADVICE);
					adviceIntent.putExtra(TopicWebPageActivity.KEY_NAME, "咨询");
					startActivity(adviceIntent);

					break;

				case R.id.me_to_settings:

					Intent settingIntent = new Intent(mContext, AboutActivity.class);
					startActivity(settingIntent);

					break;
			}

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(requestCode) {

			case 0x1:

				if(data != null) {

//					String name = data.getStringExtra(EditUserInfoActivity.KEY_NM);
//					String avat = data.getStringExtra(EditUserInfoActivity.KEY_AV);
//
//					mToLoginTx.setText(name);
//
//					Glide.with(mContext)
//							.load(avat)
//							.placeholder(R.drawable.default_avatar)
//							.bitmapTransform(new CropCircleTransformation(mContext))
//							.crossFade()
//							.into(avatarImgv);

					fetchUserInfo(StaticConfigs.mLoginResult.accessToken);

				}

				break;


		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private AsyncDBHelper.UIHandler uiHandler = new AsyncDBHelper.UIHandler<ResultEntity>() {
		@Override
		public void processMessage(int what, ResultEntity result) {
			if(AsyncDBHelper.UIHandler.SEND_DELETE_USER_RESULT == what) {

				mLoginInfo = null;
				StaticConfigs.mLoginResult = null;
				resetUI();

			} else if(AsyncDBHelper.UIHandler.SEND_SAVE_USERINFO_OK == what) {



			}
		}
	};

	public void fetchUserInfo(final String token) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlUseInfo(token), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						Log.i(TAG, response.toString());

						LoginResult lr =  new LoginParser<LoginResult>(new LoginResult()).parse(response);
						StaticConfigs.mLoginResult.dataobject = lr.dataobject;
						mLoginInfo = StaticConfigs.mLoginResult;
						resetUI();

						AsyncDBHelper.saveLoginInfoDB(mContext, StaticConfigs.mLoginResult, uiHandler);

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

}

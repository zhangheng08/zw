package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.ExamPaperActivity;
import com.zhiwang123.mobile.phone.adapter.ExamResListAdpater;
import com.zhiwang123.mobile.phone.bean.Block;
import com.zhiwang123.mobile.phone.bean.ExamPaper;
import com.zhiwang123.mobile.phone.bean.ExamResult;
import com.zhiwang123.mobile.phone.bean.QuestionZW;
import com.zhiwang123.mobile.phone.bean.parser.ExamParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author wz
 */
public class ExamResultFragment extends BaseFragment {

	public static final String TAG = "ExamResultFragment";

	public static final String KEY_EXAMID = "test_paper_id";
	public static final String KEY_BUYCID = "buy_course_id";
	public static final String KEY_TIMER = "time_str";


	private View mRootView;

	private TextView mScoreTv;
	private TextView mSpendTimeTv;
	private ListView mResList;
	private ExamResListAdpater mAdapter;


	private static ExamResultFragment self = new ExamResultFragment();

	public ExamResultFragment() {
		super();
		name = TAG;
	}

	public static ExamResultFragment getInstance() {
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
		mRootView = inflater.inflate(R.layout.exam_result_layout, null);
		initViews();
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();

		if(bundle != null) {

			if(StaticConfigs.LOGIN_MODE_PB == StaticConfigs.mLoginResult.loginMode) {

				String examId = bundle.getString(KEY_EXAMID);
				String buyCourseId = bundle.getString(KEY_BUYCID);

				mSpendTimeTv.setText(bundle.getString(KEY_TIMER, "00:00:00"));

				toFetchExamResult(StaticConfigs.mLoginResult.accessToken, examId, buyCourseId);

			} else if(StaticConfigs.LOGIN_MODE_E == StaticConfigs.mLoginResult.loginMode) {

				if(((ExamPaperActivity)getActivity()).mEtype == ExamPaperActivity.E_TYPE_EXAM_CENTER) {

					String examId = bundle.getString(KEY_EXAMID);
					toFetchExamResultE(StaticConfigs.mLoginResult.accessToken, examId);

				} else {

					toFetchExamResultInCourse();

				}

			}

		}

	}

	@Override
	protected void initViews() {

		mScoreTv = (TextView) mRootView.findViewById(R.id.exam_res_my_score);
		mSpendTimeTv = (TextView) mRootView.findViewById(R.id.exam_res_spend_time);
		mResList = (ListView) mRootView.findViewById(R.id.exam_res_list);

	}

	public void toFetchExamResult(final String token, final String testPaperId, final String buyCourseId) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCheckExamResult(token, testPaperId, buyCourseId), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						Log.i(TAG, response.toString());

						ExamResult er = new ExamParser<ExamResult>(new ExamResult()).parse(response);
						ExamPaper ep = er.dataobject.get(0);

						mScoreTv.setText(ep.userScore + "");

						ArrayList<QuestionZW> results = null;

						if(ep != null) {

							results = new ArrayList<QuestionZW>();

							for(Block b : ep.blocks) {

								if(b != null) {

									for(QuestionZW q : b.questions) {

										results.add(q);

									}

								}

							}

						}


						if(results != null && results.size() != 0) {

							mAdapter = new ExamResListAdpater(getActivity(), results);
							mResList.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();

						}

					}
				},
				new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println(error.getMessage() + "");
					}
				});

		mVolleyRequestQueue.add(jsonRequest);

	}

	public void toFetchExamResultE(String token, String testPaperId) {

		final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlExamResultE(token, testPaperId), null,

				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						Log.i(TAG, response.toString());

						ExamResult er = new ExamParser<ExamResult>(new ExamResult()).parse(response);

						if(er.dataobject == null || er.dataobject.size() == 0) return;

						ExamPaper ep = er.dataobject.get(0);

						mScoreTv.setText(ep.userScore + "");

					}
				},
				new Response.ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println(error.getMessage() + "");
					}
				});

		mVolleyRequestQueue.add(jsonRequest);

	}

	public void toFetchExamResultInCourse() {



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

package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.ExamPaperActivity;

/**
 * @author wz
 */
public class ExamGuideFragment extends BaseFragment {

	public static final String TAG = "ExamGuideFragment";
	public static final String KEY_T = "total_time";
	public static final String KEY_S = "total_score";
	public static final String KEY_QC = "question_count";
	public static final String KEY_PS = "pass_score";
	public static final String KEY_D = "description";

	private View mRootView;

	private TextView mTotalTimeTv;
	private TextView mTotalScoreTv;
	private TextView mTotalQuestionCountTv;
	private TextView mPassTv;
	private TextView mDescTv;
	private View mExamTodoBtn;

	private int mTotalTime;
	private int mTotalScore;
	private int mTotalQuestionCount;
	private int mPasScore;
	private String mDesc;

	private static ExamGuideFragment self = new ExamGuideFragment();

	public ExamGuideFragment() {
		super();
		name = TAG;
	}

	public static ExamGuideFragment getInstance() {
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
		mRootView = inflater.inflate(R.layout.exam_guide, null);
		initViews();
		return mRootView;
	}

	@Override
	protected void initViews() {

		mTotalTimeTv = (TextView) mRootView.findViewById(R.id.exam_guide_time);
		mTotalScoreTv = (TextView) mRootView.findViewById(R.id.exam_guide_score);
		mTotalQuestionCountTv = (TextView) mRootView.findViewById(R.id.exam_guide_count);
		mPassTv = (TextView) mRootView.findViewById(R.id.exam_guide_pass_score);
		mDescTv = (TextView) mRootView.findViewById(R.id.exam_guide_desc);
		mExamTodoBtn = mRootView.findViewById(R.id.exam_guide_to_do);

		mExamTodoBtn.setOnClickListener(onClickListener);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();

		if(bundle != null) {

			mTotalTime = bundle.getInt(KEY_T, 100);
			mTotalScore = bundle.getInt(KEY_S, 100);
			mTotalQuestionCount = bundle.getInt(KEY_QC, 100);
			mPasScore = bundle.getInt(KEY_PS, 60);
			mDesc = bundle.getString(KEY_D);

			mTotalTimeTv.setText(getActivity().getString(R.string.exam_guide_duration, mTotalTime));
			mTotalScoreTv.setText(getActivity().getString(R.string.exam_guide_total_score, mTotalScore));
			mTotalQuestionCountTv.setText(getActivity().getString(R.string.exam_guide_total_count, mTotalQuestionCount));
			mPassTv.setText(getActivity().getString(R.string.exam_guide_pass, mPasScore));
			mDescTv.setText(mDesc);

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

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {

				case R.id.exam_guide_to_do:

					if(getActivity() != null) {

						((ExamPaperActivity) getActivity()).choosePage(ExamPaperActivity.FRAGMETN_ANSWER_QUESTION);

					}

					break;
			}
		}
	};



}

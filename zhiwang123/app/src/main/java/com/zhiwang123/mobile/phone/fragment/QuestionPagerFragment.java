package com.zhiwang123.mobile.phone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ouchn.lib.adapter.AnswerCardAdapter;
import com.ouchn.lib.adapter.QuestionPagerAdapter;
import com.ouchn.lib.adapter.holder.QuestionViewItemHolder;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionOption;
import com.ouchn.lib.entity.QuestionType;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.BaseTask;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.ExamPaperActivity;
import com.zhiwang123.mobile.phone.bean.Block;
import com.zhiwang123.mobile.phone.bean.ExamPaper;
import com.zhiwang123.mobile.phone.bean.ExamPublic;
import com.zhiwang123.mobile.phone.bean.QuestionZW;
import com.zhiwang123.mobile.phone.bean.ResultItem;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author wz
 */
public class QuestionPagerFragment extends BaseFragment implements AsyncExecuteCallback {

	public static final String TAG = "QuestionPagerFragment";
	public static final String KEY_CONTENT = "content_data";

	private View mRootView;

	private ViewPager mPager;
	private QuestionPagerAdapter mExamAdapter;
	private AnswerCardAdapter mAnswerCardAdpater;

	private View mAnswerCardBtn;
	private View mHandOutBtn;
	private View mCheckAnswerBtn;
	private GridView mGridView;
	private View mAnswerCardLayer;
	private TextView mCurrentNumTv;
	private TextView mTotalNumTv;
	private View mCloseAnswerCardBtn;
	private TextView mAnswerCardTitleTv;

	private ExamPublic mContentData;

	private ArrayList<View> mViews;

	private static QuestionPagerFragment self = new QuestionPagerFragment();

	public QuestionPagerFragment() {
		super();
	}

	public static QuestionPagerFragment getInstance() {
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.exam_pager_layout, null);
		initViews();
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getArguments();

		if(bundle != null) {

			mContentData = (ExamPublic) bundle.getSerializable(KEY_CONTENT);

			if(mContentData != null) {

				BaseTask task = new BaseTask(0x1, mContentData.dataobject.get(0), this);
				AsyncExecutor.getInstance().execMuliteTask(task);

			}
		}

	}

	@Override
	protected void initViews() {

		mPager = (ViewPager) mRootView.findViewById(R.id.exam_question_paper);
		mAnswerCardBtn = mRootView.findViewById(R.id.exam_answer_card_btn);
		mHandOutBtn = mRootView.findViewById(R.id.exam_hand_out_btn);
		mCheckAnswerBtn = mRootView.findViewById(R.id.exam_check_answer_btn);
		mAnswerCardLayer = mRootView.findViewById(R.id.exam_answer_card_layer);
		mGridView = (GridView) mRootView.findViewById(R.id.card_option_grid);
		mTotalNumTv = (TextView) mRootView.findViewById(R.id.exam_total_page);
		mCurrentNumTv = (TextView) mRootView.findViewById(R.id.exam_current_page);
		mCloseAnswerCardBtn = mRootView.findViewById(R.id.card_go_back);
		mAnswerCardTitleTv = (TextView) mRootView.findViewById(R.id.card_depot_name);

		mAnswerCardBtn.setOnClickListener(onClickListener);
		mCloseAnswerCardBtn.setOnClickListener(onClickListener);
		mHandOutBtn.setOnClickListener(onClickListener);

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
			switch(v.getId()) {
				case R.id.exam_answer_card_btn:

					showOrHiddenCard();

					break;
				case R.id.exam_hand_out_btn:

					((ExamPaperActivity) getActivity()).choosePage(ExamPaperActivity.FRAGMENT_RESULT);

					break;
			}
		}
	};

	private void showOrHiddenCard() {
		if(mAnswerCardLayer.getVisibility() == View.GONE) {
			mAnswerCardAdpater.notifyDataSetChanged();
			Animation inAnim = AnimationUtils.loadAnimation(getActivity(), com.ouchn.lib.R.anim.translate_in);
			mAnswerCardLayer.startAnimation(inAnim);
			mAnswerCardLayer.setVisibility(View.VISIBLE);
		} else {
			Animation inAnim = AnimationUtils.loadAnimation(getActivity(), com.ouchn.lib.R.anim.translate_out);
			mAnswerCardLayer.startAnimation(inAnim);
			mAnswerCardLayer.setVisibility(View.GONE);
		}
	}

	private AdapterView.OnItemClickListener mOnGridItemClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			showOrHiddenCard();
			mPager.setCurrentItem(position, false);
		}

	};

	private void initReuseViews(boolean forWrongQuestion) {
		ArrayList<View> vList = new ArrayList<View>();
		for(int i = 0; i < QuestionPagerAdapter.MAX_REUSE_NUM; i ++) {
			View view = LayoutInflater.from(getActivity()).inflate(com.ouchn.lib.R.layout.question_item, null);
			QuestionViewItemHolder holder =
					forWrongQuestion?
							new QuestionViewItemHolder(view.findViewById(com.ouchn.lib.R.id.question_item_root), true) :
							new QuestionViewItemHolder(view.findViewById(com.ouchn.lib.R.id.question_item_root));
			holder.setOnQuestionAnsweredListener(questionAnsweredListener);
			view.setTag(holder);
			vList.add(view);
		}
		mViews = vList;

	}

	public void onBackPressed() {

		if(mAnswerCardLayer.getVisibility() == View.VISIBLE) {
			showOrHiddenCard();
			return;
		}

		getActivity().onBackPressed();
	}

	@Override
	public void pre(Object... objs) {

	}

	@Override
	public BaseTask.TaskResult async(int what, BaseTask baseTask) {

		BaseTask.TaskResult result = null;

		switch (what) {

			case 0x1:

				ExamPaper examPaper =  (ExamPaper) baseTask.param;

				ArrayList<Question> questions = new ArrayList<>();

				for(Block b : examPaper.blocks) {

					int type = QuestionType.getQuestionTypeInt("（" + b.title+ "）", getActivity());

					for(QuestionZW qzw : b.questions) {

						Question question = new Question();
						question.setDepotId(examPaper.id);
						question.setQuestionType(type);
						question.setContent(qzw.title);
						question.setId(qzw.id);
						question.setqScore(qzw.score);
						question.setqTypeString(b.title);
						question.setqScoreToString(qzw.score + "");
						question.setqSerialNumber(qzw.order);

						if(type != QuestionType.JUDGMENT) {

							QuestionOption[] questionOptions = new QuestionOption[qzw.resultItems.size()];
							int i = 0;
							for(ResultItem itm :  qzw.resultItems) {

								QuestionOption questionOption = new QuestionOption();
								questionOption.setQuestionId(qzw.id);
								questionOption.setOrderNum(itm.order);
								questionOption.setContent(itm.title);
								questionOption.setOrderString(itm.number);
								questionOptions[i ++] = questionOption;

							}
							question.setOptions(questionOptions);

						} else {

							QuestionOption questionOption = new QuestionOption();
							questionOption.setQuestionId(qzw.id);
							questionOption.setOrderNum(0);
							questionOption.setContent(getString(R.string.exam_right));
							questionOption.setOrderString("A");
							QuestionOption questionOption2 = new QuestionOption();
							questionOption2.setQuestionId(qzw.id);
							questionOption2.setOrderNum(1);
							questionOption2.setContent(getString(R.string.exam_wrong));
							questionOption2.setOrderString("B");
							QuestionOption[] questionOptions = new QuestionOption[] {questionOption, questionOption2};
							question.setOptions(questionOptions);

						}
						questions.add(question);
					}

				}

				initReuseViews(false);

				result = baseTask.new TaskResult();
				result.setResult(questions);

				return result;

			case 0x2:

				HashMap<String, String> postParam = new HashMap<String, String>();
				Question question = (Question) baseTask.param;

				postParam.put("AccessToken", StaticConfigs.mLoginResult.accessToken);
				postParam.put("TestPaperId", question.getDeoptId());
				postParam.put("QuestionId", question.getId());

				String answer = "";

				if(question.getQuestionType() == QuestionType.SINGLE || question.getQuestionType() == QuestionType.MULTIPLE || question.getQuestionType() == QuestionType.JUDGMENT) {

					HashSet<String> answers = question.mAnswerState.indexAnswers;
					Iterator<String> i = answers.iterator();

					if(question.getQuestionType() == QuestionType.SINGLE) {

						while(i.hasNext()) {

							answer = i.next();

						}

					} else if(question.getQuestionType() == QuestionType.MULTIPLE) {

						StringBuffer tempStrb = new StringBuffer("");

						while(i.hasNext()) {

							if(tempStrb.length() != 0) tempStrb.append(",");
							tempStrb.append(i.next());

						}

						answer = tempStrb.toString();

					} else if(question.getQuestionType() == QuestionType.JUDGMENT) {

						while(i.hasNext()) {

							String value = i.next();

							if("A".equalsIgnoreCase(value)) {
								answer = getString(R.string.exam_right);
							} else if("B".equalsIgnoreCase(value)) {
								answer = getString(R.string.exam_wrong);
							}

						}
					}

					postParam.put("Answer", answer);

				} else {
					// TODO: 2016/12/2
					postParam.put("Answer", "");
				}

				result = baseTask.new TaskResult();
				result.setResult(postParam);

				return result;
		}

		return null;
	}

	@Override
	public void post(Message msg) {

		BaseTask baseTask = (BaseTask) msg.obj;

		switch(msg.what) {
			case 0x1:
				ArrayList<Question> questions =  (ArrayList<Question>) baseTask.result.getResult();

				mTotalNumTv.setText(questions.size() + "");

				mExamAdapter = new QuestionPagerAdapter(getActivity(), questions, mViews);
				mPager.setAdapter(mExamAdapter);
				mPager.setPageMargin(20);
				mPager.addOnPageChangeListener(onPageChangeListener);
				mExamAdapter.notifyDataSetChanged();
				mPager.setCurrentItem(0);

				mAnswerCardAdpater = new AnswerCardAdapter(getActivity(), questions);
				mGridView.setAdapter(mAnswerCardAdpater);
				mAnswerCardAdpater.notifyDataSetChanged();
				mGridView.setOnItemClickListener(mOnGridItemClick);

				break;
			case 0x2:

				HashMap<String, String> kvs = (HashMap<String, String>) baseTask.result.getResult();

				switch (StaticConfigs.mLoginResult.loginMode) {

					case StaticConfigs.LOGIN_MODE_PB:

						toCommitAnswers(kvs);

						break;
					case StaticConfigs.LOGIN_MODE_E:

						toCommiteEAnswers(kvs);

						break;
				}

				break;
		}
	}

	public QuestionViewItemHolder.QuestionAnsweredListener questionAnsweredListener = new QuestionViewItemHolder.QuestionAnsweredListener() {
		@Override
		public void onQuestionAnswered(Question question) {

			BaseTask task = new BaseTask(0x2, question, QuestionPagerFragment.this);
			AsyncExecutor.getInstance().execMuliteTask(task);

		}
	};

	public void toCommitAnswers (final HashMap<String, String> kvs) {

		JSONObject params = new JSONObject(kvs);

		JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCommitAnswers(), params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				Log.i(TAG, "question " + kvs.get("QuestionId") + " commit answer: " + kvs.get("Answer") + " , result : " + response.toString());

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error.getMessage());
			}
		});

		mVolleyRequestQueue.add(jreq);

	}

	public void toCommiteEAnswers(final HashMap<String, String> kvs) {

		JSONObject params = new JSONObject(kvs);

		JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, StaticConfigs.getUrlCommitAnswersE(), params, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				Log.i(TAG, "question " + kvs.get("QuestionId") + " commit answer: " + kvs.get("Answer") + " , result : " + response.toString());

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error.getMessage());
			}
		});

		mVolleyRequestQueue.add(jreq);

	}

	private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {

			mCurrentNumTv.setText((position + 1) + "");

		}

		@Override
		public void onPageScrollStateChanged(int state) {



		}

	};

	@Override
	public boolean backPressed() {

		if(mAnswerCardLayer.getVisibility() == View.VISIBLE) {
			showOrHiddenCard();
			return false;
		}

		return true;

	}

}

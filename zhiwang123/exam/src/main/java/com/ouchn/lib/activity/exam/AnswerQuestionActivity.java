package com.ouchn.lib.activity.exam;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ouchn.lib.R;
import com.ouchn.lib.activity.BaseOuchnActivity;
import com.ouchn.lib.adapter.AnswerCardAdapter;
import com.ouchn.lib.adapter.QuestionPagerAdapter;
import com.ouchn.lib.adapter.holder.QuestionViewItemHolder;
import com.ouchn.lib.config.Config;
import com.ouchn.lib.db.exam.ExamDatabaseHelper;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionAnswer;
import com.ouchn.lib.entity.QuestionDepot;
import com.ouchn.lib.entity.QuestionType;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.BaseTask;
import com.ouchn.lib.handler.BaseTask.TaskResult;
import com.ouchn.lib.util.BaseConfigUitl;
import com.ouchn.lib.widget.DepotDrawLayer;
import com.ouchn.lib.widget.DepotDrawLayer.PaintMode;
import com.ouchn.lib.widget.QuestionDialog;
import com.ouchn.lib.widget.TimerView;

import java.util.ArrayList;

public class AnswerQuestionActivity extends BaseOuchnActivity {
	
	public static final String TAG = "AnswerQuestionActivity";
	public static final String TIME_KEY = "time_str";
	public static final String DATA_KEY = "data_list";
	public static final String NAME_KEY = "depot_name";
	public static final String DEPOT_KEY = "deopt_entity";
	public static final String RESULT_DATA_KEY = "result_data";
	public static final String RESULT_COUNT_KEY = "answered_count";
	public static final String RESULT_COST_TIME_KEY = "cost_time";
	public static final String IS_WRONG_KEY = "is_wrong_questions";
	
	private Context mContext;
	private ViewPager mPager;
	private QuestionPagerAdapter mAdapter;
	private ArrayList<Question> mDataList;
	private ArrayList<View> mViews;
	private ProgressBar mProgress;
	private String mDepotId;
	private String mDepotName;
	private TextView mDepotNameTv;
	private TextView mCurrentNumTv;
	private TextView mTotalNumTv;
	private TimerView mTimerView;
	
	private View mGoBackBtn;
	
	private GridView mGridView;
	private AnswerCardAdapter mAnswerCardAdpater;
	private View mAnswerCardView;
	private View mAnswerNoteView;
	private DepotDrawLayer mDrawLayer;
	private View mOpenAnswerCardBtn;
	private View mOpenAnswerNoteBtn;
	private View mAnswerCardGoback;
	private View mAnswerNoteGoback;
	private View mAnswerNoteClear;
	private ImageButton mAnswerNoteRecover;
	private View mAnswerTextSize;
	private View mAnswerTextSizeAnchor;
	private TextView mAnswerCardDepotNameTv;
	private TextView mCardSubmitBtn;
	private EditText mNoteEditView;
	private PopupWindow mPop;
	private TextView ml;
	private TextView mm;
	private TextView ms;
	private View mSaveAnswerDialog;
	
	private String mTimeString;
	private String mLastNoteString;
	private String mCurrentNoteString;
	private boolean isSaving;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mContext = this;
		getActionBar().hide();
		setContentView(R.layout.answer_question_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initView();
		
		mIsWrongBrowse = getIntent().getBooleanExtra(IS_WRONG_KEY, false);
		
		if(!mIsWrongBrowse) AsyncExecutor.getInstance().execMuliteTask(new BaseTask(0, this));
		else AsyncExecutor.getInstance().execMuliteTask(new BaseTask(1, this));
	}
	
	private void initView() {
		
		mPager = (ViewPager)findViewById(R.id.answer_pager);
		mProgress = (ProgressBar)findViewById(R.id.answer_progress);
		mDepotNameTv = (TextView)findViewById(R.id.answer_depot_name);
		mCurrentNumTv = (TextView)findViewById(R.id.answer_current_page);
		mTotalNumTv = (TextView)findViewById(R.id.answer_total_page);
		mTimerView = (TimerView)findViewById(R.id.question_timer_view);
		mGoBackBtn = findViewById(R.id.answer_go_back);
		mAnswerCardView = findViewById(R.id.answer_card_layout);
		mAnswerNoteView = findViewById(R.id.answer_note_layout);
		mDrawLayer = (DepotDrawLayer) findViewById(R.id.question_note_drawlayer);
		mAnswerNoteView.findViewById(R.id.note_title_answer_content).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
			
		});;
		mGridView = (GridView) findViewById(R.id.card_option_grid);
		mGridView.setOnItemClickListener(mOnGridItemClick);
		mOpenAnswerCardBtn = findViewById(R.id.answer_to_answer_card_btn);
		mOpenAnswerNoteBtn = findViewById(R.id.answer_to_answer_note_btn);
		mAnswerCardGoback = findViewById(R.id.card_go_back);
		mAnswerNoteGoback = findViewById(R.id.note_go_back);
		mAnswerNoteClear = findViewById(R.id.note_clear_note);
		mAnswerCardDepotNameTv = (TextView) findViewById(R.id.card_depot_name);
		mCardSubmitBtn = (TextView) findViewById(R.id.card_bottom_btn);
		mAnswerTextSize = findViewById(R.id.answer_word_size);
		mAnswerTextSizeAnchor = findViewById(R.id.answer_word_size_pop_anchor);
		mSaveAnswerDialog = findViewById(R.id.answer_save_answer_dialog);
		mNoteEditView = (EditText) findViewById(R.id.note_answer_note_edtv);
		mAnswerNoteRecover = (ImageButton) findViewById(R.id.note_recover_note);
		
		initPopDialog();
		
		mTimerView.setOnClickListener(mOnClick);
		mGoBackBtn.setOnClickListener(mOnClick);
		mOpenAnswerCardBtn.setOnClickListener(mOnClick);
		mOpenAnswerNoteBtn.setOnClickListener(mOnClick);
		mAnswerCardGoback.setOnClickListener(mOnClick);
		mAnswerNoteGoback.setOnClickListener(mOnClick);
		mAnswerNoteClear.setOnClickListener(mOnClick);
		mCardSubmitBtn.setOnClickListener(mOnClick);
		mAnswerTextSize.setOnClickListener(mOnClick);
		mAnswerNoteRecover.setOnClickListener(mOnClick);
		
		mNoteEditView.addTextChangedListener(mTextWatcher);
		
	}
	
	private OnItemClickListener mOnGridItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			showOrHiddenCard();
			mPager.setCurrentItem(position, false);
		}
		
	};
	
	private OnClickListener mOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			int id = v.getId();
			
			if(id == R.id.answer_go_back) {
				onBackPressed();
			} else if(id == R.id.answer_to_answer_card_btn) {
				showOrHiddenCard();
			} else if(id == R.id.answer_to_answer_note_btn) {
				showOrHiddenNote();
			} else if(id == R.id.card_go_back) {
				showOrHiddenCard();
			} else if(id == R.id.note_go_back) {
				showOrHiddenNote();
			} else if(id == R.id.note_clear_note) {
				//mNoteEditView.setText("");
				mDrawLayer.clearNote();
			} else if(id == R.id.note_recover_note) {
				//mNoteEditView.setText(mLastNoteString);
				if(mDrawLayer.getPaintMode() == PaintMode.common) {
					mAnswerNoteRecover.setImageResource(R.drawable.recover_note);
					mDrawLayer.setPaintMode(PaintMode.erase);
				} else {
					mAnswerNoteRecover.setImageResource(R.drawable.draw_note);
					mDrawLayer.setPaintMode(PaintMode.common);
				}
			} else if(id == R.id.card_bottom_btn) {
				onCardResult();
			} else if(id == R.id.answer_word_size) {
				if(!mPop.isShowing()) {
					mAnswerTextSizeAnchor.setVisibility(View.VISIBLE);
					mPop.showAsDropDown(mAnswerTextSizeAnchor);
				} else {
					mAnswerTextSizeAnchor.setVisibility(View.INVISIBLE);
					mPop.dismiss();
				}
			} else if(id == R.id.pop_l || id == R.id.pop_m || id == R.id.pop_s) {
				
				int size = BaseConfigUitl.S;
				if(v.getId() == R.id.pop_l) {
					size = BaseConfigUitl.L;
					ml.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
					mm.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
					ms.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
				} else if(v.getId() == R.id.pop_m) {
					size = BaseConfigUitl.M;
					ml.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
					mm.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
					ms.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
				} else {
					ml.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
					mm.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
					ms.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
				}
				
				BaseConfigUitl.Aa = size;
				
				for(View viewItem : mViews) {
					QuestionViewItemHolder holder = (QuestionViewItemHolder) viewItem.getTag();
					holder.mQuestionContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
					holder.mQuestionOptionArea.setTextSize(size);
				}
				mPop.dismiss();
				
			} else if(id == R.id.question_timer_view) {
				mTimerView.cancelClock();
				
				final QuestionDialog dialog = new QuestionDialog(mContext);
				dialog.setInfo(R.string.answer_dialog_title, R.string.answer_dialog_info2, R.string.answer_dialog_submit, R.string.answer_dialog_go_on)
				.setCancelable(false)
				.setMessageTextColor(getResources().getColor(R.color.main_green))
				.setPClick(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						onCardResult();
					}
				})
				.setNClick(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mTimerView.startClock();
						dialog.dismiss();
					}
				})
				.show();
			}
			
//			switch(v.getId()) {
//			case R.id.answer_go_back:
//				onBackPressed();
//				break;
//			case R.id.answer_to_answer_card_btn:
//				showOrHiddenCard();
//				break;
//			case R.id.card_go_back:
//				showOrHiddenCard();
//				break;
//			case R.id.card_bottom_btn:
//				onCardResult();
//				break;
//			case R.id.answer_word_size:
//				if(!mPop.isShowing()) mPop.showAsDropDown(mAnswerTextSizeAnchor);
//				else mPop.dismiss();
//				break;
//			case R.id.pop_l:
//			case R.id.pop_m:
//			case R.id.pop_s:
//				
//				int size = BaseConfigUitl.S;
//				if(v.getId() == R.id.pop_l) {
//					size = BaseConfigUitl.L;
//					ml.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
//					mm.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
//					ms.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
//				} else if(v.getId() == R.id.pop_m) {
//					size = BaseConfigUitl.M;
//					ml.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
//					mm.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
//					ms.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
//				} else {
//					ml.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
//					mm.setTextColor(mContext.getResources().getColor(R.color.main_deep_blue));
//					ms.setTextColor(mContext.getResources().getColor(R.color.main_yellow));
//				}
//				
//				BaseConfigUitl.Aa = size;
//				
//				for(View viewItem : mViews) {
//					QuestionViewItemHolder holder = (QuestionViewItemHolder) viewItem.getTag();
//					holder.mQuestionContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
//					holder.mQuestionOptionArea.setTextSize(size);
//				}
//				mPop.dismiss();
//				break;
//			case R.id.question_timer_view:
//				mTimerView.cancelClock();
//				
//				final QuestionDialog dialog = new QuestionDialog(mContext);
//				dialog.setInfo("info", "Let's have a rest!", "submit", "goon")
//				.setCancelable(false)
//				.setMessageTextColor(getResources().getColor(R.color.main_green))
//				.setPClick(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//						onCardResult();
//					}
//				})
//				.setNClick(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						mTimerView.startClock();
//						dialog.dismiss();
//					}
//				})
//				.show();
//				break;
//			default:
//				break;
//			}
			
		}
	};
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mCurrentNoteString = s.toString();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			String note = s.toString();
			mLastNoteString = note;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};
	
	private void initPopDialog() {
		View view = LayoutInflater.from(AnswerQuestionActivity.this).inflate(R.layout.poup_text_size, null);
		ml = (TextView) view.findViewById(R.id.pop_l);
		mm = (TextView) view.findViewById(R.id.pop_m);
		ms = (TextView) view.findViewById(R.id.pop_s);
		
		switch(BaseConfigUitl.Aa) {
		case BaseConfigUitl.L:
			ml.setTextColor(getResources().getColor(R.color.main_yellow));
			break;
		case BaseConfigUitl.M:
			mm.setTextColor(getResources().getColor(R.color.main_yellow));
			break;
		case BaseConfigUitl.S:
			ms.setTextColor(getResources().getColor(R.color.main_yellow));
			break;
		}
		
		ml.setOnClickListener(mOnClick);
		mm.setOnClickListener(mOnClick);
		ms.setOnClickListener(mOnClick);
		
        mPop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        mPop.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mAnswerTextSizeAnchor.setVisibility(View.INVISIBLE);
			}
		});
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setOutsideTouchable(true);
	
	}
	private void showOrHiddenCard() {
		if(mAnswerCardView.getVisibility() == View.GONE) {
			mAnswerCardAdpater.notifyDataSetChanged();
			Animation inAnim = AnimationUtils.loadAnimation(AnswerQuestionActivity.this, R.anim.translate_in);
			mAnswerCardView.startAnimation(inAnim);
			mAnswerCardView.setVisibility(View.VISIBLE);
		} else {
			Animation inAnim = AnimationUtils.loadAnimation(AnswerQuestionActivity.this, R.anim.translate_out);
			mAnswerCardView.startAnimation(inAnim);
			mAnswerCardView.setVisibility(View.GONE);
		}
	}
	
	private void showOrHiddenNote() {
		if(mAnswerNoteView.getVisibility() == View.GONE) {
			Animation inAnim = AnimationUtils.loadAnimation(AnswerQuestionActivity.this, R.anim.translate_in);
			mAnswerNoteView.startAnimation(inAnim);
			mAnswerNoteView.setVisibility(View.VISIBLE);
		} else {
			Animation inAnim = AnimationUtils.loadAnimation(AnswerQuestionActivity.this, R.anim.translate_out);
			mAnswerNoteView.startAnimation(inAnim);
			mAnswerNoteView.setVisibility(View.GONE);
		}
	}

	@Override
	public TaskResult async(int what, BaseTask baseTask) {
		switch(what) {
		case 0:
		case 1:
			Bundle bundle = getIntent().getExtras();
			ArrayList<QuestionAnswer> answers = null;
			if(bundle != null) {
				mDataList = (ArrayList<Question>) Config.tempCache.get(Config.LARGE_TRANSACTION_GOTO);//(ArrayList) bundle.getSerializable(DATA_KEY);
				Config.tempCache.remove(Config.LARGE_TRANSACTION_GOTO);
				mDepotName = bundle.getString(NAME_KEY);
				mTimeString= bundle.getString(TIME_KEY);
				QuestionDepot depot = (QuestionDepot) bundle.getSerializable(DEPOT_KEY);
				mDepotId = depot.getId();
				answers = new ExamDatabaseHelper(this).getAnswersInfo(depot.getId());
			}
			
			if(answers != null && answers.size() != 0) {
				for(Question question : mDataList) {
					for(QuestionAnswer answer : answers) {
						if(question.getId().equals(answer.getQuestionId())) {
							String answerStr = answer.getQuestionAnswer();
							if(TextUtils.isEmpty(answerStr)) break;
							String[] answerInfo = answerStr.split(ExamDatabaseHelper.SEPARATOR);
							if(question.getQuestionType() == QuestionType.MATCHING) {
								question.mAnswerState.matchAnswers.clear();
								for(String asw : answerInfo) {
									String[] kv = asw.split(Question._SEPARATOR);
									if(kv.length < 2) break; 
									question.mAnswerState.matchAnswers.put(kv[0], kv[1]);
								}
							} else if(question.getQuestionType() == QuestionType.SORT) {
								question.mAnswerState.sortAnswers.clear();
								for(String asw : answerInfo) {
									String[] ic = asw.split(Question._SEPARATOR);
									question.mAnswerState.sortAnswers.add(ic);
								}
							} else if(question.getQuestionType() == QuestionType.JUDGMENT || question.getQuestionType() == QuestionType.SINGLE || question.getQuestionType() == QuestionType.MULTIPLE) {
								question.mAnswerState.indexAnswers.clear();
								for(String asw : answerInfo) {
									question.mAnswerState.indexAnswers.add(asw);
								}
							}
							break;
						}
					}
				}
			}
			
			String note = new ExamDatabaseHelper(this).getAnswerNote(mDepotId);
//			initReuseViews();
			BaseTask.TaskResult res = baseTask.new TaskResult();
			res.setResult(note);
			return res;
		case 3:
			int count = 0;
			ExamDatabaseHelper helper = new ExamDatabaseHelper(this);
			for(Question question: mDataList) {
				helper.saveAnswersInfo(question);
				if(question.mAnswerState.isAnswered()) {
					count += 1;
				} 
			}
			helper.saveAnswerNote(mDepotId, mCurrentNoteString);
			BaseTask.TaskResult result = baseTask.new TaskResult();
			result.setResult(count);
			return result;
		default:
			return null;
		}
	}
	
	@Override
	public void post(Message msg) {
		
		BaseTask task = (BaseTask) msg.obj;
		
		switch(msg.what) {
		case 0:
			mProgress.setVisibility(View.GONE);
			mDepotNameTv.setText(mDepotName);
			mAnswerCardDepotNameTv.setText(mDepotName);
			mCurrentNumTv.setText("1");
			mTotalNumTv.setText(mDataList.size() + "");
			mTimerView.setTimeString(mTimeString);
			initReuseViews(false);
			String note = (String)task.result.getResult();
			if(!TextUtils.isEmpty(note)) {
				mNoteEditView.setText(note);
			}
			
			mAdapter = new QuestionPagerAdapter(this, mDataList, mViews);
			mPager.setAdapter(mAdapter);
			mPager.setPageMargin(20);
			mPager.setOnPageChangeListener(mOnPageChangeListener);
			mAdapter.notifyDataSetChanged();
			
			mAnswerCardAdpater = new AnswerCardAdapter(this, mDataList);
			mGridView.setAdapter(mAnswerCardAdpater);
			mAnswerCardAdpater.notifyDataSetChanged();
			
			break;
		case 1:
			
			mProgress.setVisibility(View.GONE);
			mDepotNameTv.setText(mDepotName);
			mAnswerCardDepotNameTv.setText(mDepotName);
			mCurrentNumTv.setText("1");
			mTotalNumTv.setText(mDataList.size() + "");
			initReuseViews(true);
			mNoteEditView.setEnabled(false);
			mAdapter = new QuestionPagerAdapter(this, mDataList, mViews);
			mPager.setAdapter(mAdapter);
			mPager.setPageMargin(20);
			mPager.setOnPageChangeListener(mOnPageChangeListener);
			mAdapter.notifyDataSetChanged();
			
			mAnswerCardAdpater = new AnswerCardAdapter(this, mDataList);
			mGridView.setAdapter(mAnswerCardAdpater);
			mAnswerCardAdpater.notifyDataSetChanged();
			
			mTimerView.setEnabled(false);
			mTimerView.cancelClock();
			//mOpenAnswerCardBtn.setEnabled(false);
			
			break;
		case 3:
			
			mSaveAnswerDialog.setVisibility(View.GONE);
			int count = (Integer) task.result.getResult();
			Intent intent = getIntent();
			//intent.putExtra(RESULT_DATA_KEY, mDataList);
			Config.tempCache.put(Config.LARGE_TRANSACTION_GOBACK, mDataList);
			intent.putExtra(RESULT_COUNT_KEY, count);
			intent.putExtra(RESULT_COST_TIME_KEY, mTimerView.getTimeString());
			setResult((Integer) task.param, intent);
			finish();
			
			break;
		}
	}

	private void initReuseViews(boolean forWrongQuestion) {
		ArrayList<View> views = new ArrayList<View>();
		for(int i = 0; i < QuestionPagerAdapter.MAX_REUSE_NUM; i ++) {
			View view = LayoutInflater.from(mContext).inflate(R.layout.question_item, null);
			QuestionViewItemHolder holder = 
					forWrongQuestion? 
						new QuestionViewItemHolder(view.findViewById(R.id.question_item_root), true) : 
						new QuestionViewItemHolder(view.findViewById(R.id.question_item_root));
			view.setTag(holder);
			views.add(view);
		}
		mViews = views;
	}
	
	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		
		private static final int SCROLLING = 1;
		private static final int SCROLLFINISHED = 2;
		private static final int IDEL = 0;
		
		@Override
		public void onPageSelected(int pageIndex) {
			mCurrentNumTv.setText((pageIndex + 1) + "");
		}
		
		@Override
		public void onPageScrolled(int currentIndex, float scrollPrecent, int scrollPixel) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int scrollState) {
			switch(scrollState) {
			case SCROLLING:
				break;
			case SCROLLFINISHED:
				break;
			case IDEL:
				break;
			}
		}
	};
	private boolean mIsWrongBrowse;
	
	private void onCardResult() {

		boolean isBreak = false;
		for(Question question: mDataList) {
			if(!question.mAnswerState.isAnswered()) {
				isBreak = true;
				break;
			} 
		}
		
		for(Question question : mDataList) {
			Log.v(TAG, "before result: -------------------------------------" + question.mAnswerState.matchAnswers.toString());
		}
		
		if(!isBreak) {
			putResult(QuestionDepot.FINISHED);
		} else {
			mTimerView.cancelClock();
			final QuestionDialog dialog = new QuestionDialog(mContext);
			dialog.setInfo(getResources().getString(R.string.answer_dialog_info))
			.setPClick(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					putResult(QuestionDepot.FINISHED);
				}
			})
			.setNClick(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mTimerView.startClock();
					dialog.dismiss();
				}
			})
			.show();
		}
	}
	
	public void putResult(int type) {
		
		if(type != QuestionDepot.FINISHED) {
			super.onBackPressed();
			return;
		} 
		
		BaseTask task = new BaseTask(3, this);
		task.param = type;
		AsyncExecutor.getInstance().execMuliteTask(task);
		mSaveAnswerDialog.setVisibility(View.VISIBLE);
		isSaving = true;
		
//		int count = 0;
//		for(Question question: mDataList) {
//			if(question.mAnswerState.isAnswered()) {
//				count += 1;
//			} 
//		}
//		
//		Intent intent = getIntent();
//		intent.putExtra(RESULT_DATA_KEY, mDataList);
//		intent.putExtra(RESULT_COUNT_KEY, count);
//		intent.putExtra(RESULT_COST_TIME_KEY, mTimerView.getTimeString());
//		setResult(type, intent);
//		finish();
	}
	
	@Override
	public void onBackPressed() {
		if(mAnswerCardView.getVisibility() == View.VISIBLE) {
			showOrHiddenCard();
			return;
		}
		
		if(mAnswerNoteView.getVisibility() == View.VISIBLE) {
			showOrHiddenNote();
			return;
		}
		
		if(isSaving) return; 
		
		if(mIsWrongBrowse) {
			super.onBackPressed();
		} else {
			boolean isBreak = false;
			for(Question question: mDataList) {
				if(!question.mAnswerState.isAnswered()) {
					isBreak = true;
					break;
				} 
			}
			
			if(isBreak) {
				mTimerView.cancelClock();
				final QuestionDialog dialog = new QuestionDialog(mContext);
				dialog.setInfo(R.string.answer_dialog_title, R.string.answer_dialog_info3, R.string.answer_dialog_submit2, R.string.answer_dialog_go_on)
				.setCancelable(false)
				.setMessageTextColor(getResources().getColor(R.color.main_green))
				.setPClick(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						putResult(QuestionDepot.NOFINISH);
					}
				})
				.setNClick(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mTimerView.startClock();
						dialog.dismiss();
					}
				})
				.show();
			}
		}
		//else putResult(QuestionDepot.NOFINISH);
		
	}

}

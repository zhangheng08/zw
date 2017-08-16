package com.ouchn.lib.widget;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ouchn.lib.R;
import com.ouchn.lib.adapter.holder.QuestionDepotListItemHolder;
import com.ouchn.lib.config.Config;
import com.ouchn.lib.db.exam.ExamDatabaseHelper;
import com.ouchn.lib.entity.BaseObject.ParseTarget;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionDepot;
import com.ouchn.lib.entity.QuestionType;
import com.ouchn.lib.entity.ResultObject;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.BaseJsonResponseHandler.TypeInfoes;
import com.ouchn.lib.handler.BaseTask;
import com.ouchn.lib.handler.BaseTask.TaskResult;
import com.ouchn.lib.net.AsyncNet;
import com.ouchn.lib.net.AsyncNetCallback;

public class DepotItem extends RelativeLayout implements AsyncNetCallback, AsyncExecuteCallback {
	
	private final String TAG = "DepotItem";
	
	private boolean mIsChecked;
	private ItemActionUpCallback mItemActionUpCallback;
	
	private View mRootView;
	private Context mContext;
	private TextView mTitleText;
	private ImageView mCheckedImg;
	private ImageView mLeftIcon;
	
	public DepotItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public DepotItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public DepotItem(Context context) {
		super(context);
		initView(context);
	}
	
	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.question_bank_item, this);
		
		mContext = context;
		mRootView = findViewById(R.id.main_depot_item_root);
		mTitleText = (TextView)findViewById(R.id.main_depot_item_content);
		mCheckedImg = (ImageView)findViewById(R.id.main_depot_item_right_img);
		mLeftIcon = (ImageView)findViewById(R.id.main_depot_item_left_img);

	}
	
	public void isChecked(boolean check) {
		mIsChecked = check;
		resetUi();
	}
	
	public void resetUi() {
		if(mIsChecked) {
			mRootView.setBackgroundResource(R.color.main_blue);
			mTitleText.setTextColor(0xffffffff);
			mCheckedImg.setImageResource(R.drawable.depot_check);
			mLeftIcon.setImageResource(R.drawable.depot_item_check);
			mCheckedImg.setVisibility(VISIBLE);
		} else {
			mRootView.setBackgroundResource(R.drawable.depot_item_bg_selector);
			mTitleText.setTextColor(0xff175188);
			mCheckedImg.setImageResource(R.drawable.depot_check_no);
			mLeftIcon.setImageResource(R.drawable.depot_item_check_no);
			mCheckedImg.setVisibility(GONE);
		}
	}
	
	public void loadQuestionList(Context context) {
		QuestionDepot depot = ((QuestionDepotListItemHolder)getTag()).mDepot;
		if(depot == null) return;
		String depotId = depot.getId();
		loadAllQuestion(context, depotId);
	}
	
	public void doClick() {
		int currentIndex = ((QuestionDepotListItemHolder)getTag()).mCurrentIndex;
		if(mItemActionUpCallback != null) mItemActionUpCallback.onItemTouchActionUp(currentIndex, this);
	}
	
	public ArrayList<Question> getQuestionList() {
		QuestionDepot depot = ((QuestionDepotListItemHolder)getTag()).mDepot;
		return depot.getQuestionList();
	}
	
	public QuestionDepot getEntity() {
		return ((QuestionDepotListItemHolder)getTag()).mDepot;
	}
	
	@Override
	public void onResponseSuccess(int statusCode, Header[] headers, ResultObject<?> result, TypeInfoes typeInfo) {
		
//		ArrayList<Question> list = new ArrayList<Question>();
		JSONArray jsonArr = (JSONArray) result.getResult();
		fillQuestionData(jsonArr);
//		Log.v(TAG, typeInfo.getDescription() + " -- " + jsonArr.toString());
//		try {
//			for(int i = 0; i < jsonArr.length(); i ++) {
//				ParseTarget<Question> parseTarget = (ParseTarget<Question>) Question.toParseJSON(new Question(), jsonArr.getJSONObject(i));
//				Question question = parseTarget.getResult();
//				if(question.getQuestionType() == QuestionType.FILL_BLANK || question.getQuestionType() == QuestionType.ESSAY) continue;
//				list.add(question);
//			}
//			int questionListSize = list.size();
//			TextView num = (TextView) findViewById(R.id.depot_answered_num);
//			SeekBar seek = (SeekBar) findViewById(R.id.depot_answered_progress);
//			
//			num.setText("0/" + questionListSize);
//			seek.setMax(questionListSize);
//			
//			QuestionDepot depot = ((QuestionDepotListItemHolder)getTag()).mDepot ;
//			depot.setQuestionCount(String.valueOf(questionListSize));
//			depot.setQuestionList(list);
//			new ExamDatabaseHelper(mContext).saveQuestionJsonInfo(depot.getId(), jsonArr);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	private void fillQuestionData(JSONArray jsonArr) {
		
		if(jsonArr == null) {
			Log.e(TAG, " --------------- question info is empty");
			return;
		} else {
			Log.v(TAG, " --------------- " + jsonArr.toString());
		}
		
		ArrayList<Question> list = new ArrayList<Question>();
		try {
			QuestionDepot depot = ((QuestionDepotListItemHolder)getTag()).mDepot ;
			for(int i = 0; i < jsonArr.length(); i ++) {
				ParseTarget<Question> parseTarget = (ParseTarget<Question>) Question.toParseJSON(new Question(), jsonArr.getJSONObject(i));
				Question question = parseTarget.getResult();
				question.setDepotId(depot.getId());
				if(question.getQuestionType() == QuestionType.FILL_BLANK || question.getQuestionType() == QuestionType.ESSAY) continue;
				list.add(question);
			}
			int questionListSize = list.size();
			TextView num = (TextView) findViewById(R.id.depot_answered_num);
			SeekBar seek = (SeekBar) findViewById(R.id.depot_answered_progress);
			
			num.setText("0/" + questionListSize);
			seek.setMax(questionListSize);
			
			depot.setQuestionCount(String.valueOf(questionListSize));
			depot.setQuestionList(list);
			new ExamDatabaseHelper(mContext).saveQuestionJsonInfo(depot.getId(), jsonArr);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResponseFailure(int statusCode, Header[] headers, Throwable throwable, ResultObject<?> result, TypeInfoes typeInfo) {
		Log.e(TAG, "request for " + typeInfo.getDescription() + " failure!");
		throwable.printStackTrace();
		
		QuestionDepot depot = ((QuestionDepotListItemHolder)getTag()).mDepot;
		if(depot == null) return;
		String depotId = depot.getId();
		BaseTask task = new BaseTask(0, this);
		task.param = depotId;
		
		AsyncExecutor.getInstance().execMuliteTask(task);
		
	}
	
	private void loadAllQuestion(Context context, String depotId) {
    	AsyncNet.getInstance().doGet(context, Config.getUrl(Config.URLTYPE_QUESTIONS, new String[]{"AccessToken", "ExamQuestionBankId"}, new String[]{Config.fakeToken, depotId}), this, TypeInfoes.ASYNC_LOAD_ALLQUESTION);
    }
	
	public void setOnItemActionUpCallback(ItemActionUpCallback callback) {
		mItemActionUpCallback = callback;
	}

	public interface ItemActionUpCallback {
		public void onItemTouchActionUp(int index, DepotItem item);
	}

	@Override
	public void pre(Object... objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaskResult async(int what, BaseTask baseTask) {
		
		switch(what) {
		case 0:
			String depotId = (String) baseTask.param;
			JSONArray jsonArr = new ExamDatabaseHelper(mContext).getQuestionJsonInfo(depotId);
			BaseTask.TaskResult result = baseTask.new TaskResult();
			result.setResult(jsonArr);
			return result;
		}
		
		return null;
	}

	@Override
	public void post(Message msg) {
		BaseTask task = (BaseTask) msg.obj;
		
		if(task != null) {
			JSONArray jsonArr = (JSONArray) task.result.getResult();
			fillQuestionData(jsonArr);
		}
		
	}
	
}

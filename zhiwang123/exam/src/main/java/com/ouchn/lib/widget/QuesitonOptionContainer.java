package com.ouchn.lib.widget;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouchn.lib.R;
import com.ouchn.lib.entity.Question;
import com.ouchn.lib.entity.QuestionType;
import com.ouchn.lib.handler.AsyncExecuteCallback;
import com.ouchn.lib.handler.AsyncExecutor;
import com.ouchn.lib.handler.AsyncExecutor.ExecType;
import com.ouchn.lib.handler.BaseTask;
import com.ouchn.lib.handler.BaseTask.TaskResult;
import com.ouchn.lib.util.BaseConfigUitl;
import com.ouchn.lib.widget.QuestionOptionIndex.OnSelectedMonitor;

import java.util.ArrayList;
import java.util.HashSet;

public class QuesitonOptionContainer extends LinearLayout implements OnSelectedMonitor, AsyncExecuteCallback {

	private static final String TAG = "QuesitonOptionContainer";
	
	private boolean isMutex;
	private boolean isOperated;
	private Context mContext;
	private ArrayList<QuestionOptionIndex> mOptionIndexList;
	private ArrayList<QuestionOptionMatch> mOptionMatchList;
	private ArrayList<QuestionOptionSort> mOptionSortList;
	private MatchDragEventListener mMatchDragLisener;
	private SortDragEventListener mSortDragListener;
	private QuestionOptionSort mCurrentDragingObj;
	private String mOriginalLeftIndex;
	private String mOriginalRightContent;
	private String mCurrentLeftIndex;
	private String mCurrentRightContent;
	private LayoutInflater mInflater;
	private Question mQuestionEntity;
	private int mCurrentInnerType;
	private ArrayList<View> mChildren;
	private int[] matchReorderIndexes;

	private QuestionAnsweredMonitor mQuestionAnsweredMonitor;
	
	public QuesitonOptionContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout(context);
	}

	public QuesitonOptionContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public QuesitonOptionContainer(Context context) {
		super(context);
		initLayout(context);
	}
	
	private void initLayout(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mChildren = new ArrayList<View>();
		mOptionIndexList = new ArrayList<QuestionOptionIndex>();
		mOptionMatchList = new ArrayList<QuestionOptionMatch>();
		mOptionSortList= new ArrayList<QuestionOptionSort>();
		mMatchDragLisener = new MatchDragEventListener();
		mSortDragListener = new SortDragEventListener();
		matchReorderIndexes = new int[3];
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		mOptionIndexList.clear();
//		recursionSetIndexes(this);
		super.onLayout(changed, l, t, r, b);
	}
	
	private void recursionSetIndexes(ViewGroup parent) {
		int count = parent.getChildCount();
		for(int i = 0; i < count; i ++) {
			View child = parent.getChildAt(i);
			if(child instanceof QuestionOptionIndex) {
				mOptionIndexList.add((QuestionOptionIndex) child);
			} else if(child instanceof ViewGroup) {
				recursionSetIndexes((ViewGroup)child);
			}
		}
	}
	
	public void setOptionIndexList(QuestionOptionIndex[] indexes) {
		for(QuestionOptionIndex index : indexes) {
			mOptionIndexList.add(index);
		}
	}
	
	public void setOptionIndexList(String[] options) {
		
		BaseTask[] tasks = new BaseTask[options.length];
		
//		isMutex = true;
		
		for(int i = 0; i < options.length; i ++) {
			tasks[i] = new BaseTask(0x2, options[i], this);
		}
		
		AsyncExecutor.getInstance().execMuliteTask(tasks, ExecType.concurrent);
	}
	
	public void setOptionMatchList(String[] options) {
		
		BaseTask[] tasks = new BaseTask[options.length];
		
		Integer[] leftOrder = getIndex(options.length / 2);
		
		for(int i = 0; i < leftOrder.length; i ++) {
			tasks[i] = new BaseTask(0x0, options[leftOrder[i]], this);
		}
		
		for(int i = leftOrder.length; i < tasks.length; i ++) {
			tasks[i] = new BaseTask(0x0, options[i], this);
		}
		
		AsyncExecutor.getInstance().execMuliteTask(tasks, ExecType.synchronous);
	}
	
	private Integer[] getIndex(int size) {
		HashSet<Integer> set = new HashSet<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		Integer[] indexes = new Integer[size];
		
		do {
			int index = (int)(Math.random() * size);
			if(!set.contains(index)) {
				set.add(index);
				list.add(index);
			}
		} while(set.size() < size);
		
		list.toArray(indexes);
		return indexes;
	}
	
	public void matchViewLeftContentReorder() {
		int index = (int)(Math.random() * 3);
	}
	
	public void setOptionSortList(String[] options) {
		BaseTask[] tasks = new BaseTask[options.length];
		
		for(int i = 0; i < options.length; i ++) {
			tasks[i] = new BaseTask(0x3, options[i], this);
		}
		
		AsyncExecutor.getInstance().execMuliteTask(tasks, ExecType.synchronous);
	}
	
	public void isMutex(boolean is) {
		isMutex = is;
	}
	
	public void setQuestionEntity(Question ques) {
		mQuestionEntity = ques;
	}

	@Override
	public void onOptionSelected(QuestionOptionIndex view) {
		Log.v(TAG, mOptionIndexList.size() + "---------------" + view.mOptionIndex);
		isOperated = true;
		
		if(view.isSelected) {
			mQuestionEntity.mAnswerState.indexAnswers.add(view.getText().toString());//(view.mOptionContent);
		} else {
			mQuestionEntity.mAnswerState.indexAnswers.remove(view.getText().toString());//(view.mOptionContent);
		}
		
		if(isMutex && mOptionIndexList.size() != 0) {
			for(int i = 0; i < mOptionIndexList.size(); i ++) {
				if(i != view.mOptionIndex) {
					QuestionOptionIndex iv = mOptionIndexList.get(i);
					iv.setSelected(false);
					if(mQuestionEntity.mAnswerState.indexAnswers.contains(iv.getText().toString()/*iv.mOptionContent*/)) {
						mQuestionEntity.mAnswerState.indexAnswers.remove(iv.getText().toString()/*iv.mOptionContent*/);
					}
				}
			}
		}

		if(mQuestionAnsweredMonitor != null) mQuestionAnsweredMonitor.onQuestionAnswered(mQuestionEntity);
	}
	
	public void resetInnerLatyout() {
		removeAllViews();
		mChildren.clear();
		mOptionMatchList.clear();
		mOptionIndexList.clear();
		mOptionSortList.clear();
	}
	
	private OnLongClickListener mMatchDragTrigger = new OnLongClickListener() {
		  
        @Override  
        public boolean onLongClick(View v) {  
            ClipData.Item item = new ClipData.Item(((TextView)v).getText());
  
            ClipData dragData = new ClipData(v.toString(),  new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);  
  
            DragShadowBuilder myShadow = new DragShadowBuilder(v);//new MyDragShadowBuilder(imageView);  
  
            return v.startDrag(dragData, myShadow, null, 0);
        }  
    };
    
    private OnLongClickListener mSortDragTrigger = new OnLongClickListener() {
		  
        @Override  
        public boolean onLongClick(View v) {  
        	mCurrentDragingObj = (QuestionOptionSort)v;
        	
        	mCurrentLeftIndex = mCurrentDragingObj.getLeftIndex().getText().toString();
        	mCurrentRightContent = mCurrentDragingObj.getRightContent().getText().toString();
        	
        	Intent intent = new Intent();
        	intent.putExtra("left", mCurrentLeftIndex);
        	intent.putExtra("right", mCurrentRightContent);
        	
        	ClipData.Item item = new ClipData.Item(intent);
        	ClipData dragData = new ClipData(v.toString(),  new String[] { ClipDescription.MIMETYPE_TEXT_INTENT }, item); 
        	
            DragShadowBuilder myShadow = new DragShadowBuilder(v);
  
            return v.startDrag(dragData, myShadow, null, 0);
        }  
    };

    private class MatchDragEventListener implements OnDragListener {
  
        public boolean onDrag(View v, DragEvent event) {  
            final int action = event.getAction();  
            switch (action) {  
  
                case DragEvent.ACTION_DRAG_STARTED:  
                    System.out.println("ACTION_DRAG_STARTED----------------");  
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {  
                        v.setBackgroundResource(R.drawable.quesom_bg_shape2);
                        v.invalidate();  
                        return true;
                    } else {  
                        return false;  
                    } 
  
                case DragEvent.ACTION_DRAG_ENTERED:  
                    System.out.println("ACTION_DRAG_ENTERED----------------");  
                    v.setBackgroundResource(R.drawable.quesom_bg_shape3);
                    v.invalidate();  
                    return true; 
  
                case DragEvent.ACTION_DRAG_LOCATION:  
                    return true;  
  
                case DragEvent.ACTION_DRAG_EXITED:  
                    System.out.println("ACTION_DRAG_EXITED----------------");  
                    v.setBackgroundResource(R.drawable.quesom_bg_shape2);
                    v.invalidate();  
                    return true;  
  
                case DragEvent.ACTION_DROP:  
                    System.out.println("ACTION_DROP----------------");  
                    isOperated = true;
                    
                    ClipData.Item item = event.getClipData().getItemAt(0);  
                    CharSequence dragData = item.getText();  
                    v.setBackgroundResource(R.drawable.quesom_border_shape);
                    ((TextView)v).setText(dragData);
                    v.invalidate();  
                    
                    String key = (String)(v.getTag());
                    mQuestionEntity.mAnswerState.matchAnswers.put(key, dragData.toString());
                    Log.v(TAG, "put:" + " key-" + key + " value-" + dragData.toString() + " ------------" + mQuestionEntity.mAnswerState.matchAnswers.toString());
                    
                    return false;  
  
                case DragEvent.ACTION_DRAG_ENDED:  
                    System.out.println("ACTION_DRAG_ENDED----------------");  
                    TextView tv = (TextView) v;
                    if(TextUtils.isEmpty(tv.getText())) {
                    	v.setBackgroundResource(R.drawable.quesom_bg_shape);
                    } else {
                    	v.setBackgroundResource(R.drawable.quesom_border_shape);
                    }
                    
                    v.invalidate();  
                    return true;  
                    
                default:  
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");  
                    
                    break;  
            }  
            
            return true;  
        }
    }
    
    private class SortDragEventListener implements OnDragListener {
    	  
        public boolean onDrag(View v, DragEvent event) {  
            final int action = event.getAction();  
            QuestionOptionSort vSort = (QuestionOptionSort) v;
            
            switch (action) {  
  
                case DragEvent.ACTION_DRAG_STARTED:  
                    System.out.println("ACTION_DRAG_STARTED----------------");  
                    
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) {
                    	
                    	mCurrentDragingObj.getLeftIndex().setText("");
                        mCurrentDragingObj.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape3);
                        mCurrentDragingObj.getRightContent().setText("");
                        mCurrentDragingObj.getRightContent().setBackgroundResource(R.drawable.quesom_bg_shape);
                        
                        v.invalidate();  
                        return true;
                    } else {  
                        return false;  
                    } 
  
                case DragEvent.ACTION_DRAG_ENTERED:  
                    System.out.println("ACTION_DRAG_ENTERED----------------");
                    
                    if(!v.equals(mCurrentDragingObj)) {
                    	
                    	mOriginalLeftIndex = vSort.getLeftIndex().getText().toString();
                    	mOriginalRightContent = vSort.getRightContent().getText().toString();
                    	
                    	vSort.getLeftIndex().setText("");
                    	vSort.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape2);
                    	vSort.getRightContent().setText("");
                    	vSort.getRightContent().setBackgroundResource(R.drawable.quesom_bg_shape2);
                    	
                    	mCurrentDragingObj.getLeftIndex().setText(mOriginalLeftIndex);
                    	mCurrentDragingObj.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape);
                    	mCurrentDragingObj.getRightContent().setText(mOriginalRightContent);
                    	mCurrentDragingObj.getRightContent().setBackgroundResource(R.drawable.quesom_border_shape);
                    	
                    	v.invalidate();  
                    	return true; 
                    } else {
                    	return false;
                    }
                    
  
                case DragEvent.ACTION_DRAG_LOCATION:  
                    return true;  
  
                case DragEvent.ACTION_DRAG_EXITED:  
                    System.out.println("ACTION_DRAG_EXITED----------------");  
                    
                    if(!v.equals(mCurrentDragingObj)) {
                    	vSort.getLeftIndex().setText(mOriginalLeftIndex);
                    	vSort.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape);
                    	vSort.getRightContent().setText(mOriginalRightContent);
                    	vSort.getRightContent().setBackgroundResource(R.drawable.quesom_border_shape);
                    	
                    	mCurrentDragingObj.getLeftIndex().setText("");
                    	mCurrentDragingObj.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape3);
                    	mCurrentDragingObj.getRightContent().setText("");
                    	mCurrentDragingObj.getRightContent().setBackgroundResource(R.drawable.quesom_bg_shape);
                    	
                    	v.invalidate();  
                    	return true;  
                    } else {
                    	return false;
                    }
                    
  
                case DragEvent.ACTION_DROP:  
                    System.out.println("ACTION_DROP----------------");  
                    isOperated = true;
                    
                    if(!v.equals(mCurrentDragingObj)) {
                    	
                    	ClipData.Item item = event.getClipData().getItemAt(0);  
                    	String leftIndex = item.getIntent().getStringExtra("left");
                    	String rightContent = item.getIntent().getStringExtra("right");
                    	
                    	System.out.println(leftIndex + "----------" + rightContent);
                    	
                    	mCurrentLeftIndex = vSort.getLeftIndex().getText().toString();
                    	mCurrentRightContent = vSort.getRightContent().getText().toString();
                    	
                    	vSort.getLeftIndex().setText(leftIndex);
                    	vSort.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape);
                    	vSort.getRightContent().setText(rightContent);
                    	vSort.getRightContent().setBackgroundResource(R.drawable.quesom_border_shape);
                    	
                    	mCurrentDragingObj.getLeftIndex().setText(mOriginalLeftIndex);
                    	mCurrentDragingObj.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape);
                    	mCurrentDragingObj.getRightContent().setText(mOriginalRightContent);
                    	mCurrentDragingObj.getRightContent().setBackgroundResource(R.drawable.quesom_border_shape);
                    	
                    	v.invalidate();
                    	
                    	mQuestionEntity.mAnswerState.sortAnswers.clear();
                    	for(int i = 0 ; i < getChildCount(); i ++) {
                    		QuestionOptionSort inner = (QuestionOptionSort) getChildAt(i);
                    		String index = inner.getLeftIndex().getText().toString();
                    		String content = inner.getRightContent().getText().toString();
                    		mQuestionEntity.mAnswerState.sortAnswers.add(new String[] {index, content});
                    		Log.v(TAG, "add: " + index + "--" + content);
                    	}
                    	
                    	return true;
                    } else {
                    	return false;
                    }
                    
  
                case DragEvent.ACTION_DRAG_ENDED:  
                    System.out.println("ACTION_DRAG_ENDED----------------");  
                    
                    if(mCurrentDragingObj == null) return false; 
                    
                    String content = mCurrentDragingObj.getRightContent().getText().toString();
                    
                    if(TextUtils.isEmpty(content)) {
                    	mCurrentDragingObj.getLeftIndex().setText(mCurrentLeftIndex);
                    	mCurrentDragingObj.getLeftIndex().setBackgroundResource(R.drawable.question_option_bg_shape);
                    	mCurrentDragingObj.getRightContent().setText(mCurrentRightContent);
                    	mCurrentDragingObj.getRightContent().setBackgroundResource(R.drawable.quesom_border_shape);
                    }
                    
                    v.invalidate();  
                    return true;  
                    
                default:  
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");  
                    
                    break; 
            }  
            
            return false;  
        }
    }

	@Override
	public void pre(Object... objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaskResult async(int what, BaseTask baseTask) {
		
		TaskResult result = null;
		
		switch(what) {
		case 0x0:
			mCurrentInnerType = QuestionType.MATCHING;
			QuestionOptionMatch qom = (QuestionOptionMatch) mInflater.inflate(R.layout.match_option_item, null);
			mOptionMatchList.add(qom);
			int currentIndex = mOptionMatchList.size();
			if(currentIndex <= 3) {
				qom.setIsDragTarget(true);
				qom.getDragTarget().setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.quesom_border_shape2));
				qom.setLongClickListener(mMatchDragTrigger);
			} else {
				qom.setDragListener(mMatchDragLisener);
			}
			result = baseTask.new TaskResult();
			result.setResult(qom);
			mChildren.add(qom);
			return result;
		case 0x2:
			mCurrentInnerType = QuestionType.SINGLE;
			View v = mInflater.inflate(R.layout.index_option_item, null);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			llp.setMargins(0, 0, 0, 60);
			v.setLayoutParams(llp);
			QuestionOptionIndex index = (QuestionOptionIndex) v.findViewById(R.id.question_option_index);
			mOptionIndexList.add(index);
			index.mOptionIndex = mOptionIndexList.size() - 1;
			result = baseTask.new TaskResult();
			result.setResult(v);
			mChildren.add(v);
			return result;
		case 0x3:
			mCurrentInnerType = QuestionType.SORT;
			QuestionOptionSort vSort = (QuestionOptionSort) mInflater.inflate(R.layout.sort_option_item, null);
			LinearLayout.LayoutParams llpSort = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			llpSort.setMargins(0, 0, 0, 60);
			vSort.setLayoutParams(llpSort);
			vSort.setLongClickListener(mSortDragTrigger);
			vSort.setDragListener(mSortDragListener);
			result = baseTask.new TaskResult();
			result.setResult(vSort);
			mChildren.add(vSort);
			return result;
		default:
			return null;
		}
	}

	@Override
	public void post(Message msg) {
		BaseTask baseTask = (BaseTask) msg.obj;
		
		switch(msg.what) {
		case 0x0:
			String content = (String)baseTask.param;
			QuestionOptionMatch result0 = (QuestionOptionMatch) (baseTask.result.getResult());
			result0.setLeftContent(content);

			TextView tvt = result0.getDragTarget();
			tvt.setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseConfigUitl.Aa);
			TextView tvc = result0.getDragContainer();
			tvc.setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseConfigUitl.Aa);
			
			if(mQuestionEntity.mAnswerState.matchAnswers.containsKey(content)) {
				String rightContent = mQuestionEntity.mAnswerState.matchAnswers.get(content);
				result0.setRightContent(rightContent);
				result0.getDragContainer().setBackgroundResource(R.drawable.quesom_border_shape);
			} 
			addView(result0);
			break;
		case 0x2:
			String indexContent = (String)baseTask.param;
			View result2 = (View) (baseTask.result.getResult());
			
			final QuestionOptionIndex index = (QuestionOptionIndex) result2.findViewById(R.id.question_option_index);
			//index.setText((index.mOptionIndex + 1) + "");
			index.setOptionIndexString(index.mOptionIndex);
			index.setOnSelectedCallback(this);
			index.mOptionContent = indexContent;
			
			TextView indexContentTv = (TextView) result2.findViewById(R.id.question_option_content);
			indexContentTv.setText(indexContent);
			indexContentTv.setTextSize(BaseConfigUitl.Aa);
			indexContentTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					index.performClick();
				}
			});
			
//			if(mQuestionEntity.mAnswerState.indexAnswers.contains(index.mOptionContent)) {
			if(mQuestionEntity.mAnswerState.indexAnswers.contains(index.getOptionIndexString(index.mOptionIndex))) {
				index.setSelected(true);
			}
			
			addView(result2);
			break;
		case 0x3:
			QuestionOptionSort result3 = null;
			String sortContent = (String) baseTask.param;
			result3 = (QuestionOptionSort) (baseTask.result.getResult());
			mOptionSortList.add(result3);
			
			TextView tv = result3.getRightContent();
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseConfigUitl.Aa);
			
			if(mQuestionEntity.mAnswerState.sortAnswers.size() != 0) {
				String[] inner = mQuestionEntity.mAnswerState.sortAnswers.get(mOptionSortList.size() - 1);
				result3.setLeftIndex(inner[0]);
				result3.setRightContent(inner[1]);
			} else {
				result3.setLeftIndex((mOptionSortList.size()) + "");
				result3.setRightContent(sortContent);
			}
			
			addView(result3);
			break;
		}
		
	}
	
	public void setTextSize(int size) {

		switch(mCurrentInnerType) {
		case QuestionType.SINGLE:
			
			for(View view : mChildren) {
				LinearLayout v = (LinearLayout) view;
				TextView tv = (TextView) v.findViewById(R.id.question_option_content);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			}
			
			break;
		case QuestionType.MATCHING:
			
			for(View view : mChildren) {
				QuestionOptionMatch v = (QuestionOptionMatch) view;
				TextView tvt = v.getDragTarget();
				tvt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
				TextView tvc = v.getDragContainer();
				tvc.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			}
			
			break;
		case QuestionType.SORT:
			
			for(View view : mChildren) {
				QuestionOptionSort v = (QuestionOptionSort) view;
				TextView tv = v.getRightContent();
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			}
			
			break;
		}
	}

	public void setOnQuestionAnsweredMonitor(QuestionAnsweredMonitor monitor) {
		mQuestionAnsweredMonitor = monitor;
	}

	public interface QuestionAnsweredMonitor {

		public void onQuestionAnswered(Question question);

	}

}

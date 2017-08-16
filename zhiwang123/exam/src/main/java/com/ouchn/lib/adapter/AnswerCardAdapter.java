package com.ouchn.lib.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ouchn.lib.R;
import com.ouchn.lib.adapter.holder.AnswerCardItemHolder;
import com.ouchn.lib.entity.Question;


public class AnswerCardAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<Question> mList;
	
	public AnswerCardAdapter(Context context, ArrayList<Question> dataList) {
		mContext = context;
		mList = dataList;
	}
	
	public ArrayList<Question> getDataList() {
		return mList;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if(mList != null) {
			count = mList.size();
		}
		return count;
	}

	@Override
	public Question getItem(int position) {
		return (mList == null ? null : mList.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AnswerCardItemHolder holder = null;
		
		if(convertView == null) {
			 convertView = LayoutInflater.from(mContext).inflate(R.layout.answer_card_item, null);
			 holder = new AnswerCardItemHolder(convertView);
			 convertView.setTag(holder);
		} else {
			holder = (AnswerCardItemHolder) convertView.getTag();
		}
		
		holder.mIndex.setText(String.valueOf(position + 1));
		
		if(mList.get(position).mAnswerState.isAnswered()) {
			holder.mIndex.setBackgroundResource(R.drawable.question_option_selected_bg_shape);
			holder.mIndex.setTextColor(mContext.getResources().getColor(R.color.main_white));
		} else {
			holder.mIndex.setBackgroundResource(R.drawable.question_option_bg_shape);
			holder.mIndex.setTextColor(mContext.getResources().getColor(R.color.main_blue));
		}
		
		return convertView;
	}
	
}

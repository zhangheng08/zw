package com.ouchn.lib.adapter;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ouchn.lib.R;
import com.ouchn.lib.adapter.holder.QuestionDepotListItemHolder;
import com.ouchn.lib.widget.DepotItem;
import com.ouchn.lib.widget.DepotItem.ItemActionUpCallback;
import com.ouchn.lib.entity.QuestionDepot;


public class QuestionDepotListAdapter extends BaseAdapter implements ItemActionUpCallback {
	
	private static final String TAG = "QuestionDepotListAdapter";

	private FragmentActivity mContext;
	private ArrayList<QuestionDepot> mList;
	
	private int mCheckedItemIndex;
	
	public QuestionDepotListAdapter(FragmentActivity context, ArrayList<QuestionDepot> dataList) {
		mContext = context;
		mList = dataList;
	}
	
	public ArrayList<QuestionDepot> getDataList() {
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
	public QuestionDepot getItem(int position) {
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
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		QuestionDepot qd = mList.get(position);
		QuestionDepotListItemHolder holder = null;
		
		if(convertView == null) {
			DepotItem root = (DepotItem)mContext.getLayoutInflater().inflate(R.layout.depot_item, null);
			holder = new QuestionDepotListItemHolder(root);
			root.setOnItemActionUpCallback(this);
			convertView = root;
			convertView.setTag(holder);
		} else {
			holder = (QuestionDepotListItemHolder) convertView.getTag();
		}
		
		holder.mCurrentIndex = position;
		holder.mDepotName.setText(qd.getName());
		holder.mDepotAnsweredProgress.setMax(100);
		holder.mDepotAnsweredProgress.setProgress(0);
		holder.setDepot(qd);
		
		if(qd.getQuestionList() == null) {
			((DepotItem) convertView).loadQuestionList(mContext);
		}
		
		if(mCheckedItemIndex == position) {
			((DepotItem) convertView).isChecked(true);
		} else {
			((DepotItem) convertView).isChecked(false);
		}
		
		return convertView;
	}

	@Override
	public void onItemTouchActionUp(int index, DepotItem item) {
		Log.v(TAG, "-------------onItemTouchActionUp------------" + index);
		mCheckedItemIndex = index;
		notifyDataSetChanged();
	}
	
}

package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.QuestionZW;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class ExamResListAdpater extends BaseZwAdapter {

    private static final String TAG = "ExamEListAdpater";

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<QuestionZW> mDataList;

    public ExamResListAdpater(Context c, ArrayList<QuestionZW> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mDataList = list;
    }

    public void setDataList(ArrayList<QuestionZW> dataList) {

        if(mDataList != null) mDataList.clear();
        else mDataList = new ArrayList<>();
        mDataList.addAll(dataList);

    }

    public ArrayList<QuestionZW> getDataList() {

        return mDataList;
    }

    public void addDataList(ArrayList<QuestionZW> dataList) {

        if(mDataList == null) mDataList = new ArrayList<>();
        mDataList.addAll(dataList);

    }

    public void removeList(ArrayList<QuestionZW> subList) {

        if(mDataList == null || mDataList.size() < subList.size()) return;
        mDataList.removeAll(subList);

    }

    @Override
    public int getCount() {
        return  mDataList.size();
    }

    @Override
    public QuestionZW getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        QuestionZW q = mDataList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.exam_res_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }


        holder.num.setText(mContext.getString(R.string.exam_res_num, position + 1));
        holder.title.setText(q.title);

        if(q.userAnswers.size() != 0) {

            holder.status.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.status.setText(R.string.exam_res_state_ok);

            StringBuffer answerInfo = new StringBuffer("");

            for(QuestionZW.ItemAnswer itemAnswer : q.userAnswers) {

                answerInfo.append(itemAnswer.content + "\n");

            }

            holder.myAnswer.setText(answerInfo.toString());

        } else {

            holder.status.setTextColor(mContext.getResources().getColor(R.color.colorPrimary_red));
            holder.myAnswer.setText("");
            holder.status.setText(R.string.exam_res_state_no);

        }

        if(q.rightAnswers.size() != 0) {

            StringBuffer answerInfo = new StringBuffer("");

            for(QuestionZW.ItemAnswer itemAnswer : q.rightAnswers) {

                answerInfo.append(itemAnswer.content + "\n");

            }

            holder.rightAnswer.setText(answerInfo.toString());

        }

        holder.score.setText(q.score + "");

        if(q.score != 0) holder.myIcon.setImageResource(R.drawable.res_y);
        else holder.myIcon.setImageResource(R.drawable.res_n);

        return convertView;
    }

    public class CellHolder {

        TextView num;
        TextView title;
        TextView status;
        TextView score;
        ImageView myIcon;
        TextView myAnswer;
        TextView rightAnswer;

        public CellHolder(View cellView) {

            num = (TextView) cellView.findViewById(R.id.exam_res_q_num);
            title = (TextView) cellView.findViewById(R.id.exam_res_q_t);
            status = (TextView) cellView.findViewById(R.id.exam_res_finish_status);
            score = (TextView) cellView.findViewById(R.id.exam_res_q_score);
            myIcon = (ImageView) cellView.findViewById(R.id.exam_res_my_icon);
            myAnswer = (TextView) cellView.findViewById(R.id.exam_res_my_content);
            rightAnswer = (TextView) cellView.findViewById(R.id.exam_res_right_content);

        }

    }


}

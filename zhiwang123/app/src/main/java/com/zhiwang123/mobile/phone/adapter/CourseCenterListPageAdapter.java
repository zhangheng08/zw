package com.zhiwang123.mobile.phone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.CourseIntroduceActivity;
import com.zhiwang123.mobile.phone.activity.ExamPaperActivity;
import com.zhiwang123.mobile.phone.activity.VideoPlayActivity;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ty on 2016/6/22.
 */
public class CourseCenterListPageAdapter extends BaseZwAdapter {

    private static final String TAG = "CourseCenterListPageAdapter";

    private Activity mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mCenterCourseList;

    public CourseCenterListPageAdapter(Activity c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mCenterCourseList = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<Course> dataList) {

        if(mCenterCourseList != null) mCenterCourseList.clear();
        else mCenterCourseList = new ArrayList<>();
        mCenterCourseList.addAll(dataList);

    }

    public void addDataList(ArrayList<Course> dataList) {

        if(mCenterCourseList == null) mCenterCourseList = new ArrayList<>();
        mCenterCourseList.addAll(dataList);

    }

    @Override
    public int getCount() {
        return  mCenterCourseList.size();
    }

    @Override
    public Course getItem(int position) {
        return mCenterCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course c = mCenterCourseList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.course_center_cell, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }


        Glide.with(mContext).load(c.picture).crossFade().into(holder.imgv);

        holder.c = c;
        holder.courseId = c.courseid;
        holder.titleTv.setText(c.name);
        holder.teacherTv.setText(mContext.getString(R.string.course_teacher, c.teacherName));
        holder.buyDateTv.setText(mContext.getString(R.string.center_to_when_buy, c.creatDate));

        if(c.isHaveTestPaper && c.isHaveExamNum && c.judgeWhetherExam != StaticConfigs.ISNULL) {

            holder.toExam.setVisibility(View.VISIBLE);

            switch (c.judgeWhetherExam) {
                case StaticConfigs.ISEXAM:
                    holder.toExamTv.setText(R.string.center_to_exam);
                    break;
                case StaticConfigs.ISTEST:
                    holder.toExamTv.setText(R.string.center_to_test);
                    break;
            }

        } else {

            holder.toExam.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class CellHolder {

        public Course c;

        public String courseId;
        public TextView titleTv;
        public TextView teacherTv;
        public TextView buyDateTv;
        public ImageView imgv;
        public TextView toExamTv;
        public View toStudy;
        public View toExam;
        public View detailContent;

        public CellHolder(View cellView) {

            titleTv = (TextView) cellView.findViewById(R.id.center_title);
            teacherTv = (TextView) cellView.findViewById(R.id.center_sub_teacher);
            buyDateTv = (TextView) cellView.findViewById(R.id.center_sub_when_buy);
            toStudy = cellView.findViewById(R.id.center_to_study_btn);
            toExam = cellView.findViewById(R.id.center_to_exam_btn);
            toExamTv = (TextView) cellView.findViewById(R.id.center_to_exam_txt);
            imgv = (ImageView) cellView.findViewById(R.id.center_left_img);
            detailContent = cellView.findViewById(R.id.center_detail_content);

            toExam.setOnClickListener(mCellBtnClick);
            detailContent.setOnClickListener(mCellBtnClick);
            imgv.setOnClickListener(mCellBtnClick);

        }

        private View.OnClickListener mCellBtnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = null;

                switch(v.getId()) {

                    case R.id.center_to_exam_btn:

                        intent = new Intent(mContext, ExamPaperActivity.class);
                        intent.putExtra(StaticConfigs.KEY_EXAM_PAPER, c.buyCourseId);
                        mContext.startActivity(intent);

                        break;
                    case R.id.center_to_study_btn:

                    case R.id.center_detail_content:

                        intent = new Intent(mContext, VideoPlayActivity.class);
                        intent.putExtra(VideoPlayActivity.EXTRA_KEY_VNAME, c.name);
                        intent.putExtra(VideoPlayActivity.EXTRA_KEY_VIMG, c.picture);
                        intent.putExtra(VideoPlayActivity.EXTRA_KEY_BUYCOURSEID, c.buyCourseId);
                        mContext.startActivity(intent);

                        break;

                    case R.id.center_left_img:

                        String courseId = c.courseid;
                        boolean isFav = c.isInFavorite;
                        intent = new Intent(mContext, CourseIntroduceActivity.class);
                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_CID, courseId);
                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_COURSE_TYPE, c.courseType);
                        intent.putExtra(CourseIntroduceActivity.EXTRA_KEY_FAVSTATE, isFav);
                        mContext.startActivity(intent);

                        break;
                }
            }
        };

    }


}

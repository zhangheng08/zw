package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.activity.VideoPlayActivity;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ty on 2016/6/22.
 */
public class TeachCoursesAdapter extends BaseZwAdapter {

    private static final String TAG = "TeachCoursesAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mTeachCourses;
    private String teachPlanId;

    public TeachCoursesAdapter(Context c, String teachPlanId, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        this.teachPlanId = teachPlanId;
        mTeachCourses = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public void setDataList(ArrayList<Course> dataList) {

        if(mTeachCourses != null) mTeachCourses.clear();
        else mTeachCourses = new ArrayList<>();
        mTeachCourses.addAll(dataList);

    }

    public ArrayList<Course> getDataList() {

        return mTeachCourses;
    }

    public void addDataList(ArrayList<Course> dataList) {

        if(mTeachCourses == null) mTeachCourses = new ArrayList<>();
        mTeachCourses.addAll(dataList);

    }

    public void removeList(ArrayList<Course> subList) {

        if(mTeachCourses == null || mTeachCourses.size() < subList.size()) return;
        mTeachCourses.removeAll(subList);

    }

    @Override
    public int getCount() {
        return  mTeachCourses.size();
    }

    @Override
    public Course getItem(int position) {
        return mTeachCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course c = mTeachCourses.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.teach_plan_sub_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        holder.c = c;
        holder.title.setText(c.courseName);
        holder.num.setText(mContext.getString(R.string.tachp_sub_course_number, c.courseNum));
        holder.score.setText(mContext.getString(R.string.tachp_sub_course_score, c.courseCredit));
        holder.progress.setText(mContext.getString(R.string.tachp_sub_course_prog, c.studyPace));
        holder.duration.setText(mContext.getString(R.string.tachp_sub_course_minitue, c.studyMinute));
        holder.progressBar.setProgress(c.studyPace);

        return convertView;
    }

    public class CellHolder {

        public Course c;
        public TextView title;
        public TextView num;
        public TextView score;
        public TextView progress;
        public TextView duration;
        public SeekBar  progressBar;
        public View goToLearn;

        public CellHolder(View cellView) {

            title = (TextView) cellView.findViewById(R.id.tachp_sub_name);
            num = (TextView) cellView.findViewById(R.id.tachp_sub_course_num);
            score = (TextView) cellView.findViewById(R.id.tachp_sub_course_sc);
            duration = (TextView) cellView.findViewById(R.id.tachp_sub_course_min);
            progress = (TextView) cellView.findViewById(R.id.tachp_sub_course_pro);
            progressBar = (SeekBar) cellView.findViewById(R.id.tachp_sub_courser_probar);
            goToLearn = cellView.findViewById(R.id.tachp_sub_go_to_learn);

            goToLearn.setOnClickListener(mOnclick);

        }

        private View.OnClickListener mOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra(VideoPlayActivity.EXTRA_KEY_VNAME, c.courseName);
                intent.putExtra(VideoPlayActivity.EXTRA_KEY_VIMG, c.picture);
                intent.putExtra(VideoPlayActivity.EXTRA_KEY_TEACHERPLANID, teachPlanId);
                intent.putExtra(VideoPlayActivity.EXTRA_KEY_COURSEID, c.courseid);
                mContext.startActivity(intent);

            }
        };

    }


}

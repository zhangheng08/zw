package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.download.Downloader;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.cc.ConfigUtil;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRoot;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.db.AsyncDBHelper;
import com.zhiwang123.mobile.phone.service.ZWDownLoaderService;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class CourseCenterCubCAdapter extends BaseZwAdapter {

    private static final String TAG = "ChapterListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private CourseRoot mCourseRoot;
    private ArrayList<Course> mChapterList;

    public String picUrl;


    public CourseCenterCubCAdapter(Context c, CourseRoot cr) {
        super(c);
        mContext = c;
        mCourseRoot = cr;
        mInflater = LayoutInflater.from(c);
    }

    public CourseCenterCubCAdapter(Context c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mChapterList = list;
    }

    @Override
    public int getCount() {
        return  mChapterList.size();
    }

    @Override
    public Course getItem(int position) {
        return mChapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Course course = mChapterList.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.course_center_sub_c_list_item, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }

        if(position == 0) {
            holder.sepTopv.setVisibility(View.INVISIBLE);
        } else if(position == mChapterList.size() - 1) {
            holder.sepBtmv.setVisibility(View.INVISIBLE);
        } else {
            holder.sepTopv.setVisibility(View.VISIBLE);
            holder.sepBtmv.setVisibility(View.VISIBLE);
        }

        holder.entity = course;
        holder.courseId = course.courseid;
        holder.cckey = course.ccKey;
        holder.nameTxv.setText(course.courseName);

        switch (course.status) {

            case 0:
                holder.statusTx.setText(R.string.chapter_status_0);
                holder.statusTx.setTextColor(mContext.getResources().getColor(R.color.zw_green));
                holder.statusTx.setBackgroundResource(R.drawable.exam_e_list_shape_3);
                break;
            case 1:

                holder.statusTx.setText(R.string.chapter_status_1);
                holder.statusTx.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.statusTx.setBackgroundResource(R.drawable.exam_e_list_shape_1);

                break;
            case 2:

                holder.statusTx.setText(R.string.chapter_status_2);
                holder.statusTx.setTextColor(mContext.getResources().getColor(R.color.zw_red));
                holder.statusTx.setBackgroundResource(R.drawable.exam_e_list_shape_2);

                break;

        }

        switch (course.downloadState) {

            case Course.LOADING:

                holder.stateLoading.setVisibility(View.VISIBLE);
                holder.stateInLocal.setVisibility(View.GONE);
                holder.stateInCloud.setVisibility(View.GONE);

                break;

            case Course.IN_CLOUD:

                holder.stateLoading.setVisibility(View.GONE);
                holder.stateInLocal.setVisibility(View.GONE);
                holder.stateInCloud.setVisibility(View.VISIBLE);

                break;

            case Course.IN_LOCAL:

                holder.stateLoading.setVisibility(View.GONE);
                holder.stateInLocal.setVisibility(View.VISIBLE);
                holder.stateInCloud.setVisibility(View.GONE);

                break;
        }

        return convertView;
    }

    private class CellHolder {

        public Course entity;
        public TextView nameTxv;
        public TextView statusTx;
        public String courseId;
        public String cckey;
        public View sepTopv;
        public View sepBtmv;
        public View downloadBtn;

        public ImageView stateInCloud;
        public ImageView stateInLocal;
        public ProgressBar stateLoading;

        public CellHolder(View cellView) {

            downloadBtn = cellView.findViewById(R.id.chapter_item_download_state_con);
            nameTxv = (TextView) cellView.findViewById(R.id.chapter_name);
            statusTx = (TextView) cellView.findViewById(R.id.chapter_status);
            sepTopv = cellView.findViewById(R.id.sep_top);
            sepBtmv = cellView.findViewById(R.id.sep_btm);
            stateInCloud = (ImageView) cellView.findViewById(R.id.chapter_item_cloud);
            stateInLocal = (ImageView) cellView.findViewById(R.id.chapter_item_local);
            stateLoading = (ProgressBar) cellView.findViewById(R.id.chapter_item_buffer);

//            nameTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            statusTx.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));

            downloadBtn.setOnClickListener(onClickListener);

        }


        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(entity.status == 0) {

                    Toast.makeText(mContext, "请先激活课程", Toast.LENGTH_SHORT).show();
                    return;

                } else if (entity.status == 2) {

                    Toast.makeText(mContext, "视频已过期", Toast.LENGTH_SHORT).show();
                    return;

                }

                entity.downloadState = Course.LOADING;

                notifyDataSetChanged();

                startLoadingVideo(entity);

            }
        };

    }

    public void startLoadingVideo(final Course course) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            String path = Environment.getExternalStorageDirectory() + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/");
            File savePath = new File(path);
            if (!savePath.exists()) savePath.mkdirs();
            String filePath = path.concat(course.courseName).concat(".pcm");
            File file = new File(filePath);

//            if(file.exists()) return;

            Downloader downloader = new Downloader(file, course.ccKey, ConfigUtil.USERID, ConfigUtil.API_KEY);
            DownloadTask taskItem = new DownloadTask();
            taskItem.videoId = course.ccKey;
            taskItem.title = course.courseName;
            taskItem.cover = picUrl;
            taskItem.filePath = filePath;
            taskItem.cover = course.picture;
            taskItem.downloader = downloader;

            AsyncDBHelper.saveVideoCacheDB(mContext, taskItem, new AsyncDBHelper.UIHandler<DownloadTask>() {
                @Override
                public void processMessage(int what, DownloadTask cache) {

                    switch(what) {
                        case SEND_SAVE_OK:

                            ZWDownLoaderService.mTaskItems.put(course.ccKey, cache);
                            Intent intent = new Intent(mContext, ZWDownLoaderService.class);
                            intent.putExtra(ZWDownLoaderService.VID, course.ccKey);
                            mContext.startService(intent);
                            Toast.makeText(mContext, R.string.chapter_loading, Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
            });

        }
    }

}

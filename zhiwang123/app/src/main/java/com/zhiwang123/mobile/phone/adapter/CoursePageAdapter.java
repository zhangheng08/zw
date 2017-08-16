package com.zhiwang123.mobile.phone.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhiwang123.mobile.R;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseRecommand;
import com.zhiwang123.mobile.phone.bean.CourseRoot;
import com.zhiwang123.mobile.phone.bean.parser.CourseParser;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ty on 2016/6/22.
 */
public class CoursePageAdapter extends BaseZwAdapter {

    private static final String TAG = "CoursePageAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private CourseRoot mCourseRoot;
    private HashMap<String, SoftReference<CourseRecommand>> mCacheData;
    private ArrayList<Course> mRecommandCourses;


    public CoursePageAdapter(Context c, CourseRoot cr) {
        super(c);
        mContext = c;
        mCourseRoot = cr;
        mInflater = LayoutInflater.from(c);
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    public CoursePageAdapter(Context c, ArrayList<Course> list) {

        super(c);
        mContext = c;
        mInflater = LayoutInflater.from(c);
        mRecommandCourses = list;
        mCacheData = new HashMap<String, SoftReference<CourseRecommand>>();
    }

    @Override
    public int getCount() {
        return  mRecommandCourses.size();
    }

    @Override
    public Course getItem(int position) {
        return mRecommandCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(TAG, "getView............");

        Course c = mRecommandCourses.get(position);//mCourseRoot.dataobject.get(position);
        CellHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.course_fragment_page_cell_reuse, null);
            holder = new CellHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (CellHolder) convertView.getTag();
        }


        CourseRecommand recommand = null;
        if(mCacheData.get(c.name) != null) {
            recommand = mCacheData.get(c.name).get();
        }

        if(recommand == null) {
            holder.loadRecommandCourse(c.pkId, c.name);
            if(holder.subAdapter != null) holder.subAdapter.clearDataset();
        } else {
            if(holder.subAdapter == null) {
                holder.subAdapter = new CourseSubAdapter(mContext, recommand.dataobject);
                holder.recyclerView.setAdapter(holder.subAdapter);
            } else {
                holder.subAdapter.setDataset(recommand.dataobject);
            }
            holder.subAdapter.notifyDataSetChanged();
        }

        holder.courseNameTxv.setText(c.name);
        holder.toMore.setOnClickListener(mOnToMoreClick);

        return convertView;
    }

    private class CellHolder {

        public TextView courseNameTxv;
        public View toMore;
        public RecyclerView recyclerView;
        public CourseSubAdapter subAdapter;
        public LinearLayout recommanContainer;

        public CellHolder(View cellView) {

            courseNameTxv = (TextView) cellView.findViewById(R.id.cell_course_root);
            toMore = cellView.findViewById(R.id.cell_to_more);
            recyclerView = (RecyclerView) cellView.findViewById(R.id.cell_recommand_container);
//            courseNameTxv.setTypeface(ZWTypefaceUtil.get(StaticConfigs.FONT_QD));
//            recommanContainer = (LinearLayout) cellView.findViewById(R.id.cell_recommand_container);

            LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
            recyclerViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(recyclerViewLayoutManager);
            recyclerView.setHasFixedSize(true);

        }

        private void loadRecommandCourse(final int pkId, final String name) {

            final JsonObjectRequest jsonRequest = new JsonObjectRequest(StaticConfigs.getUrlCourseList(pkId, "360*270", 1, 6), null,

                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            CourseRecommand recommand = new CourseParser<CourseRecommand>(new CourseRecommand()).parse(response);
                            mCacheData.put(name, new SoftReference<CourseRecommand>(recommand));
                            if(subAdapter == null) {
                                subAdapter = new CourseSubAdapter(mContext, recommand.dataobject);
                                recyclerView.setAdapter(subAdapter);
                            } else {
                                subAdapter.setDataset(recommand.dataobject);
                            }
                            subAdapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage() + "");
                        }
                    });

            mRequestQueue.add(jsonRequest);
        }
    }



//    public class RecommandViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView img;
//        public ImageView favoriteImg;
//        public TextView cellTitleTxv;
//        public TextView rmbValue;
//        public View rightMargin;
//
//
//        public RecommandViewHolder(View itemLayout) {
//            super(itemLayout);
//            img = (ImageView) itemLayout.findViewById(R.id.cell_recommand_img);
//            favoriteImg = (ImageView) itemLayout.findViewById(R.id.cell_recommand_favorite);
//            cellTitleTxv = (TextView) itemLayout.findViewById(R.id.cell_recommand_name);
//            rmbValue = (TextView) itemLayout.findViewById(R.id.rmb_value);
//            rightMargin = itemLayout.findViewById(R.id.right_margin);
//        }
//
//        public void fillData(final Course c) {
//
//            Log.d(TAG, c.picture);
//            Glide.with(mContext).load(c.picture).crossFade().into(img);
//            cellTitleTxv.setText(c.name);
//            rmbValue.setText(c.money + "");
//
//        }
//    }

    @Override
    public void notifyDataSetChanged() {
        Log.i(TAG, "notifyDataSetChanged..........");
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        Log.i(TAG, "notifyDataSetInvalidated..........");
        super.notifyDataSetInvalidated();
    }

    private View.OnClickListener mOnToMoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}

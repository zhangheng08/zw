package com.zhiwang123.mobile.phone.bean;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;

/**
 * Created by ty on 2016/6/22.
 */
public class CourseCart extends CourseEntity implements Serializable {

    public Course parentCourse;

    public RecyclerView viewList;

    public CourseCart() {
        super();
    }

}
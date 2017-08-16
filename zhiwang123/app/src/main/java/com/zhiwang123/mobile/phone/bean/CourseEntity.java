package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class CourseEntity extends BaseEntity<Course> {

    private static String TAG = "CourseEntity";

    public CourseEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList();
    }

}

package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class TeacherEntity extends BaseEntity<Teacher> {

    private static String TAG = TeacherEntity.class.getName();

    public TeacherEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList();
    }

}

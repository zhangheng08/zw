package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class ExamEntity extends BaseEntity<ExamPaper> {

    private static String TAG = ExamEntity.class.getName();

    public ExamEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList();
    }

}

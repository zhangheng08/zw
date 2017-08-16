package com.zhiwang123.mobile.phone.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class ExamECEntity extends BaseEntity<ExamECategory> implements Serializable {

    private static String TAG = "OrganEntity";

    public ExamECEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList();
    }

}

package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/12/7.
 */

public class TeachPlanEntity extends BaseEntity<TeachPlan> {

    public TeachPlanEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList();
    }

}

package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/22.
 */

public class ResultEntity<T extends BaseEntity> extends BaseEntity<T> {

    public ResultEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList<>();
    }


}

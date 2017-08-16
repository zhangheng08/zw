package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class PayTypeEntity extends BaseEntity<PayType> {

    private static String TAG = PayTypeEntity.class.getName();

    public PayTypeEntity() {

        state = false;
        message = "";
        dataobject = new ArrayList();
    }

}

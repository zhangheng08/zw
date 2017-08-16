package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/25.
 */

public class OrderEntity extends BaseEntity<Order> {

    public OrderEntity() {
        state = false;
        message = "";
        dataobject = new ArrayList();
    }


}

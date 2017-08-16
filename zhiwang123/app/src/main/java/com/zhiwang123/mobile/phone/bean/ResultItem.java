package com.zhiwang123.mobile.phone.bean;

import java.io.Serializable;

/**
 * Created by ty on 2016/11/30.
 */

public class ResultItem implements Serializable {

    public static final String NUMBER = "Number";
    public static final String TITLE = "Title";
    public static final String ORDER = "Order";

    public String number;
    public String title;
    public int order;

}

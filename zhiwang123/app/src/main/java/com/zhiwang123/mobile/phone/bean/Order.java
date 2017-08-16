package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/25.
 */

public class Order extends BaseEntity {

    public static final String ID = "Id";
    public static final String ORDERNUM = "OrderNum";
    public static final String CREATEDATE = "CreateDate";
    public static final String STATUS = "Status";
    public static final String STATUSNAME = "StatusName";
    public static final String MONEY = "Money";
    public static final String PAYKEYWORD = "PayKeyword";
    public static final String ORDERITEMS = "OrderItems";

    public static final int ORDER_STATUS_PAY_NO = 0;
    public static final int ORDER_STATUS_PAY_YES = 1;
    public static final int ORDER_STATUS_PAY_CLOSE = 2;

    public String id;
    public String orderNum;
    public String createDate;
    public int status;
    public String statusName;
    public int money;
    public String payKeyword;
    public ArrayList<Course> orderItems;



}

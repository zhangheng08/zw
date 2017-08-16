package com.zhiwang123.mobile.phone.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ty on 2016/11/17.
 */

public class BaseEntity<T extends BaseEntity> implements Serializable {

    public static final String STATE = "State";
    public static final String MESSAGE = "Message";
    public static final String DATA = "Data";
    public static final String DATAOBJECT = "DataObject";

    public boolean state;
    public String message;
    public String[] data;


    public ArrayList<T> dataobject;

}

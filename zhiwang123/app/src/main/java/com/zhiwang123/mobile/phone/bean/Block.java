package com.zhiwang123.mobile.phone.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ty on 2016/11/30.
 */

public class Block implements Serializable {

    public static final String TITLE = "Title";
    public static final String ORDER = "Order";
    public static final String QUESTIONS = "Questions";

    public String title;
    public int order;
    public ArrayList<QuestionZW> questions;


}

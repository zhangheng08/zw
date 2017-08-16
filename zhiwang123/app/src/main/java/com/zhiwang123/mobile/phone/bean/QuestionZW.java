package com.zhiwang123.mobile.phone.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ty on 2016/11/30.
 */

public class QuestionZW implements Serializable {

    public static final String ID = "Id";
    public static final String TITLE = "Title";
    public static final String QUESTIONTYPE = "QuestionType";
    public static final String SCORE = "Score";
    public static final String ANSWER = "Answer";
    public static final String ORDER = "Order";
    public static final String RESULTITEMS = "ResultItems";

    public static final String RIGHTANSWER = "RightAnwser";
    public static final String USERANSWER = "UserAnwser";
    public static final String CONTENT = "Content";

    public String id;
    public String title;
    public int questionType;
    public int score;
    public String answer;
    public int order;
    public ArrayList<ResultItem> resultItems;

    public ArrayList<ItemAnswer> rightAnswers;
    public ArrayList<ItemAnswer> userAnswers;


    public class ItemAnswer {

        public String content;

    }


}

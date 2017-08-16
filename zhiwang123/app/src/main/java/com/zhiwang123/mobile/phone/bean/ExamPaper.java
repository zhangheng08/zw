package com.zhiwang123.mobile.phone.bean;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/30.
 */

public class ExamPaper extends BaseEntity {

    public static final String ID = "Id";
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Description";
    public static final String TIMELONG = "TimeLong";
    public static final String TOTALSCORE = "TotalScore";
    public static final String QUESTIONNUM = "QuestionNum";
    public static final String PASSSCORE = "PassScore";
    public static final String BLOCKS = "Blocks";

    public static final String USERSCORE = "UserScore";

    public String id;
    public String title;
    public String description;
    public int timeLong;
    public int totalScore;
    public int questionNum;
    public int passScore;
    public int userScore;
    public ArrayList<Block> blocks;


}

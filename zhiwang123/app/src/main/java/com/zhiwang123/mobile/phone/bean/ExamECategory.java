package com.zhiwang123.mobile.phone.bean;

/**
 * Created by ty on 2016/12/12.
 */

public class ExamECategory extends BaseEntity {

    public static final String ID = "Id";
    public static final String TITLE = "Title";
    public static final String HAVECOURSEID = "HaveCourseId";
    public static final String STARTDATE = "StartDate";
    public static final String ENDDATE = "EndDate";
    public static final String HOUR = "Hour";
    public static final String STATE_INT = "State";
    public static final String STATENAME = "StateName";
    public static final String LEAVEEXAMNUM = "LeaveExamNum";

    public String id;
    public String title;
    public String haveCourseId;
    public String startDate;
    public String endDate;
    public int hour;
    public int stateInt;
    public String stateName;
    public int leaveExamNum;


}

package com.zhiwang123.mobile.phone.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ty on 2016/6/22.
 */
public class Course extends BaseEntity {

    public static final String TAG = "Course";

    public static final int IN_LOCAL = 0;
    public static final int LOADING = 1;
    public static final int IN_CLOUD = 2;

    public static final String ID = "Id";
    public static final String CONFIGID = "ConfigId";
    public static final String PKID = "PkId";
    public static final String NAME = "Name";
    public static final String MONEY = "Money";
    public static final String PREFERENTIAL = "Preferential";
    public static final String PICTURE = "Picture";
    public static final String COURSEID = "CourseId";
    public static final String COURSETYPE = "CourseType";
    public static final String COURSEHOUR = "CourseHour";
    public static final String HAVEEXAM = "HaveExam";
    public static final String CREATEDATE = "CreateDate";
    public static final String TEACHERS = "Teachers";
    public static final String CHILDCOURSES = "ChildCourses";
    public static final String COURSEDESCRIPTION = "CourseDescription";

    public static final String BUYCOURSEID = "BuyCourseId";
    public static final String ISHAVETESTPAPER = "IsHaveTestPaper";
    public static final String ISHAVEEXAMNUM = "IsHaveExamNum";
    public static final String JUDGEWHETHEREXAM = "JudgeWhetherExam";

    public static final String COURSENAME = "CourseName";
    public static final String COURSENUM = "CourseNum";
    public static final String COURSECREDIT = "CourseCredit";
    public static final String STUDYMINUTE = "StudyMinute";
    public static final String STUDYPACE = "StudyPace";

    public static final String CARDID = "CardId";
    public static final String STATUS = "Status";
    public static final String BUYTIME = "BuyTime";
    public static final String VALIDITYTIME = "ValidityTime";
    public static final String VALIDITYTOHOUR = "ValidityToHour";
    public static final String CCKEY = "CCkey";

    public static final String COURSES = "Courses";

    public static final String TEACHPLANID = "TeachPlanId";
    public static final String HAVECOURSEID = "HaveCourseId";

    public static final String CHILDCOURSENUM = "ChildCoursesNum";

    public static final String CATEGORYPKIDS = "CategoryPkIds";
    public static final String DESCRIPTION = "Description";
    public static final String TITLE = "Title";


    public String id;
    public String configid;
    public int pkId;
    public String name;
    public int money;
    public String picture;
    public String courseid;
    public int preferential;
    public boolean haveExam;
    public int courseType;
    public int courseHour;
    public String creatDate;
    public String teacherName;
    public String courseDescription;
    public ArrayList<Teacher> teachers;
    public ArrayList<CourseChild> childCourses;

    public String buyCourseId;
    public boolean isHaveTestPaper;
    public boolean isHaveExamNum;
    public int judgeWhetherExam;

    public String courseName;
    public int courseNum;
    public int courseCredit;
    public int studyMinute;
    public int studyPace;

    public String cardId;
    public int status;
    public String buyTime;
    public String validityTime;
    public String validitytohour;
    public String ccKey;

    public boolean isInFavorite = false;
    public boolean isInCart = false;
    public String favoriteId;
    public String cartId;

    public String teachPlanId;
    public String haveCourseId;

    public int childCourseNum;

    public String title;
    public String description;
    public String categoryPkIds;


    public int downloadState = IN_CLOUD;

    public ArrayList<Course> recmList;

    public class CourseChild implements Serializable {

        public String name;
        public String courseid;
        public int money;

    }

}

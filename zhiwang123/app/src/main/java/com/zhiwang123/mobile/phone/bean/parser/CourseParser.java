package com.zhiwang123.mobile.phone.bean.parser;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.zhiwang123.mobile.ZWApplication;
import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseEntity;
import com.zhiwang123.mobile.phone.bean.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/9.
 */

public class CourseParser<T extends CourseEntity> {

    private static final String TAG = "CourseParser";

    private T t;

    public CourseParser(T t) {

        this.t = t;

    }

    public T parse(JSONObject jsonObject) {

        try {
            if(jsonObject.has(T.STATE)) t.state = jsonObject.getBoolean(T.STATE);
            if(jsonObject.has(T.MESSAGE)) t.message = jsonObject.getString(T.MESSAGE);
            if(jsonObject.has(T.DATA)) {
                JSONArray jsonArray = jsonObject.getJSONArray(T.DATA);
                if(jsonArray != null && jsonArray.length() != 0) {
                    t.data = new String[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i ++) {
                        t.data[i] = jsonArray.getString(i);
                    }
                }
            }
            if(jsonObject.has(T.DATAOBJECT)) {
                Object obj = jsonObject.get(T.DATAOBJECT);
                if(obj instanceof JSONArray) {
                    JSONArray jsonArray = jsonObject.getJSONArray(T.DATAOBJECT);
                    if(jsonArray != null && jsonArray.length() != 0) {
                        t.dataobject = new ArrayList<Course>();
                        for(int i = 0; i < jsonArray.length(); i ++) {
                            JSONObject subJo = jsonArray.getJSONObject(i);
                            Course c = parseCourse(subJo);
                            t.dataobject.add(c);
                        }
                    }
                } else if(obj instanceof JSONObject) {
                    JSONObject subJo = jsonObject.getJSONObject(T.DATAOBJECT);
                    Course c = parseCourse(subJo);
                    t.dataobject = new ArrayList<>();
                    t.dataobject.add(c);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return t;
    }

    public Course parseCourse(JSONObject subJo) throws JSONException {

        Course c = new Course();

        if(subJo.has(Course.COURSES)) {

            JSONArray recmArr = subJo.getJSONArray(Course.COURSES);

            if(recmArr != null && recmArr.length() != 0) {

                ArrayList<Course> courses = new ArrayList<>();

                for(int i = 0; i < recmArr.length(); i ++) {

                    Course recmd = parseCourse(recmArr.getJSONObject(i));
                    courses.add(recmd);

                }

                c.recmList = courses;

            }

            if(subJo.has(Course.PKID)) c.pkId = subJo.getInt(Course.PKID);
            if(subJo.has(Course.NAME)) c.name = subJo.getString(Course.NAME);

            return c;

        }

        if(subJo.has(Course.ID)) c.id = subJo.getString(Course.ID);
        if(subJo.has(Course.CONFIGID)) c.configid = subJo.getString(Course.CONFIGID);
        if(subJo.has(Course.PKID)) c.pkId = subJo.getInt(Course.PKID);
        if(subJo.has(Course.NAME)) c.name = subJo.getString(Course.NAME);
        if(subJo.has(Course.PICTURE)) c.picture = subJo.getString(Course.PICTURE);
        if(subJo.has(Course.COURSEID)) c.courseid = subJo.getString(Course.COURSEID);
        if(subJo.has(Course.MONEY)) c.money = subJo.getInt(Course.MONEY);
        if(subJo.has(Course.PREFERENTIAL)) {
            c.preferential = subJo.getInt(Course.PREFERENTIAL);
            c.money = c.preferential;
        }
        if(subJo.has(Course.COURSETYPE)) c.courseType = subJo.getInt(Course.COURSETYPE);
        if(subJo.has(Course.COURSEHOUR)) c.courseHour = subJo.getInt(Course.COURSEHOUR);
        if(subJo.has(Course.HAVEEXAM)) c.haveExam = subJo.getBoolean(Course.HAVEEXAM);
        if(subJo.has(Course.CREATEDATE)) c.creatDate = subJo.getString(Course.CREATEDATE);
        if(subJo.has(Course.COURSEDESCRIPTION)) c.courseDescription = subJo.getString(Course.COURSEDESCRIPTION);
        if(subJo.has(Course.BUYCOURSEID)) c.buyCourseId = subJo.getString(Course.BUYCOURSEID);
        if(subJo.has(Course.ISHAVEEXAMNUM)) c.isHaveExamNum = subJo.getBoolean(Course.ISHAVEEXAMNUM);
        if(subJo.has(Course.ISHAVETESTPAPER)) c.isHaveTestPaper = subJo.getBoolean(Course.ISHAVEEXAMNUM);
        if(subJo.has(Course.JUDGEWHETHEREXAM)) c.judgeWhetherExam = subJo.getInt(Course.JUDGEWHETHEREXAM);
        if(subJo.has(Course.COURSENAME))  c.courseName = subJo.getString(Course.COURSENAME);
        if(subJo.has(Course.NAME)) c.courseName = subJo.getString(Course.NAME);


        if(subJo.has(Course.COURSENUM)) c.courseNum = subJo.getInt(Course.COURSENUM);
        if(subJo.has(Course.COURSECREDIT)) c.courseCredit = subJo.getInt(Course.COURSECREDIT);
        if(subJo.has(Course.STUDYMINUTE)) c.studyMinute = subJo.getInt(Course.STUDYMINUTE);
        if(subJo.has(Course.STUDYPACE)) c.studyPace = subJo.getInt(Course.STUDYPACE);

        if(subJo.has(Course.CHILDCOURSENUM)) c.childCourseNum = subJo.getInt(Course.CHILDCOURSENUM);

        if(subJo.has(Course.DESCRIPTION)) c.description = subJo.getString(Course.DESCRIPTION);
        if(subJo.has(Course.CATEGORYPKIDS)) c.categoryPkIds = subJo.getString(Course.CATEGORYPKIDS);
        if(subJo.has(Course.TITLE)) c.title = subJo.getString(Course.TITLE);

        if(subJo.has(Course.CARDID)) {

            c.cardId = subJo.getString(Course.CARDID);
            c.status = subJo.getInt(Course.STATUS);
            c.buyTime = subJo.getString(Course.BUYTIME);
            c.validityTime = subJo.getString(Course.VALIDITYTIME);
            c.validitytohour = subJo.getString(Course.VALIDITYTOHOUR);
            c.ccKey = subJo.getString(Course.CCKEY);

        }

        if(subJo.has(Course.TEACHPLANID)) {

            c.teachPlanId = subJo.getString(Course.TEACHPLANID);
            c.haveCourseId = subJo.getString(Course.HAVECOURSEID);
            c.ccKey = subJo.getString("CCKey");
            c.status = 1;

        }

        if(subJo.has(Course.TEACHERS)) {
            Object object = subJo.get(Course.TEACHERS);
            if(object instanceof String) {
                c.teacherName = (String) object;
            } else if(object instanceof JSONArray) {
                JSONArray ts = (JSONArray) object;
                if(ts != null && ts.length() != 0) {
                    c.teachers = new ArrayList<> ();
                    for(int k = 0; k < ts.length(); k ++) {
                        JSONObject teacherJo = ts.getJSONObject(k);
                        Teacher t = parseTeach(teacherJo);
                        c.teachers.add(t);
                    }
                }
            }
        }

        if(subJo.has(Course.CHILDCOURSES)) {
            JSONArray arrayChildCourses = subJo.getJSONArray(Course.CHILDCOURSES);
            if(arrayChildCourses != null && arrayChildCourses.length() != 0) {
                c.childCourses = new ArrayList<>();
                for(int w = 0; w < arrayChildCourses.length(); w ++) {
                    JSONObject child = arrayChildCourses.getJSONObject(w);
                    Course.CourseChild cc = c. new CourseChild();
                    cc.name = child.getString(Course.NAME);
                    if(child.has(Course.COURSEID)) cc.courseid = child.getString(Course.COURSEID);
                    if(child.has(Course.MONEY)) cc.money = child.getInt(Course.MONEY);
                    c.childCourses.add(cc);
                }
            }
        }

        SharedPreferences sp = ZWApplication.getAppContext().getSharedPreferences("favorite_kv", 0);
        String favoriteId = sp.getString(c.courseid, "");

        if(!TextUtils.isEmpty(favoriteId)) {

            c.favoriteId = favoriteId;
            c.isInFavorite = true;

        } else {

            c.isInFavorite = false;

        }

        SharedPreferences sp2 = ZWApplication.getAppContext().getSharedPreferences("cart_kv", 0);
        String cartId = sp2.getString(c.courseid, "");

        if(!TextUtils.isEmpty(cartId)) {

            c.cartId = cartId;
            c.isInCart = true;

        } else {

            c.isInCart = false;

        }

        return c;
    }

    public Teacher parseTeach(JSONObject jsonObject) throws JSONException {

        Teacher teacher = new Teacher();

        if(jsonObject.has(Teacher.ID)) teacher.id = jsonObject.getString(Teacher.ID);
        if(jsonObject.has(Teacher.NAME)) teacher.name = jsonObject.getString(Teacher.NAME);
        if(jsonObject.has(Teacher.AVATAR)) teacher.setAvatar(jsonObject.getString(Teacher.AVATAR));
        if(jsonObject.has(Teacher.INTRODUCTION)) teacher.introduction = jsonObject.getString(Teacher.INTRODUCTION);

        return  teacher;

    }

}

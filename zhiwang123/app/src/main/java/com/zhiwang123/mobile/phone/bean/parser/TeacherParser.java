package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.PayType;
import com.zhiwang123.mobile.phone.bean.PayTypeEntity;
import com.zhiwang123.mobile.phone.bean.Teacher;
import com.zhiwang123.mobile.phone.bean.TeacherEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeacherParser {

    private static final String TAG = "TeacherParser";

    public static TeacherEntity parse(JSONObject jo) {

        TeacherEntity ce = new TeacherEntity();

        try {

            if(jo.has(TeacherEntity.STATE)) ce.state = jo.getBoolean(TeacherEntity.STATE);

            if(jo.has(TeacherEntity.MESSAGE))ce.message = jo.getString(TeacherEntity.MESSAGE);

            if(jo.has(TeacherEntity.DATAOBJECT)) {

                JSONArray jarr = jo.getJSONArray(PayTypeEntity.DATAOBJECT);

                if(jarr != null && jarr.length() != 0) {

                    ce.dataobject = new ArrayList<>();

                    for(int i = 0; i < jarr.length(); i ++) {

                        JSONObject subJo = jarr.getJSONObject(i);

                        Teacher c = new Teacher();

                        if(subJo.has(Teacher.NAME)) c.name = subJo.getString(Teacher.NAME);
                        if(subJo.has(Teacher.AVATAR)) c.avatar = subJo.getString(Teacher.AVATAR);
                        if(subJo.has(Teacher.ID)) c.id = subJo.getString(Teacher.ID);
                        if(subJo.has(Teacher.INTRODUCTION)) c.introduction = subJo.getString(Teacher.INTRODUCTION);

                        ce.dataobject.add(c);

                    }
                }
            }

        } catch(Exception e) {

            e.printStackTrace();

            Log.e(TAG, e.getMessage() + "");

        }

        return ce;
    }

}

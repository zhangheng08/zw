package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.ExamECEntity;
import com.zhiwang123.mobile.phone.bean.ExamECategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExamECategroyParser {

    private static final String TAG = "TeachPlanParser";

    public static ExamECEntity parse(JSONObject jo) {

        ExamECEntity ec = new ExamECEntity();

        try {

            if(jo.has(ExamECEntity.STATE)) ec.state = jo.getBoolean(ExamECEntity.STATE);

            if(jo.has(ExamECEntity.MESSAGE)) ec.message = jo.getString(ExamECEntity.MESSAGE);

            if(jo.has(ExamECEntity.DATAOBJECT)) {

                JSONArray jarr = jo.getJSONArray(ExamECEntity.DATAOBJECT);

                if(jarr != null && jarr.length() != 0) {

                    ec.dataobject = new ArrayList<>();

                    for(int i = 0; i < jarr.length(); i ++) {

                        JSONObject subj = jarr.getJSONObject(i);
                        ExamECategory ect = parseExamECategroy(subj);
                        ec.dataobject.add(ect);

                    }

                }

            }

        } catch(Exception e) {

            e.printStackTrace();

            Log.e(TAG, e.getMessage() + "");

        }

        return ec;
    }


    public static ExamECategory parseExamECategroy(JSONObject jo) throws JSONException {

        ExamECategory ec = new ExamECategory();

        if(jo.has(ExamECategory.ID)) ec.id = jo.getString(ExamECategory.ID);
        if(jo.has(ExamECategory.TITLE)) ec.title = jo.getString(ExamECategory.TITLE);
        if(jo.has(ExamECategory.HAVECOURSEID)) ec.haveCourseId = jo.getString(ExamECategory.HAVECOURSEID);
        if(jo.has(ExamECategory.STARTDATE)) ec.startDate = jo.getString(ExamECategory.STARTDATE);
        if(jo.has(ExamECategory.ENDDATE)) ec.endDate = jo.getString(ExamECategory.ENDDATE);
        if(jo.has(ExamECategory.HOUR)) ec.hour = jo.getInt(ExamECategory.HOUR);
        if(jo.has(ExamECategory.STATE_INT)) ec.stateInt = jo.getInt(ExamECategory.STATE_INT);
        if(jo.has(ExamECategory.STATENAME)) ec.stateName = jo.getString(ExamECategory.STATENAME);
        if(jo.has(ExamECategory.LEAVEEXAMNUM)) ec.leaveExamNum = jo.getInt(ExamECategory.LEAVEEXAMNUM);

        return ec;

    }


}

package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.TeachPlanEntity;
import com.zhiwang123.mobile.phone.bean.TeachPlanSubc;
import com.zhiwang123.mobile.phone.bean.TeachPlanSubcEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeachPlanSubcParser{

    private static final String TAG = "TeachPlanParser";

    public static TeachPlanSubcEntity parse(JSONObject jo) {

        TeachPlanSubcEntity ce = new TeachPlanSubcEntity();

        try {

            if(jo.has(TeachPlanSubc.STATE)) ce.state = jo.getBoolean(TeachPlanSubc.STATE);

            if(jo.has(TeachPlanSubc.MESSAGE))ce.message = jo.getString(TeachPlanSubc.MESSAGE);

            if(jo.has(TeachPlanSubc.DATAOBJECT)) {

                JSONArray jarr = jo.getJSONArray(TeachPlanEntity.DATAOBJECT);

                if(jarr != null && jarr.length() != 0) {

                    ce.dataobject = new ArrayList<>();

                    for(int i = 0; i < jarr.length(); i ++) {

                        JSONObject subJo = jarr.getJSONObject(i);

                        TeachPlanSubc c = new TeachPlanSubc();
                        if(subJo.has(TeachPlanSubc.NAME)) c.name = subJo.getString(TeachPlanSubc.NAME);
                        if(subJo.has(TeachPlanSubc.CCKEY)) c.ccKey = subJo.getString(TeachPlanSubc.CCKEY);
                        if(subJo.has(TeachPlanSubc.COURSEHOUR)) c.courseHour = subJo.getString(TeachPlanSubc.COURSEHOUR);
                        if(subJo.has(TeachPlanSubc.TEACHPLANID)) c.teachPlanId = subJo.getString(TeachPlanSubc.TEACHPLANID);
                        if(subJo.has(TeachPlanSubc.HAVECOURSEID)) c.haveCourseId = subJo.getString(TeachPlanSubc.HAVECOURSEID);

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

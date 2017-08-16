package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.TeachPlan;
import com.zhiwang123.mobile.phone.bean.TeachPlanEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeachPlanParser<T extends TeachPlanEntity> {

    private static final String TAG = "TeachPlanParser";

    private T t;

    public TeachPlanParser(T t) {

        this.t = t;

    }

    public T parse(JSONObject jo) {

        try {

            if(jo.has(TeachPlanEntity.STATE)) t.state = jo.getBoolean(TeachPlanEntity.STATE);
            if(jo.has(TeachPlanEntity.MESSAGE))t.message = jo.getString(TeachPlanEntity.MESSAGE);

            if(jo.has(TeachPlanEntity.DATAOBJECT)) {
                JSONArray jarr = jo.getJSONArray(TeachPlanEntity.DATAOBJECT);
                if(jarr != null && jarr.length() != 0) {
                    t.dataobject = new ArrayList<>();
                    for(int i = 0; i < jarr.length(); i ++) {
                        JSONObject subJo = jarr.getJSONObject(i);
                        TeachPlan plan = parseTeachPlan(subJo);
                        t.dataobject.add(plan);
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() + "");
        }

        return t;
    }

    private TeachPlan parseTeachPlan(JSONObject jo) throws JSONException {

        TeachPlan plan = new TeachPlan();

        if(jo.has(TeachPlan.ID)) plan.id = jo.getString(TeachPlan.ID);
        if(jo.has(TeachPlan.NAME)) plan.name = jo.getString(TeachPlan.NAME);
        if(jo.has(TeachPlan.STARTDATE)) plan.startDate = jo.getString(TeachPlan.STARTDATE);
        if(jo.has(TeachPlan.ENDDATE)) plan.endDate = jo.getString(TeachPlan.ENDDATE);
        if(jo.has(TeachPlan.STUDYPERSONS)) plan.studyPersons = jo.getInt(TeachPlan.STUDYPERSONS);
        if(jo.has(TeachPlan.DESCRIPTION)) plan.description = jo.getString(TeachPlan.DESCRIPTION);

        return plan;
    }


}

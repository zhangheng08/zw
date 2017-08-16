package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.PayType;
import com.zhiwang123.mobile.phone.bean.PayTypeEntity;
import com.zhiwang123.mobile.phone.bean.TeachPlanEntity;
import com.zhiwang123.mobile.phone.bean.TeachPlanSubc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PayTypeParser {

    private static final String TAG = "PayTypeParser";

    public static PayTypeEntity parse(JSONObject jo) {

        PayTypeEntity ce = new PayTypeEntity();

        try {

            if(jo.has(PayTypeEntity.STATE)) ce.state = jo.getBoolean(PayTypeEntity.STATE);

            if(jo.has(PayTypeEntity.MESSAGE))ce.message = jo.getString(PayTypeEntity.MESSAGE);

            if(jo.has(PayTypeEntity.DATAOBJECT)) {

                JSONArray jarr = jo.getJSONArray(PayTypeEntity.DATAOBJECT);

                if(jarr != null && jarr.length() != 0) {

                    ce.dataobject = new ArrayList<>();

                    for(int i = 0; i < jarr.length(); i ++) {

                        JSONObject subJo = jarr.getJSONObject(i);

                        PayType c = new PayType();

                        if(subJo.has(PayType.KEY)) c.key = subJo.getString(PayType.KEY);
                        if(subJo.has(PayType.NAME)) c.name = subJo.getString(PayType.NAME);

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

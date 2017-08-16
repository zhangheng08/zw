package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.Organ;
import com.zhiwang123.mobile.phone.bean.OrganEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/9.
 */

public class OrganParser<T extends OrganEntity> {

    private static final String TAG = "OrganParser";

    private T t;

    public OrganParser(T t) {

        this.t = t;

    }

    public T parse(JSONObject jo) {

        try {

            if(jo.has(OrganEntity.STATE)) t.state = jo.getBoolean(OrganEntity.STATE);
            if(jo.has(OrganEntity.MESSAGE))t.message = jo.getString(OrganEntity.MESSAGE);

            if(jo.has(OrganEntity.DATAOBJECT)) {
                JSONArray jarr = jo.getJSONArray(OrganEntity.DATAOBJECT);
                if(jarr != null && jarr.length() != 0) {
                    t.dataobject = new ArrayList<>();
                    for(int i = 0; i < jarr.length(); i ++) {
                        JSONObject subJo = jarr.getJSONObject(i);
                        Organ organ = parseOrgan(subJo);
                        t.dataobject.add(organ);
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return t;
    }

    private Organ parseOrgan(JSONObject jo) throws JSONException {

        Organ organ = new Organ();

        if(jo.has(Organ.NAME)) organ.name = jo.getString(Organ.NAME);
        if(jo.has(Organ.KEY))  organ.key  = jo.getString(Organ.KEY);

        return organ;
    }


}

package com.zhiwang123.mobile.phone.bean;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ty on 2016/6/29.
 */
public class Snippet extends BaseEntity {

    private static final String TAG = "Snippet";

    private static final String ID = "Id";
    private static final String TITLE = "Title";
    private static final String SUMMARY = "Summary";
    private static final String COVER = "Cover";
    private static final String URL = "Url";
    private static final String PICTURE = "Picture";
    private static final String DATAOBJECT = "DataObject";

    public String id;
    public String summary;
    public String cover;

    public ArrayList<FocusPic> focusPics;

    public Snippet parse(JSONObject jsonObject) {

        try {

            state = jsonObject.getBoolean(STATE);
            message = jsonObject.getString(MESSAGE);

            JSONArray jsonArray = jsonObject.getJSONArray(DATAOBJECT);
            if(jsonArray != null && jsonArray.length() != 0) {
                focusPics = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i ++) {
                    FocusPic pc = new FocusPic();
                    JSONObject subJo = jsonArray.getJSONObject(i);
                    if(subJo.has(TITLE)) pc.title = subJo.getString(TITLE);
                    if(subJo.has(URL)) pc.url = subJo.getString(URL);
                    if(subJo.has(PICTURE)) pc.picture = subJo.getString(PICTURE);
                    focusPics.add(pc);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }



    public class FocusPic {

        public String url;
        public String title;
        public String picture;

    }


}

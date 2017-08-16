package com.zhiwang123.mobile.phone.bean.parser;

import android.util.Log;

import com.zhiwang123.mobile.phone.bean.Course;
import com.zhiwang123.mobile.phone.bean.CourseEntity;
import com.zhiwang123.mobile.phone.bean.Order;
import com.zhiwang123.mobile.phone.bean.OrderEntity;
import com.zhiwang123.mobile.phone.bean.OrganEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ty on 2016/11/9.
 */

public class OrderParser<T extends OrderEntity> {

    private static final String TAG = "OrganParser";

    private T t;

    public OrderParser(T t) {

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
                        Order order = parserOrder(subJo);
                        t.dataobject.add(order);
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return t;
    }

    public Order parserOrder(JSONObject jo) throws JSONException {

        Order order = new Order();
        if(jo.has(Order.ID)) order.id = jo.getString(Order.ID);
        if(jo.has(Order.ORDERNUM)) order.orderNum = jo.getString(Order.ORDERNUM);
        if(jo.has(Order.CREATEDATE)) order.createDate = jo.getString(Order.CREATEDATE);
        if(jo.has(Order.STATUS)) order.status = jo.getInt(Order.STATUS);
        if(jo.has(Order.STATUSNAME)) order.statusName = jo.getString(Order.STATUSNAME);
        if(jo.has(Order.MONEY)) order.money = jo.getInt(Order.MONEY);
        if(jo.has(Order.PAYKEYWORD)) order.payKeyword = jo.getString(Order.PAYKEYWORD);
        if(jo.has(Order.ORDERITEMS)) {

            JSONArray jarr = jo.getJSONArray(Order.ORDERITEMS);
            if(jarr != null && jarr.length() != 0) {
                order.orderItems = new ArrayList<Course>();
                for(int i = 0; i < jarr.length(); i ++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    Course c = new CourseParser<>(new CourseEntity()).parseCourse(jobj);
                    for(int k = 0; k < 1; k ++) {
                        order.orderItems.add(c);
                    }
                }
            }
        }
        return order;
    }


}

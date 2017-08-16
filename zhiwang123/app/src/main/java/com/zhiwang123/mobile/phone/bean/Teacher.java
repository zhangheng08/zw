package com.zhiwang123.mobile.phone.bean;

/**
 * Created by ty on 2016/11/14.
 */

public class Teacher extends BaseEntity {

    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String AVATAR = "Avatar";
    public static final String INTRODUCTION = "Introduction";

    public String id;
    public String name;
    public String avatar;
    public String introduction;

    public void setAvatar(String url) {

        avatar = url.replace("width=64", "width=360").replace("height=64", "height=270");

    }

    public String getAvatar() {

        return avatar;
    }

}


package com.zhiwang123.mobile.phone.bean;

/**
 * Created by ty on 2016/11/23.
 */

public class UserInfo extends BaseEntity {

    public static final String PHONE = "Phone";
    public static final String NAME = "Name";
    public static final String ROLENAME = "RoleName";
    public static final String ISORGANROLE = "IsOrganRole";
    public static final String AVATAR = "Avatar";
    public static final String USERNAME = "UserName";
    public static final String EXPIRES = "Expires";
    public static final String ACCESSTOKEN = "AccessToken";
    public static final String MONEY = "Money";
    public static final String THIRIDLOGINKEY = "ThirdLoginKey";
    public static final String THIRDLOGINOPENID = "ThirdLoginOpenId";

    public static final int TYPE_QQ = 1;
    public static final int TYPE_WX = 2;

    public String phone;
    public String name;
    public String roleName;
    public boolean isOrganRole;
    public String avatar;
    public String userName;
    public String token;
    public String expires;
    public int money;
    public String thirdLoginOpenId;
    public String thirdLoginKey;

    public int type;

}

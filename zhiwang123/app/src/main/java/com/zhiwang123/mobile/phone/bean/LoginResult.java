package com.zhiwang123.mobile.phone.bean;

/**
 * Created by ty on 2016/11/22.
 */

public class LoginResult extends ResultEntity<UserInfo> {

    public static final String ACCESSTOKEN = "AccessToken";
    public static final String EXPIRES = "Expires";

    public String expires;
    public String accessToken;
    public String account;
    public int loginMode;

    public LoginResult() {
        super();
    }

}

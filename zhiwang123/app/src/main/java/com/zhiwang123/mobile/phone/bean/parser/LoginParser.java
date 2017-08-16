package com.zhiwang123.mobile.phone.bean.parser;

import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.ResultEntity;
import com.zhiwang123.mobile.phone.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ty on 2016/11/9.
 */

public class LoginParser<T extends ResultEntity> {

    private T t;

    public LoginParser(T t) {
        this.t = t;
    }

    public T parse(JSONObject jo) {

        try {

            if(jo.has(T.STATE)) t.state = jo.getBoolean(t.STATE);
            if(jo.has(T.MESSAGE)) t.message = jo.getString(t.MESSAGE);

            if(t instanceof LoginResult) {
                LoginResult lr = (LoginResult) t;
                if(jo.has(LoginResult.ACCESSTOKEN)) lr.accessToken = jo.getString(LoginResult.ACCESSTOKEN);
                if(jo.has(LoginResult.EXPIRES)) lr.expires = jo.getString(LoginResult.EXPIRES);

                if(jo.has(LoginResult.DATAOBJECT)) {
                    JSONObject subJo = jo.getJSONObject(LoginResult.DATAOBJECT);
                    UserInfo userInfo = new UserInfo();
                    if(subJo.has(UserInfo.NAME)) userInfo.name = subJo.getString(UserInfo.NAME);
                    if(subJo.has(UserInfo.PHONE)) userInfo.phone = subJo.getString(UserInfo.PHONE);
                    if(subJo.has(UserInfo.ROLENAME)) userInfo.roleName = subJo.getString(UserInfo.ROLENAME);
                    if(subJo.has(UserInfo.ISORGANROLE)) userInfo.isOrganRole = subJo.getBoolean(UserInfo.ISORGANROLE);
                    if(subJo.has(UserInfo.AVATAR)) userInfo.avatar = subJo.getString(UserInfo.AVATAR);
                    if(subJo.has(UserInfo.USERNAME)) userInfo.userName = subJo.getString(UserInfo.USERNAME);
                    if(subJo.has(UserInfo.ACCESSTOKEN)) userInfo.token = subJo.getString(UserInfo.ACCESSTOKEN);
                    if(subJo.has(UserInfo.EXPIRES)) userInfo.expires = subJo.getString(UserInfo.EXPIRES);
                    if(subJo.has(UserInfo.MONEY)) userInfo.money = subJo.getInt(UserInfo.MONEY);
                    if(subJo.has(UserInfo.THIRIDLOGINKEY)) userInfo.thirdLoginKey = subJo.getString(UserInfo.THIRIDLOGINKEY);
                    if(subJo.has(UserInfo.THIRDLOGINOPENID)) userInfo.thirdLoginOpenId = subJo.getString(UserInfo.THIRDLOGINOPENID);
                    lr.dataobject.add(userInfo);

                }
            }

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }


}

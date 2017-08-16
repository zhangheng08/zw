package com.zhiwang123.mobile.phone.exception;

/**
 * Created by ty on 2016/10/27.
 */

public class WeixinException extends Exception {

    public String mExecptionInfo;

    public WeixinException(String execptionInfo) {
        super(execptionInfo);
        mExecptionInfo = execptionInfo;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

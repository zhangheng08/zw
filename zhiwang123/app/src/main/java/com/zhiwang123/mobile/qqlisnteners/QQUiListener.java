package com.zhiwang123.mobile.qqlisnteners;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by ty on 2016/10/31.
 */

public interface QQUiListener extends IUiListener {

    @Override
    public void onComplete(Object o);

    @Override
    public void onError(UiError uiError);

    @Override
    public void onCancel();

}

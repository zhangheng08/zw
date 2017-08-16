package com.zhiwang123.mobile.phone.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by ty on 2016/12/23.
 */

public class ShareUtil {

    public static void setShareToWxcs(Context context, View view) {

        view.setOnClickListener(onClickListener);

    }

    private static View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

}

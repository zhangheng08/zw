package com.zhiwang123.mobile;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.zhiwang123.mobile.cc.CCDataSet;
import com.zhiwang123.mobile.phone.db.ZWDatabaseHelper;
import com.zhiwang123.mobile.phone.exception.WeixinException;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

/**
 * Created by ty on 2016/10/24.
 */

public class ZWApplication extends Application {

    private static final String TAG = "ZWApplication";

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        StaticConfigs.setUrlBase(this);

        CCDataSet.init(this);

//        registUmengPush();

        PlatformConfig.setSinaWeibo("2406481066", "8b16b5fb95488ca060bc81d9054d100c", "");
        Config.DEBUG = true;

        new ZWDatabaseHelper(this).createDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ApplicationInfo mAppInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                    weixinRegist(mAppInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (WeixinException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.mExecptionInfo);
                }
            }
        }).start();
    }

    private void weixinRegist(final ApplicationInfo appInfo) throws WeixinException {
        String appId = appInfo.metaData.getString("WEIXIN_APPID");
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, appId, true);
        boolean bo = iwxapi.registerApp(appId);
        if(!bo) {
            throw new WeixinException("registerApp to WX failed!");
        } else {
            Log.i(TAG, "regist to WX success !");
        }
    }

//    private void registUmengPush() {
//
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.setDebugMode(true);
//
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            /**
//             * 自定义消息的回调方法
//             * */
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//                new Handler().post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            /**
//             * 自定义通知栏样式的回调方法
//             * */
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
////                switch (msg.builder_id) {
////                    case 1:
////                        Notification.Builder builder = new Notification.Builder(context);
////                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
////                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
////                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
////                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
////                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
////                        builder.setContent(myNotificationView)
////                                .setSmallIcon(getSmallIconId(context, msg))
////                                .setTicker(msg.ticker)
////                                .setAutoCancel(true);
////
////                        return builder.getNotification();
////                    default:
////                        //默认为0，若填写的builder_id并不存在，也使用默认。
////                        return super.getNotification(context, msg);
////                }
//
//                return super.getNotification(context, msg);
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);
//
//        /**
//         * 自定义行为的回调处理
//         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
//         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//         * */
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                Log.i(TAG, "custom info : " + msg.getRaw());
//            }
//        };
//
//        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
//        //参考http://bbs.umeng.com/thread-11112-1-1.html
//        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                Log.i(TAG, "regist umeng push sucdess, deviceToken : " + deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//
//            }
//        });
//
//    }

    public static Context getAppContext() {
        return mContext;
    }

}

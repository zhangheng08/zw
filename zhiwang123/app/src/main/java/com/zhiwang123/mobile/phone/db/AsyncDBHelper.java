package com.zhiwang123.mobile.phone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.zhiwang123.mobile.phone.bean.BaseEntity;
import com.zhiwang123.mobile.phone.bean.DownloadTask;
import com.zhiwang123.mobile.phone.bean.LocalVideo;
import com.zhiwang123.mobile.phone.bean.LoginResult;
import com.zhiwang123.mobile.phone.bean.UserInfo;
import com.zhiwang123.mobile.phone.utils.StaticConfigs;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ty on 2016/6/21.
 */
public class AsyncDBHelper {

    private static final String TAG = "AsyncDBHelper";

    private static final ExecutorService mExecutorService = Executors.newFixedThreadPool(10);

    private AsyncDBHelper() {};

    private static final AsyncDBHelper self = new AsyncDBHelper();

    public static AsyncDBHelper a() {
        return self;
    }

    public static void saveVideoCacheDB(final Context context, final DownloadTask cache, final UIHandler uiHandler) {

        if(uiHandler == null) return;

        mExecutorService.submit(new Runnable() {

            @Override
            public void run() {

                SQLiteDatabase db = new ZWDatabaseHelper(context).getWritableDatabase();
                Cursor c = null;
                Message msg = uiHandler.obtainMessage();

                try {

                    c = db.rawQuery("select * from " + ZWDatabaseHelper.VIDEO_CACHE_TAB + " where _id = ?", new String[] {cache.videoId});

                    ContentValues cv = new ContentValues();
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_ID, cache.videoId);
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_TITLE, cache.title);
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_COVER, cache.cover);
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_FILEPATH, cache.filePath);
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_TOTAL_LEN, cache.totalBits);
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_LENGTH, cache.currentBits);
                    cv.put(ZWDatabaseHelper.VIDEO_CACHE_TAB_IFCOMP, cache.status);

                    db.beginTransaction();
                    if(c.getCount() == 0) {

                        db.insertOrThrow(ZWDatabaseHelper.VIDEO_CACHE_TAB, null, cv);

                    } else {

                        db.update(ZWDatabaseHelper.VIDEO_CACHE_TAB, cv, "_id = ?", new String[] {cache.videoId});

                    }

                    db.setTransactionSuccessful();

                    msg.what = uiHandler.SEND_SAVE_OK;
                    msg.obj = cache;

                } catch(Exception e) {
                    msg.what = uiHandler.ERROR;
                    e.printStackTrace();
                } finally {
                    msg.sendToTarget();
                    db.endTransaction();
                    if(c != null) c.close();
                    db.close();
                }
            }
        });
    }

    public static void getVideoCacheDB(final Context context, final UIHandler uiHandler) {

        if(uiHandler == null) return;

        mExecutorService.submit(new Runnable() {

            @Override
            public void run() {

                SQLiteDatabase db = new ZWDatabaseHelper(context).getWritableDatabase();
                Cursor c = null;
                Message msg = uiHandler.obtainMessage();
                msg.what = uiHandler.SEND_DB_DATA;

                ArrayList<DownloadTask> videoCaches = null;

                try {

                    db.beginTransaction();
                    c = db.rawQuery("select * from " + ZWDatabaseHelper.VIDEO_CACHE_TAB, null);

                    if(c.getCount() != 0) {

                        videoCaches = new ArrayList<>();

                        c.moveToFirst();

                        do {

                            DownloadTask task = new DownloadTask();
                            task.videoId = c.getString(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_ID));
                            task.title = c.getString(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_TITLE));
                            task.currentBits = c.getLong(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_LENGTH));
                            task.totalBits = c.getLong(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_TOTAL_LEN));
                            task.cover = c.getString(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_COVER));
                            task.filePath = c.getString(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_FILEPATH));
                            task.status = c.getInt(c.getColumnIndex(ZWDatabaseHelper.VIDEO_CACHE_TAB_IFCOMP));
                            videoCaches.add(task);

                        } while (c.moveToNext());


                        msg.obj = videoCaches;

                    } else {

                        msg.obj = null;

                    }
                    db.setTransactionSuccessful();

                } catch(Exception e) {

                    e.printStackTrace();
                } finally {

                    msg.sendToTarget();
                    db.endTransaction();
                    if(c != null) c.close();
                    db.close();
                }
            }
        });
    }

    public static void deleteVideoCacheSD(String title) {

        String ccCachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CCDownload";

        Log.i("-----sdf:", ccCachePath);

        File file = new File(ccCachePath);

        if(file.exists()) {

            File[] files = file.listFiles();

            for(File f : files) {

                if(f.getName().contains(title)) {

                    Log.i("-----sdf:", f.getName());

                    f.delete();

                    break;
                }

            }

        }

    }

    public static void deleteVideoCacheDB(final Context context, final LocalVideo lv, final UIHandler uiHandler) {

//        if(uiHandler == null) return;

        mExecutorService.submit(new Runnable() {

            @Override
            public void run() {

                SQLiteDatabase db = new ZWDatabaseHelper(context).getWritableDatabase();

                try {

                    db.beginTransaction();

                    db.delete(ZWDatabaseHelper.VIDEO_CACHE_TAB, "_id = ?", new String[] {lv.videoId});

                    db.setTransactionSuccessful();

                    deleteVideoCacheSD(lv.title);

                } catch(Exception e) {

                    Log.e(TAG, e.getMessage() + "");
                    e.printStackTrace();

                } finally {

                    db.endTransaction();
                    db.close();
                }
            }
        });
    }


//    public static void saveCategoryInfoDB(final Context context, final String version, final String jsonString) {
//
//        mExecutorService.submit(new Runnable() {
//
//            @Override
//            public void run() {
//
//                SQLiteDatabase db = new XingxueDbHelper(context).getWritableDatabase();
//                Cursor c = null;
//
//                try {
//
//                    c = db.rawQuery(XingxueDbHelper.TABLE_CATEGORY_ALL_QUERY_VERSION, new String[] {"jsonString"});
//                    ContentValues cv = new ContentValues();
//                    String v = null;
//
//                    if(c != null && c.getCount() != 0 && c.getColumnCount() != 0) {
//
//                        c.moveToFirst();
//                        v = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOMU_VERSION));
//
//
//                        if(!v.equals(version)) {
//
//                            Log.i(TAG, "update category/all json string in db.");
//                            db.beginTransaction();
//                            cv.put(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOMU_VERSION, version);
//                            cv.put(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOUM_JSON, jsonString);
//                            db.update(XingxueDbHelper.TABLE_CATEGORY_ALL, null, "_id = 'jsonString'", null);
//                            db.setTransactionSuccessful();
//                        } else {
//
//                            Log.i(TAG, "this version of (category/all json string) already saved in db, no need to update.");
//                        }
//                    } else {
//
//                        Log.i(TAG, "insert category/all json string to db.");
//                        db.beginTransaction();
//                        cv.put(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOUM_ID, "jsonString");
//                        cv.put(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOMU_VERSION, version);
//                        cv.put(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOUM_JSON, jsonString);
//                        db.insertOrThrow(XingxueDbHelper.TABLE_CATEGORY_ALL, null, cv);
//                        db.setTransactionSuccessful();
//                    }
//
//                } catch(Exception e) {
//
//                    e.printStackTrace();
//                    Log.e(TAG, e.getMessage());
//                } finally {
//
//                    db.endTransaction();
//                    if(c != null) c.close();
//                    db.close();
//                }
//
//            }
//        });
//
//    }
//
//    public static void getCategoryInfoDB(final Context context, final XingxueUIHandler uiHandler) {
//
//        if(uiHandler == null) return;
//
//        mExecutorService.submit(new Runnable() {
//
//            @Override
//            public void run() {
//
//                SQLiteDatabase db = new XingxueDbHelper(context).getWritableDatabase();
//                Cursor c = null;
//                Message msg = uiHandler.obtainMessage();
//
//                try {
//                    c = db.rawQuery(XingxueDbHelper.TABLE_CATEGORY_ALL_QUERY_JSON, new String[] {"jsonString"});
//
//                    if(c != null && c.getCount() != 0 && c.getColumnCount() != 0) {
//                        Log.i(TAG, "get category/all json string from db.");
//                        c.moveToFirst();
//                        String jsonString = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_CATEGORY_ALL_COLOMU_VERSION));
//                        CategoryInfo categoryInfo = new CategoryInfo().parse(new JSONObject(jsonString));
//
//                        msg.obj = categoryInfo;
//                        msg.what = uiHandler.SEND_CATEGORY_ALL_DB;
//                    } else {
//                        Log.i(TAG, "query category/all json string from db failed!");
//                        msg.what = uiHandler.ERROR;
//                    }
//
//                    msg.sendToTarget();
//
//                } catch(Exception e) {
//
//                    e.printStackTrace();
//                    Log.e(TAG, e.getMessage());
//                } finally {
//
//                    db.endTransaction();
//                    if(c != null) c.close();
//                    db.close();
//                }
//            }
//        });
//    }
//
    public static void saveLoginInfoDB(final Context context, final LoginResult logininfo, final UIHandler uiHandler) {

        if(uiHandler == null) return;

        mExecutorService.submit(new Runnable() {

            @Override
            public void run() {

                SQLiteDatabase db = new ZWDatabaseHelper(context).getWritableDatabase();
                Cursor c = null;
                Message msg = uiHandler.obtainMessage();

                try {

                    c = db.rawQuery("select * from " + ZWDatabaseHelper.USER_TAB, null);

                    ContentValues cv = new ContentValues();

                    UserInfo userInfo = logininfo.dataobject.get(0);

                    cv.put(ZWDatabaseHelper.USER_TAB_ID, ZWDatabaseHelper.USER_FINAL_KEY);
                    cv.put(ZWDatabaseHelper.USER_TAB_ACCOUNT, TextUtils.isEmpty(logininfo.account) ? userInfo.userName : logininfo.account);

                    if(TextUtils.isEmpty(logininfo.accessToken) && !TextUtils.isEmpty(userInfo.token)) {

                        logininfo.accessToken = userInfo.token;

                    }
                    cv.put(ZWDatabaseHelper.USER_TAB_TOKEN, logininfo.accessToken);

                    if(TextUtils.isEmpty(logininfo.expires) && !TextUtils.isEmpty(userInfo.expires)) {

                        logininfo.expires = userInfo.expires;

                    }
                    cv.put(ZWDatabaseHelper.USER_TAB_EXPIRE, logininfo.expires);

                    cv.put(ZWDatabaseHelper.USER_TAB_LOGINMODE, logininfo.loginMode);

                    cv.put(ZWDatabaseHelper.USER_TAB_PHONE, userInfo.phone);
                    cv.put(ZWDatabaseHelper.USER_TAB_USER_NAME, userInfo.userName);
                    cv.put(ZWDatabaseHelper.USER_TAB_NAME, userInfo.name);
                    cv.put(ZWDatabaseHelper.USER_TAB_ROLENAME, userInfo.roleName);
                    cv.put(ZWDatabaseHelper.USER_TAB_ISORGAN, userInfo.isOrganRole ? 1 : 0);
                    cv.put(ZWDatabaseHelper.USER_TAB_AVATAR, userInfo.avatar);
                    cv.put(ZWDatabaseHelper.USER_TAB_MONEY, userInfo.money);

                    db.beginTransaction();
                    if(c.getCount() == 0) {

                        db.insertOrThrow(ZWDatabaseHelper.USER_TAB, null, cv);

                    } else {

                        db.update(ZWDatabaseHelper.USER_TAB, cv, "_id = ?", new String[] {ZWDatabaseHelper.USER_FINAL_KEY});

                    }
                    db.setTransactionSuccessful();

                    msg.what = uiHandler.SEND_SAVE_USERINFO_OK;
                    msg.obj = logininfo;

                } catch(Exception e) {
                    msg.what = uiHandler.ERROR;
                    e.printStackTrace();
                } finally {
                    msg.sendToTarget();
                    db.endTransaction();
                    if(c != null) c.close();
                    db.close();
                }
            }
        });
    }

    public static void deleteUserInfoDB(final Context context, final UIHandler uiHandler) {

        if(uiHandler == null) return;

        mExecutorService.submit(new Runnable() {

            @Override
            public void run() {

                SQLiteDatabase db = new ZWDatabaseHelper(context).getWritableDatabase();
                Message msg = uiHandler.obtainMessage();

                try {

                    db.delete(ZWDatabaseHelper.USER_TAB, "_id = ?", new String[] {ZWDatabaseHelper.USER_FINAL_KEY});
                    msg.what = uiHandler.SEND_DELETE_USER_RESULT;
                    msg.obj = null;

                } catch(Exception e) {
                    msg.what = uiHandler.ERROR;
                    e.printStackTrace();
                } finally {
                    msg.sendToTarget();
                    db.close();
                }
            }
        });
    }

    public static void getUserInfoDB(final Context context, final UIHandler uiHandler) {
        if(uiHandler == null) return;

        mExecutorService.submit(new Runnable() {

            @Override
            public void run() {

                SQLiteDatabase db = new ZWDatabaseHelper(context).getWritableDatabase();
                Cursor c = null;
                Message msg = uiHandler.obtainMessage();
                msg.what = uiHandler.SEND_USERINFO;

                String access = "";
                String account = "";
                String name = "";
                String phone = "";
                int money = 0;
                String roleName = "";
                boolean isRogan = false;
                String avatar = "";
                int loginMode = StaticConfigs.LOGIN_MODE_PB;

                try {

                    db.beginTransaction();
                    c = db.rawQuery("select * from " + ZWDatabaseHelper.USER_TAB, null);

                    if(c.getCount() != 0) {

                        c.moveToFirst();
                        access = c.getString(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_TOKEN));
                        account = c.getString(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_ACCOUNT));
                        loginMode = c.getInt(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_LOGINMODE));
                        name = c.getString(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_NAME));
                        phone = c.getString(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_PHONE));
                        roleName = c.getString(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_ROLENAME));
                        isRogan = c.getInt(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_ISORGAN)) > 0 ? true : false;
                        avatar = c.getString(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_AVATAR));
                        money = c.getInt(c.getColumnIndex(ZWDatabaseHelper.USER_TAB_MONEY));

                        LoginResult loginResult = new LoginResult();
                        loginResult.accessToken = access;
                        loginResult.account = account;
                        loginResult.loginMode = loginMode;

                        UserInfo u = new UserInfo();
                        u.name = name;
                        u.phone = phone;
                        u.roleName = roleName;
                        u.isOrganRole = isRogan;
                        u.avatar = avatar;
                        u.money = money;

                        loginResult.dataobject.add(u);

                        msg.obj = loginResult;

                    } else {

                        msg.obj = null;

                    }
                    db.setTransactionSuccessful();

                } catch(Exception e) {

                    e.printStackTrace();
                } finally {

                    msg.sendToTarget();
                    db.endTransaction();
                    if(c != null) c.close();
                    db.close();
                }
            }
        });
    }
//
//    public static void saveHistoryInfoDB(final Context context, final Course t, final XingxueUIHandler uiHandler) {
//
//        if(uiHandler == null) return;
//
//        mExecutorService.submit(new Runnable() {
//
//            @Override
//            public void run() {
//
//                SQLiteDatabase db = new XingxueDbHelper(context).getWritableDatabase();
//                Cursor c = null;
//                Message msg = uiHandler.obtainMessage();
//
//                try {
//
//                    c = db.rawQuery("select * from " + XingxueDbHelper.TABLE_HISTORY + " where _id = ?", new String[] {t.id_});
//
//                    if(c.getCount() == 0) {
//                        ContentValues cv = new ContentValues();
//                        cv.put(XingxueDbHelper.TABLE_HISTORY_COLOUM_ID, t.id_);
//                        cv.put(XingxueDbHelper.TABLE_HISTORY_COLOUM_NM, t.name_);
//                        cv.put(XingxueDbHelper.TABLE_HISTORY_COLOUM_COVER, t.cover);
//                        cv.put(XingxueDbHelper.TABLE_HISTORY_COLOUM_DESC, t.description);
//                        cv.put(XingxueDbHelper.TABLE_HISTORY_COLOUM_PERIOD, t.period);
//                        db.insertOrThrow(XingxueDbHelper.TABLE_HISTORY, null, cv);
//                        msg.what = uiHandler.SEND_SAVEHISTORY_RESULT;
//                        msg.obj = null;
//                        msg.sendToTarget();
//                    }
//
//                } catch(Exception e) {
//                    msg.what = uiHandler.ERROR;
//                    msg.sendToTarget();
//                    e.printStackTrace();
//                } finally {
//                    db.endTransaction();
//                    if(c != null) c.close();
//                    db.close();
//                }
//            }
//        });
//    }
//
//    public static void getHistoryInfoDB(final Context context, final XingxueUIHandler uiHandler) {
//
//        if(uiHandler == null) return;
//
//        mExecutorService.submit(new Runnable() {
//
//            @Override
//            public void run() {
//
//                SQLiteDatabase db = new XingxueDbHelper(context).getWritableDatabase();
//                Cursor c = null;
//                Message msg = uiHandler.obtainMessage();
//
//                ArrayList<Course> historyList = new ArrayList<Course>();
//
//                try {
//                    c = db.rawQuery("select * from " + XingxueDbHelper.TABLE_HISTORY, null);
//
//                    c.moveToFirst();
//
//                    do {
//                        Course t = new Course();
//                        t.id_ = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_HISTORY_COLOUM_ID));
//                        t.name_ = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_HISTORY_COLOUM_NM));
//                        t.cover = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_HISTORY_COLOUM_COVER));
//                        t.description = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_HISTORY_COLOUM_DESC));
//                        t.period = c.getString(c.getColumnIndex(XingxueDbHelper.TABLE_HISTORY_COLOUM_PERIOD));
//                        historyList.add(t);
//                    } while(c.moveToNext());
//
//                    msg.what = uiHandler.SEND_GETHISTORY_RESULT;
//                    msg.obj = historyList;
//
//                } catch(Exception e) {
//                    msg.what = uiHandler.ERROR;
//                    msg.sendToTarget();
//                    e.printStackTrace();
//                } finally {
//                    msg.sendToTarget();
//                    if(c != null) c.close();
//                    db.close();
//                }
//            }
//        });
//    }

    public static abstract class UIHandler<T extends BaseEntity> extends Handler {

        public static final int SEND_CATEGORY_ALL_DB = 0x2d1;
        public static final int SEND_USERINFO = 0x321;
        public static final int SEND_SAVE_USERINFO_OK = 0x211;
        public static final int SEND_DELETE_USER_RESULT = 0x2121;
        public static final int SEND_SAVEHISTORY_RESULT = 0x112;
        public static final int SEND_GETHISTORY_RESULT = 0x111;
        public static final int ERROR = 0x00;

        public static final int SEND_SAVE_OK = 0x232;
        public static final int SEND_DB_DATA = 0x421;

        public T t;

        @Override
        public void handleMessage(Message msg) {

            try {

                t = (T) msg.obj;

                processMessage(msg.what, t);

            } catch(ClassCastException e) {

                Log.e(TAG, "send other typs of result object , please override handleMessage() and do not invoke super.handleMessage() !");

            }

        }

        public void processMessage(int what, T t) {

        }

    }


}

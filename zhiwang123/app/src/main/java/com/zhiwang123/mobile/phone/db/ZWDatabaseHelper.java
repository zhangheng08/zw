package com.zhiwang123.mobile.phone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ty on 2016/11/18.
 */

public class ZWDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "ZWDatabaseHelper";
    private static final int 	VERSION = 1;
    private static final String NAME = "zhiwang.db";

    private Context mContext;

    /**
     *  用户表
     */
    public static final String DDL_USER_TAB =
            "create table user_tab\n" +
            "(\n" +
            "_id         text primary key,\n" +
            "account     text,\n" +
            "token       text,\n" +
            "expire      text,\n" +
            "loginmode   integer,\n" +
            "user_name     text,\n" +
            "name        text,\n" +
            "roleName    text,\n" +
            "isOrganRole integer,\n" +
            "avatar      text,\n" +
            "phone       text,\n"  +
            "money       integer\n" +
            ")";

    public static final String USER_FINAL_KEY = "o9xjzhisUemme_wo_t_buzhDdVo4a";
    public static final String USER_TAB = "user_tab";
    public static final String USER_TAB_ID = "_id";
    public static final String USER_TAB_ACCOUNT = "account";
    public static final String USER_TAB_TOKEN = "token";
    public static final String USER_TAB_EXPIRE = "expire";
    public static final String USER_TAB_LOGINMODE = "loginmode";
    public static final String USER_TAB_USER_NAME = "user_name";
    public static final String USER_TAB_NAME = "name";
    public static final String USER_TAB_ROLENAME = "roleName";
    public static final String USER_TAB_ISORGAN = "isOrganRole"; //Y:1 N:0
    public static final String USER_TAB_AVATAR = "avatar";
    public static final String USER_TAB_PHONE = "phone";
    public static final String USER_TAB_MONEY = "money";


    /**
     * 已缓存的视频数据表
     */
    public static final String DDL_VIDEO_CACHE_TAB = "create table video_file_cache\n" +
            "(\n" +
            "_id          text primary key,\n" +
            "filepath     text,\n" +
            "title        text,\n" +
            "cover        text,\n" +
            "total_len    long,\n" +
            "length       long,\n" +
            "ifcomplete   integer\n" +
            ")";

    public static final String VIDEO_CACHE_TAB = "video_file_cache";
    public static final String VIDEO_CACHE_TAB_ID = "_id"; //video id
    public static final String VIDEO_CACHE_TAB_FILEPATH = "filepath";
    public static final String VIDEO_CACHE_TAB_TITLE = "title";
    public static final String VIDEO_CACHE_TAB_COVER = "cover";
    public static final String VIDEO_CACHE_TAB_LENGTH = "length";
    public static final String VIDEO_CACHE_TAB_TOTAL_LEN = "total_len";
    public static final String VIDEO_CACHE_TAB_IFCOMP = "ifcomplete";


    private final static String PREFIX_DROP = "drop table if exists ";


    public ZWDatabaseHelper(Context context) {
        this(context, NAME, null, VERSION);
        mContext = context;
    }

    public ZWDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDatabase createDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null) {
            db.close();
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.beginTransaction();

            db.execSQL(DDL_USER_TAB);
            db.execSQL(DDL_VIDEO_CACHE_TAB);

//            db.execSQL(DML_INSERT_VERSIONINFO);
//            db.execSQL(DDL_TABLE_DETAIL_INFO);
//            db.execSQL(DDL_TABLE_SEARCH_HISTORY);
//            db.execSQL(DDL_TABLE_HISTORY);
//            db.execSQL(DDL_TABLE_CATEGROY_ALL);
//            db.execSQL(DDL_TABLE_FOCUSINFO);
//            db.execSQL(DDL_TABLE_FAVORITEINFO);
//            db.execSQL(DDL_TABLE_FAVORITEINFO_OFFLINE);

            db.setTransactionSuccessful();
            Log.v(TAG, "SQL: db process successful!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

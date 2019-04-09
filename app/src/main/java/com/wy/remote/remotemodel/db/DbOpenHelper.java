package com.wy.remote.remotemodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 版本：V1.2.5
 * 时间： 2018/5/23 11:05
 * 创建人：laoqb
 * 作用：
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "user_provider.db";
    public static final String USER_TABLE_NAME = "user";
    public static final String INFO_TABLE_NAME = "info";
    private static final int DB_VERSION = 1;
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "+USER_TABLE_NAME+" (_ID INTEGER PRIMARY KEY AUTOINCREMENT," + "NAME TEXT," +"PASS TEXT,"+"PHONE LONG"+")";
    private String CREATE_INFO_TABLE = "CREATE TABLE IF NOT EXISTS "+INFO_TABLE_NAME+" (_ID INTEGER PRIMARY KEY AUTOINCREMENT," + "MSG TEXT"+")";
    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_INFO_TABLE);
        Toast.makeText(context, "版本："+db.getVersion()+"--数据库创建成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

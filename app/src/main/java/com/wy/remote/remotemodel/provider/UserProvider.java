package com.wy.remote.remotemodel.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.wy.remote.remotemodel.db.DbOpenHelper;

/**
 * 版本：V1.2.5
 * 时间： 2018/5/23 10:54
 * 创建人：laoqb
 * 作用：
 */
public class UserProvider extends ContentProvider {
    private static final String TAG = "UserProvider";
    private static final String AUTHORITY = "com.wy.remote.remotemodel";
    public static final int USER_URI_CODE = 0;
    public static final int INFO_URI_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Context mContext;
    private SQLiteDatabase mDb;

    static {
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "info", INFO_URI_CODE);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            case INFO_URI_CODE:
                tableName = DbOpenHelper.INFO_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
    }


    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs,
                        String orderBy) {
        Log.e(TAG, "query current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDb.query(table, columns, selection, selectionArgs, null, null, orderBy, null);
    }

    @Override
    public String getType(Uri arg0) {
        Log.e(TAG, "getType current thread:" + Thread.currentThread().getName());
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.e(TAG, "insert current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb.insert(table, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        Log.e(TAG, "delete current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.delete(table, whereClause, whereArgs);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        Log.e(TAG, "update current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = mDb.update(table, values, whereClause, whereArgs);
        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Log.e(TAG, "method:"+method+"---arg:"+arg+"---extras:"+extras.getBoolean("method"));
        Bundle bundle = new Bundle();
        // put一个boolean值，如果执行方法成功返回true、失败返回fasle
        bundle.putBoolean(method, false);
        return bundle;
    }
}

package com.vlee78.android.vl;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public abstract class VLDatabaseModel extends VLModel {
    private HashMap<String, SQLiteDatabase> mDatabaseMap;

    @Override
    protected void onCreate() {
        super.onCreate();
        mDatabaseMap = new HashMap<>();
    }

    protected abstract int onGetSchemeVersion();

    protected abstract void onCreateScheme(SQLiteDatabase db);

    protected abstract void onUpdateScheme(SQLiteDatabase db, int schemeVersion);

    protected abstract void onOpenDatabase(SQLiteDatabase db);

    public SQLiteDatabase getDatabase(String name) {

        if (VLUtils.threadInMain()) {
            VLDebug.logD("UI线程中调用数据库建议优化");
        }

        SQLiteDatabase database = mDatabaseMap.get(name);
        if (database == null) {
            database = openDatabase(name);
            mDatabaseMap.put(name, database);
        }
        return database;
    }

    public SQLiteDatabase openDatabase(String name) {
        if (VLUtils.threadInMain()) {
            VLDebug.logD("UI线程中调用数据库建议优化");
        }
        String dbName = name + ".db";
        int schemeVersion = onGetSchemeVersion();
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(getConcretApplication(), dbName, null, schemeVersion) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                onCreateScheme(db);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                for (int i = oldVersion + 1; i <= newVersion; i++) onUpdateScheme(db, i);
            }

            @Override
            public void onOpen(SQLiteDatabase db) {
                onOpenDatabase(db);
            }
        };
        return dbHelper.getWritableDatabase();
    }

    public void closeDatabse(String name) {
        if (VLUtils.threadInMain()) {
            VLDebug.logD("UI线程中调用数据库建议优化");
        }
        SQLiteDatabase database = mDatabaseMap.get(name);
        if (database != null) {
            database.close();
            mDatabaseMap.remove(name);
        }
    }

}

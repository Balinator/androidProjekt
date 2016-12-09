package com.example.balinator.androidprojekt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Balinator on 10/9/2016.
 */
public class MySqLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SERVICES = "services";
    public static final String COLUMN_SERVICES_ID = "id";
    public static final String COLUMN_SERVICES_NAME = "name";
    public static final String COLUMN_SERVICES_DESCRIPTION = "description";

    public static final String TABLE_SERVICE_LOGS = "service_logs";
    public static final String COLUMN_SERVICE_LOGS_ID = "id";
    public static final String COLUMN_SERVICE_LOGS_TIME = "time";
    public static final String COLUMN_SERVICE_LOGS_DATA = "data";

    private static final String DATABASE_NAME = "widget.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SERVICES = "create table "
            + TABLE_SERVICES + "( "
            + COLUMN_SERVICES_ID + " integer primary key autoincrement, "
            + COLUMN_SERVICES_NAME + " text not null,"
            + COLUMN_SERVICES_DESCRIPTION + " text not null"
            + " )";

    private static final String DATABASE_CREATE_SERVICE_LOGS = "create table "
            + TABLE_SERVICE_LOGS + "( "
            + COLUMN_SERVICE_LOGS_ID + " integer not null, "
            + COLUMN_SERVICE_LOGS_TIME + " integer not null,"
            + COLUMN_SERVICE_LOGS_DATA + " text not null"
            + " )";

    public MySqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_SERVICES);
        database.execSQL(DATABASE_CREATE_SERVICE_LOGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySqLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " +newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_LOGS);
        onCreate(db);
    }
}

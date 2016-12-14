package com.example.balinator.androidprojekt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.balinator.androidprojekt.struct.MyService;
import com.example.balinator.androidprojekt.struct.ServiceLog;

import java.util.ArrayList;

/**
 * Created by Balinator on 2016. 12. 08..
 */
public class Database {

    private SQLiteDatabase database;
    private MySqLiteHelper dbHelper;
    private String[] allColumnsService = {
            MySqLiteHelper.COLUMN_SERVICES_ID,
            MySqLiteHelper.COLUMN_SERVICES_NAME,
            MySqLiteHelper.COLUMN_SERVICES_DESCRIPTION,
            MySqLiteHelper.COLUMN_SERVICES_FAVORITE
    };

    private String[] allColumnsServiceLog = {
            MySqLiteHelper.COLUMN_SERVICE_LOGS_ID,
            MySqLiteHelper.COLUMN_SERVICE_LOGS_TIME,
            MySqLiteHelper.COLUMN_SERVICE_LOGS_DATA
    };

    public Database(Context context) {
        dbHelper = new MySqLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public MyService createService(String name, String description) {
        ContentValues values = new ContentValues();

        values.put(MySqLiteHelper.COLUMN_SERVICES_NAME, name);
        values.put(MySqLiteHelper.COLUMN_SERVICES_DESCRIPTION, description);
        values.put(MySqLiteHelper.COLUMN_SERVICES_FAVORITE, 0);

        long insertId = database.insert(MySqLiteHelper.TABLE_SERVICES, null,
                values);
        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICES,
                allColumnsService, MySqLiteHelper.COLUMN_SERVICES_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        MyService newService = cursorToService(cursor);
        cursor.close();
        return newService;
    }

    public ServiceLog createServiceLog(int id, String data) {
        ContentValues values = new ContentValues();

        values.put(MySqLiteHelper.COLUMN_SERVICE_LOGS_ID, id);
        values.put(MySqLiteHelper.COLUMN_SERVICE_LOGS_TIME, System.currentTimeMillis());
        values.put(MySqLiteHelper.COLUMN_SERVICE_LOGS_DATA, data);

        long insertId = database.insert(MySqLiteHelper.TABLE_SERVICE_LOGS, null,
                values);
        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICE_LOGS,
                allColumnsServiceLog, MySqLiteHelper.COLUMN_SERVICE_LOGS_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ServiceLog newServiceLog = cursorToLog(cursor);
        cursor.close();
        return newServiceLog;
    }

    public MyService updateService(long id, String name, String desciption, int favorite) {
        ContentValues values = new ContentValues();

        values.put(MySqLiteHelper.COLUMN_SERVICES_NAME, name);
        values.put(MySqLiteHelper.COLUMN_SERVICES_DESCRIPTION, desciption);
        values.put(MySqLiteHelper.COLUMN_SERVICES_FAVORITE, favorite);

        String[] updateId = {Long.toString(id)};

        long backId = database.update(MySqLiteHelper.TABLE_SERVICES,values,MySqLiteHelper.COLUMN_SERVICES_ID + " = ?", updateId);
        return getService(backId);
    }

    private MyService getService(long id) {

        String[] getId = {Long.toString(id)};

        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICES,
                allColumnsService,MySqLiteHelper.COLUMN_SERVICES_ID + " = ?",getId,null,null,null);

        cursor.moveToFirst();
        MyService service = cursorToService(cursor);

        cursor.close();
        return service;
    }

    private ServiceLog getServiceLog(long id) {

        String[] getId = {Long.toString(id)};

        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICE_LOGS,
                allColumnsServiceLog,MySqLiteHelper.COLUMN_SERVICE_LOGS_ID + " = ?",getId,null,null,null);

        cursor.moveToFirst();
        ServiceLog log = cursorToLog(cursor);
        cursor.close();
        return log;
    }

    public void deleteService(MyService service) {
        long id = service.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySqLiteHelper.TABLE_SERVICES, MySqLiteHelper.COLUMN_SERVICES_ID
                + " = " + id, null);
    }

    public void deleteServiceLog(ServiceLog log) {
        long id = log.getId();
        long time = log.getTime();
        System.out.println("Comment deleted with id: " + id + " and time: " + time);
        database.delete(MySqLiteHelper.TABLE_SERVICE_LOGS, MySqLiteHelper.COLUMN_SERVICE_LOGS_ID
                + " = " + id + " and " + MySqLiteHelper.COLUMN_SERVICE_LOGS_TIME + " = " + time, null);
    }

    public ArrayList<MyService> getAllService() {
        ArrayList<MyService> services = new ArrayList<>();

        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICES,
                allColumnsService, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MyService service = cursorToService(cursor);
            services.add(service);
            cursor.moveToNext();
        }
        cursor.close();
        return services;
    }

    public ArrayList<MyService> getAllFavoriteService() {
        ArrayList<MyService> services = new ArrayList<>();

        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICES,
                allColumnsService, MySqLiteHelper.COLUMN_SERVICES_FAVORITE + " = 1", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MyService service = cursorToService(cursor);
            services.add(service);
            cursor.moveToNext();
        }
        cursor.close();
        return services;
    }

    public ArrayList<ServiceLog> getAllServiceLog(long id) {
        ArrayList<ServiceLog> logs = new ArrayList<>();

        Cursor cursor = database.query(MySqLiteHelper.TABLE_SERVICE_LOGS,
                allColumnsServiceLog, MySqLiteHelper.COLUMN_SERVICES_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ServiceLog log = cursorToLog(cursor);
            logs.add(log);
            cursor.moveToNext();
        }
        cursor.close();
        return logs;
    }

    private MyService cursorToService(Cursor cursor) {
        MyService service = new MyService();

        service.setId(cursor.getLong(0));
        service.setName(cursor.getString(1));
        service.setDescription(cursor.getString(2));
        service.setFavorite(cursor.getInt(3)==1);

        return service;
    }
    private ServiceLog cursorToLog(Cursor cursor) {
        return new ServiceLog(cursor.getLong(0),cursor.getLong(1),cursor.getString(2));
    }
}
package com.acceedo.attendancesystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(long id, String type, String title, String description, String target,
                       String start, String end, String created, String specific, String duration, String reminder_date, String reminder_time) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ID, id);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.DESC, created);
        contentValue.put(DatabaseHelper.TARGET, target);
        contentValue.put(DatabaseHelper.START, start);
        contentValue.put(DatabaseHelper.END, end);
        contentValue.put(DatabaseHelper.CREATED, created);
        contentValue.put(DatabaseHelper.SPECIFIC, specific);
        contentValue.put(DatabaseHelper.DURATION, duration);
        contentValue.put(DatabaseHelper.REMINDER_DATE, reminder_date);
        contentValue.put(DatabaseHelper.REMINDER_TIME, reminder_time);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor check(long id)
    {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, null, DatabaseHelper.ID + " = " + id, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch()
    {
//        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.DURATION };
        String ORDERBY = DatabaseHelper.ID+" DESC";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, ORDERBY);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long id, String type, String title, String description, String target,
                      String start, String end, String created, String specific, String duration, String reminder_date, String reminder_time) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.DESC, created);
        contentValue.put(DatabaseHelper.TARGET, target);
        contentValue.put(DatabaseHelper.START, start);
        contentValue.put(DatabaseHelper.END, end);
        contentValue.put(DatabaseHelper.CREATED, created);
        contentValue.put(DatabaseHelper.SPECIFIC, specific);
        contentValue.put(DatabaseHelper.DURATION, duration);
        contentValue.put(DatabaseHelper.REMINDER_DATE, reminder_date);
        contentValue.put(DatabaseHelper.REMINDER_TIME, reminder_time);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValue, DatabaseHelper.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + id, null);
    }

}

package com.acceedo.attendancesystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "EVENTS";

    // Table columns
    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String DESC = "description";
    public static final String TARGET = "target";
    public static final String START = "start_time";
    public static final String END = "end_time";
    public static final String CREATED = "created";
    public static final String SPECIFIC = "specific";
    public static final String DURATION = "duration";
    public static final String REMINDER_DATE = "reminder_date";
    public static final String REMINDER_TIME = "reminder_time";

    // Database Information
    static final String DB_NAME = "SEYYONE.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY, "
            + TYPE + " TEXT, "
            + TITLE + " TEXT NOT NULL, "
            + DESC + " TEXT, "
            + TARGET + " TEXT, "
            + START + " TEXT NOT NULL, "
            + END + " TEXT NOT NULL, "
            + CREATED + " TEXT, "
            + SPECIFIC + " TEXT, "
            + DURATION + " TEXT, "
            + REMINDER_DATE + " TEXT, "
            + REMINDER_TIME + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

package com.example.hp.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    // columns of the events table
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_DATA_START = "data_start";
    public static final String COLUMN_TIME_START = "time_start";
    public static final String COLUMN_DATA_END = "data_end";
    public static final String COLUMN_TIME_END = "time_end";
    public static final String COLUMN_HISTORY = "history";
    public static final String COLUMN_NOTIFICATIONS_START = "notifications_start";
    public static final String COLUMN_NOTIFICATIONS_END = "notification_end";
    public static final String COLUMN_AUTO_NOTIFICATIONS = "auto_notification";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_LOCALISATION = "localisation";
    public static final String COLUMN_REPETITION = "repetition";
    public static final String COLUMN_MOTHER_ID = "mother_id";


    // columns of the attendance table
    public static final String TABLE_ATTENDANCE = "attendance";
    public static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    public static final String COLUMN_ATTENDANCE_EVENT_ID = "event_id";
    public static final String COLUMN_ATTENDANCE_CONTACT_ID = "contact_id";


    //columns of the contacts table

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_CONTACTS_ID = "contacts_id";
    public static final String COLUMN_CONTACTS_PHONE = "phone";
//    public static final String COLUMN_CONTACTS_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_CONTACTS_NAME = "name";
    public static final String COLUMN_CONTACTS_LAST_NAME = "last_name";


    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

//    // SQL statement of the events table creation
//    private static final String SQL_CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + "("
//            + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + COLUMN_DATA_START + " TEXT NOT NULL, "
//            + COLUMN_TIME_START + " TEXT, "
//            + COLUMN_DATA_END + " TEXT NOT NULL, "
//            + COLUMN_TIME_END + " TEXT, "
//            + COLUMN_HISTORY + " BOOLEAN, "
//            + COLUMN_NOTIFICATIONS_START + " BOOLEAN, "
//            + COLUMN_NOTIFICATIONS_END + " BOOLEAN, "
//            + COLUMN_AUTO_NOTIFICATIONS + " BOOLEAN, "
//            + COLUMN_RADIUS + " INTEGER NOT NULL, "
//            + COLUMN_LOCALISATION + " TEXT NOT NULL, "
//            + COLUMN_REPETITION + " INTEGER NOT NULL, "
//            + COLUMN_MOTHER_ID + " INTEGER NOT NULL "
//            +");";

    private static final String SQL_CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + "("
            + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATA_START + " TEXT NOT NULL, "
            + COLUMN_TIME_START + " TEXT, "
            + COLUMN_DATA_END + " TEXT NOT NULL, "
            + COLUMN_TIME_END + " TEXT, "
            + COLUMN_HISTORY + " BOOLEAN, "
            + COLUMN_NOTIFICATIONS_START + " BOOLEAN, "
            + COLUMN_NOTIFICATIONS_END + " BOOLEAN, "
            + COLUMN_AUTO_NOTIFICATIONS + " BOOLEAN, "
            + COLUMN_RADIUS + " INTEGER, "
            + COLUMN_LOCALISATION + " TEXT, "
            + COLUMN_REPETITION + " INTEGER, "
            + COLUMN_MOTHER_ID + " INTEGER "
            +");";

    // SQL statement of the attendance table creation
    private static final String SQL_CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
            + COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ATTENDANCE_EVENT_ID + " INTEGER, "
//            + COLUMN_ATTENDANCE_EVENT_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_ATTENDANCE_CONTACT_ID + " INTEGER NOT NULL "
            +");";


    private static final String SQL_CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
            + COLUMN_CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACTS_PHONE + " TEXT NOT NULL, "
//            + COLUMN_CONTACTS_PHONE_NUMBER + " TEXT NOT NULL, "
            + COLUMN_CONTACTS_NAME + " TEXT NOT NULL, "
            + COLUMN_CONTACTS_LAST_NAME + " TEXT NOT NULL "
            +");";



    public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

    @Override
    public void onCreate(SQLiteDatabase database) {
            database.execSQL(SQL_CREATE_TABLE_EVENTS);
            database.execSQL(SQL_CREATE_TABLE_ATTENDANCE);
            database.execSQL(SQL_CREATE_TABLE_CONTACTS);
            }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG,
            "Upgrading the database from version " + oldVersion + " to "+ newVersion);
            // clear all data
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            // recreate the tables
            onCreate(db);
            }

    public DBHelper(Context context, String name, CursorFactory factory,int version) {
            super(context, DATABASE_NAME, factory, DATABASE_VERSION);
            }
        }
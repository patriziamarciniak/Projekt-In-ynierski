package com.example.hp.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hp.sqlite.model.Contacts;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.Attendance;


public class EventDAO {

    public static final String TAG = "EventDAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_EVENT_ID,
            DBHelper.COLUMN_DATA_START, DBHelper.COLUMN_TIME_START,
            DBHelper.COLUMN_DATA_END, DBHelper.COLUMN_TIME_END, DBHelper.COLUMN_HISTORY,
            DBHelper.COLUMN_NOTIFICATIONS_START, DBHelper.COLUMN_NOTIFICATIONS_END, DBHelper.COLUMN_AUTO_NOTIFICATIONS,
            DBHelper.COLUMN_RADIUS, DBHelper.COLUMN_LOCALISATION, DBHelper.COLUMN_REPETITION, DBHelper.COLUMN_MOTHER_ID };


    public EventDAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Event createEvent( String dataStart, String timeStart, String dataEnd, String timeEnd, boolean history, boolean notificationsStart, boolean notificationsEnd,
                              boolean autoNotifications, int radius, String localisation, int repetition, long motherId ) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_DATA_START, dataStart);
        values.put(DBHelper.COLUMN_TIME_START, timeStart);
        values.put(DBHelper.COLUMN_DATA_END, dataEnd);
        values.put(DBHelper.COLUMN_TIME_END, timeEnd);
        values.put(DBHelper.COLUMN_HISTORY, history);
        values.put(DBHelper.COLUMN_NOTIFICATIONS_START, notificationsStart);
        values.put(DBHelper.COLUMN_NOTIFICATIONS_END, notificationsEnd);
        values.put(DBHelper.COLUMN_AUTO_NOTIFICATIONS, autoNotifications);
        values.put(DBHelper.COLUMN_RADIUS, radius);
        values.put(DBHelper.COLUMN_LOCALISATION, localisation);
        values.put(DBHelper.COLUMN_REPETITION, repetition);
        values.put(DBHelper.COLUMN_MOTHER_ID, motherId);


        long insertId = mDatabase
                .insert(DBHelper.TABLE_EVENTS, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_EVENTS, mAllColumns,
                DBHelper.COLUMN_EVENT_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Event newEvent = cursorToEvent(cursor);
        cursor.close();
        return newEvent;
    }


    public void deleteEvent(Event event) {
        long id = event.getId();
        System.out.println("the deleted event has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_EVENTS, DBHelper.COLUMN_EVENT_ID
                + " = " + id, null);
    }

    public void editEvent( Long id, String dataStart, String dataEnd, String timeStart,  String timeEnd, boolean history, boolean notificationsStart, boolean notificationsEnd,
                         boolean autoNotifications, int radius, String localisation, int repetition, long motherId ) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_DATA_START, dataStart);
        values.put(DBHelper.COLUMN_DATA_END, dataEnd);
        values.put(DBHelper.COLUMN_TIME_START, timeStart);
        values.put(DBHelper.COLUMN_TIME_END, timeEnd);
        values.put(DBHelper.COLUMN_HISTORY, history);
        values.put(DBHelper.COLUMN_NOTIFICATIONS_START, notificationsStart);
        values.put(DBHelper.COLUMN_NOTIFICATIONS_END, notificationsEnd);
        values.put(DBHelper.COLUMN_AUTO_NOTIFICATIONS, autoNotifications);
        values.put(DBHelper.COLUMN_RADIUS, radius);
        values.put(DBHelper.COLUMN_LOCALISATION, localisation);
        values.put(DBHelper.COLUMN_REPETITION, repetition);
        values.put(DBHelper.COLUMN_MOTHER_ID, motherId);

        mDatabase.update(DBHelper.TABLE_EVENTS, values, DBHelper.COLUMN_EVENT_ID + " = " + id, null);
    }


    public List<Event> getAllEvents() {
        List<Event> listEvents = new ArrayList<Event>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_EVENTS, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Event event = cursorToEvent(cursor);
                listEvents.add(event);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
        }
        return listEvents;
    }

    public int countEvents() {

        Cursor cursor = mDatabase.query(DBHelper.TABLE_EVENTS, mAllColumns, null, null, null, null, null);
        if (cursor == null){
            return 0;
        }
        cursor.moveToLast();
        return cursor.getPosition();
    }

    public Event getEventById(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_EVENTS, mAllColumns,
                DBHelper.COLUMN_EVENT_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        cursor.close();
        Event event = cursorToEvent(cursor);
        return event;
    }

    public Event getLastEvent(){
        Cursor cursor = mDatabase.query(DBHelper.TABLE_EVENTS, mAllColumns, null, null, null, null, null);
        cursor.moveToLast();
        Event event = cursorToEvent(cursor);
        return event;
    }

    protected Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getLong(0));
        event.setDataStart(cursor.getString(1));
        event.setTimeStart(cursor.getString(2));
        event.setDataEnd(cursor.getString(3));
        event.setTimeEnd(cursor.getString(4));
        event.setHistory(cursor.getInt(5) > 0);
        event.setNotificationsStart(cursor.getInt(6) > 0);
        event.setNotificationsEnd(cursor.getInt(7) > 0);
        event.setAutoNotifications(cursor.getInt(8) > 0);
        event.setRadius(cursor.getInt(9));
        event.setLocalisation(cursor.getString(10));
        event.setRepetition(cursor.getInt(11));
        event.setMotherId(cursor.getLong(12));

        return event;
    }

}

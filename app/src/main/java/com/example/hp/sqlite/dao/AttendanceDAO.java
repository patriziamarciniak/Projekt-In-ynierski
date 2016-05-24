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

public class AttendanceDAO {

    public static final String TAG = "AttendanceDAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_ATTENDANCE_ID, DBHelper.COLUMN_ATTENDANCE_EVENT_ID, DBHelper.COLUMN_ATTENDANCE_CONTACT_ID };


    public AttendanceDAO(Context context) {
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

    public Attendance createAttendance( long eventId,  long contactsId ) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_ATTENDANCE_EVENT_ID, eventId);
        values.put(DBHelper.COLUMN_CONTACTS_ID, contactsId);


        long insertId = mDatabase
                .insert(DBHelper.TABLE_ATTENDANCE, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ATTENDANCE, mAllColumns,
                DBHelper.COLUMN_ATTENDANCE_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Attendance newAttendance = cursorToAttendance(cursor);
        cursor.close();
        return newAttendance;
    }


    public void deleteAttendance(Attendance attendance) {
        long id = attendance.getId();
        System.out.println("the deleted attendance has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_ATTENDANCE, DBHelper.COLUMN_ATTENDANCE_ID
                + " = " + id, null);
    }


    public List<Attendance> getAllAttendance() {
        List<Attendance> listAttendance = new ArrayList<Attendance>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_ATTENDANCE, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attendance attendance = cursorToAttendance(cursor);
                listAttendance.add(attendance);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
        }
        return listAttendance;
    }

    public Attendance getAttendanceById(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ATTENDANCE, mAllColumns,
                DBHelper.COLUMN_ATTENDANCE_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Attendance attendance = cursorToAttendance(cursor);
        return attendance;
    }

    protected Attendance cursorToAttendance(Cursor cursor) {
        Attendance attendance = new Attendance();

        long eventId = cursor.getLong(0);
        EventDAO dao= new EventDAO(mContext);
        Event event = dao.getEventById(eventId);
        if(event!=null){
            attendance.setEvent(event);
        }

        long contactsId = cursor.getLong(1);
        ContactsDAO dao1= new ContactsDAO(mContext);
        Contacts contacts = dao1.getContactsById(contactsId);
        if(event!=null){
            attendance.setContacts(contacts);
        }


        return attendance;
    }

}

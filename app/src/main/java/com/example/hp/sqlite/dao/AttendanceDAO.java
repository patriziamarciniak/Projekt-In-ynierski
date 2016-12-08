package com.example.hp.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hp.sqlite.model.Contacts;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.Attendance;
import com.example.hp.sqlite.model.PhoneContact;

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
        values.put(DBHelper.COLUMN_ATTENDANCE_CONTACT_ID, contactsId);


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


    public List<Attendance> getAttendancesByEventId(long eventId) {

        List<Attendance> attendances = new ArrayList<Attendance>(){};

        Cursor cursor = mDatabase.query(DBHelper.TABLE_ATTENDANCE, mAllColumns,
                DBHelper.COLUMN_ATTENDANCE_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attendance attendance = cursorToAttendance(cursor);
                attendances.add(attendance);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return attendances;
    }


    protected Attendance cursorToAttendance(Cursor cursor) {
        Attendance attendance = new Attendance();

        attendance.setId(cursor.getLong(0));
        attendance.setEvent(cursor.getLong(1));
        attendance.setContacts(cursor.getLong(2));

        return attendance;
    }


    public List<PhoneContact> getContacts(long eventId) {

        List<PhoneContact> phoneContactList = new ArrayList<PhoneContact>(){};
        PhoneContactsDAO phoneContactsDAO = new PhoneContactsDAO(mContext);

        Cursor cursor = mDatabase.query(DBHelper.TABLE_ATTENDANCE, mAllColumns,
                DBHelper.COLUMN_ATTENDANCE_EVENT_ID + " = ?",
                new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attendance attendance = cursorToAttendance(cursor);
                phoneContactList.add(phoneContactsDAO.getContactsById(attendance.getContacts()));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return phoneContactList;
    }


}

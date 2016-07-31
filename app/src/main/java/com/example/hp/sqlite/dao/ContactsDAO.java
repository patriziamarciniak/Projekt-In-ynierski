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

public class ContactsDAO {

    public static final String TAG = "ContactsDAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_CONTACTS_ID,
            DBHelper.COLUMN_CONTACTS_PHONE, DBHelper.COLUMN_CONTACTS_NAME,
            DBHelper.COLUMN_CONTACTS_LAST_NAME };

    public ContactsDAO(Context context) {
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
        Log.println(Log.INFO,TAG,"SQLException on openning database ");
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Contacts createContacts( String phoneNumber, String name, String lastName ) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CONTACTS_PHONE, phoneNumber);
        values.put(DBHelper.COLUMN_CONTACTS_NAME, name);
        values.put(DBHelper.COLUMN_CONTACTS_LAST_NAME, lastName);
        long insertId = mDatabase
                .insert(DBHelper.TABLE_CONTACTS, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_CONTACTS, mAllColumns,
                DBHelper.COLUMN_CONTACTS_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Contacts newContacts = cursorToContacts(cursor);
        cursor.close();
        return newContacts;
    }


    public void deleteContacts(Contacts contacts) {
        long id = contacts.getId();
        System.out.println("the deleted contact has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_CONTACTS, DBHelper.COLUMN_CONTACTS_ID
                + " = " + id, null);
    }


    public List<Contacts> getAllContacts() {
        List<Contacts> listContacts = new ArrayList<Contacts>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_CONTACTS, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Contacts contact = cursorToContacts(cursor);
                listContacts.add(contact);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
        }
        return listContacts;
    }

    public Contacts getContactsById(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_CONTACTS, mAllColumns,
                DBHelper.COLUMN_CONTACTS_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contacts contact = cursorToContacts(cursor);
        return contact;
    }

    protected Contacts cursorToContacts(Cursor cursor) {
        Contacts contact = new Contacts();
        contact.setId(cursor.getLong(0));
        contact.setPhoneNumber(cursor.getString(1));
        contact.setName(cursor.getString(2));
        contact.setLastName(cursor.getString(3));
        return contact;
    }

}

package com.example.hp.sqlite.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.hp.sqlite.model.PhoneContact;

import java.util.ArrayList;
import java.util.List;

public class PhoneContactsDAO {

        public static final String TAG = "PhoneContactsDAO";

        private SQLiteDatabase mDatabase;
        private DBHelper mDbHelper;
        private Context mContext;
        private ContentResolver contentResolver;
        private String[] mAllColumns = { DBHelper.COLUMN_CONTACTS_ID,
                DBHelper.COLUMN_CONTACTS_PHONE, DBHelper.COLUMN_CONTACTS_NAME};

        public PhoneContactsDAO(Context context) {
            this.mContext = context;
            this.contentResolver = mContext.getContentResolver();
            mDbHelper = new DBHelper(context);
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

        public List<PhoneContact> getAllContacts(ContentResolver cr) {

            List<PhoneContact> listContacts = new ArrayList<PhoneContact>();
            Cursor c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

            while (c.moveToNext()) {
                PhoneContact phoneContact = new PhoneContact();
                phoneContact.setId(c.getLong(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
                phoneContact.setName(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                phoneContact.setPhoneNumber(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                //ograniczenie zeby nie oddawaly sie dwa razy te same kontakty - do przemyslenia kryterium
                boolean present = false;
                for (int i = 0; i < listContacts.size(); i++){
                    if (listContacts.get(i).getName() == phoneContact.getName()) present = true;
                }

                if (!present)listContacts.add(phoneContact);
            }
            c.close();
            return listContacts;
        }

        public PhoneContact getContactsById(long id) {
            PhoneContact contact = new PhoneContact();
            Cursor c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone._ID + " = ?",
                    new String[]{String.valueOf(id)}, null);
            while (c.moveToNext()) {
                contact.setId(c.getLong(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
                contact.setName(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                contact.setPhoneNumber(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
            c.close();
            return contact;
        }

        protected PhoneContact cursorToContacts(Cursor cursor) {
            PhoneContact contact = new PhoneContact();
            contact.setId(cursor.getLong(0));
            contact.setPhoneNumber(cursor.getString(1));
            contact.setName(cursor.getString(2));
            return contact;
        }

    }



package com.example.hp.sqlite.model;

import android.os.Parcelable;

import java.io.Serializable;

public class PhoneContact implements Serializable {

    public static final String TAG = "PhoneContacts";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mPhoneNumber;
    private String mName;

    public PhoneContact() {    }

    public PhoneContact(long id, String phoneNumber, String name) {
        this.mId = id;
        this.mPhoneNumber = phoneNumber;
        this.mName = name;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}

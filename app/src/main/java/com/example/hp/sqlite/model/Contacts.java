package com.example.hp.sqlite.model;

import java.io.Serializable;

public class Contacts implements Serializable {

    public static final String TAG = "Contacts";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mPhoneNumber;
    private String mName;
    private String mLastName;

    public Contacts() {}


    public Contacts(String phoneNumber, String name, String lastName) {
        this.mPhoneNumber = phoneNumber;
        this.mName = name;
        this.mLastName = lastName;
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

    public String getLastName() {
        return mLastName;
    }
    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }
}

package com.example.hp.sqlite.model;

import java.io.Serializable;

public class Event implements Serializable {

    public static final String TAG = "Event";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mDataStart;
    private String mTimeStart;
    private String mDataEnd;
    private String mTimeEnd;
    private boolean mHistory;
    private boolean mNotificationsStart;
    private boolean mNotificationsEnd;
    private boolean mAutoNotifications;
    private int mRadius;
    private String mLocalisation;
    private int mRepetition;
    private long mMotherId;


    public Event() {}


    public Event(String dataStart, String timeStart, String dataEnd, String timeEnd, boolean history, boolean notificationsStart, boolean notificationsEnd,
                 boolean autoNotifications, int radius, String localisation, int repetition, long motherId ) {

        this.mDataStart = dataStart;
        this.mTimeStart = timeStart;
        this.mDataEnd = dataEnd;
        this.mTimeEnd = timeEnd;
        this.mHistory = history;
        this.mNotificationsStart = notificationsStart;
        this.mNotificationsEnd = notificationsEnd;
        this.mAutoNotifications = autoNotifications;
        this.mRadius = radius;
        this.mLocalisation = localisation;
        this.mRepetition = repetition;
        this.mMotherId = motherId;


    }


    public long getId() {
        return mId;
    }
    public void setId(long mId) {
        this.mId = mId;
    }

    public String getDataStart() {
        return mDataStart;
    }
    public void setDataStart(String mDataStart) {
        this.mDataStart = mDataStart;
    }

    public String getTimeStart() {
        return mTimeStart;
    }
    public void setTimeStart(String mTimeStart) { this.mTimeStart = mTimeStart; }

    public String getDataEnd() {
        return mDataEnd;
    }
    public void setDataEnd(String mDataEnd) {
        this.mDataEnd = mDataEnd;
    }

    public String getTimeEnd() {
        return mTimeEnd;
    }
    public void setTimeEnd(String mTimeEnd) {
        this.mTimeEnd = mTimeEnd;
    }

    public boolean getHistory() {
        return mHistory;
    }
    public void setHistory(boolean mHistory) {
        this.mHistory = mHistory;
    }

    public boolean getNotificationsStart() {
        return mNotificationsStart;
    }
    public void setNotificationsStart(boolean mNotificationsStart) { this.mNotificationsStart = mNotificationsStart; }

    public boolean getNotificationsEnd() {
        return mNotificationsEnd;
    }
    public void setNotificationsEnd(boolean mNotificationsEnd) { this.mNotificationsEnd = mNotificationsEnd; }

    public boolean getAutoNotifications() {
        return mAutoNotifications;
    }
    public void setAutoNotifications(boolean mAutoNotifications) { this.mAutoNotifications = mAutoNotifications; }

    public int getRadius() {
        return mRadius;
    }
    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public String getLocalisation() { return mLocalisation; }
    public void setLocalisation(String mLocalisation) { this.mLocalisation = mLocalisation;}

    public int getRepetition(){ return mRepetition; }
    public void setRepetition(int mRepetition) { this.mRepetition = mRepetition;}

    public long getMotherId(){return mMotherId;}
    public void setMotherId(long mMotherId){this.mMotherId = mMotherId;}

}

package com.example.hp.sqlite.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Event implements  Parcelable{

    public static final String TAG = "Event";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mDataStart;
    private String mTimeStart;
    private String mDataEnd;
    private String mTimeEnd;
    private boolean mNotificationsStart;
    private boolean mNotificationsEnd;
    private boolean mAutoNotifications;
    private int mRadius;
    private String mStartLocalisationX;
    private String mStartLocalisationY;
    private String mEndLocalisationX;
    private String mEndLocalisationY;
    private int mRepetition;
    private long mMotherId;


    public Event(){};

    public Event(String dataStart, String timeStart, String dataEnd, String timeEnd, boolean notificationsStart, boolean notificationsEnd,
                 boolean autoNotifications, int radius, String startLocalisationX, String startLocalisationY, String endLocalisationX, String endLocalisationY, int repetition, long motherId ) {

        this.mDataStart = dataStart;
        this.mTimeStart = timeStart;
        this.mDataEnd = dataEnd;
        this.mTimeEnd = timeEnd;
        this.mNotificationsStart = notificationsStart;
        this.mNotificationsEnd = notificationsEnd;
        this.mAutoNotifications = autoNotifications;
        this.mRadius = radius;
        this.mStartLocalisationX = startLocalisationX;
        this.mStartLocalisationY = startLocalisationY;
        this.mEndLocalisationX = endLocalisationX;
        this.mEndLocalisationY = endLocalisationY;
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

    public String getStartLocalisationX() { return mStartLocalisationX; }

    public void setStartLocalisationX(String mStartLocalisationX) { this.mStartLocalisationX = mStartLocalisationX;}

    public String getStartLocalisationY() { return mStartLocalisationY; }

    public void setStartLocalisationY(String mStartLocalisationY) { this.mStartLocalisationY = mStartLocalisationY;}

    public String getEndLocalisationX() { return mEndLocalisationX; }

    public void setEndLocalisationX(String mEndLocalisationX) { this.mEndLocalisationX = mEndLocalisationX;}

    public String getEndLocalisationY() { return mEndLocalisationY; }

    public void setEndLocalisationY(String mEndLocalisationY) { this.mEndLocalisationY = mEndLocalisationY;}

    public int getRepetition(){ return mRepetition; }
    public void setRepetition(int mRepetition) { this.mRepetition = mRepetition;}

    public long getMotherId(){return mMotherId;}
    public void setMotherId(long mMotherId){this.mMotherId = mMotherId;}

    // Parcelling part
    public Event(Parcel in){
        String[] data = new String[15];

        in.readStringArray(data);
        this.mId = Long.parseLong(data[0]);
        this.mDataStart = data[1];
        this.mTimeStart = data[2];
        this.mDataEnd = data[3];
        this.mTimeEnd = data[4];
        this.mNotificationsStart = Boolean.parseBoolean(data[5]);
        this.mNotificationsEnd = Boolean.parseBoolean(data[6]);
        this.mAutoNotifications = Boolean.parseBoolean(data[7]);
        this.mRadius = Integer.parseInt(data[8]);
        this.mStartLocalisationX = data[9];
        this.mStartLocalisationY = data[10];
        this.mEndLocalisationX = data[11];
        this.mEndLocalisationY = data[12];
        this.mRepetition = Integer.parseInt(data[13]);
        this.mMotherId = Long.parseLong(data[14]);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(this.mId),
                this.mDataStart,
                this.mTimeStart ,
                this.mDataEnd ,
                this.mTimeEnd ,
                String.valueOf(this.mNotificationsStart),
                String.valueOf(this.mNotificationsEnd),
                String.valueOf(this.mAutoNotifications),
                String.valueOf(this.mRadius),
                this.mStartLocalisationX ,
                this.mStartLocalisationY ,
                this.mEndLocalisationX ,
                this.mEndLocalisationY ,
                String.valueOf(this.mRepetition),
                String.valueOf(this.mMotherId)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };


}

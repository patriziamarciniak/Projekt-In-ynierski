package com.example.hp.sqlite.model;

import java.io.Serializable;
import com.example.hp.sqlite.model.Contacts;
import com.example.hp.sqlite.model.Event;

public class Attendance implements Serializable {

    public static final String TAG = "Attendance";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private Event mEventId;
    private Contacts mContactId;

    public Attendance() {}

    public long getId() {
        return mId;
    }
    public void setId(long mId) {
        this.mId = mId;
    }

    public Event getEvent() {
        return mEventId;
    }
    public void setEvent(Event mEventId) {
        this.mEventId = mEventId;
    }

    public Contacts getContacts() {
        return mContactId;
    }
    public void setContacts(Contacts mContactId) {
        this.mContactId = mContactId;
    }

    }



package com.example.hp.firstapp2;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Attendance;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.PhoneContact;

import java.util.List;

public class TripFinished extends AppCompatActivity {

    Event event;
    EventDAO eventDAO;
    AttendanceDAO attendanceDAO;
    Button btnSendSMS, btnIgnore;
    List<PhoneContact> contacts;
    Context context = this;
    TextView people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_finished);
        eventDAO = new EventDAO(this);
        attendanceDAO = new AttendanceDAO(this);

        // zakładam tu, że przy wzbudzaniu obiekt event zostanie przekazany przez Intent.getextras jak w EditEvent
        Bundle bundle = getIntent().getExtras();
        event = bundle.getParcelable("event");

        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        contacts = attendanceDAO.getContacts(event.getId());
        people = (TextView) findViewById(R.id.txt_people);
        String displayContacts = "";
        for (PhoneContact phoneContact : contacts){displayContacts += " " + phoneContact.getName();}
        people.setText(displayContacts);

        btnSendSMS = (Button) findViewById(R.id.btn_send_sms);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strSMSBody = "Treść SMS";
                String strReceipentsList = "607162400";
                SmsManager sms = SmsManager.getDefault();
                List<String> messages = sms.divideMessage(strSMSBody);
                for (String message : messages) {
                    sms.sendTextMessage(strReceipentsList, null, message, PendingIntent.getBroadcast(
                            context, 0, new Intent(""), 0), null);
                }
            }
        });

        btnIgnore = (Button) findViewById(R.id.btn_ignore);
        btnIgnore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                eventDAO.deleteEvent(event);
            }
        });

    }
}

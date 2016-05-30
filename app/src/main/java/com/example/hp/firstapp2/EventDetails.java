package com.example.hp.firstapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hp.sqlite.model.Event;

public class EventDetails extends AppCompatActivity {

    TextView dateStart, dateEnd, timeStart, timeEnd, localizationStart, spinner, notAuto, notStart, notEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Bundle bundle = getIntent().getExtras();
        Event event = bundle.getParcelable("event");

        dateStart = (TextView) findViewById(R.id.txt_details_data_start);
        dateEnd = (TextView) findViewById(R.id.txt_details_data_end);
        timeStart = (TextView) findViewById(R.id.txt_details_time_start);
        timeEnd = (TextView) findViewById(R.id.txt_details_time_end);
        localizationStart = (TextView) findViewById(R.id.txt_details_localization);
        spinner = (TextView) findViewById(R.id.txt_details_radius);
        notAuto = (TextView) findViewById(R.id.txt_details_auto_notifications);
        notStart = (TextView) findViewById(R.id.txt_details_notification_start);
        notEnd = (TextView) findViewById(R.id.txt_details_notification_end);

        dateEnd.setText(event.getDataEnd());
        dateStart.setText(event.getDataStart());
        timeEnd.setText(event.getTimeEnd());
        timeStart.setText(event.getTimeStart());
        localizationStart.setText(event.getLocalisation());
        spinner.setText(String.valueOf(event.getRadius()));
        notAuto.setText(String.valueOf(event.getAutoNotifications()));
        notStart.setText(String.valueOf(event.getNotificationsStart()));
        notEnd.setText(String.valueOf(event.getNotificationsEnd()));

    }
}

package com.example.hp.firstapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hp.sqlite.dao.EventDAO;

public class AddEvent extends AppCompatActivity {

    EditText eventName, dateStart, dateEnd, timeStart, timeEnd, localizationStart, localizationEnd;
    Spinner radius;
    CheckBox notificationStart, notificationEnd, notificationAutomatic;
    Integer cyclicEvent;
    EventDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventName = (EditText) findViewById(R.id.text_trip_name);
        dateStart = (EditText) findViewById(R.id.text_date_start);
        dateEnd = (EditText) findViewById(R.id.text_date_end);
        timeStart = (EditText) findViewById(R.id.text_time_start);
        timeEnd = (EditText) findViewById(R.id.text_time_end);
        localizationStart = (EditText) findViewById(R.id.text_localization_start);
        localizationEnd = (EditText) findViewById(R.id.text_localization_end);
        notificationStart = (CheckBox) findViewById(R.id.checkBox_notification_start);
        notificationEnd = (CheckBox) findViewById(R.id.checkBox_notification_end);
        notificationAutomatic = (CheckBox) findViewById(R.id.checkBox_automatic_notification);
        cyclicEvent = 0;
        db = new EventDAO(this);

        radius = (Spinner) findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.radius_spinner, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(adapter);



        Button btnAddEvent = (Button) findViewById(R.id.btn_event_add);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                db.createEvent(
                        dateStart.getText().toString(),
                        timeStart.getText().toString(),
                        dateEnd.getText().toString(),
                        timeEnd.getText().toString(),
                        false,
                        notificationStart.isChecked(),
                        notificationEnd.isChecked(),
                        notificationAutomatic.isChecked(),
                        getRadius(radius.getSelectedItem().toString()),
                        localizationStart.getText().toString(),
                        cyclicEvent,
                        Long.valueOf(db.countEvents())
                );

                Intent nextScreen = new Intent(getApplicationContext(), EventAdded.class);
                startActivity(nextScreen);
            }
        });
    }

    private int getRadius(String selected){

        switch (selected){
            case "200m":
                return 200;
            case "500m":
                return 500;
            case "1km":
                return 1000;
            case "2km":
                return 2000;
            case "5km":
                return 5000;
        }
        return 0;
    }
}
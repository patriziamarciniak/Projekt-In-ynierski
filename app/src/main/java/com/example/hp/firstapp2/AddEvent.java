package com.example.hp.firstapp2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hp.sqlite.dao.EventDAO;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    EditText eventName, dateStart, dateEnd, timeStart, timeEnd, localizationStart, localizationEnd;
    Spinner radius;
    CheckBox notificationStart, notificationEnd, notificationAutomatic;
    Integer cyclicEvent;
    EventDAO db;

    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 998;

    private Calendar actualDate;

    private TextView activeDateDisplay;
    private Calendar activeDate;



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
        actualDate = Calendar.getInstance();

        Button btnAddEvent = (Button) findViewById(R.id.btn_add_event);

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

        radius = (Spinner) findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.radius_spinner, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(adapter);


        dateStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDateDialog(dateStart, actualDate);
                return false;
            }
        });

        dateEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDateDialog(dateEnd, actualDate);
                return false;
            }
        });

        timeStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showTimeDialog(timeStart, actualDate);
                return false;
            }
        });
        timeEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showTimeDialog(timeEnd, actualDate);
                return false;
            }
        });
        localizationEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                localizationEnd.setText(" ");
                return false;
            }
        });
        localizationStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                localizationStart.setText(" ");
                return false;
            }
        });

        updateDisplay(dateStart, actualDate);
        updateDisplay(dateEnd, actualDate);
        updateTimeDisplay(timeStart, actualDate);
        updateTimeDisplay(timeEnd, actualDate);
    }

    ////////////////////////////////////////////////////////////////////

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("/")
                        .append(date.get(Calendar.MONTH) + 1).append("/")
                        .append(date.get(Calendar.YEAR)).append(" "));
    }

    private void updateTimeDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        .append(date.get(Calendar.HOUR_OF_DAY)).append(":")
                        .append(date.get(Calendar.MINUTE)));
    }
    Calendar c = Calendar.getInstance();

    int Hr24=c.get(Calendar.HOUR_OF_DAY);
    int Min=c.get(Calendar.MINUTE);

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

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    public void showTimeDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(TIME_DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeSetListener, activeDate.get(Calendar.HOUR_OF_DAY), activeDate.get(Calendar.MINUTE),true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener
        = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
        }
    };


    private TimePickerDialog.OnTimeSetListener timeSetListener
            = new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute ) {

            activeDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeDateDisplay, activeDate);
        }
    };



}
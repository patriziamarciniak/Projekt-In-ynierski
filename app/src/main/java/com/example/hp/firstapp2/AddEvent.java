package com.example.hp.firstapp2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;


import com.example.hp.sqlite.dao.EventDAO;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AddEvent extends AppCompatActivity {

    EditText eventName, dateStart, dateEnd, timeStart, timeEnd, localisationStartX, localisationStartY, localisationEndX, localisationEndY, localisationStart, localisationEnd;
    Spinner radius;
    CheckBox notificationStart, notificationEnd, notificationAutomatic, myLocalisation;
    Integer cyclicEvent;
    EventDAO db;

    private Geocoder geocoder;
    private LocationManager locationManager;


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
        localisationStart = (EditText) findViewById(R.id.text_localization_start);
        localisationEnd = (EditText) findViewById(R.id.text_localization_end);
        myLocalisation = (CheckBox) findViewById(R.id.checkBox_my_localisation);
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
                        notificationStart.isChecked(),
                        notificationEnd.isChecked(),
                        notificationAutomatic.isChecked(),
                        getRadius(radius.getSelectedItem().toString()),
                        /// Longitude start
                        startAddressToGeolocationX(),
                        /// Latitude start
                        startAddressToGeolocationY(),
                        /// Longitude end
                        endAddressToGeolocationX(),
                        /// Latitude end
                        endAddressToGeolocationY(),
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
        localisationStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                localisationStart.setText("");
                return false;
            }
        });
        localisationEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                localisationEnd.setText("");
                return false;
            }
        });


        updateDisplay(dateStart, actualDate);
        updateDisplay(dateEnd, actualDate);
        updateTimeDisplay(timeStart, actualDate);
        updateTimeDisplay(timeEnd, actualDate);
    }


    ////////// POBIERANIE MOJEJ LOKALIZACJI /////////////////////////

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

//    private String getAddressFrom(Location location) {
 //       String result = " ";
  //      try {
   //         List<Address> addresses = geocoder.getFromLocation(
    //                location.getLatitude(),	location.getLongitude(), 1);
     //       for (Address address : addresses) {
     //           for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
      //              result += address.getAddressLine(i) + "\n";
      //          }
       //         result += " ";
        //    }
       // } catch (Exception e) {
       //     showToast(e.toString());
       // }
       // return result;
   // }

   // private void currentLocationToAddress() {
   //     Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
   //     String result = getAddressFrom(location);
   //     localisationStart.setText(result);
   // }

//    public void onCheckboxClicked(View view) {
 //       // Is the view now checked?
  //      boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
   //     switch(view.getId()) {
    //        case R.id.checkBox_my_localisation:
     //           if (checked)
      //              currentLocationToAddress();
       //         break;
       // }
   // }

    ////////////////////////////////////////////////////////////////////////////////

    //////////////////// ZAMIANA ADRESU NA WSPOLRZEDNE ////////////////////////////


    private String getXFrom(String locationStr) {
        String result = "";
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationStr, 1);
            for (Address address : addresses) {
                    result += address.getLongitude();
            }
        } catch (IOException e) {
            showToast(e.toString());
        }
        return result;
    }

    private String getYFrom(String locationStr) {
        String result = "";
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationStr, 1);
            for (Address address : addresses) {
                result += address.getLatitude();
            }
        } catch (IOException e) {
            showToast(e.toString());
        }
        return result;
    }


    private String startAddressToGeolocationX() {
        String location = localisationStart.getText().toString();
        String result = getXFrom(location);
        return result;
    }

    private String startAddressToGeolocationY() {
        String location = localisationStart.getText().toString();
        String result = getYFrom(location);
        return result;
    }

    private String endAddressToGeolocationX() {
        String location = localisationEnd.getText().toString();
        String result = getXFrom(location);
        return result;
    }

    private String endAddressToGeolocationY() {
        String location = localisationEnd.getText().toString();
        String result = getXFrom(location);
        return result;
    }

    //////////////////////////////////////////////////////////////////////////////

    public void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("/")
                        .append(date.get(Calendar.MONTH) + 1).append("/")
                        .append(date.get(Calendar.YEAR)).append(" "));
    }

    public void updateTimeDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        .append(date.get(Calendar.HOUR_OF_DAY)).append(":")
                        .append(date.get(Calendar.MINUTE)));
    }
    Calendar c = Calendar.getInstance();

    int Hr24=c.get(Calendar.HOUR_OF_DAY);
    int Min=c.get(Calendar.MINUTE);

    public int getRadius(String selected){

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
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeSetListener, activeDate.get(Calendar.HOUR_OF_DAY), activeDate.get(Calendar.MINUTE),true);
        }
        return null;
    }

    public DatePickerDialog.OnDateSetListener dateSetListener
        = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
        }
    };


    public TimePickerDialog.OnTimeSetListener timeSetListener
            = new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute ) {

            activeDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeDateDisplay, activeDate);
        }
    };



}
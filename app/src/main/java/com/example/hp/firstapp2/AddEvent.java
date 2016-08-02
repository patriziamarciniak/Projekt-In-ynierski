package com.example.hp.firstapp2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.Location;
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

import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.PhoneContact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class AddEvent extends AppCompatActivity {

    EditText eventName, dateStart, dateEnd, timeStart, timeEnd, localizationStart, localizationEnd;
    Spinner radius;
    CheckBox notificationStart, notificationEnd, notificationAutomatic;
    Integer cyclicEvent;
    EventDAO db;
    Button btnAddEvent, btnAddPeople;
    List<PhoneContact> contactsList = new ArrayList<>();
    Context context ;

    private Geocoder geocoder;
    private LocationManager locationManager;


    static final int DATE_DIALOG_ID = 999;
    static final int TIME_DIALOG_ID = 998;

    private Calendar actualDate;
    private TextView activeDateDisplay;
    private Calendar activeDate;

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("kk:mm");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        context = getApplicationContext();

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

        buildGoogleApiClient();

        db = new EventDAO(this);
        actualDate = Calendar.getInstance();

        btnAddEvent = (Button) findViewById(R.id.btn_add_event);
        btnAddPeople = (Button) findViewById(R.id.btn_add_people);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                db.createEvent(
                        dateStart.getText().toString(),
                        dateEnd.getText().toString(),
                        timeStart.getText().toString(),
                        timeEnd.getText().toString(),
                        notificationStart.isChecked(),
                        notificationEnd.isChecked(),
                        notificationAutomatic.isChecked(),
                        getRadius(radius.getSelectedItem().toString()),
                        /// Longitude start
                        currentLocationX(),
                        /// Latitude start
                        currentLocationY(),
                        /// Longitude end
                        getLoc(localizationStart.getText().toString()),
                        /// Latitude end
                        getLoc(localizationEnd.getText().toString()),

                        cyclicEvent,
                        Long.valueOf(db.countEvents())
                );

                AttendanceDAO attendanceDAO = new AttendanceDAO(context);
                for (PhoneContact phoneContact : contactsList){
                    attendanceDAO.createAttendance(db.getLastEvent().getId(), phoneContact.getId());
                }

                Intent nextScreen = new Intent(getApplicationContext(), EventAdded.class);
                startActivity(nextScreen);
            }

        });

        btnAddPeople.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(getApplicationContext(), AddPeople.class);
                Bundle b = getEventInfo();
                intent.putExtra("eventData", b);
                if (contactsList.size() != 0){
                    b.putSerializable("contacts", new ArrayList<>(contactsList));
                }
                startActivity(intent);
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


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras().getBundle("eventData");
            fillEventInfo(bundle);
        }
    }
    
    public String getLoc(String local){
        try {
            Double local2 = Double.parseDouble(local);
            return (Double.toString(local2));
        }
        catch (NumberFormatException e) {
            return ("0.00");
        }
    }

    //////////////////// CURRENT LOCATION //////////////

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public String currentLocationX() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            return(Double.toString(mLastLocation.getLongitude()));
        }
        else return("0.00");
    }

    public String currentLocationY() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            return(Double.toString(mLastLocation.getLatitude()));
        }
        else return("0.00");
    }


    //////////////////// ZAMIANA ADRESU NA WSPOLRZEDNE ////////////////////////////
/*
    private String getXFromAddress(String addressName) {
        String result = "0";
        try {
            List<Address> geoResults = geocoder.getFromLocationName(addressName, 1);
            while (geoResults.size()==0) {
                geoResults = geocoder.getFromLocationName(addressName, 1);
            }
            if (geoResults.size()>0) {
                Address addr = geoResults.get(0);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return result;
    }

    private String getYFromAddress(String addressName) {
        String result = "0";
        try {
            List<Address> geoResults = geocoder.getFromLocationName(addressName, 1);
            while (geoResults.size()==0) {
                geoResults = geocoder.getFromLocationName(addressName, 1);
            }
            if (geoResults.size()>0) {
                Address addr = geoResults.get(0);
                result += addr.getLatitude();

            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return result;
    }

    private String startAddressToGeolocationX() {
        String location = localizationStart.getText().toString();
        String result = getXFromAddress(location);
        return result;
    }

    private String startAddressToGeolocationY() {
        String location = localizationStart.getText().toString();
        String result = getYFromAddress(location);
        return result;
    }

    private String endAddressToGeolocationX() {
        String location = localizationEnd.getText().toString();
        String result = getXFromAddress(location);
        return result;
    }

    private String endAddressToGeolocationY() {
        String location = localizationEnd.getText().toString();
        String result = getXFromAddress(location);
        return result;
    }
*/
    //////////////////////////////////////////////////////////////////////////////

    public Bundle getEventInfo(){
        Bundle b = new Bundle();
        b.putString("dateStart", dateStart.getText().toString());
        b.putString("dateEnd", dateEnd.getText().toString());
        b.putString("timeStart", timeStart.getText().toString());
        b.putString("timeEnd", timeEnd.getText().toString());
        b.putString("localizationStart", localizationStart.getText().toString());
        b.putString("localizationEnd", localizationEnd.getText().toString());
        return b;
    }

    public void fillEventInfo(Bundle b){
        dateStart.setText(b.getString("dateStart"));
        dateEnd.setText(b.getString("dateEnd"));
        timeStart.setText(b.getString("timeStart"));
        timeEnd.setText(b.getString("timeEnd"));
        localizationStart.setText(b.getString("localizationStart"));
        localizationEnd.setText(b.getString("localizationEnd"));
        contactsList =  (List<PhoneContact>)(b.getSerializable("contacts"));
    }

    public void updateDisplay(TextView dateDisplay, Calendar date) {

        String dateToSet = format1.format(date.getTime());
        dateDisplay.setText(dateToSet);
    }

    public void updateTimeDisplay(TextView dateDisplay, Calendar date) {

        String timeToSet = format2.format(date.getTime());
        dateDisplay.setText(timeToSet);
    }

    public int getRadius(String selected) {

        switch (selected) {
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
                return new TimePickerDialog(this, timeSetListener, activeDate.get(Calendar.HOUR_OF_DAY), activeDate.get(Calendar.MINUTE), true);
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

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            activeDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeDateDisplay, activeDate);
        }
    };


}

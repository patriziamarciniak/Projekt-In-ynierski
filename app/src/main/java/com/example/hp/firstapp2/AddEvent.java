package com.example.hp.firstapp2;
import android.os.StrictMode;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.view.ViewGroup.LayoutParams;


import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.PhoneContact;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;;
import org.apache.http.impl.client.DefaultHttpClient;


public class AddEvent extends AppCompatActivity {

    EditText dateStart, dateEnd, timeStart, timeEnd, localisationStart, localisationEnd;
    RelativeLayout mRelativeLayout;
    Spinner radius;
    CheckBox notificationStart, notificationEnd, notificationAutomatic, cyclicalEvent;
    Integer cyclicEvent;
    EventDAO db;
    Button btnAddEvent, btnAddPeople, btnCyclicalEventDetails;
    List<PhoneContact> contactsList = new ArrayList<>();
    ArrayList<Integer> days = new ArrayList<>();
    ArrayList<String> cyclicalEventDays = new ArrayList<>();
    Context context;
    Bundle bundle;
    Calendar actualDate, activeStartDate, activeEndDate;
    TextView activeStartDateDisplay, activeEndDateDisplay;
    PopupWindow popupWindow;
    Boolean cyclic = false;

    private Geocoder geocoder;
    private LocationManager locationManager;

    static final int START_DATE_DIALOG_ID = 999;
    static final int END_DATE_DIALOG_ID = 998;
    static final int START_TIME_DIALOG_ID = 997;
    static final int END_TIME_DIALOG_ID = 996;
    static final int ACTIVITY_ID = 0;

    SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dateFormatToDB = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context = getApplicationContext();


        buildGoogleApiClient();

        initComponents();


        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                if (checkRequirements()) {

                    String start = "";
                    String end = "";
                    try {
                        end = convertDateFormat(dateEnd.getText().toString());
                        start = convertDateFormat(dateStart.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (cyclic) {

                        for (String date : cyclicalEventDays) {

                            db.createEvent(
                                    date,
                                    date,
                                    timeStart.getText().toString(),
                                    timeEnd.getText().toString(),
                                    notificationStart.isChecked(),
                                    notificationEnd.isChecked(),
                                    notificationAutomatic.isChecked(),
                                    getRadius(radius.getSelectedItem().toString()),
                                    /// Longitude start
                                    getLatLong(getLocationInfo(localisationStart.getText().toString()), 0),
                                    /// Latitude start
                                    getLatLong(getLocationInfo(localisationStart.getText().toString()), 1),
                                    /// Longitude end
                                    getLatLong(getLocationInfo(localisationEnd.getText().toString()), 0),
                                    /// Latitude end
                                    getLatLong(getLocationInfo(localisationEnd.getText().toString()), 1),

                                    cyclicEvent,
                                    Long.valueOf(db.countEvents())
                            );

                            AttendanceDAO attendanceDAO = new AttendanceDAO(context);
                            for (PhoneContact phoneContact : contactsList) {
                                attendanceDAO.createAttendance(db.getLastEvent().getId(), phoneContact.getId());
                            }
                        }

                        Intent nextScreen = new Intent(getApplicationContext(), EventAdded.class);
                        nextScreen.putExtra("event", db.getLastEvent());
                        startActivity(nextScreen);

                    } else {

                        db.createEvent(
                                start,
                                end,
                                timeStart.getText().toString(),
                                timeEnd.getText().toString(),
                                notificationStart.isChecked(),
                                notificationEnd.isChecked(),
                                notificationAutomatic.isChecked(),
                                getRadius(radius.getSelectedItem().toString()),
                                /// Longitude start
                                getLatLong(getLocationInfo(localisationStart.getText().toString()), 0),
                                /// Latitude start
                                getLatLong(getLocationInfo(localisationStart.getText().toString()), 1),
                                /// Longitude end
                                getLatLong(getLocationInfo(localisationEnd.getText().toString()), 0),
                                /// Latitude end
                                getLatLong(getLocationInfo(localisationEnd.getText().toString()), 1),

                                cyclicEvent,
                                Long.valueOf(db.countEvents())
                        );

                        AttendanceDAO attendanceDAO = new AttendanceDAO(context);
                        for (PhoneContact phoneContact : contactsList) {
                            attendanceDAO.createAttendance(db.getLastEvent().getId(), phoneContact.getId());
                        }

                        Intent nextScreen = new Intent(getApplicationContext(), EventAdded.class);
                        nextScreen.putExtra("event", db.getLastEvent());
                        startActivity(nextScreen);
                    }
                }

            }

        });


    btnAddPeople.setOnClickListener(new View.OnClickListener()

    {
        public void onClick (View arg0){

        Intent intent = new Intent(getApplicationContext(), AddPeople.class);
        Bundle b = getEventInfo();
        b.putInt("parentActivity", ACTIVITY_ID);
        if (contactsList != null) {
            if (contactsList.size() != 0) {
                b.putSerializable("contacts", new ArrayList<>(contactsList));
            }
        }
        b.putBoolean("cancel", true);
        b.putIntegerArrayList("days", days);
        b.putBoolean("alreadyCyclic", cyclic);
        intent.putExtra("eventData", b);
        startActivity(intent);

    }
    }

    );

    radius=(Spinner)

    findViewById(R.id.spinner_radius);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.radius_spinner, android.R.layout.simple_spinner_dropdown_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    radius.setAdapter(adapter);


    dateStart.setOnTouchListener(new View.OnTouchListener()

    {
        @Override
        public boolean onTouch (View v, MotionEvent event){
        showStartDateDialog(dateStart, actualDate);
        return false;
    }
    }

    );

    dateEnd.setOnTouchListener(new View.OnTouchListener()

    {
        @Override
        public boolean onTouch (View v, MotionEvent event){
        showEndDateDialog(dateEnd, actualDate);
        return false;
    }
    }

    );

    timeStart.setOnTouchListener(new View.OnTouchListener()

    {
        @Override
        public boolean onTouch (View v, MotionEvent event){
        showStartTimeDialog(timeStart, actualDate);
        return false;
    }
    }

    );
    timeEnd.setOnTouchListener(new View.OnTouchListener()

    {
        @Override
        public boolean onTouch (View v, MotionEvent event){
        showEndTimeDialog(timeEnd, actualDate);
        return false;
    }
    }

    );
    localisationEnd.setOnTouchListener(new View.OnTouchListener()

    {
        @Override
        public boolean onTouch (View v, MotionEvent event){
        localisationEnd.setText(" ");
        return false;
    }
    }

    );

    localisationEnd.setOnFocusChangeListener(new View.OnFocusChangeListener()

    {
        @Override
        public void onFocusChange (View v,boolean hasFocus){
        if (!hasFocus) {
            hideKeyboard(v);
        }
    }
    }

    );

    localisationStart.setOnTouchListener(new View.OnTouchListener()

    {
        @Override
        public boolean onTouch (View v, MotionEvent event){
        localisationStart.setText(" ");
        return false;
    }
    }

    );

    localisationStart.setOnFocusChangeListener(new View.OnFocusChangeListener()

    {
        @Override
        public void onFocusChange (View v,boolean hasFocus){
        if (!hasFocus) {
            hideKeyboard(v);
        }
    }
    }

    );


    updateDisplay(dateStart, actualDate);

    updateDisplay(dateEnd, actualDate);

    updateTimeDisplay(timeStart, actualDate);

    updateTimeDisplay(timeEnd, actualDate);

    getBundle();

    findDays();

    cyclicalEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

    {
        @Override
        public void onCheckedChanged (CompoundButton buttonView,boolean isChecked){
        if (isChecked) {
            Intent intent = new Intent(getApplicationContext(), AddCyclicalEvent.class);
            Bundle b = getEventInfo();
            b.putInt("parentActivity", ACTIVITY_ID);
            if (contactsList != null) {
                if (contactsList.size() != 0) {
                    b.putSerializable("contacts", new ArrayList<>(contactsList));
                }
            }
            intent.putExtra("eventData", b);
            startActivity(intent);

        } else {
            dateStart.setEnabled(true);
            dateEnd.setEnabled(true);
            cyclic = false;
            btnCyclicalEventDetails.setVisibility(View.INVISIBLE);
            updateDisplay(dateStart, actualDate);
            updateDisplay(dateEnd, actualDate);
        }

    }
    }

    );

    btnCyclicalEventDetails.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

        Intent intent = new Intent(getApplicationContext(), AddCyclicalEvent.class);
        Bundle b = getEventInfo();
        b.putInt("parentActivity", ACTIVITY_ID);
        if (contactsList != null) {
            if (contactsList.size() != 0) {
                b.putSerializable("contacts", new ArrayList<>(contactsList));
            }
        }
        b.putIntegerArrayList("days", days);
        b.putBoolean("alreadyCyclic", cyclic);
        intent.putExtra("eventData", b);
        startActivity(intent);
    }
    }

    );


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
            return (Double.toString(mLastLocation.getLongitude()));
        } else return ("0.00");
    }

    public String currentLocationY() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            return (Double.toString(mLastLocation.getLatitude()));
        } else return ("0.00");
    }


    ////////// ZAMIANA ADRESU NA WSPÓLRZĘDNE /////////////////////////

    public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ", "%20");
            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getLatLong(JSONObject jsonObject, int latlong) {
        double longitute, latitude;
        String mLongitute, mLatitude;

        try {

            longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            mLongitute = Double.toString(longitute);

            latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            mLatitude = Double.toString(latitude);


        } catch (JSONException e) {
            return "0";

        }
        if (latlong == 1)
            return mLatitude;
        else
            return mLongitute;
    }
    ////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////




    private void initComponents() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_add_event);
        dateStart = (EditText) findViewById(R.id.text_date_start);
        dateEnd = (EditText) findViewById(R.id.text_date_end);
        timeStart = (EditText) findViewById(R.id.text_time_start);
        timeEnd = (EditText) findViewById(R.id.text_time_end);
        localisationStart = (EditText) findViewById(R.id.text_localization_start);
        localisationEnd = (EditText) findViewById(R.id.text_localization_end);
        notificationStart = (CheckBox) findViewById(R.id.checkBox_notification_start);
        notificationEnd = (CheckBox) findViewById(R.id.checkBox_notification_end);
        notificationAutomatic = (CheckBox) findViewById(R.id.checkBox_automatic_notification);
        cyclicalEvent = (CheckBox) findViewById(R.id.checkBox_cyclical);
        cyclicEvent = 0;
        db = new EventDAO(this);
        actualDate = Calendar.getInstance();
        activeStartDate = Calendar.getInstance();
        activeEndDate = Calendar.getInstance();
        btnAddEvent = (Button) findViewById(R.id.btn_add_event);
        btnAddPeople = (Button) findViewById(R.id.btn_add_people);
        btnCyclicalEventDetails = (Button) findViewById(R.id.btn_cyclical_details);
    }

    private void getBundle() {
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras().getBundle("eventData");
            fillEventInfo(bundle);
            if (bundle.containsKey("cancel")) {
                if (bundle.getBoolean("cancel") && !bundle.getBoolean("alreadyCyclic")) {
                    cyclicalEvent.setChecked(false);
                    cyclic = false;
                } else {
                    cyclic = true;
                    btnCyclicalEventDetails.setClickable(true);
                    btnCyclicalEventDetails.setVisibility(View.VISIBLE);
                    days = bundle.getIntegerArrayList("days");
                    cyclicalEvent.setChecked(true);
                    dateStart.setEnabled(false);
                    dateEnd.setEnabled(false);
                }
            }
        }

    }

    public boolean checkRequirements() {

        if (!checkListOfPeople() || !checkDates()) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.pop_up_info, null);
            ImageView closeButton = (ImageView) customView.findViewById(R.id.ib_close);
            TextView popUpTextView = (TextView) customView.findViewById(R.id.tv);

            popupWindow = new PopupWindow(
                    customView,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );

            if (Build.VERSION.SDK_INT >= 21) {
                popupWindow.setElevation(5.0f);
            }

            if (!checkListOfPeople())
                popUpTextView.setText(getString(R.string.pop_up_info_no_contacts) + "\n");
            if (!checkDates())
                popUpTextView.setText(popUpTextView.getText() + "\n" + getString(R.string.pop_up_info_wrong_dates));

            mRelativeLayout.setVisibility(View.GONE);
            popupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mRelativeLayout.setVisibility(View.VISIBLE);
                }
            });

            return false;
        }
        return true;
    }

    private boolean checkListOfPeople() {

        if (contactsList == null || contactsList.isEmpty()) return false;
        return true;
    }

    private boolean checkDates() {

        try {
            Date startDate = dateFormatDisplay.parse(dateStart.getText().toString());
            Date endDate = dateFormatDisplay.parse(dateEnd.getText().toString());
            Date startTime = timeFormat.parse(timeStart.getText().toString());
            Date endTime = timeFormat.parse(timeEnd.getText().toString());

            if (endDate.before(startDate) || (endDate.equals(startDate) && (!endTime.after(startTime))))
                return false;

            if (cyclic && (!endTime.after(startTime)))
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    private String convertDateFormat(String displayedData) throws ParseException {

        Date date = dateFormatDisplay.parse(displayedData);
        return dateFormatToDB.format(date);
    }


    private ArrayList<Calendar> findDays() {

        ArrayList<Calendar> dates = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = dateFormatDisplay.parse(dateStart.getText().toString());
            endDate = dateFormatDisplay.parse(dateEnd.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        end.setTime(endDate);
        end.add(Calendar.DATE, -1);

        for (Integer day : days) {

            start.setTime(startDate);
            start.add(Calendar.DATE, -1);

            while (start.getTimeInMillis() <= end.getTimeInMillis()) {

                if (start.get(Calendar.DAY_OF_WEEK) == day) {

                    start.add(Calendar.DATE, 1);
                    cyclicalEventDays.add(dateFormatToDB.format(start.getTime()));
                    start.add(Calendar.DATE, -1);
                }
                start.add(Calendar.DATE, 1);
            }
        }
        return dates;

    }

    public Bundle getEventInfo() {
        Bundle b = new Bundle();
        b.putString("dateStart", dateStart.getText().toString());
        b.putString("dateEnd", dateEnd.getText().toString());
        b.putString("timeStart", timeStart.getText().toString());
        b.putString("timeEnd", timeEnd.getText().toString());
        b.putString("localizationStart", localisationStart.getText().toString());
        b.putString("localizationEnd", localisationEnd.getText().toString());
        b.putBoolean("notificationStart", notificationStart.isChecked());
        b.putBoolean("notificationEnd", notificationEnd.isChecked());
        b.putBoolean("notificationAuto", notificationAutomatic.isChecked());
        return b;
    }

    public void fillEventInfo(Bundle b) {
        dateStart.setText(b.getString("dateStart"));
        dateEnd.setText(b.getString("dateEnd"));
        timeStart.setText(b.getString("timeStart"));
        timeEnd.setText(b.getString("timeEnd"));
        localisationStart.setText(b.getString("localizationStart"));
        localisationEnd.setText(b.getString("localizationEnd"));
        contactsList = (List<PhoneContact>) (b.getSerializable("contacts"));
        notificationStart.setChecked(b.getBoolean("notificationStart"));
        notificationEnd.setChecked(b.getBoolean("notificationEnd"));
        notificationAutomatic.setChecked(b.getBoolean("notificationAuto"));
    }

    public void updateDisplay(TextView dateDisplay, Calendar date) {

        String dateToSet = dateFormatDisplay.format(date.getTime());
        dateDisplay.setText(dateToSet);
    }

    public void updateTimeDisplay(TextView dateDisplay, Calendar date) {

        String timeToSet = timeFormat.format(date.getTime());
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

    public void showStartDateDialog(TextView dateDisplay, Calendar date) {
        activeStartDateDisplay = dateDisplay;
        activeStartDate = date;
        removeDialog(END_DATE_DIALOG_ID);
        showDialog(START_DATE_DIALOG_ID);
    }

    public void showEndDateDialog(TextView dateDisplay, Calendar date) {

        activeEndDateDisplay = dateDisplay;
        activeEndDate = date;
        showDialog(END_DATE_DIALOG_ID);
    }


    public void showStartTimeDialog(TextView dateDisplay, Calendar date) {
        activeStartDateDisplay = dateDisplay;
        activeStartDate = date;
        removeDialog(START_TIME_DIALOG_ID);
        showDialog(START_TIME_DIALOG_ID);
    }

    public void showEndTimeDialog(TextView dateDisplay, Calendar date) {
        activeEndDateDisplay = dateDisplay;
        activeEndDate = date;
        removeDialog(END_TIME_DIALOG_ID);
        showDialog(END_TIME_DIALOG_ID);
    }

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                DatePickerDialog dateStartPickerDialog = new DatePickerDialog(this, dateStartSetListener, Integer.parseInt(dateStart.getText().toString().substring(6, 10)), Integer.parseInt(dateStart.getText().toString().substring(3, 5)) - 1, Integer.parseInt(dateStart.getText().toString().substring(0, 2)));
                dateStartPickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return dateStartPickerDialog;

            case END_DATE_DIALOG_ID:
                DatePickerDialog dateEndPickerDialog = new DatePickerDialog(this, dateEndSetListener, Integer.parseInt(dateEnd.getText().toString().substring(6, 10)), Integer.parseInt(dateEnd.getText().toString().substring(3, 5)) - 1, Integer.parseInt(dateEnd.getText().toString().substring(0, 2)));
                dateEndPickerDialog.getDatePicker().setMinDate(activeStartDate.getTimeInMillis());
                return dateEndPickerDialog;

            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeStartSetListener, Integer.parseInt(timeStart.getText().toString().substring(0, 2)), Integer.parseInt(timeStart.getText().toString().substring(3)), true);

            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this, timeEndSetListener, Integer.parseInt(timeEnd.getText().toString().substring(0, 2)), Integer.parseInt(timeEnd.getText().toString().substring(3)), true);
        }
        return null;
    }


    public DatePickerDialog.OnDateSetListener dateStartSetListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            activeStartDate.set(Calendar.YEAR, year);
            activeStartDate.set(Calendar.MONTH, monthOfYear);
            activeStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeStartDateDisplay, activeStartDate);
        }
    };

    public DatePickerDialog.OnDateSetListener dateEndSetListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            activeEndDate.set(Calendar.YEAR, year);
            activeEndDate.set(Calendar.MONTH, monthOfYear);
            activeEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeEndDateDisplay, activeEndDate);
        }
    };


    public TimePickerDialog.OnTimeSetListener timeStartSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            activeStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeStartDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeStartDateDisplay, activeStartDate);
        }
    };


    public TimePickerDialog.OnTimeSetListener timeEndSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            activeEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeEndDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeEndDateDisplay, activeEndDate);
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent previousScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(previousScreen);
    }

}


package com.example.hp.firstapp2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Attendance;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.PhoneContact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditEvent extends AppCompatActivity {

    Context context;
    Bundle bundle, contactBundle;
    RelativeLayout mRelativeLayout;
    EditText dateStart, dateEnd, timeStart, timeEnd, localizationStart, localizationEnd;
    Spinner radius;
    Button btnEditEvent, btnAddPeople, btnCancel;
    CheckBox notificationStart, notificationEnd, notificationAutomatic, cyclicalEvent;
    EventDAO eventDAO;
    Event event;
    List<PhoneContact> contactsList = new ArrayList<>();
    List<Attendance> attendances = new ArrayList<>();
    AttendanceDAO attendanceDAO;
    private PopupWindow popupWindow;

    static final int START_DATE_DIALOG_ID = 999;
    static final int END_DATE_DIALOG_ID = 998;
    static final int START_TIME_DIALOG_ID = 997;
    static final int END_TIME_DIALOG_ID = 996;
    static final int ACTIVITY_ID = 1;

    private Calendar actualDate;
    private TextView activeDateDisplay;
    private Calendar activeStartDate;
    private Calendar activeEndDate;


    SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dateFormatToDB = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        context = getApplicationContext();

        attendanceDAO = new AttendanceDAO(this);
        eventDAO = new EventDAO(this);

        initComponents();
        initBundle();
        fillEventInfo();


        dateStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showStartDateDialog(dateStart, actualDate);
                return false;
            }
        });

        dateEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showEndDateDialog(dateEnd, actualDate);
                return false;
            }
        });

        timeStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showStartTimeDialog(timeStart, actualDate);
                return false;
            }
        });
        timeEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showEndTimeDialog(timeEnd, actualDate);
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
        localizationEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        localizationStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                localizationStart.setText(" ");
                return false;
            }
        });
        localizationStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        btnEditEvent = (Button) findViewById(R.id.btn_edit_event);
        btnEditEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (checkRequirements()) {

                    String start = " ";
                    String end = " ";

                    try {
                        start = convertDateFormatForDB(dateStart.getText().toString());
                        end = convertDateFormatForDB(dateEnd.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    eventDAO.editEvent(
                            event.getId(),
                            start,
                            end,
                            timeStart.getText().toString(),
                            timeEnd.getText().toString(),
                            notificationStart.isChecked(),
                            notificationEnd.isChecked(),
                            notificationAutomatic.isChecked(),
                            getRadius(radius.getSelectedItem().toString()),
                            localizationStart.getText().toString(),
                            localizationStart.getText().toString(),
                            localizationEnd.getText().toString(),
                            localizationEnd.getText().toString(),
                            0,
                            event.getId());

                    attendances = attendanceDAO.getAttendancesByEventId(event.getId());
                    for (Attendance attendance : attendances)
                        attendanceDAO.deleteAttendance(attendance);

                    for (PhoneContact phoneContact : contactsList) {
                        attendanceDAO.createAttendance(event.getId(), phoneContact.getId());
                    }

                    Intent nextScreen = new Intent(getApplicationContext(), EventAdded.class);
                    nextScreen.putExtra("event", eventDAO.getEventById(event.getId()));
                    startActivity(nextScreen);
                }
            }
        });

        btnAddPeople = (Button) findViewById(R.id.btn_edit_add_people);
        btnAddPeople.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), AddPeople.class);
                Bundle b = getEventInfo();
                b.putInt("parentActivity", ACTIVITY_ID);
                b.putParcelable("event", event);
                intent.putExtra("eventData", b);
                if (contactsList.size() != 0) {
                    b.putSerializable("contacts", new ArrayList<>(contactsList));
                }
                startActivity(intent);
            }
        });


        btnCancel = (Button) findViewById(R.id.btn_cancel_edit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public Bundle getEventInfo() {
        Bundle b = new Bundle();
        b.putString("dateStart", dateStart.getText().toString());
        b.putString("dateEnd", dateEnd.getText().toString());
        b.putString("timeStart", timeStart.getText().toString());
        b.putString("timeEnd", timeEnd.getText().toString());
        b.putString("localizationStart", localizationStart.getText().toString());
        b.putString("localizationEnd", localizationEnd.getText().toString());
        b.putBoolean("notificationStart", notificationStart.isChecked());
        b.putBoolean("notificationEnd", notificationEnd.isChecked());
        b.putBoolean("notificationAuto", notificationAutomatic.isChecked());
        return b;
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        contactBundle = bundle.getBundle("eventData");

        if (bundle.containsKey("eventData") && contactBundle.containsKey("cancel")) {
            if (contactBundle.getBoolean("cancel") == true) {
                cyclicalEvent.setChecked(false);
            } else {
                cyclicalEvent.setChecked(true);
                dateStart.setEnabled(false);
                dateEnd.setEnabled(false);
            }
        }

        event = bundle.getParcelable("event");

        if (contactBundle != null) {
            contactsList = (List<PhoneContact>) contactBundle.getSerializable("contacts");
        } else {
            contactsList = attendanceDAO.getContacts(event.getId());
        }
    }

    private void initComponents() {
        actualDate = Calendar.getInstance();
        activeStartDate = actualDate;
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_edit_event);
        dateStart = (EditText) findViewById(R.id.text_edit_date_start);
        dateEnd = (EditText) findViewById(R.id.text_edit_date_end);
        timeStart = (EditText) findViewById(R.id.text_edit_time_start);
        timeEnd = (EditText) findViewById(R.id.text_edit_time_end);
        localizationStart = (EditText) findViewById(R.id.text_edit_localization_start);
        localizationEnd = (EditText) findViewById(R.id.text_edit_localization_end);
        notificationStart = (CheckBox) findViewById(R.id.checkBox_edit_notification_start);
        notificationEnd = (CheckBox) findViewById(R.id.checkBox_edit_notification_end);
        notificationAutomatic = (CheckBox) findViewById(R.id.checkBox_edit_automatic_notification);
        radius = (Spinner) findViewById(R.id.spinner_edit_radius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.radius_spinner, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius.setAdapter(adapter);
    }

    private void fillEventInfo() {

        if (contactBundle == null) {

            String start = " ";
            String end = " ";

            try {
                start = convertDateFormatForDisplay(event.getDataStart());
                end = convertDateFormatForDisplay(event.getDataEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dateEnd.setText(end);
            dateStart.setText(start);
            timeStart.setText(event.getTimeStart());
            timeEnd.setText(event.getTimeEnd());
            localizationStart.setText(event.getStartLocalisationX());
            localizationEnd.setText(event.getEndLocalisationX());
            if (event.getAutoNotifications()) notificationAutomatic.setChecked(true);
            if (event.getNotificationsStart()) notificationStart.setChecked(true);
            if (event.getNotificationsEnd()) notificationEnd.setChecked(true);
        } else {
            dateStart.setText(contactBundle.getString("dateStart"));
            dateEnd.setText(contactBundle.getString("dateEnd"));
            timeStart.setText(contactBundle.getString("timeStart"));
            timeEnd.setText(contactBundle.getString("timeEnd"));
            localizationStart.setText(contactBundle.getString("localizationStart"));
            localizationEnd.setText(contactBundle.getString("localizationEnd"));
            notificationStart.setChecked(contactBundle.getBoolean("notificationStart"));
            notificationEnd.setChecked(contactBundle.getBoolean("notificationEnd"));
            notificationAutomatic.setChecked(contactBundle.getBoolean("notificationAuto"));
        }
    }
    ////////////////////////////////////////////////////////////////////


    public boolean checkRequirements() {

        if (!checkListOfPeople() || !checkDates()) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.pop_up_info, null);
            ImageView closeButton = (ImageView) customView.findViewById(R.id.ib_close);
            TextView popUpTextView = (TextView) customView.findViewById(R.id.tv);

            popupWindow = new PopupWindow(
                    customView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
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

        if (contactsList.isEmpty()) return false;
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

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
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
        activeDateDisplay = dateDisplay;
        activeStartDate = date;
        removeDialog(END_DATE_DIALOG_ID);
        showDialog(START_DATE_DIALOG_ID);
    }

    public void showEndDateDialog(TextView dateDisplay, Calendar date) {

        activeDateDisplay = dateDisplay;
        activeEndDate = date;
        showDialog(END_DATE_DIALOG_ID);
    }


    public void showStartTimeDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeStartDate = date;
        removeDialog(START_TIME_DIALOG_ID);
        showDialog(START_TIME_DIALOG_ID);
    }

    public void showEndTimeDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
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
            updateDisplay(activeDateDisplay, activeStartDate);
        }
    };

    public DatePickerDialog.OnDateSetListener dateEndSetListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            activeEndDate.set(Calendar.YEAR, year);
            activeEndDate.set(Calendar.MONTH, monthOfYear);
            activeEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeEndDate);
        }
    };


    public TimePickerDialog.OnTimeSetListener timeStartSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            activeStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeStartDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeDateDisplay, activeStartDate);
        }
    };


    public TimePickerDialog.OnTimeSetListener timeEndSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            activeEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            activeEndDate.set(Calendar.MINUTE, minute);
            updateTimeDisplay(activeDateDisplay, activeEndDate);
        }
    };


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String convertDateFormatForDB(String displayedData) throws ParseException {

        Date date = dateFormatDisplay.parse(displayedData);
        return dateFormatToDB.format(date);
    }

    private String convertDateFormatForDisplay(String displayedData) throws ParseException {

        Date date = dateFormatToDB.parse(displayedData);
        return dateFormatDisplay.format(date);
    }
}

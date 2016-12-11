package com.example.hp.firstapp2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddCyclicalEvent extends AppCompatActivity {

    Context context;
    Bundle bundle;
    Intent intent;
    EditText editTextFrom, editTextTo;
    Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday, btnReady, btnCancel;
    ArrayList<Integer> selectedButtons = new ArrayList<>();
//    ArrayList<String> selectedButtons = new ArrayList<>();
    PopupWindow popupWindow;
    RelativeLayout mRelativeLayout;

    static final int START_DATE_DIALOG_ID = 999;
    static final int END_DATE_DIALOG_ID = 998;
    private Calendar actualDate, activeStartDate, activeEndDate, timeLimit;
    private TextView activeDateDisplay;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_add_cyclical_event);
        getSupportActionBar().setTitle(getString(R.string.title_activity_add_cyclical_event));

        initComponents();


    }

    private void initComponents() {

        actualDate = Calendar.getInstance();
        activeStartDate = actualDate;
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextTo = (EditText) findViewById(R.id.editTextTo);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutAddCyclicalEvent);
        btnMonday = (Button) findViewById(R.id.buttonMonday);
        btnTuesday = (Button) findViewById(R.id.buttonTuesday);
        btnWednesday = (Button) findViewById(R.id.buttonWednesday);
        btnThursday = (Button) findViewById(R.id.buttonThursday);
        btnFriday = (Button) findViewById(R.id.buttonFriday);
        btnSaturday = (Button) findViewById(R.id.buttonSaturday);
        btnSunday = (Button) findViewById(R.id.buttonSunday);
        bundle = getIntent().getExtras().getBundle("eventData");

        if(bundle.containsKey("days")) {
            ArrayList<Integer> days = bundle.getIntegerArrayList("days");
            setDays(days);
        }

        if( bundle.getBoolean("alreadyCyclic") == true ){
            editTextFrom.setText(bundle.getString("dateStart"));
            editTextTo.setText(bundle.getString("dateEnd"));
        }else {
            editTextFrom.setText(dateFormat.format(actualDate.getTime()));
            Calendar weekFromNow = Calendar.getInstance();
            weekFromNow.add(Calendar.WEEK_OF_MONTH, 1);
            editTextTo.setText(dateFormat.format(weekFromNow.getTime()));
        }

        editTextFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showStartDateDialog(editTextFrom, actualDate);
                return false;
            }
        });

        editTextTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showEndDateDialog(editTextTo, actualDate);
                return false;
            }
        });


        int[] buttons = {R.id.buttonMonday, R.id.buttonTuesday, R.id.buttonWednesday, R.id.buttonThursday,
                R.id.buttonFriday, R.id.buttonSaturday, R.id.buttonSunday};
        for (int i = 0; i < buttons.length; i++) {
            Button buttonNum = (Button) findViewById(buttons[i]);
            setOnClick(buttonNum);
        }

        btnReady = (Button) findViewById(R.id.btn_add_event_cyclical);
        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelectedDays()) {

                    if (bundle.getInt("parentActivity") == 0) {
                        intent = new Intent(getApplicationContext(), AddEvent.class);
                    } else {
                        intent = new Intent(getApplicationContext(), EditEvent.class);
                        intent.putExtra("event", bundle.getParcelable("event"));

                    }
                    bundle.putString("dateStart", editTextFrom.getText().toString());
                    bundle.putString("dateEnd", editTextTo.getText().toString());
                    bundle.putBoolean("cancel", false);
                    bundle.putIntegerArrayList("days", selectedButtons);
                    intent.putExtra("eventData", bundle);
                    startActivity(intent);
                } else {
                    showAlert();
                }
            }
        });

        btnCancel = (Button) findViewById(R.id.btn_cancel_cyclical);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle.getInt("parentActivity") == 0) {
                    intent = new Intent(getApplicationContext(), AddEvent.class);
                } else {
                    intent = new Intent(getApplicationContext(), EditEvent.class);
                }
                bundle.putBoolean("alreadyCyclic", bundle.getBoolean("alreadyCyclic"));
                bundle.putBoolean("cancel", true);
                intent.putExtra("eventData", bundle);
                startActivity(intent);
            }
        });
    }


    private void setOnClick(final Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn.isSelected()) {
                    btn.setBackground(getResources().getDrawable(R.drawable.circular_button_default));
                    btn.setSelected(false);
                } else {
                    btn.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
                    btn.setSelected(true);
                }
            }
        });
    }

    public void updateDisplay(TextView dateDisplay, Calendar date) {

        String dateToSet = dateFormat.format(date.getTime());
        dateDisplay.setText(dateToSet);
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

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                DatePickerDialog dateStartPickerDialog = new DatePickerDialog(this, dateStartSetListener, Integer.parseInt( editTextFrom.getText().toString().substring(6, 10)), Integer.parseInt(editTextFrom.getText().toString().substring(3, 5)) - 1, Integer.parseInt(editTextFrom.getText().toString().substring(0, 2)));
                dateStartPickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return dateStartPickerDialog;

            case END_DATE_DIALOG_ID:
                DatePickerDialog dateEndPickerDialog = new DatePickerDialog(this, dateEndSetListener, Integer.parseInt(editTextTo.getText().toString().substring(6, 10)), Integer.parseInt(editTextTo.getText().toString().substring(3, 5)) - 1, Integer.parseInt(editTextTo.getText().toString().substring(0, 2)));
                timeLimit = actualDate;
                timeLimit.add(Calendar.WEEK_OF_MONTH, 1);
                dateEndPickerDialog.getDatePicker().setMinDate(timeLimit.getTimeInMillis());
                timeLimit.add(Calendar.YEAR, 1);
                dateEndPickerDialog.getDatePicker().setMaxDate(timeLimit.getTimeInMillis());
                return dateEndPickerDialog;
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

    private boolean checkSelectedDays() {

        if (btnMonday.isSelected()) selectedButtons.add(1);
        if (btnTuesday.isSelected()) selectedButtons.add(2);
        if (btnWednesday.isSelected()) selectedButtons.add(3);
        if (btnThursday.isSelected()) selectedButtons.add(4);
        if (btnFriday.isSelected()) selectedButtons.add(5);
        if (btnSaturday.isSelected()) selectedButtons.add(6);
        if (btnSunday.isSelected()) selectedButtons.add(7);

        if (!selectedButtons.isEmpty()) return true;
        return false;
    }

    private void setDays(ArrayList<Integer> days){

        if (days.contains(1)) {
            btnMonday.setSelected(true);
            btnMonday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
        }
        if (days.contains(2)) {
            btnTuesday.setSelected(true);
            btnTuesday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
        }
        if (days.contains(3)) {
            btnWednesday.setSelected(true);
            btnWednesday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
        }
        if (days.contains(4)) {
            btnThursday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
            btnThursday.setSelected(true);
        }
        if (days.contains(5)) {
            btnFriday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
            btnFriday.setSelected(true);
        }
        if (days.contains(6)) {
            btnSaturday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
            btnSaturday.setSelected(true);
        }
        if (days.contains(7)) {
            btnSunday.setBackground(getResources().getDrawable(R.drawable.circular_button_pressed));
            btnSunday.setSelected(true);
        }
    }

    private void showAlert() {

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

        popUpTextView.setText(popUpTextView.getText() + getString(R.string.pop_up_info_no_days));

        mRelativeLayout.setVisibility(View.GONE);
        popupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mRelativeLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}

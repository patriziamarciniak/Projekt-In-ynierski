package com.example.hp.firstapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

public class EditEvent extends AppCompatActivity {

    EditText eventName, dateStart, dateEnd, timeStart, timeEnd, localizationStart, localizationEnd;
    Spinner radius;
    Button btnEditEvent;
    CheckBox notificationStart, notificationEnd, notificationAutomatic;
    Integer cyclicEvent;
    Long eventID;
    EventDAO eventDAO;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Bundle bundle = getIntent().getExtras();
        event = bundle.getParcelable("event");

        eventDAO = new EventDAO(this);
        eventID = event.getId();
        dateStart = (EditText) findViewById(R.id.text_edit_date_start);
        dateEnd = (EditText) findViewById(R.id.text_edit_date_end);
        timeStart = (EditText) findViewById(R.id.text_edit_time_start);
        timeEnd = (EditText) findViewById(R.id.text_edit_time_end);
        localizationStart = (EditText) findViewById(R.id.text_edit_localization_start);
        localizationEnd = (EditText) findViewById(R.id.text_edit_localization_end);

        dateEnd.setText(event.getDataEnd());
        dateStart.setText(event.getDataStart());
        timeStart.setText(event.getTimeStart());
        timeEnd.setText(event.getTimeEnd());
        localizationStart.setText(event.getLocalisation());
        localizationEnd.setText(event.getLocalisation());

        btnEditEvent = (Button) findViewById(R.id.btn_edit_event);
        btnEditEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {

       //         Event event = eventDAO.getEventById(eventID);
        //        Log.d("ID", eventID.toString());
        //        Log.d("Event", event.toString());
        //        Log.d("Event", event.getDataStart());

                event.setDataStart(dateStart.getText().toString());
                event.setDataEnd(dateEnd.getText().toString());
                event.setTimeStart(timeStart.getText().toString());
                event.setTimeEnd(timeEnd.getText().toString());
//                        dateStart.getText().toString(),
//                        timeStart.getText().toString(),
//                        dateEnd.getText().toString(),
//                        timeEnd.getText().toString(),
//                        false,
//                        notificationStart.isChecked(),
//                        notificationEnd.isChecked(),
//                        notificationAutomatic.isChecked(),
//                        getRadius(radius.getSelectedItem().toString()),
//                        localizationStart.getText().toString(),
//                        cyclicEvent,
//                        Long.valueOf(db.countEvents())
                        Log.d("Event", event.getDataStart());
                        Log.d("ID", String.valueOf(event.getId()));

                Intent nextScreen = new Intent(getApplicationContext(), ComingTrips.class);
                startActivity(nextScreen);

            }});




    }
}

package com.example.hp.firstapp2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.sqlite.dao.DBHelper;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

public class EventAdded extends AppCompatActivity {

    EventDAO eventDAO;
    Event event;
    TextView localization, data, time;
    Button btnMainMenu, btnEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_added);

        eventDAO = new EventDAO(this);
        event = eventDAO.getLastEvent();

        //localization = (TextView)this.findViewById(R.id.txt_added_localization);
       // localization.setText(event.getLocalisation().toString());
        data = (TextView)this.findViewById(R.id.txt_added_data_start);
        data.setText(event.getDataEnd().toString());
        time = (TextView)this.findViewById(R.id.txt_added_time_end);
        time.setText(event.getTimeEnd().toString());

        btnMainMenu = (Button) findViewById(R.id.btn_main_menu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);
            }
        });
        btnEdit = (Button) findViewById(R.id.btn_edit_event);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), EditEvent.class);
                nextScreen.putExtra("event", event);
                startActivity(nextScreen);
            }
        });
    }

}

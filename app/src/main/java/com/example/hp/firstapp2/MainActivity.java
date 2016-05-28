package com.example.hp.firstapp2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.hp.sqlite.dao.ContactsDAO;
import com.example.hp.sqlite.dao.DBHelper;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Contacts;
import com.example.hp.sqlite.model.Event;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    Button btnMyHistory, btnMyEvents, btnAddEvent ;
    Locale locale;
    Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locale = new Locale("pl");
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

// ////////////////////////DataBase checking////////////////////

//        ContactsDAO dbContact = new ContactsDAO(context);
 //       EventDAO db = new EventDAO(context);

//        Log.d("Insert: ", "Inserting ..");
//        dbContact.createContacts("9100000000","Ravi","Black");
  //      db.createEvent("dd","ee","bb","bb",true,true,true,true,8,"aa",12,1233445566788999L);

//        Log.d("Reading: ", "Reading all contacts..");
//        List<Contacts> contacts = dbContact.getAllContacts();
//        List<Event> events = db.getAllEvents();

//        for (Event cn : events) {
//            db.deleteEvent(cn);
//        }
//        events = db.getAllEvents();
//
//        for (Event cn : events) {
//            String log = "Id: " + cn.getId() + " ,Name: " + cn.getDataEnd() + " ,Radius: " + cn.getRadius();
//            // Writing Contacts to log
//            Log.d("Name: ", log);
//        }
//
//        for (Contacts cn : contacts) {
//            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() ;
////             Writing Contacts to log
//            Log.d("Name: ", log);
//        }


        btnMyHistory = (Button) findViewById(R.id.button3);
        btnMyHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), MyHistory.class);
                startActivity(nextScreen);
            }
        });

        btnMyEvents = (Button) findViewById(R.id.button2);
        btnMyEvents.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), ComingTrips.class);
//                Intent nextScreen = new Intent(getApplicationContext(), AddContacts.class);
                startActivity(nextScreen);
            }
        });

        btnAddEvent = (Button) findViewById(R.id.button1);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), AddEvent.class);
                startActivity(nextScreen);
            }
        });
        changeItemTranslation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_changeLanguage:
                return true;
            case R.id.lang_pl:
                locale = new Locale("pl");
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                context.getApplicationContext().getResources().updateConfiguration(config, null);
                changeItemTranslation();
                return true;
            case R.id.lang_en:
                locale = new Locale("en");
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                context.getApplicationContext().getResources().updateConfiguration(config, null);
                changeItemTranslation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void changeItemTranslation(){

        btnAddEvent.setText(R.string.title_activity_add_event);
        btnMyEvents.setText(R.string.title_coming_trips);
        btnMyHistory.setText(R.string.title_history);
    }
}

package com.example.hp.firstapp2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.dao.ContactsDAO;
import com.example.hp.sqlite.dao.DBHelper;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Attendance;
import com.example.hp.sqlite.model.Contacts;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.PhoneContact;

import java.util.List;
import java.util.Locale;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.content.BroadcastReceiver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    Button btnMyHistory, btnMyEvents, btnAddEvent;
    Locale locale;
    Configuration config;
    List<Event> lastingEventsList;
    EventDAO db;
    private static final int RESULT_SETTINGS = 1;




    /*
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                String string = bundle.getString(MyService.LOCATION_SERVICE);

                int resultCode = bundle.getInt(MyService.RESULT);

                if (resultCode == RESULT_OK) {

                }
            }
        }
    };

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface ubuntu_font = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-B.ttf");

        locale = new Locale("pl");
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

//        db = new EventDAO(context);
//        AttendanceDAO cdb = new AttendanceDAO(context);
//        List<Event> list = db.getAllEvents();
//        for (Event event: list) {
//            List<PhoneContact> contacts = cdb.getContacts(event.getId());
//            for (PhoneContact c : contacts ) {
//                Log.d("osoby", String.valueOf((event.getId()) + " " + String.valueOf(c.getName())));
//            }
//        }


        checkLastingTrips();


        btnMyHistory = (Button) findViewById(R.id.button3);
        btnMyHistory.setTypeface(ubuntu_font);

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
                startActivity(nextScreen);
            }
        });
        btnMyEvents.setTypeface(ubuntu_font);

        btnAddEvent = (Button) findViewById(R.id.button1);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), AddEvent.class);
                startActivity(nextScreen);
            }
        });
        btnAddEvent.setTypeface(ubuntu_font);


        changeItemTranslation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                Intent nextScreen = new Intent(getApplicationContext(), Settings.class);
                startActivity(nextScreen);
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

    private void checkLastingTrips(){
        db = new EventDAO(context);
        lastingEventsList = db.findLastingEvents();
        if (lastingEventsList.size()!=0){
            Intent nextScreen = new Intent(getApplicationContext(), MainActivityLastingTrip.class);
            startActivity(nextScreen);
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

}

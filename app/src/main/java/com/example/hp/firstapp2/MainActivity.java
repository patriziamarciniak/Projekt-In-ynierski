package com.example.hp.firstapp2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

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

package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.sqlite.adapter.ListEventsAdapter;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

public class MainActivityLastingTrip extends AppCompatActivity {

    EventDAO eventDAO;
    ListView lastingEventsListView;
    List<Event> eventsList;
    ListEventsAdapter listEventsAdapter;
    Activity activity = this;
    private GetEmpTask task;
    Button btnMyHistory, btnMyEvents, btnAddEvent;
    Context context = this;
    Locale locale;
    Configuration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_lasting_trip);
        eventDAO = new EventDAO(this);
        lastingEventsListView = (ListView) findViewById(R.id.listViewLastingEvents);

        locale = new Locale("pl");
        Locale.setDefault(locale);
        config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);

        task = new GetEmpTask(activity);
        task.execute((Void) null);
        updateView();
        registerForContextMenu(lastingEventsListView);

        listEventsAdapter = new ListEventsAdapter(activity, eventsList, 1);
        lastingEventsListView.setAdapter(listEventsAdapter);
        lastingEventsListView.deferNotifyDataSetChanged();

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Event event = listEventsAdapter.getItem(position);
                Intent intent = new Intent(view.getContext(), EventDetails.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        };
        lastingEventsListView.setOnItemClickListener(listener);

        btnMyHistory = (Button) findViewById(R.id.btn_history_lasting);
        btnMyHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent nextScreen = new Intent(getApplicationContext(), MyHistory.class);
                startActivity(nextScreen);
            }
        });

        btnMyEvents = (Button) findViewById(R.id.btn_coming_trips_lasting);
        btnMyEvents.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), ComingTrips.class);
                startActivity(nextScreen);
            }
        });

        btnAddEvent = (Button) findViewById(R.id.btn_add_event_lasting);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), AddEvent.class);
                startActivity(nextScreen);
            }
        });

        changeItemTranslation();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewLastingEvents) {
            getMenuInflater().inflate(R.menu.menu_lasting_event, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                eventDAO.deleteEvent(listEventsAdapter.getItem(info.position));
                listEventsAdapter.remove(listEventsAdapter.getItem(info.position));
                return true;

            case R.id.menu_item_edit:
                Event event = listEventsAdapter.getItem(info.position);
                Intent intent = new Intent(getApplicationContext(), EditEvent.class);
                intent.putExtra("event", event);
                startActivity(intent);
                return true;


            case R.id.menu_item_finish_trip:
                Event event2 = listEventsAdapter.getItem(info.position);
                Intent intent2 = new Intent(getApplicationContext(), TripFinished.class);
                intent2.putExtra("event", event2);
                startActivity(intent2);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public class GetEmpTask extends AsyncTask<Void, Void, List<Event>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected List<Event> doInBackground(Void... arg0) {

            List<Event> eventsList = eventDAO.findLastingEvents();
            return eventsList;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                eventsList = events;
                if (events != null) {
                    if (events.size() != 0) {
                        listEventsAdapter = new ListEventsAdapter(activity,
                                events, 1);
                        lastingEventsListView.setAdapter(listEventsAdapter);
                        listEventsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void updateView() {
        task = new GetEmpTask(activity);
        task.execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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

    private void changeItemTranslation() {

        btnAddEvent.setText(R.string.title_activity_add_event);
        btnMyEvents.setText(R.string.title_coming_trips);
        btnMyHistory.setText(R.string.title_history);
        TextView txtActualTrip = (TextView) findViewById(R.id.textView5);
        txtActualTrip.setText(R.string.actual_event);
    }

}

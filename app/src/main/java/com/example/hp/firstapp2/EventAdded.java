package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.sqlite.adapter.ListEventsAdapter;
import com.example.hp.sqlite.dao.DBHelper;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class EventAdded extends AppCompatActivity {

    EventDAO eventDAO;
    Event event;
    Button btnMainMenu, btnEdit;
    Activity activity = this;
    ListView eventsListView;
    List<Event> eventsList;
    ListEventsAdapter listEventsAdapter;
    private GetEmpTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_added);
        eventsListView = (ListView) findViewById(R.id.listView);

        eventDAO = new EventDAO(this);
        event = eventDAO.getLastEvent();
        task = new GetEmpTask(activity);
        task.execute((Void) null);
        updateView();
        registerForContextMenu(eventsListView);
        listEventsAdapter = new ListEventsAdapter(activity, eventsList,0);
        eventsListView.setAdapter(listEventsAdapter);
        eventsListView.deferNotifyDataSetChanged();


        btnMainMenu = (Button) findViewById(R.id.btn_main_menu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);
            }
        });
        btnEdit = (Button) findViewById(R.id.btn_edit_event);
        btnEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent nextScreen = new Intent(getApplicationContext(), EditEvent.class);
                nextScreen.putExtra("event", event);
                startActivity(nextScreen);
            }
        });
    }

    public class GetEmpTask extends AsyncTask<Void, Void, List<Event>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetEmpTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected List<Event> doInBackground(Void... arg0) {

            List<Event> eventsList = new ArrayList<>();
            eventsList.add(eventDAO.getLastEvent());
            return eventsList;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                eventsList = events;
                if (events!= null) {
                    if (events.size() != 0) {
                        listEventsAdapter = new ListEventsAdapter(activity,
                                events, 0);
                        eventsListView.setAdapter(listEventsAdapter);
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

}

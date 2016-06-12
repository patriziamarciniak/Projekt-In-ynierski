package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.hp.sqlite.adapter.ListContactsAdapter;
import com.example.hp.sqlite.adapter.ListEventsAdapter;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ComingTrips extends AppCompatActivity {

    Activity activity = this;
    ListView eventsListView;
    List<Event> eventsList;
    ListEventsAdapter listEventsAdapter;
    EventDAO eventDAO;
    private GetEmpTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_trips);

        eventDAO = new EventDAO(this);
        task = new GetEmpTask(activity);
        task.execute((Void) null);
        updateView();
        eventsListView = (ListView) findViewById(R.id.eventslistView);
        registerForContextMenu(eventsListView);
        listEventsAdapter = new ListEventsAdapter(activity, eventsList);
        eventsListView.setAdapter(listEventsAdapter);
        eventsListView.deferNotifyDataSetChanged();

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Event event = listEventsAdapter.getItem(position);
                Intent intent = new Intent(view.getContext(), EventDetails.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        };
        eventsListView.setOnItemClickListener(listener);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.eventslistView) {
            getMenuInflater().inflate(R.menu.menu_item_event, menu);
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
            List<Event> eventsList = eventDAO.getAllEvents();
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
                                events);
                        eventsListView.setAdapter(listEventsAdapter);
                        listEventsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity, "No Event Records",
                                Toast.LENGTH_LONG).show();
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


package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
        eventsListView = (ListView)findViewById(R.id.eventslistView);

        listEventsAdapter = new ListEventsAdapter(activity,eventsList);
        eventsListView.setAdapter(listEventsAdapter);
        eventsListView.deferNotifyDataSetChanged();


        AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                setTitle(parent.getItemAtPosition(position).toString());

                eventDAO.deleteEvent(listEventsAdapter.getItem(position));
                listEventsAdapter.remove(listEventsAdapter.getItem(position));
                return true;

            }
        };

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                setTitle(parent.getItemAtPosition(position).toString());

                //  Log.d("position", parent.getItemAtPosition(position).toString());

                Event event = listEventsAdapter.getItem(position);
                Log.d("moj event", event.getDataEnd());

                Intent intent = new Intent(view.getContext(), EventDetails.class);
                intent.putExtra("event", event);
                startActivity(intent);


            }
        };

        eventsListView.setOnItemLongClickListener(longListener);
    eventsListView.setOnItemClickListener(listener);


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


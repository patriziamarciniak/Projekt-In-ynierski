package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.sqlite.adapter.ListEventsAdapter;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyHistory extends AppCompatActivity {

    Activity activity = this;
    ListView eventsListView;
    List<Event> eventsList;
    ListEventsAdapter listEventsAdapter;
    EventDAO eventDAO;
    GetEmpTask task;
    Button btnDay, btnAll, btnWeek;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    String today = format1.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        eventsListView = (ListView) findViewById(R.id.eventslistViewHistory);
        eventDAO = new EventDAO(this);
        loadEvents();

        btnDay = (Button) findViewById(R.id.btn_day_history);
        btnAll = (Button) findViewById(R.id.btn_month_history);
        btnWeek = (Button) findViewById(R.id.btn_week_history);

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

        btnDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                eventsList = eventDAO.getPassEvents(today, today);
                registerForContextMenu(eventsListView);
                listEventsAdapter = new ListEventsAdapter(activity, eventsList, 0);
                eventsListView.setAdapter(listEventsAdapter);
                eventsListView.deferNotifyDataSetChanged();
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                eventsList = eventDAO.getPassEvents(findLastWeek(), today);
                registerForContextMenu(eventsListView);
                listEventsAdapter = new ListEventsAdapter(activity, eventsList, 0);
                eventsListView.setAdapter(listEventsAdapter);
                eventsListView.deferNotifyDataSetChanged();
            }
        });


        btnAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                loadEvents();
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
            List<Event> eventsList = eventDAO.getPassEvents("1999-01-01", today.toString());
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
                                events, 0);
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

    private void loadEvents() {
        task = new GetEmpTask(activity);
        task.execute((Void) null);
        updateView();
        registerForContextMenu(eventsListView);
        listEventsAdapter = new ListEventsAdapter(activity, eventsList,0);
        eventsListView.setAdapter(listEventsAdapter);
        eventsListView.deferNotifyDataSetChanged();
    }

    private String findLastWeek(){
        int day = Integer.parseInt(today.substring(8));
        int month = Integer.parseInt(today.substring(5,7));
        int year = Integer.parseInt(today.substring(0,4));

        if(day > 7){
            day -= 7;
        }else{
            if(month > 1){
                month--;
                day = 30 - (7 - day);
            }else {
                year--;
                month = 12;
                day = 30 - (7 - day);
            }
        }
       return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }
}



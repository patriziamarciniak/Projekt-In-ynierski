package com.example.hp.firstapp2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.sqlite.adapter.ListEventsAdapter;
import com.example.hp.sqlite.dao.EventDAO;
import com.example.hp.sqlite.model.Event;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MyHistory extends AppCompatActivity {

    Activity activity = this;
    ListView eventsListView;
    List<Event> eventsList;
    ListEventsAdapter listEventsAdapter;
    EventDAO eventDAO;
    GetEmpTask task;
    Button btnDay, btnAll, btnWeek, btnMonth;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String today = dateFormat.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        Typeface ubuntu_font = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-B.ttf");
        eventsListView = (ListView) findViewById(R.id.eventsListViewHistory);
        eventDAO = new EventDAO(this);
        loadEvents();

        btnDay = (Button) findViewById(R.id.btn_day_history);
        btnAll = (Button) findViewById(R.id.btn_all_history);
        btnMonth = (Button) findViewById(R.id.btn_month_history);
        btnWeek = (Button) findViewById(R.id.btn_week_history);
        btnDay.setTypeface(ubuntu_font);
        btnAll.setTypeface(ubuntu_font);
        btnMonth.setTypeface(ubuntu_font);
        btnWeek.setTypeface(ubuntu_font);
        setButtonsUnselected();
        btnAll.setTypeface(null, Typeface.BOLD);


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
                setButtonsUnselected();
                btnDay.setTypeface(null, Typeface.BOLD);
                eventsList = eventDAO.getPassEvents(today, today);
                registerForContextMenu(eventsListView);
                listEventsAdapter = new ListEventsAdapter(activity, eventsList, 0);
                eventsListView.setAdapter(listEventsAdapter);
                eventsListView.deferNotifyDataSetChanged();
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setButtonsUnselected();
                btnWeek.setTypeface(null, Typeface.BOLD);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -6);
                eventsList = eventDAO.getPassEvents(dateFormat.format(cal.getTime()), today);
                registerForContextMenu(eventsListView);
                listEventsAdapter = new ListEventsAdapter(activity, eventsList, 0);
                eventsListView.setAdapter(listEventsAdapter);
                eventsListView.deferNotifyDataSetChanged();
            }
        });

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsUnselected();
                btnMonth.setTypeface(null, Typeface.BOLD);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                eventsList = eventDAO.getPassEvents(dateFormat.format(cal.getTime()), today);
                registerForContextMenu(eventsListView);
                listEventsAdapter = new ListEventsAdapter(activity, eventsList, 0);
                eventsListView.setAdapter(listEventsAdapter);
                eventsListView.deferNotifyDataSetChanged();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setButtonsUnselected();
                btnAll.setTypeface(null, Typeface.BOLD);
                loadEvents();
            }
        });

    }

    private void setButtonsUnselected() {
        btnDay.setTypeface(null, Typeface.NORMAL);
        btnWeek.setTypeface(null, Typeface.NORMAL);
        btnMonth.setTypeface(null, Typeface.NORMAL);
        btnAll.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.eventsListViewHistory) {
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
        listEventsAdapter = new ListEventsAdapter(activity, eventsList, 0);
        eventsListView.setAdapter(listEventsAdapter);
        eventsListView.deferNotifyDataSetChanged();
    }

}



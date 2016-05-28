package com.example.hp.sqlite.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.firstapp2.R;

import com.example.hp.sqlite.model.Event;

public class ListEventsAdapter extends BaseAdapter {

    public static final String TAG = "ListEventAdapter";

    private List<Event> mItems;
    private LayoutInflater mInflater;

    public ListEventsAdapter(Context context, List<Event> listEvent) {
        this.setItems(listEvent);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Event getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = mInflater.inflate(R.layout.list_item_event, parent, false);
            holder = new ViewHolder();
            holder.txtDataStart = (TextView) v.findViewById(R.id.txt_added_data_start);
            holder.txtTimeStart = (TextView) v.findViewById(R.id.txt_time_start);
            holder.txtDataEnd = (TextView) v.findViewById(R.id.txt_data_end);
            holder.txtTimeEnd = (TextView) v.findViewById(R.id.txt_time_end);
            holder.txtHistory = (TextView) v.findViewById(R.id.txt_history);
            holder.txtNotificationsStart = (TextView) v.findViewById(R.id.txt_notifications_start);
            holder.txtNotificationsEnd = (TextView) v.findViewById(R.id.txt_notifications_end);
            holder.txtAutoNotifications = (TextView) v.findViewById(R.id.txt_auto_notifications);
            holder.txtRadius = (TextView) v.findViewById(R.id.txt_radius);
            holder.txtLocalisation = (TextView) v.findViewById(R.id.txt_localisation);
            holder.txtRepetition = (TextView) v.findViewById(R.id.txt_repetition);


            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        Event currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtDataStart.setText(currentItem.getDataStart());
            holder.txtTimeStart.setText(currentItem.getTimeStart());
            holder.txtDataEnd.setText(currentItem.getDataEnd());
            holder.txtTimeEnd.setText(currentItem.getTimeEnd());
            holder.txtHistory.setText(Boolean.toString(currentItem.getHistory()));
            holder.txtNotificationsStart.setText(Boolean.toString(currentItem.getNotificationsStart()));
            holder.txtNotificationsEnd.setText(Boolean.toString(currentItem.getNotificationsEnd()));
            holder.txtAutoNotifications.setText(Boolean.toString(currentItem.getAutoNotifications()));
            holder.txtRadius.setText(String.valueOf(currentItem.getRadius()));//added conversion
            holder.txtLocalisation.setText(currentItem.getLocalisation());
            holder.txtRepetition.setText(String.valueOf(currentItem.getRepetition()));
        }

        return v;
    }



    public List<Event> getItems() {
        return mItems;
    }

    public void setItems(List<Event> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtDataStart;
        TextView txtTimeStart;
        TextView txtDataEnd;
        TextView txtTimeEnd;
        TextView txtHistory;
        TextView txtNotificationsStart;
        TextView txtNotificationsEnd;
        TextView txtAutoNotifications;
        TextView txtRadius;
        TextView txtLocalisation;
        TextView txtRepetition;


    }

}

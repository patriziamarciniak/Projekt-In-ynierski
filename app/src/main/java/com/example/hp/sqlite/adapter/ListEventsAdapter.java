package com.example.hp.sqlite.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.firstapp2.R;

import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.PhoneContact;

public class ListEventsAdapter extends BaseAdapter {

    //w obydwu adapterach, w konstruktorze wybieramy jaki typ layout'u chcemy
    // tu mamy do wyboru wyswietlanie eventow lub wyswietlanie trwajacych eventow
    // w PhoneContactAdapter jedyna roznica jest ikonka do usuwania ich z listy

    public static final String TAG = "ListEventAdapter";

    private List<Event> mItems;
    private LayoutInflater mInflater;
    private int layoutType;

    public ListEventsAdapter(Context context, List<Event> listEvent, int type) {
        this.setItems(listEvent);
        this.mInflater = LayoutInflater.from(context);
        this.layoutType = type;

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
        Context context = parent.getContext();
        AttendanceDAO attendanceDAO = new AttendanceDAO(context);

        if(layoutType == 0){
        if(v == null) {
            v = mInflater.inflate(R.layout.list_item_event, parent, false);
            holder = new ViewHolder();
            holder.txtDataStart = (TextView) v.findViewById(R.id.txt_data_start);
            holder.txtTimeStart = (TextView) v.findViewById(R.id.txt_time_start);
            holder.txtDataEnd = (TextView) v.findViewById(R.id.txt_data_end);
            holder.txtTimeEnd = (TextView) v.findViewById(R.id.txt_time_end);
//            holder.txtNotificationsStart = (TextView) v.findViewById(R.id.txt_notifications_start);
//            holder.txtNotificationsEnd = (TextView) v.findViewById(R.id.txt_notifications_end);
//            holder.txtAutoNotifications = (TextView) v.findViewById(R.id.txt_auto_notifications);
//            holder.txtRadius = (TextView) v.findViewById(R.id.txt_radius);
            holder.txtStartLocalizationX = (TextView) v.findViewById(R.id.txt_start_localization_x);
//            holder.txtStartLocalizationY = (TextView) v.findViewById(R.id.txt_start_localisation_y);
            holder.txtEndLocalizationX = (TextView) v.findViewById(R.id.txt_end_localization_x);
//            holder.txtEndLocalizationY = (TextView) v.findViewById(R.id.txt_end_localisation_y);
//            holder.txtRepetition = (TextView) v.findViewById(R.id.txt_repetition);
            holder.txtPeople = (TextView) v.findViewById(R.id.txt_people);
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
//            holder.txtNotificationsStart.setText(Boolean.toString(currentItem.getNotificationsStart()));
//            holder.txtNotificationsEnd.setText(Boolean.toString(currentItem.getNotificationsEnd()));
//            holder.txtAutoNotifications.setText(Boolean.toString(currentItem.getAutoNotifications()));
//            holder.txtRadius.setText(String.valueOf(currentItem.getRadius()));//added conversion
            holder.txtStartLocalizationX.setText(currentItem.getStartLocalisationX());
//            holder.txtStartLocalizationY.setText(currentItem.getStartLocalisationY());
            holder.txtEndLocalizationX.setText(currentItem.getEndLocalisationX());
//            holder.txtEndLocalizationY.setText(currentItem.getEndLocalisationY());
//            holder.txtRepetition.setText(String.valueOf(currentItem.getRepetition()));
            List<PhoneContact> phoneContactList = attendanceDAO.getContacts(currentItem.getId());
            String displayContacts = "";
            for (PhoneContact phoneContact : phoneContactList){displayContacts += " " + phoneContact.getName();}
            holder.txtPeople.setText(displayContacts);
        }
        return v;
        } else {
            Event currentItem = getItem(position);
            if (v == null) {
                v = mInflater.inflate(R.layout.list_item_lasting_event, parent, false);
                holder = new ViewHolder();
                holder.txtTimeEnd = (TextView) v.findViewById(R.id.txt_scheduled_time);
                holder.txtStartLocalizationX = (TextView) v.findViewById(R.id.txt_localization_start);
                holder.txtEndLocalizationX = (TextView) v.findViewById(R.id.txt_localization_end);
                holder.txtPeople = (TextView) v.findViewById(R.id.txt_people);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            if (currentItem != null) {
                holder.txtTimeEnd.setText(currentItem.getTimeEnd());
                holder.txtStartLocalizationX.setText(currentItem.getStartLocalisationX());
                holder.txtEndLocalizationX.setText(currentItem.getEndLocalisationX());
                List<PhoneContact> phoneContactList = attendanceDAO.getContacts(currentItem.getId());
                String displayContacts = "";
                for (PhoneContact phoneContact : phoneContactList){displayContacts += " " + phoneContact.getName();}
                holder.txtPeople.setText(displayContacts);
            }
            return v;
        }
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
        TextView txtNotificationsStart;
        TextView txtNotificationsEnd;
        TextView txtAutoNotifications;
        TextView txtRadius;
        TextView txtStartLocalizationX;
        TextView txtStartLocalizationY;
        TextView txtEndLocalizationX;
        TextView txtEndLocalizationY;
        TextView txtRepetition;
        TextView txtPeople;
    }


    public void remove(Event employee) {
        mItems.remove(employee);
        notifyDataSetChanged();
    }
}

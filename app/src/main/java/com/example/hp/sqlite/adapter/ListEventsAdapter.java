package com.example.hp.sqlite.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.firstapp2.R;

import com.example.hp.sqlite.dao.AttendanceDAO;
import com.example.hp.sqlite.model.Event;
import com.example.hp.sqlite.model.PhoneContact;

public class ListEventsAdapter extends BaseAdapter {

    //w obydwu adapterach, w konstruktorze wybieramy jaki typ layout'u chcemy
    // tu mamy do wyboru wyswietlanie eventow lub wyswietlanie trwajacych eventow
    // w PhoneContactAdapter jedyna roznica jest ikonka do usuwania ich z listy

    public static final String TAG = "ListEventAdapter";

    private Context context;
    private List<Event> mItems;
    private LayoutInflater mInflater;
    private int layoutType;

    public ListEventsAdapter(Context context, List<Event> listEvent, int type) {
        this.setItems(listEvent);
        this.context = context;
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


    /////////////////////////////////////////////////


    private String startGeolocationToAddress(String mLongitude, String mLatitude) {

        double longitude = Double.valueOf(mLongitude);
        double latitude = Double.valueOf(mLatitude);
        String result = changeToAddress(latitude, longitude);
        return (result);

    }

    public String changeToAddress(double latitude, double longitude){
        List<Address> addresses = null;

        Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = "";

        if (addresses.size() != 0) {
            Address address = addresses.get(0);

            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            if (addresses.get(0).getLocality() != null) {
                result += addresses.get(0).getLocality();
                result += ", ";
                result += addresses.get(0).getCountryName();
            } else {
                result += addresses.get(0).getCountryName();
            }

            return result;
        } else {
            return ("0");
        }
    }
//////////////////////




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
            try {
                holder.txtStartLocalizationX.setText(startGeolocationToAddress(currentItem.getStartLocalisationX(), currentItem.getStartLocalisationY()));
            }
            catch (NumberFormatException e) {
                holder.txtStartLocalizationX.setText(currentItem.getStartLocalisationX() + " " + currentItem.getStartLocalisationY());
            }
//            holder.txtStartLocalizationY.setText(currentItem.getStartLocalisationY());
            try {
                holder.txtEndLocalizationX.setText(startGeolocationToAddress(currentItem.getEndLocalisationX(), currentItem.getEndLocalisationY()));
            }
            catch (NumberFormatException e) {
                holder.txtEndLocalizationX.setText(currentItem.getEndLocalisationX() + " " + currentItem.getEndLocalisationY());
            }
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

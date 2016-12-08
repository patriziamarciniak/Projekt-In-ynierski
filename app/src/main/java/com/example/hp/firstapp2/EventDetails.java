package com.example.hp.firstapp2;

import android.location.Address;
import android.location.Geocoder;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.sqlite.model.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventDetails extends AppCompatActivity {

    TextView dateStart, dateEnd, timeStart, timeEnd, localisationStart, localisationEnd, spinner, notAuto, notStart, notEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Bundle bundle = getIntent().getExtras();
        Event event = bundle.getParcelable("event");

        dateStart = (TextView) findViewById(R.id.txt_details_data_start);
        dateEnd = (TextView) findViewById(R.id.txt_details_data_end);
        timeStart = (TextView) findViewById(R.id.txt_details_time_start);
        timeEnd = (TextView) findViewById(R.id.txt_details_time_end);
        localisationStart = (TextView) findViewById(R.id.txt_details_localization_start);
        localisationEnd = (TextView) findViewById(R.id.txt_details_localization_end);
        spinner = (TextView) findViewById(R.id.txt_details_radius);
        notAuto = (TextView) findViewById(R.id.txt_details_auto_notifications);
        notStart = (TextView) findViewById(R.id.txt_details_notification_start);
        notEnd = (TextView) findViewById(R.id.txt_details_notification_end);

        dateEnd.setText(event.getDataEnd());
        dateStart.setText(event.getDataStart());
        timeEnd.setText(event.getTimeEnd());
        timeStart.setText(event.getTimeStart());
        localisationStart.setText(startGeolocationToAddress(event.getStartLocalisationX(), event.getStartLocalisationY()));
        localisationEnd.setText(startGeolocationToAddress(event.getEndLocalisationX(), event.getEndLocalisationY()));
        spinner.setText(String.valueOf(event.getRadius()));
        notAuto.setText(String.valueOf(event.getAutoNotifications()));
        notStart.setText(String.valueOf(event.getNotificationsStart()));
        notEnd.setText(String.valueOf(event.getNotificationsEnd()));

    }

//////////////////////ZAMIANA WSPOLRZEDNYCH NA ADRES ///////////////////////////

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String startGeolocationToAddress(String mLongitude, String mLatitude) {

        double longitude = new Double(mLongitude);
        double latitude = new Double(mLatitude);
        String result = changeToAddress(latitude, longitude);
        return (result);

    }

    private String endGeolocationToAddress(String mLongitude, String mLatitude) {

        double longitude = new Double(mLongitude);
        double latitude = new Double(mLatitude);
        String result = changeToAddress(latitude, longitude);
        return (result);

    }


    public String changeToAddress(double latitude, double longitude) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = "";

        if (addresses.size() != 0) {
            Address address = addresses.get(0);

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)

            if (addresses.get(0).getLocality() != null) {
                if(addresses.get(0).getAddressLine(0) != null) result += addresses.get(0).getAddressLine(0) + ", ";
                result += addresses.get(0).getLocality();
                result += ", ";
                result += addresses.get(0).getCountryName();
            } else {
                result += addresses.get(0).getCountryName();
            }

            return result;
        }

        else{
            return("0");
        }
   }
}
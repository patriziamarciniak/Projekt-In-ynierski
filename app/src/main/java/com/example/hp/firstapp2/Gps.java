package com.example.hp.firstapp2;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Gps  extends AppCompatActivity {
    TextView t1;
    TextView t2;
    TextView t3;

    LocationManager lm;
    Criteria kr;
    Location loc;
    String theBestProvider;


    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps);
        t1=(TextView) findViewById(R.id.textView2);
        t2=(TextView) findViewById(R.id.textView3);
        t3=(TextView) findViewById(R.id.textView4);

        kr=new Criteria();
        lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        theBestProvider=lm.getBestProvider(kr, true);
        loc=lm.getLastKnownLocation(theBestProvider);

        t1.setText("najlepszy dostawca: "+theBestProvider);
        t2.setText("długość geograficzna: "+loc.getLongitude());
        t3.setText("szerokość geograficzna: "+loc.getLatitude());
    }


}

package com.example.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView lonTextView,latTextView,accTextView,altTextView,addTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lonTextView=findViewById(R.id.longTextView);
        latTextView=findViewById(R.id.latTextView);
        accTextView=findViewById(R.id.accTextView);
        altTextView=findViewById(R.id.addTextView);
        addTextView=findViewById(R.id.addTextView);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLoaction(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    public void updateLoaction(Location location)
    {
        double lat=location.getLatitude();
        double lon=location.getLongitude();
        lonTextView.setText("Longitude: "+String.valueOf(lon));
        latTextView.setText("Latitude: "+String.valueOf(lat));
        accTextView.setText("Accuracy: "+String.valueOf(location.getAccuracy()));
        altTextView.setText("Altitude: "+String.valueOf(location.getAltitude()));

        String Address="Could not find Address!";

        Geocoder geocoder=new Geocoder(this,Locale.getDefault());

        try {
            List<android.location.Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0) {
                Address = "Address: " + "\n";
                if (listAddresses.get(0).getThoroughfare() != null) {
                    Address += listAddresses.get(0).getThoroughfare() + "\n";
                }
                if (listAddresses.get(0).getLocality() != null) {
                    Address += listAddresses.get(0).getLocality() + " ";

                }
                if (listAddresses.get(0).getPostalCode() != null) {
                    Address += listAddresses.get(0).getPostalCode();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        addTextView.setText(Address);
    }
}

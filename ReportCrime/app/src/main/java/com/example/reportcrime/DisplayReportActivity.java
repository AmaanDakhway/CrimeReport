package com.example.reportcrime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayReportActivity extends FragmentActivity implements OnMapReadyCallback {

    TextView CRIME, CRIME_DESCRIPTION, LOCATION, STATUS;
    double Latitude,Longitude;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_report);

        CRIME = findViewById(R.id.CRIME);
        CRIME_DESCRIPTION = findViewById(R.id.CRIME_DESCRIPTION);
        LOCATION = findViewById(R.id.LOCATION);
        STATUS = findViewById(R.id.STATUS);

        CRIME.setText("Crime: " + getIntent().getStringExtra("CRIME"));
        CRIME_DESCRIPTION.setText("Crime Description: "+getIntent().getStringExtra("CRIME_DESC"));
        LOCATION.setText("Location: "+getIntent().getStringExtra("LOCATION"));
        STATUS.setText("Status: "+getIntent().getStringExtra("STATUS"));

        String location = getIntent().getStringExtra("LOCATION");
        String[] latLng  = location.split(",");;
        Latitude = Double.parseDouble(latLng[0]);
        Longitude = Double.parseDouble(latLng[1]);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng latLng = new LatLng(Latitude,Longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);

    }
}
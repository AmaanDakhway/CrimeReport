package com.example.receivecrime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receivecrime.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DescribedReportHistory extends Fragment implements OnMapReadyCallback {

    String status, location, crime, description;
    double Latitude,Longitude;

    public DescribedReportHistory() {
        // Required empty public constructor
    }

    public DescribedReportHistory(String status, String location, String crime, String description) {
        this.status = status;
        this.location = location;
        this.crime = crime;
        this.description = description;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_described_report_history, container, false);

        TextView CRIME, CRIME_DESCRIPTION,LOCATION, STATUS;

        CRIME = view.findViewById(R.id.CRIME);
        CRIME_DESCRIPTION = view.findViewById(R.id.CRIME_DESCRIPTION);
        LOCATION = view.findViewById(R.id.LOCATION);
        STATUS = view.findViewById(R.id.STATUS);

        CRIME.setText("Crime: "+crime);
        CRIME_DESCRIPTION.setText("Crime Description: "+description);
        LOCATION.setText("Location: "+location);
        STATUS.setText("Status: "+status);

        String maplocation = location;
        String[] latLng  = maplocation.split(",");;
        Latitude = Double.parseDouble(latLng[0]);
        Longitude = Double.parseDouble(latLng[1]);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        return  view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(Latitude,Longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("The Crime Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);
    }
}
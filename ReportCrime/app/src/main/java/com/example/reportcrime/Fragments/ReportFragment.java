package com.example.reportcrime.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.reportcrime.DisplayReportActivity;
import com.example.reportcrime.Models.EmergencyModel;
import com.example.reportcrime.Models.ReportModel;
import com.example.reportcrime.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportFragment extends Fragment implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    EditText Crime, CrimeDescription;
    Button SubmitReport;
    ReportModel reportModel;
    DatabaseReference databaseReference,reference;
    double Latitude,Longitude;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;
    MarkerOptions markerOptions1;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        Crime = view.findViewById(R.id.CrimeType);
        CrimeDescription = view.findViewById(R.id.Description);
        SubmitReport = view.findViewById(R.id.SubmitReport);

        reportModel = new ReportModel();

        SubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(Crime.getText().toString().trim())){
                    Crime.setError("Date is Required");
                    return;
                }
                if (TextUtils.isEmpty(CrimeDescription.getText().toString().trim())){
                    CrimeDescription.setError("Time is Required");
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userID = user.getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference("ALL_REPORTS");
                reference = FirebaseDatabase.getInstance().getReference(userID).child("REPORTS");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reportModel.setUSERID(userID);
                        reportModel.setCRIME(Crime.getText().toString().trim());
                        reportModel.setDESCRIPTION(CrimeDescription.getText().toString().trim());
                        reportModel.setLOCATION(currentLocation.getLatitude()+","+currentLocation.getLongitude());
                        reportModel.setSTATUS("Pending");

                        reference.push().setValue(reportModel);
                        databaseReference.push().setValue(reportModel);

                        Intent intent = new Intent(getContext(), DisplayReportActivity.class);
                        intent.putExtra("CRIME",Crime.getText().toString().trim());
                        intent.putExtra("CRIME_DESC",CrimeDescription.getText().toString().trim());
                        intent.putExtra("LOCATION", currentLocation.getLatitude()+","+currentLocation.getLongitude());
                        intent.putExtra("STATUS","Pending");
                        startActivity(intent);

                        Crime.setText("");
                        CrimeDescription.setText("");



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();

        return view;
    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{

                    Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(ReportFragment.this);
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap.clear();

        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,6));
        googleMap.addMarker(markerOptions);

        databaseReference = FirebaseDatabase.getInstance().getReference("POLICE_LOCATIONS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EmergencyModel emergencyModel = dataSnapshot.getValue(EmergencyModel.class);
                    String locations = emergencyModel.getLOCATION();

                    String[] latLng  = locations.split(",");
                    Latitude = Double.parseDouble(latLng[0]);
                    Longitude = Double.parseDouble(latLng[1]);

                    LatLng center1 = new LatLng(Latitude, Longitude);
                    markerOptions1 = new MarkerOptions().position(center1).title("Police");
                    markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    googleMap.addMarker(markerOptions1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                fetchLastLocation();
            }
        }, delay);
        super.onResume();
    }
}
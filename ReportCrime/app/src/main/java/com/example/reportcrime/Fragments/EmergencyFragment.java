package com.example.reportcrime.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reportcrime.Models.EmergencyModel;
import com.example.reportcrime.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyFragment extends Fragment {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    EmergencyModel emergencyModel;
    DatabaseReference databaseReference,reference;
    Button Emergency;
    TextView Emergency_text;


    public EmergencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        Emergency = view.findViewById(R.id.emergency);
        Emergency_text = view.findViewById(R.id.emergency_text);
        emergencyModel = new EmergencyModel();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        Emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Emergency.setEnabled(false);
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fetchLastLocation();

                Emergency_text.setVisibility(View.VISIBLE);

                databaseReference = FirebaseDatabase.getInstance().getReference("EMERGENCY");
                reference = FirebaseDatabase.getInstance().getReference(userID).child("USER_EMERGENCY");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        emergencyModel.setEMERGENCY_STATUS("Pending");
                        emergencyModel.setLOCATION(currentLocation.getLatitude()+","+currentLocation.getLongitude());
                        emergencyModel.setUSERID(userID);

                        reference.push().setValue(emergencyModel);
                        databaseReference.push().setValue(emergencyModel);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



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
                    Toast.makeText(getContext(),currentLocation.getLatitude()+","+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Emergency.setEnabled(true);
    }
}
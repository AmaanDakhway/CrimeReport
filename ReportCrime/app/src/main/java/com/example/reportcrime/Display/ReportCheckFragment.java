package com.example.reportcrime.Display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reportcrime.Models.ReportHistoryModel;
import com.example.reportcrime.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ReportCheckFragment extends Fragment {

    RecyclerView recview;
    ReportAdapter ReportAdapter;
    String userID;

    public ReportCheckFragment(String userID) {
        this.userID = userID;
    }

    public ReportCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_report_check, container, false);

        recview = view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<ReportHistoryModel> options =
                new FirebaseRecyclerOptions.Builder<ReportHistoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(userID).child("REPORTS")
                                ,ReportHistoryModel.class)
                        .build();
        ReportAdapter = new ReportAdapter(options);
        recview.setAdapter(ReportAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ReportAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ReportAdapter.stopListening();
    }

}
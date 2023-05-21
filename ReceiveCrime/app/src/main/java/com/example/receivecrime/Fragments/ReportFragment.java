package com.example.receivecrime.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.receivecrime.Models.ReportHistoryModel;
import com.example.receivecrime.R;
import com.example.receivecrime.ReportAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ReportFragment extends Fragment {

    RecyclerView recview;
    com.example.receivecrime.ReportAdapter ReportAdapter;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        recview = view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<ReportHistoryModel> options =
                new FirebaseRecyclerOptions.Builder<ReportHistoryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("ALL_REPORTS").orderByChild("status").equalTo("Pending")
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
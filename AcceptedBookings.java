package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AcceptedBookings extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Schedule> schedules;
    private DatabaseReference reference;
    private BookingAdapter adapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_bookings);

        recyclerView = findViewById(R.id.adminAppointmentsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference("Consultations");

        schedules = new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.accept_item);
    }

    public void onStart(){
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                schedules.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Schedule schedule = ds.getValue(Schedule.class);
                    if (schedule != null && schedule.getSchedule_status().equals("Accepted")){
                        schedules.add(schedule);
                    }
                }
                adapter = new BookingAdapter(getApplicationContext(),schedules);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
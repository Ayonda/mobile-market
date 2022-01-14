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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminBookings extends AppCompatActivity {
    private RecyclerView recyclerView,recyclerView1;
    private List<Schedule> schedules,schedules1;
    private DatabaseReference reference;
    private BookingAdapter adapter,adapter1;
    private Dialog dialog;
    private TextView textView1,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bookings);

        recyclerView = findViewById(R.id.adminBookingsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView1 = findViewById(R.id.adminAppointmentsRecycler);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference("Consultations");

        schedules = new ArrayList<>();

        schedules1 = new ArrayList<>();

        dialog = new Dialog(AdminBookings.this);
        dialog.setContentView(R.layout.accept_item);

        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);

    }

    public void onStart(){
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                schedules.clear();
                schedules1.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Schedule schedule = ds.getValue(Schedule.class);
                    if (schedule != null && schedule.getSchedule_status().equals("Pending")){
                        schedules.add(schedule);
                    } else if (schedule != null && schedule.getSchedule_status().equals("Accepted")){
                        schedules1.add(schedule);
                    }
                }
                adapter1 = new BookingAdapter(getApplicationContext(),schedules1);
                recyclerView1.setAdapter(adapter1);

                adapter = new BookingAdapter(getApplicationContext(),schedules);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView
                        , new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Schedule schedule = schedules.get(position);

                        Button rej,acc;
                        rej = dialog.findViewById(R.id.reject);
                        acc = dialog.findViewById(R.id.accept);

                        acc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reference.child(schedule.getSchedule_uid()).child("schedule_status").setValue("Accepted");
                                dialog.dismiss();
                            }
                        });

                        rej.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                reference.child(schedule.getSchedule_uid()).child("status").setValue("Rejected");
                                dialog.dismiss();
                            }
                        });

                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void newOnClick(View view) {
        recyclerView1.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        textView1.setTextColor(Color.BLACK);
    }

    public void acceptedOnClick(View view) {
        recyclerView1.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        textView2.setTextColor(Color.BLACK);
    }
}
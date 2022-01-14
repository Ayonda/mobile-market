package com.example.mobilemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class BookingsTab extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_tab);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("New bookings");
        spec1.setIndicator("New bookings");
        Intent intent1 = new Intent(this, AdminBookings.class);
        spec1.setContent(intent1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Appointments");
        spec2.setIndicator("Appointments");
        Intent intent2 = new Intent(this, AcceptedBookings.class);
        spec2.setContent(intent2);

        // Adding all TabSpec to TabHost
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
    }
}
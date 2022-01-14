package com.example.mobilemarket;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ClientTabs extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tabs);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.ic_home));
        Intent intent1 = new Intent(this, ClientHomeActivity.class);
        spec1.setContent(intent1);

        TabHost.TabSpec spec3 = tabHost.newTabSpec("");
        spec3.setIndicator("",getResources().getDrawable(R.drawable.ic_settings));
        Intent intent3 = new Intent(this, ClientSettingsActivity.class);
        spec3.setContent(intent3);

        TabHost.TabSpec spec4 = tabHost.newTabSpec("");
        spec4.setIndicator("",getResources().getDrawable(R.drawable.ic_orders));
        Intent intent4 = new Intent(this, ClientOrdersActivity.class);
        spec4.setContent(intent4);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("");
        spec2.setIndicator("",getResources().getDrawable(R.drawable.ic_book));
        Intent intent2 = new Intent(this, BookConsultation.class);
        spec2.setContent(intent2);

        // Adding all TabSpec to TabHost
        tabHost.addTab(spec1);
        tabHost.addTab(spec4);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
    }
}
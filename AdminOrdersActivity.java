package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersActivity extends AppCompatActivity{
    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private List<Order> orders;
    private AdminOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ref = FirebaseDatabase.getInstance().getReference("Orders");
        recyclerView = findViewById(R.id.AdminOrdersRecyclerview);
        orders = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onStart(){
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for (DataSnapshot ds:snapshot.getChildren()){

                    Order order = ds.getValue(Order.class);

                    if (order != null && !(order.getStatus().equals("Canceled") || order.getStatus().equals("incomplete"))){

                        orders.add(order);

                    }
                }

                adapter = new AdminOrdersAdapter(getApplicationContext(),orders);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
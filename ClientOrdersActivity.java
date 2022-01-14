package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientOrdersActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private List<Order> orders;
    private OrderAdapter adapter;
    private String auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_orders);

        ref = FirebaseDatabase.getInstance().getReference("Orders");
        recyclerView = findViewById(R.id.odersRecyclerview);
        orders = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth = FirebaseAuth.getInstance().getUid();
    }

    public void onStart(){
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
               for (DataSnapshot ds:snapshot.getChildren()){

                   Order order = ds.getValue(Order.class);

                   if (order != null && auth.equals(order.getUuid())){

                       orders.add(order);

                   }
               }

               adapter = new OrderAdapter(getApplicationContext(),orders);
               recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
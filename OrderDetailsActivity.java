package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDetailsActivity extends AppCompatActivity {
    private String oid;
    private DatabaseReference orderRef,itemRef;
    private TextView id,date,details,amaount,status;
    private ItemsAdapter adapter;
    private List<Cart> carts;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        oid = Objects.requireNonNull(getIntent().getExtras()).getString("OID");
        id = findViewById(R.id.or_id);
        date = findViewById(R.id.or_amount);
        details = findViewById(R.id.bu_details);
        amaount = findViewById(R.id.or_amount);
        status = findViewById(R.id.or_status);
        itemRef = FirebaseDatabase.getInstance().getReference("Ordered Items");
        carts = new ArrayList<>();
        recyclerView = findViewById(R.id.itemRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    public void onStart(){
        super.onStart();

        orderRef = FirebaseDatabase.getInstance().getReference("Orders");

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){

                    Order order = ds.getValue(Order.class);

                    if (order != null && oid.equals(order.getUid())){

                        id.setText(order.getUid());
                        date.setText(order.getDate()+" "+order.getTime());
                        details.setText(order.getName()+"\n"+order.getPhone()+"\n"+order.getAddress());
                        amaount.setText(order.getTotalAmount());

                        if (order.getStatus().equals("Ready for pickup")){

                            status.setText("RP");

                        }else {

                            status.setText(order.getStatus());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    Cart cart = ds.getValue(Cart.class);

                    if (cart != null){
                        carts.add(cart);
                    }
                }

                adapter = new ItemsAdapter(getApplicationContext(),carts);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private DatabaseReference ref,ref1;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Cart> carts;
    private String user,name,phone,address;
    private List<String> prices,uids,pnames,quantities,sizes,uris,pids;
    private TextView subtotal,total,delivery,total1,items;
    private LinearLayout linearLayout;
    private CardView cardView;
    private Dialog dialog;
    private int deliveryFee;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ref = FirebaseDatabase.getInstance().getReference("Cart");

        ref1 = FirebaseDatabase.getInstance().getReference("Users");

        carts = new ArrayList<>();

        prices = new ArrayList<>();

        uids = new ArrayList<>();

        pnames = new ArrayList<>();

        quantities = new ArrayList<>();

        sizes = new ArrayList<>();

        uris = new ArrayList<>();

        pids = new ArrayList<>();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.succes_dialog);

        linearLayout = findViewById(R.id.viewAll);
        cardView = findViewById(R.id.viewLess);

        subtotal = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);
        delivery = findViewById(R.id.delivery);
        total1 = findViewById(R.id.total2);
        items = findViewById(R.id.items);

        checkBox = findViewById(R.id.checkBox);

        recyclerView = findViewById(R.id.cartRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getUid();


        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){
                    User user1 = ds.getValue(User.class);
                    if (user1!=null && user.equals(user1.getUser_unique_id())){

                        name = user1.getUser_name();
                        phone = user1.getUser_cell();
                        address = user1.getUser_location();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onStart(){
        super.onStart();

        deliveryFee = 100;

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                prices.clear();
                prices.clear();
                uids.clear();
                pnames.clear();
                quantities.clear();
                sizes.clear();
                uris.clear();
                pids.clear();

                checkBox.setChecked(true);

                for (DataSnapshot ds:snapshot.getChildren()){

                    Cart cart = ds.getValue(Cart.class);

                    if (cart != null && user.equals(cart.getUid())){
                        carts.add(cart);
                        Double actualPrice = Double.parseDouble(cart.getPrice())*Integer.parseInt(cart.getQuantity());
                        prices.add(Double.toString(actualPrice));
                        uids.add(cart.getCart_id());
                        pnames.add(cart.getPname());
                        quantities.add(cart.getQuantity());
                        sizes.add(cart.getSize());
                        uris.add(cart.getUri());
                        pids.add(cart.getPid());

                        double sum = 0;

                        for (int i=0;i<prices.size();i++){

                            sum = sum+Double.parseDouble(prices.get(i));

                        }

                        subtotal.setText(Double.toString(sum));

                        delivery.setText("R "+Integer.toString(deliveryFee));

                        total.setText(Double.toString(deliveryFee+sum));

                        total1.setText(Double.toString(deliveryFee+sum));

                        items.setText("My Cart ("+Integer.toString(carts.size())+")");

                        double finalSum = sum;
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (checkBox.isChecked()){

                                    delivery.setText("R 100");

                                    total.setText(Double.toString(100+ finalSum));

                                    total1.setText(Double.toString(100+ finalSum));

                                }else if (!checkBox.isChecked()){

                                    delivery.setText("R 0.00");

                                    total.setText(Double.toString(finalSum));

                                    total1.setText(Double.toString(finalSum));

                                }

                            }
                        });
                    }

                }
                adapter = new CartAdapter(getApplicationContext(), carts);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext()
                        , recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                        Cart cart = carts.get(position);
                        String cart_uid = cart.getCart_id();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("Cart").child(cart_uid);

                        reference.removeValue();

                        double sum = 0;

                        prices.remove(uids.indexOf(cart_uid));

                        for (int i=0;i<prices.size();i++){

                            sum = sum+Double.parseDouble(prices.get(i));

                        }

                        subtotal.setText(Double.toString(sum));

                        delivery.setText("R "+Integer.toString(deliveryFee));

                        total.setText(Double.toString(deliveryFee+sum));

                        total1.setText(Double.toString(deliveryFee+sum));

                        double finalSum = sum;
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (checkBox.isChecked()){

                                    delivery.setText("R 100");

                                    total.setText(Double.toString(100+ finalSum));

                                    total1.setText(Double.toString(100+ finalSum));

                                }else if (!checkBox.isChecked()){

                                    delivery.setText("R 0.00");

                                    total.setText(Double.toString(finalSum));

                                    total1.setText(Double.toString(finalSum));

                                }

                            }
                        });
                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkoutOnClick(View view) {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
        String uid = ref.push().getKey();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Ordered Items");

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Cart");

        Order order = new Order(name,phone,address,"","",saveCurrentDate,saveCurrentTime
                ,total1.getText().toString(),uid,Integer.toString(prices.size()),user,""
                ,"",Integer.toString(deliveryFee),"incomplete","");

        ref.child(Objects.requireNonNull(uid)).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    for (int i=0; i<prices.size();i++){

                        Cart cart = new Cart(uids.get(i),pids.get(i),pnames.get(i),prices.get(i)
                                ,quantities.get(i),"",user,sizes.get(i),uris.get(i));

                        ref1.child(uids.get(i)).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                        ref2.child(uids.get(i)).removeValue();
                    }
                    Intent intent = new Intent(CartActivity.this,CheckoutActivity.class);
                    intent.putExtra("id",uid);
                    intent.putExtra("USER",user);
                    startActivity(intent);
                }
            }
        });
    }

    public void viewAllorLessOnClick(View view) {
        linearLayout.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
    }

    public void viewLessOnClick(View view) {
        linearLayout.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }
}
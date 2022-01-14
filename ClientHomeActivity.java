package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientHomeActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private List<Products> products;
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        ref = FirebaseDatabase.getInstance().getReference("Products");

        recyclerView = findViewById(R.id.productsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        editText = findViewById(R.id.cProductsSearchBox);

        products = new ArrayList<>();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
    }
    public void filter(String text){

        ArrayList<Products> filteredList = new ArrayList<>();

        for (Products item: products){

            if (item.getPname().toLowerCase().contains(text.toLowerCase())){

                filteredList.add(item);

            }else if (item.getCategory().toLowerCase().contains(text.toLowerCase())){

                filteredList.add(item);

            }
        }
        adapter.filterlist(filteredList);
    }

    public void onStart(){
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(DataSnapshot spst:snapshot.getChildren()){
                    Products product = spst.getValue(Products.class);
                    if (product != null){

                        products.add(product);

                    }
                }

                adapter = new ProductsAdapter(ClientHomeActivity.this, products);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ClientHomeActivity.this, recyclerView
                        , new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Products product = products.get(position);
                        Intent intent = new Intent(ClientHomeActivity.this,ProductFullView.class);
                        intent.putExtra("PROID",product.getPid());
                        startActivity(intent);

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

    public void enableOnClick(View view) {
        editText.setEnabled(true);
    }

    public void cartOnClick(View view) {
        startActivity(new Intent(ClientHomeActivity.this,CartActivity.class));
    }
}
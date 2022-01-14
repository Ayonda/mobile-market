package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminProductsActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private List<Products> products;
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private Dialog dialog;
    private EditText editText;
    private Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        ref = FirebaseDatabase.getInstance().getReference("Products");

        recyclerView = findViewById(R.id.products_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        products = new ArrayList<>();

        dialog = new Dialog(this);

        dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.comfirm_dialog);

        dialog.setContentView(R.layout.update_popup);
        editText = findViewById(R.id.aProductsSearchBox);

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

                adapter = new ProductsAdapter(AdminProductsActivity.this, products);
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(AdminProductsActivity.this, recyclerView
                        , new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Products product = products.get(position);

                        TextView pname;
                        EditText qnty,size,price;
                        ImageView imageView;
                        Button update;

                        pname = dialog.findViewById(R.id.product_nameTv);
                        qnty = dialog.findViewById(R.id.product_quantityEt);
                        size = dialog.findViewById(R.id.product_sizeEt);
                        price = dialog.findViewById(R.id.product_priceEt);
                        imageView = dialog.findViewById(R.id.product_ImageV);
                        update = dialog.findViewById(R.id.update);

                        pname.setText(product.getPname());
                        qnty.setText(product.getQuantity());
                        size.setText(product.getSize());
                        price.setText(product.getPrice());
                        Picasso.get().load(product.getImage()).fit().into(imageView);

                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (qnty.getText().toString().isEmpty()
                                        && size.getText().toString().isEmpty()
                                        && price.getText().toString().isEmpty()){

                                    Toast.makeText(AdminProductsActivity.this,
                                            "Provide the missing information!",Toast.LENGTH_SHORT).show();

                                }else {

                                    DatabaseReference ref = FirebaseDatabase.getInstance()
                                            .getReference("Products").child(product.getPid());

                                    ref.child("quantity").setValue(qnty.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                ref.child("size").setValue(size.getText().toString());
                                                ref.child("price").setValue(price.getText().toString());
                                                Toast.makeText(AdminProductsActivity.this,"Product updated"
                                                        ,Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                            }
                                        }
                                    });
                                }

                            }
                        });
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Button yes,no;
                        yes = dialog1.findViewById(R.id.yes);
                        no = dialog1.findViewById(R.id.no);

                        Products product = products.get(position);

                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ref.child(product.getPid()).removeValue();
                                dialog1.dismiss();
                                Toast.makeText(getApplicationContext(),"Item deleted",Toast.LENGTH_SHORT)
                                        .show();

                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog1.dismiss();

                            }
                        });

                        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();

                    }
                }));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void enablesOnClick(View view) {
        editText.setEnabled(true);
    }
}
package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProductFullView extends AppCompatActivity {
    private String pro_uid,amount;
    private TextView nameTv,sizeTv,priceTv,qtyTv,totalTv,desTv;
    private ImageView productImage;
    private DatabaseReference reference,ref;
    private String pid,pname,price,quantity,discount,uid,size,uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_full_view);

        pro_uid = Objects.requireNonNull(getIntent().getExtras()).getString("PROID");

        reference = FirebaseDatabase.getInstance().getReference("Products");

        ref = FirebaseDatabase.getInstance().getReference("Cart");

        nameTv = findViewById(R.id.p_name);
        sizeTv = findViewById(R.id.p_size);
        desTv = findViewById(R.id.p_description);
        priceTv = findViewById(R.id.p_price);
        qtyTv = findViewById(R.id.p_quantity);
        totalTv = findViewById(R.id.total);
        productImage = findViewById(R.id.p_image);
    }
    public void onStart(){
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Products product = ds.getValue(Products.class);
                    if (product != null && pro_uid.equals(product.getPid())){

                        nameTv.setText(product.getPname());
                        sizeTv.setText(product.getSize());
                        desTv.setText(product.getDescription());
                        priceTv.setText(product.getPrice());
                        qtyTv.setText("1");
                        totalTv.setText(product.getPrice());
                        Picasso.get().load(product.getImage()).fit().into(productImage);
                        amount = product.getPrice();

                        pid = pro_uid;
                        pname = product.getPname();
                        size = product.getSize();
                        uri = product.getImage();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void arrowBackOnClick(View view) {
        startActivity(new Intent(ProductFullView.this,ClientTabs.class));
    }

    public void heartOnClick(View view) {
    }

    @SuppressLint("SetTextI18n")
    public void addOnClick(View view) {
        int qnty = Integer.parseInt(qtyTv.getText().toString());
        qtyTv.setText(Integer.toString(qnty+1));
        int qnt = Integer.parseInt(qtyTv.getText().toString());
        int price = Integer.parseInt(amount)*qnt;
        totalTv.setText(Integer.toString(price));
    }

    @SuppressLint("SetTextI18n")
    public void subtractOnClick(View view) {
        int qnty = Integer.parseInt(qtyTv.getText().toString());

        if (qnty != 1){

            qtyTv.setText(Integer.toString(qnty-1));
            int qnt = Integer.parseInt(qtyTv.getText().toString());
            int price = Integer.parseInt(amount)*qnt;
            totalTv.setText(Integer.toString(price));
        }
    }

    public void addToCartOnClick(View view) {

        String key = ref.push().getKey();

        Cart cart = new Cart(key,pro_uid,pname,amount,qtyTv.getText().toString()
                ,"",FirebaseAuth.getInstance().getUid(),size,uri);

        ref.child(Objects.requireNonNull(key)).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ProductFullView.this,"Product added to cart",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
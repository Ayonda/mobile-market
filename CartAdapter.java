package com.example.mobilemarket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mContext;
    private List<Cart> mPosts;

    public CartAdapter(Context context, List<Cart> posts){
        mContext = context;
        mPosts = posts;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private TextView name,size,price,qnty,sub,add;
        private ImageView imageView;

        public CartViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productName);
            size = itemView.findViewById(R.id.productSize);
            price = itemView.findViewById(R.id.productPrice);
            imageView = itemView.findViewById(R.id.productIv);
            qnty = itemView.findViewById(R.id.productQuantity);
            add = itemView.findViewById(R.id.addbut);
            sub = itemView.findViewById(R.id.subtract);
        }
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.cart_item,parent,false);
        return new CartAdapter.CartViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Cart products = mPosts.get(position);
        holder.name.setText(products.getPname());
        holder.size.setText("R"+products.getPrice()+"/"+products.getSize());
        holder.qnty.setText(products.getQuantity());
        Picasso.get().load(products.getUri()).fit().into(holder.imageView);

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Cart")
                .child(products.getCart_id()).child("quantity");

        int qnty = Integer.parseInt(products.getQuantity());
        double prize = Double.parseDouble(products.getPrice());
        double amount = qnty*prize;
        holder.price.setText(Double.toString(amount));

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int total = qnty + 1;
                reference1.setValue(Integer.toString(total));

            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qnty != 1){

                    int total = qnty - 1;
                    reference1.setValue(Integer.toString(total));

                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}

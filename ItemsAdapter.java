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

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private Context mContext;
    private List<Cart> mPosts;

    public ItemsAdapter(Context context, List<Cart> posts){
        mContext = context;
        mPosts = posts;
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder{
        private TextView name,size,price,qnty;
        private ImageView imageView;

        public ItemsViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_Name);
            size = itemView.findViewById(R.id.product_Size);
            price = itemView.findViewById(R.id.product_Price);
            imageView = itemView.findViewById(R.id.product_Iv);
            qnty = itemView.findViewById(R.id.product_Quantity);

        }
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.items,parent,false);
        return new ItemsAdapter.ItemsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemsViewHolder holder, int position) {
        Cart products = mPosts.get(position);
        holder.name.setText(products.getPname());
        holder.size.setText("R"+products.getPrice()+"/"+products.getSize());
        holder.qnty.setText(products.getQuantity());
        Picasso.get().load(products.getUri()).fit().into(holder.imageView);
        holder.price.setText(products.getPrice());
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}

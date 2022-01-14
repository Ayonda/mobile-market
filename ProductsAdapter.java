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

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context mContext;
    private List<Products> mPosts;

    public ProductsAdapter(Context context, List<Products> posts){
        mContext = context;
        mPosts = posts;
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder{
        private TextView name,size,price;
        private ImageView imageView;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            size = itemView.findViewById(R.id.product_size);
            price = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_iv);
        }
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.product_item,parent,false);
        return new ProductsAdapter.ProductsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductsViewHolder holder, int position) {
        Products products = mPosts.get(position);
        holder.name.setText(products.getPname());
        holder.size.setText(products.getSize());
        holder.price.setText(products.getPrice());
        Picasso.get().load(products.getImage()).fit().into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void filterlist(ArrayList<Products> filteredList){
        mPosts = filteredList;
        notifyDataSetChanged();
    }
}
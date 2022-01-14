package com.example.mobilemarket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context mContext;
    private List<Order> mPosts;

    public OrderAdapter(Context context, List<Order> posts){
        mContext = context;
        mPosts = posts;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView id,date,time,items,status,payment,amount,mode,delivery,circle2,circle3,circle4;
        private Button options,details;
        private LinearLayout layout2,layout3,layout4;

        public OrderViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.o_id);
            date = itemView.findViewById(R.id.o_date);
            time = itemView.findViewById(R.id.o_time);
            items = itemView.findViewById(R.id.o_items);
            payment = itemView.findViewById(R.id.o_payment);
            amount = itemView.findViewById(R.id.o_payment_amount);
            delivery = itemView.findViewById(R.id.o_delivery);
            mode = itemView.findViewById(R.id.o_mode);
            status = itemView.findViewById(R.id.o_status);
            options = itemView.findViewById(R.id.options);
            details = itemView.findViewById(R.id.details);
            circle2 = itemView.findViewById(R.id.circle2);
            circle3 = itemView.findViewById(R.id.circle3);
            circle4 = itemView.findViewById(R.id.circle4);
        }
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.order_item,parent,false);
        return new OrderAdapter.OrderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order products = mPosts.get(position);

        holder.id.setText(products.getUid());
        holder.date.setText(products.getDate());
        holder.time.setText(products.getTime());
        holder.items.setText(products.getItems());
        holder.payment.setText(products.getPayment());
        holder.amount.setText(products.getTotalAmount());
        holder.delivery.setText(products.getDelivery());
        holder.mode.setText(products.getMode());
        holder.status.setText(products.getStatus());

        if (products.getStatus().equals("Pending")){

            holder.options.setText("cancel order");

        }else if (products.getStatus().equals("Canceled")){

            holder.options.setText("Reorder");

        }

        if (products.getStatus().equals("Confirmed")){

            holder.circle2.setVisibility(View.VISIBLE);

        }

        if (products.getStatus().equals("Ready for pickup")){

            holder.circle2.setVisibility(View.VISIBLE);
            holder.circle3.setVisibility(View.VISIBLE);

        }

        if (products.getStatus().equals("Delivered")){

            holder.circle2.setVisibility(View.VISIBLE);
            holder.circle3.setVisibility(View.VISIBLE);
            holder.circle4.setVisibility(View.VISIBLE);

        }

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.options.getText().toString().equals("cancel order")
                        && products.getStatus().equals("Pending")){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders")
                            .child(products.getUid()).child("status");
                    ref.setValue("Canceled");

                }else if (holder.options.getText().toString().equals("Reorder")){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders")
                            .child(products.getUid()).child("status");
                    ref.setValue("Pending");

                }

            }
        });

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,OrderDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("OID",products.getUid());
                mContext.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}

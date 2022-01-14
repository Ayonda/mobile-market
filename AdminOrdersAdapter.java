package com.example.mobilemarket;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.AdminOrdersViewHolder> {
    private Context mContext;
    private List<Order> mPosts;
    private String uri;

    public AdminOrdersAdapter(Context context, List<Order> posts){
        mContext = context;
        mPosts = posts;
    }

    public class AdminOrdersViewHolder extends RecyclerView.ViewHolder{
        private TextView id,date,time,items,status,payment,amount,mode,delivery,circle2,circle3,circle4,cancel;
        private Button options,details,proof,delivered;
        private Dialog dialog;
        private ImageView imageView;
        private CardView cardView1,cardView2;
        private LinearLayout layout2,layout3,layout4;

        public AdminOrdersViewHolder(View itemView) {
            super(itemView);

            cancel = itemView.findViewById(R.id.close);
            imageView = itemView.findViewById(R.id.proofIm);

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
            proof = itemView.findViewById(R.id.file);
            cardView1 = itemView.findViewById(R.id.card1);
            cardView2 = itemView.findViewById(R.id.card2);
            delivered = itemView.findViewById(R.id.dilivered);

            layout2 = itemView.findViewById(R.id.circle2L);
            layout3 = itemView.findViewById(R.id.circle3L);
            layout4 = itemView.findViewById(R.id.circle4L);
        }
    }

    @NonNull
    @Override
    public AdminOrdersAdapter.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.order_item,parent,false);
        return new AdminOrdersAdapter.AdminOrdersViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminOrdersAdapter.AdminOrdersViewHolder holder, int position) {
        Order products = mPosts.get(position);
        String o_uid = products.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Proofs");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){

                    Payments payment = ds.getValue(Payments.class);

                    if (payment != null && o_uid.equals(payment.getO_uid())){

                        uri = payment.getP_uri();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.id.setText(products.getUid());
        holder.date.setText(products.getDate());
        holder.time.setText(products.getTime());
        holder.items.setText(products.getItems());
        holder.payment.setText(products.getPayment());
        holder.amount.setText(products.getTotalAmount());
        holder.delivery.setText(products.getDelivery());
        holder.mode.setText(products.getMode());
        holder.status.setText(products.getStatus());

        if (!products.getPayment().equals("Made")){
            holder.delivered.setVisibility(View.VISIBLE);
        }

        if (products.getStatus().equals("Pending")){

            holder.options.setVisibility(View.GONE);

        }

        if (products.getStatus().equals("Confirmed")){

            holder.options.setVisibility(View.GONE);

            holder.circle2.setVisibility(View.VISIBLE);

        }

        if (products.getStatus().equals("Ready for pickup")){

            holder.options.setVisibility(View.GONE);
            holder.circle2.setVisibility(View.VISIBLE);
            holder.circle3.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.VISIBLE);
            holder.options.setVisibility(View.GONE);

        }

        if (products.getStatus().equals("Delivered")){

            holder.options.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.GONE);
            holder.circle2.setVisibility(View.VISIBLE);
            holder.circle3.setVisibility(View.VISIBLE);
            holder.circle4.setVisibility(View.VISIBLE);
            holder.options.setText("Remove");

        }

        if (products.getPayment().equals("Made")){

            holder.proof.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.GONE);

        }

        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(products.getUid()).child("payment");

                ref1.setValue("Made");

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.cardView1.setVisibility(View.GONE);
                holder.cardView2.setVisibility(View.VISIBLE);

            }
        });

        holder.proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!products.getMode().equals("COD") || !products.getMode().equals("COP")){

                    holder.cardView2.setVisibility(View.GONE);
                    holder.cardView1.setVisibility(View.VISIBLE);

                    Picasso.get().load(uri).fit().into(holder.imageView);

                }

            }
        });

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(products.getUid());
                ref2.removeValue();

            }
        });

        holder.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(products.getUid()).child("status");

                ref1.setValue("Confirmed");

            }
        });

        holder.layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(products.getUid()).child("status");

                ref1.setValue("Ready for pickup");

            }
        });

        holder.layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (products.getMode().equals("COM") && !products.getPayment().equals("Made")){

                }else if (products.getMode().equals("COP") && !products.getPayment().equals("Made")){

                }else {

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Orders")
                            .child(products.getUid()).child("status");

                    ref1.setValue("Delivered");

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

package com.example.mobilemarket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context mContext;
    private List<Schedule> mPosts;
    private String uid,name,gender,cell,id;
    private DatabaseReference reference;

    public BookingAdapter(Context context, List<Schedule> posts){
        mContext = context;
        mPosts = posts;
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder{
        private TextView pname,pagender,padate,patime,pacell;

        public BookingViewHolder(View itemView) {
            super(itemView);

            pname = itemView.findViewById(R.id.paName);
            pagender = itemView.findViewById(R.id.paGender);
            padate = itemView.findViewById(R.id.paDate);
            patime = itemView.findViewById(R.id.paTime);
            pacell = itemView.findViewById(R.id.paCell);

        }
    }

    @NonNull
    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.booking_item,parent,false);
        return new BookingAdapter.BookingViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {
        Schedule schedule = mPosts.get(position);

        holder.patime.setText("Time: "+schedule.getSchedule_time());
        holder.padate.setText("Date: "+schedule.getSchedule_date());
        holder.pname.setText("Name: "+schedule.getPatient_name());
        holder.pagender.setText("Gender: "+schedule.getPatient_gender());
        holder.pacell.setText("Cell no: "+schedule.getPatient_cell());
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}


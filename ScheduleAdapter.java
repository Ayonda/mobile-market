package com.example.mobilemarket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.mobilemarket.BookConsultation.dateString;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private Context mContext;
    private List<Schedule> mPosts;

    public ScheduleAdapter(Context context, List<Schedule> posts){
        mContext = context;
        mPosts = posts;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        private TextView time1,time2;
        private LinearLayout l1,l2;

        public ScheduleViewHolder(View itemView) {
            super(itemView);

            time1 = itemView.findViewById(R.id.timeTvblue);
            time2 = itemView.findViewById(R.id.timeTvred);
            l1 = itemView.findViewById(R.id.blueLay);
            l2 = itemView.findViewById(R.id.redLay);

        }
    }

    @NonNull
    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.time_item,parent,false);
        return new ScheduleAdapter.ScheduleViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position) {
        Schedule schedule = mPosts.get(position);

        if (schedule.getSchedule_status().equals("Accepted") || schedule.getSchedule_status().equals("Pending")){

            holder.l1.setVisibility(View.GONE);
            holder.l2.setVisibility(View.VISIBLE);

        }else {

            holder.l1.setVisibility(View.VISIBLE);
            holder.l2.setVisibility(View.GONE);

        }

        holder.time1.setText(schedule.getSchedule_time());
        holder.time2.setText(schedule.getSchedule_time());
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}

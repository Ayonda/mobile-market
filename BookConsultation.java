package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class BookConsultation extends AppCompatActivity {
    private Calendar calendar;
    private ScheduleAdapter adapter;
    private List<Schedule> schedules;
    private RecyclerView recyclerView;
    private TextView dateField;
    private int year, month, day;
    public static String dateString,UID;
    private DatabaseReference reference,reference1;
    public static String status,paname,pacell,paid;
    private TextView textView;
    private String[] times;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_consultation);
        dateField = findViewById(R.id.datefield);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        textView = findViewById(R.id.tex);

        list = new ArrayList<>();

        UID = FirebaseAuth.getInstance().getUid();

        times = new String[]{"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"
                , "14:00-15:00", "15:00-16:00","16:00-17:00"};

        reference = FirebaseDatabase.getInstance().getReference("Consultations");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Schedule schedule = ds.getValue(Schedule.class);
                    if (schedule != null && UID.equals(schedule.getSchedule_user())){
                        textView.setText("Last booking"+"\n"+"Date: "+schedule.getSchedule_date()+"\n"+"Time: "
                                +schedule.getSchedule_time()+"\n"+"Status: "+schedule.getSchedule_status());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    Schedule schedule = ds.getValue(Schedule.class);
                    if (schedule != null && UID.equals(schedule.getSchedule_user()) &&
                            schedule.getSchedule_status().equals("Pending")){

                        list.add(schedule.getSchedule_time());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference1 = FirebaseDatabase.getInstance().getReference("Users");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){

                    User user = ds.getValue(User.class);
                    if (user != null && UID.equals(user.getUser_unique_id())){
                        paname = user.getUser_name();
                        pacell = user.getUser_cell();
                        paid = user.getUser_id();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        schedules = new ArrayList<>();
        recyclerView = findViewById(R.id.schedule_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));/*

        Schedule schedule = new Schedule();
        schedule = new Schedule("8:00-9:00",""
                ,"","","",""
                ,"","","");
        schedules.add(schedule);
        schedule = new Schedule("9:00-10:00",""
                ,"","",""
                ,"","","","");
        schedules.add(schedule);
        schedule = new Schedule("10:00-11:00",""
                ,"","","","","","","");
        schedules.add(schedule);
        schedule = new Schedule("11:00-12:00",""
                ,"","","","","","","");
        schedules.add(schedule);
        schedule = new Schedule("13:00-14:00",""
                ,"","","","","","","");
        schedules.add(schedule);
        schedule = new Schedule("15:00-16:00",""
                ,"","","","","","","");
        schedules.add(schedule);
        schedule = new Schedule("16:00-17:00",""
                ,"","","","","","","");
        schedules.add(schedule);*/
    }

    public void DatePickerOnClick(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id==999){
            return new DatePickerDialog(this, myDateListener,year,month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            showDate(i,i1+1,i2);
        }
    };

    private void showDate(int year, int month, int day){
        dateField.setText(Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year));
        dateString = Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
    }

    public void AddScheduleOnClick(View view) {

        if (dateString.isEmpty()){

            Toast.makeText(getApplicationContext(),"Select date!",Toast.LENGTH_SHORT).show();

        }else {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    schedules.clear();

                    for (DataSnapshot ds:snapshot.getChildren()) {
                        Schedule schedule = ds.getValue(Schedule.class);

                        if (schedule != null && schedule.getSchedule_date().equals(dateString)){

                            schedules.add(schedule);

                            for (int i=0;i<times.length;i++){
                                if (!schedule.getSchedule_time().equals(times[i])){

                                    Schedule schedule1 = new Schedule(times[i],"",""
                                            ,"","","","","","");
                                    schedules.add(schedule1);

                                }
                            }
                        }
                    }

                    if (schedules.size()==0){

                        for (int i=0;i<times.length;i++){

                            Schedule schedule1 = new Schedule(times[i],"",""
                                    ,"","","","","","");
                            schedules.add(schedule1);

                        }

                    }

                    adapter = new ScheduleAdapter(getApplicationContext(),schedules);
                    recyclerView.setAdapter(adapter);

                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView
                            , new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                            Schedule schedule = schedules.get(position);

                            if (list.size()>0){

                                Toast.makeText(getApplicationContext(),"You already have a pending booking",Toast.LENGTH_SHORT).show();

                            }else if (schedule.getSchedule_status().equals("Accepted") || schedule.getSchedule_status().equals("Pending")){

                                Toast.makeText(getApplicationContext(),"The slot has been already been booked!",Toast.LENGTH_SHORT).show();

                            }else if (!schedule.getSchedule_status().equals("Accepted") || !schedule.getSchedule_status().equals("Pending")){

                                String key = reference.push().getKey();

                                Schedule schedule1 = new Schedule(schedule.getSchedule_time(),dateString,UID,key,"Pending"
                                        ,paname,getPatientGender(paid),paid,pacell);

                                reference.child(Objects.requireNonNull(key)).setValue(schedule1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Toast.makeText(getApplicationContext(),"You have successful booked!",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }

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
    }
    public String getPatientGender(String idNo){
        int num = Integer.parseInt(idNo.substring(6,10));
        String gender;

        if (num>4999){

            gender = "Male";

        }else {

            gender = "Female";

        }
        return gender;
    }
}
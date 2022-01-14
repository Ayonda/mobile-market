package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSettings extends AppCompatActivity {
    private EditText name,email,cell,location;
    private DatabaseReference reference;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        name = findViewById(R.id.aaName);
        email = findViewById(R.id.aEmail);
        cell = findViewById(R.id.aCell);
        location = findViewById(R.id.aLocation);

        reference = FirebaseDatabase.getInstance().getReference("Admins");

        uid = FirebaseAuth.getInstance().getUid();

    }
    public void onStart(){
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){

                    Admin user = ds.getValue(Admin.class);

                    if(user != null && uid.equals(user.getAdmin_uid())){

                        name.setText(user.getAdmin_name());
                        email.setText(user.getAdmin_email());
                        cell.setText(user.getAdmin_cell());
                        location.setText(user.getAdmin_location());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateIOnClick(View view) {

            if (name.getText().toString().isEmpty()
                    || email.getText().toString().isEmpty() || cell.getText().toString().isEmpty()
                    || location.getText().toString().isEmpty()){

                Toast.makeText(AdminSettings.this,"No empty field is required!"
                        ,Toast.LENGTH_SHORT).show();

            }else {

                reference.child(uid).child("user_name").setValue(name.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                reference.child(uid).child("user_email").setValue(email.getText().toString());
                                reference.child(uid).child("user_cell").setValue(cell.getText().toString());
                                reference.child(uid).child("user_location").setValue(location.getText().toString());
                                Toast.makeText(AdminSettings.this,"Chenges saved!",Toast.LENGTH_SHORT).show();

                            }
                        });
            }

    }

    public void logoutonClick(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AdminSettings.this,Login.class));
    }
}
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

public class ClientSettingsActivity extends AppCompatActivity {
    private EditText name,id,email,cell,location;
    private DatabaseReference reference;
    private String uid,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_settings);

        name = findViewById(R.id.uName);
        id = findViewById(R.id.uId);
        email = findViewById(R.id.uEmail);
        cell = findViewById(R.id.uCell);
        location = findViewById(R.id.uLocation);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        uid = FirebaseAuth.getInstance().getUid();

    }
    public void onStart(){
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){

                    User user = ds.getValue(User.class);

                    if(user != null && uid.equals(user.getUser_unique_id())){

                        name.setText(user.getUser_name());
                        id.setText(user.getUser_id());
                        email.setText(user.getUser_email());
                        cell.setText(user.getUser_cell());
                        location.setText(user.getUser_location());
                        password = user.getUser_password();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateOnClick(View view) {

        if (name.getText().toString().isEmpty() || id.getText().toString().isEmpty()
                || email.getText().toString().isEmpty() || cell.getText().toString().isEmpty()
                || location.getText().toString().isEmpty()){

            Toast.makeText(ClientSettingsActivity.this,"No empty field is required!"
                    ,Toast.LENGTH_SHORT).show();

        }else {

            reference.child(uid).child("user_name").setValue(name.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    reference.child(uid).child("user_id").setValue(id.getText().toString());
                    reference.child(uid).child("user_email").setValue(email.getText().toString());
                    reference.child(uid).child("user_cell").setValue(cell.getText().toString());
                    reference.child(uid).child("user_location").setValue(location.getText().toString());
                    Toast.makeText(ClientSettingsActivity.this,"Chenges saved!",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void logoutOnClick(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ClientSettingsActivity.this,Login.class));
    }
}
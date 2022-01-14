package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText email,password;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private DatabaseReference reference;
    private String passwordA,emailA;
    private Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.signInEmail);
        password = findViewById(R.id.signInPassword);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

        dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.password_reset);

        reference = FirebaseDatabase.getInstance().getReference("Admins");

        auth = FirebaseAuth.getInstance();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds:snapshot.getChildren()){

                    Admin admin = ds.getValue(Admin.class);

                    if (admin != null){

                        emailA = admin.getAdmin_email();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loginButtonOnClick(View view) {

        dialog.show();

        String EMAIL = email.getText().toString();
        String PASSWORD = password.getText().toString();

        if (TextUtils.isEmpty(EMAIL) && TextUtils.isEmpty(PASSWORD)){

            dialog.dismiss();

            email.setError("Enter email address!");

            password.setError("Enter password!");

        }else if (TextUtils.isEmpty(EMAIL)){

            dialog.dismiss();

            email.setError("Enter email address!");

        }else if (TextUtils.isEmpty(PASSWORD)){

            dialog.dismiss();

            password.setError("Enter password!");

        }else if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){

            dialog.dismiss();

            email.setError("Invalid Email Address!");

        }else if (PASSWORD.length()<4){

            dialog.dismiss();

            password.setError("Enter valid password!");

        }else {

            auth.signInWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        dialog.dismiss();

                        if (emailA.equals(EMAIL)){

                            startActivity(new Intent(Login.this,AdminTabs.class));

                        }else {

                            Intent intent = new Intent(Login.this,ClientTabs.class);
                            intent.putExtra("Email",EMAIL);
                            startActivity(intent);
                            finish();

                        }

                    }else {

                        dialog.dismiss();

                        Toast.makeText(Login.this,"Incorrect email or password!",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    public void signupTextOnClick(View view) {
        startActivity(new Intent(Login.this,RegisterActivity.class));
    }

    public void resetOnClick(View view) {

        Button button;
        EditText editText;
        button = dialog1.findViewById(R.id.but);
        editText = dialog1.findViewById(R.id.email);

        if (editText.getText().toString().isEmpty()){

            Toast.makeText(getApplicationContext(),"Enter email!",Toast.LENGTH_SHORT).show();

        }else if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()){

            Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();

        }else {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseAuth.getInstance().sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(),"Reset link sent!",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
    }
}
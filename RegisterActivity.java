package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText name,id,email,password,location,cell,cpassword;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

        name = findViewById(R.id.signUpName);
        id = findViewById(R.id.signUpId);
        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        location = findViewById(R.id.signUpLocation);
        cell = findViewById(R.id.signUpCell);
        cpassword = findViewById(R.id.signUpConfirmPassword);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        auth = FirebaseAuth.getInstance();
    }
    public void signupButOnClick(View view) {
        dialog.show();
        String NAME = name.getText().toString();
        String ID = id.getText().toString();
        String EMAIL = email.getText().toString();
        String CELL = cell.getText().toString();
        String LOCATION = location.getText().toString();
        String PASSWORD = password.getText().toString();
        String CPASSWORD = cpassword.getText().toString();

        if (TextUtils.isEmpty(EMAIL) && TextUtils.isEmpty(PASSWORD) && TextUtils.isEmpty(ID) && TextUtils.isEmpty(NAME)
                && TextUtils.isEmpty(LOCATION) && TextUtils.isEmpty(CELL) && TextUtils.isEmpty(CPASSWORD)){
            name.setError("Enter name!");
            id.setError("Enter ID!");
            email.setError("Enter email address!");
            cell.setError("Enter cell!");
            location.setError("Enter location!");
            password.setError("Enter password!");
            cpassword.setError("Confirm password!");
            dialog.dismiss();

        }else if (TextUtils.isEmpty(NAME)){ name.setError("Enter name!");dialog.dismiss();
        }else if (TextUtils.isEmpty(ID)){ id.setError("Enter ID!");dialog.dismiss();
        }else if (TextUtils.isEmpty(EMAIL)){ email.setError("Enter email address!");dialog.dismiss();
        }else if (TextUtils.isEmpty(CELL)){ cell.setError("Enter cell!");dialog.dismiss();
        }else if (TextUtils.isEmpty(LOCATION)){ location.setError("Enter location!");dialog.dismiss();
        }else if (TextUtils.isEmpty(PASSWORD)){ password.setError("Enter password!");dialog.dismiss();
        }else if (TextUtils.isEmpty(CPASSWORD)){ cpassword.setError("Confirm password!");dialog.dismiss();
        }else if (ID.length()<13){ id.setError("ID length should be 13!");dialog.dismiss();
        }else if (TextUtils.isEmpty(CPASSWORD)){ cpassword.setError("Confirm password!");dialog.dismiss();
        }else if (validateID(id.getText().toString()).equals("invalid")){ id.setError("Invalid id number!");dialog.dismiss();
        }else if (!Patterns.PHONE.matcher(CELL).matches()){ cell.setError("Invalid cell no!");dialog.dismiss();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){ email.setError("Invalid id email!");dialog.dismiss();
        }else if (PASSWORD.length()<4){ password.setError("Password should contain at least 4 characters!");dialog.dismiss();
        }else if (!PASSWORD.equals(CPASSWORD)){ cpassword.setError("Passwords do not match!");dialog.dismiss();
        }else {

            auth.createUserWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        User user = new User(NAME,ID,EMAIL,CELL,LOCATION,PASSWORD,auth.getUid(),"Online","Online");

                        reference.child(auth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Intent intent = new Intent(RegisterActivity.this,ClientTabs.class);
                                    intent.putExtra("Email",EMAIL);
                                    startActivity(intent);

                                    dialog.dismiss();

                                    Toast.makeText(RegisterActivity.this,"Account created successfully",Toast.LENGTH_SHORT).show();

                                    finish();

                                }else {

                                    dialog.dismiss();

                                    Toast.makeText(RegisterActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }else {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Failed to create account",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public String validateID(String idNumber){
        String validity;

        if (Integer.parseInt(idNumber.substring(2,4)) == 0 && Integer.parseInt(idNumber.substring(4,6)) == 0
                && Integer.parseInt(idNumber.substring(9,10)) == 0){

            validity = "invalid";

        }else if (!(Integer.parseInt(idNumber.substring(2,4))>0 && Integer.parseInt(idNumber.substring(2,4))<13)){

            validity = "invalid";

        }else if (!(Integer.parseInt(idNumber.substring(4,6))>0 && Integer.parseInt(idNumber.substring(4,6))<32)){

            validity = "invalid";

        }else if (!(Integer.parseInt(idNumber.substring(10,11))<2)){

            validity = "invalid";

        }else if (!(Integer.parseInt(idNumber.substring(11,13))>0)){

            validity = "invalid";

        }else {

            validity = "valid";

        }
        return validity;
    }

    public void signinTextOnClick(View view) {
        startActivity(new Intent(RegisterActivity.this,Login.class));
    }

}
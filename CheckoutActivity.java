package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

public class CheckoutActivity extends AppCompatActivity {
    private EditText name,email,cell,address,note,state,city;
    private CheckBox cod,cop,mt;
    private String auth,ouid,method;
    private DatabaseReference ref;
    private Dialog dialog;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 234;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Dialog dialog1,dialog2;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        name = findViewById(R.id.buyerName);
        email = findViewById(R.id.buyerEmail);
        cell = findViewById(R.id.buyerCell);
        address = findViewById(R.id.buyerAddress);
        note = findViewById(R.id.buyerNote);
        cod = findViewById(R.id.cod);
        cop = findViewById(R.id.cop);
        mt = findViewById(R.id.mt);
        state = findViewById(R.id.buyerState);
        city = findViewById(R.id.buyerCity);
        dialog = new Dialog(this);
        dialog1 = new Dialog(this);
        dialog.setContentView(R.layout.succes_dialog);
        dialog1.setContentView(R.layout.payment_dialog);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ouid = Objects.requireNonNull(getIntent().getExtras()).getString("id");
        auth = FirebaseAuth.getInstance().getUid();
        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.account_dialog);

    }

    public void onStart(){
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (user != null && auth.equals(user.getUser_unique_id())){

                        name.setText(user.getUser_name());
                        email.setText(user.getUser_email());
                        cell.setText(user.getUser_cell());
                        address.setText(user.getUser_location());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void confirmOnClick(View view) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(ouid);

        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(email.getText().toString())
                || TextUtils.isEmpty(cell.getText().toString()) || TextUtils.isEmpty(address.getText().toString())
                || TextUtils.isEmpty(city.getText().toString())|| TextUtils.isEmpty(state.getText().toString())){

            Toast.makeText(CheckoutActivity.this,"Provide the missing information!"
                    ,Toast.LENGTH_SHORT).show();

        }else {

            if (cod.isChecked()){

                reference.child("name").setValue(name.getText().toString()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    dialog.show();

                                    reference.child("email").setValue(email.getText().toString());
                                    reference.child("cell").setValue(cell.getText().toString());
                                    reference.child("address").setValue(address.getText().toString());
                                    reference.child("city").setValue(city.getText().toString());
                                    reference.child("state").setValue(state.getText().toString());
                                    reference.child("payment").setValue("Not made");
                                    reference.child("mode").setValue("COD");
                                    reference.child("status").setValue("Pending");
                                    if (!note.getText().toString().isEmpty()){
                                        reference.child("note").setValue(note.getText().toString());
                                    }
                                    startActivity(new Intent(CheckoutActivity.this,ClientTabs.class));

                                }
                            }
                        });

            }else if (cop.isChecked()){

                reference.child("name").setValue(name.getText().toString()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    dialog.show();

                                    reference.child("email").setValue(email.getText().toString());
                                    reference.child("cell").setValue(cell.getText().toString());
                                    reference.child("address").setValue(address.getText().toString());
                                    reference.child("city").setValue(city.getText().toString());
                                    reference.child("state").setValue(state.getText().toString());
                                    reference.child("payment").setValue("Not made");
                                    reference.child("mode").setValue("COP");
                                    reference.child("status").setValue("Pending");
                                    if (!note.getText().toString().isEmpty()){
                                        reference.child("note").setValue(note.getText().toString());
                                    }
                                    startActivity(new Intent(CheckoutActivity.this,ClientTabs.class));

                                }
                            }
                        });

            }else if (mt.isChecked()){

                imageView = dialog1.findViewById(R.id.proof);
                upload = dialog1.findViewById(R.id.upload);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                    }
                });

                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //displaying progress dialog while image is uploading
                        final ProgressDialog progressDialog = new ProgressDialog(CheckoutActivity.this);
                        progressDialog.setTitle("Uploading proof");
                        progressDialog.show();

                        //getting the storage reference
                        final StorageReference sRef = storageReference.child("uploads/"+System.currentTimeMillis() + "." + getFileExtension(filePath));

                        ref = FirebaseDatabase.getInstance().getReference("Proofs");

                        //adding the file to reference


                        if (filePath != null){
                            sRef.putFile(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //dismissing the progress dialog
                                            progressDialog.dismiss();

                                            //displaying success toast
                                            Toast.makeText(getApplicationContext(), "Proof submitted!", Toast.LENGTH_SHORT).show();

                                            //creating the upload object to store uploaded image details
                                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    String key = ref.push().getKey();

                                                    Payments payment = new Payments(uri.toString(),key,auth,ouid);

                                                    ref.child(Objects.requireNonNull(key)).setValue(payment);
                                                    reference.child("email").setValue(email.getText().toString());
                                                    reference.child("cell").setValue(cell.getText().toString());
                                                    reference.child("address").setValue(address.getText().toString());
                                                    reference.child("city").setValue(city.getText().toString());
                                                    reference.child("state").setValue(state.getText().toString());
                                                    reference.child("mode").setValue("MT");
                                                    reference.child("status").setValue("Pending");
                                                    reference.child("payment").setValue("Made");
                                                    if (!note.getText().toString().isEmpty()){
                                                        reference.child("note").setValue(note.getText().toString());
                                                    }
                                                    dialog1.dismiss();

                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            //displaying the upload progress
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                            progressDialog.setMessage("Uploaded" + ((int) progress) + "%...");
                                        }
                                    });

                        }else {

                            Toast.makeText(CheckoutActivity.this,"No file selected!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

            }else {

                Toast.makeText(CheckoutActivity.this,"Select the payment method",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public void onCheckboxClicked(View view) {

        if (cop.isChecked()){

            cod.setChecked(false);
            mt.setChecked(false);

        }else if (cod.isChecked()){

            cop.setChecked(false);
            mt.setChecked(false);

        }else if (mt.isChecked()){

            cod.setChecked(false);
            cop.setChecked(false);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void detailsOnClick(View view) {
        Objects.requireNonNull(dialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.show();
    }
}
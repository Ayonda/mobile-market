package com.example.mobilemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AdminAddProductActivity extends AppCompatActivity {
    private EditText name,size,price,qnty,desc;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 234;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        name = findViewById(R.id.pName);
        size = findViewById(R.id.pSize);
        price = findViewById(R.id.pPrice);
        qnty = findViewById(R.id.pQuantity);
        desc = findViewById(R.id.pdescription);
        imageView = findViewById(R.id.pImage);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference = FirebaseDatabase.getInstance().getReference("Products");

    }

    public void addProductOnClick(View view) {

        if (name.getText().toString().isEmpty() || size.getText().toString().isEmpty()
                || price.getText().toString().isEmpty()|| qnty.getText().toString().isEmpty()
                || desc.getText().toString().isEmpty() || filePath == null){

            Toast.makeText(getApplicationContext(),"Provide the missing information!"
                    ,Toast.LENGTH_SHORT).show();

        }else {

            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(AdminAddProductActivity.this);
            progressDialog.setTitle("Uploading product");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child("uploads/"+System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference

            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Product uploaded!", Toast.LENGTH_SHORT).show();

                            //creating the upload object to store uploaded image details
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Calendar calendar = Calendar.getInstance();
                                    String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))
                                            +"/"+String.valueOf(calendar.get(Calendar.MONTH)+1)
                                            +"/"+String.valueOf(calendar.get(Calendar.YEAR));

                                    String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))
                                            +":"+String.valueOf(calendar.get(Calendar.MINUTE));

                                    String key = reference.push().getKey();

                                    Products products = new Products(name.getText().toString()
                                            ,desc.getText().toString(),price.getText().toString()
                                            ,uri.toString(),"",key,date,time,size.getText().toString()
                                            ,qnty.getText().toString());

                                    reference.child(Objects.requireNonNull(key)).setValue(products);

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

    public void addPhotoOnClick(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
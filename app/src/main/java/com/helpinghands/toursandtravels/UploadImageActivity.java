package com.helpinghands.toursandtravels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class UploadImageActivity extends AppCompatActivity {
    //for imageView
    ImageView imageView;
    private Uri mImageHolder;
    public Bitmap imageBitmap;
    Button choose_btn, upload_btn;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        String id = getIntent().getStringExtra("docId");
        imageView = findViewById(R.id.imageView);
        choose_btn = findViewById(R.id.choose_btn);
        upload_btn = findViewById(R.id.upload_btn);
        progressDialog = new ProgressDialog(UploadImageActivity.this);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("Images/" + id);
        final long ONE_MEGABYTE = 1024 * 1024;
        progressDialog.show();
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
                progressDialog.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadImageActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("UploadImageActivity", e.getMessage());
                    }
                });


        choose_btn.setOnClickListener(v -> selectImage());

        upload_btn.setOnClickListener(v -> saveImage(id));

    }


    public void saveImage(String docId) {
        progressDialog.show();
        progressDialog.setMessage("Processing");

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference reference = mStorageReference.child("Images" + "/" + docId);
        reference.putFile(mImageHolder).addOnSuccessListener(taskSnapshot -> {

            Log.d("UploadImageActivity", "Image Set Successfully");
            progressDialog.dismiss();
            Toast.makeText(UploadImageActivity.this, "Successfully Uploaded Image", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(UploadImageActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageHolder = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageHolder);
                imageView.setImageBitmap(bitmap);
                imageBitmap = bitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            } catch (Exception e) {
                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
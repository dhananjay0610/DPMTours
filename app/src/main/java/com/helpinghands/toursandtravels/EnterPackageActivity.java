package com.helpinghands.toursandtravels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

public class EnterPackageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EnterPackageActivity";
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    //for imageView
    private ImageView imageView;
    private Uri mImageHolder;
    public Bitmap imageBitmap;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_package);
        getWindow().getAttributes().windowAnimations = R.style.Fade;


//        Objects.requireNonNull(getSupportActionBar()).setTitle("New Package");


        //dialog
        progressDialog = new ProgressDialog(EnterPackageActivity.this);

        //for drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        Button submitbutton = findViewById(R.id.edit_btn);
        final EditText money = findViewById(R.id.money);
        final EditText location = findViewById(R.id.location);
        final EditText duration = findViewById(R.id.duration);
        final EditText description = findViewById(R.id.description);
        final EditText name = findViewById(R.id.editText_Name);
        final EditText highlights = findViewById(R.id.editText_Highlight);
        final EditText most_famour_for = findViewById(R.id.editText_Famour);
        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setMessage("Processing");

                progressDialog.setCanceledOnTouchOutside(false);
                EditText editText_DailyEternary = findViewById(R.id.editText_DailyEternary);

                String moneyss = String.valueOf(money.getText());
                String duratoinss = String.valueOf(duration.getText());
                String locationss = String.valueOf(location.getText());
                String descriptionss = String.valueOf(description.getText());
                String namess = String.valueOf(name.getText());
                String highlightss = String.valueOf(highlights.getText());
                String most_famour_forss = String.valueOf(most_famour_for.getText());
                String editText_DailyEternar = String.valueOf(editText_DailyEternary.getText());
                String[] arr = editText_DailyEternar.split(",");

                if ((moneyss.equals("") || moneyss.isEmpty())) {
                    Toast.makeText(EnterPackageActivity.this, "Money can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((duratoinss.equals("") || duratoinss.isEmpty())) {
                    Toast.makeText(EnterPackageActivity.this, "Duration can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((locationss.equals("") || locationss.isEmpty())) {
                    Toast.makeText(EnterPackageActivity.this, "Location field can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((namess.equals("") || namess.isEmpty())) {
                    Toast.makeText(EnterPackageActivity.this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((highlightss.equals("") || highlightss.isEmpty())) {
                    Toast.makeText(EnterPackageActivity.this, "Location field can't be empty", Toast.LENGTH_SHORT).show();
                } else {


                    //adding geopoint

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    double lat = 0;
                    double lon = 0;
                    try {
                        List addressList = geocoder.getFromLocationName(locationss, 1);
                        Address address = (Address) addressList.get(0);
                        lon = address.getLongitude();
                        lat = address.getLatitude();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.d("abc", "-=-=-=-" + lon + "  " + lat);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> singlepackage = new HashMap<>();
                    singlepackage.put("money", moneyss);
                    singlepackage.put("duration", duratoinss);
                    singlepackage.put("location", locationss);
                    singlepackage.put("description", descriptionss);
                    singlepackage.put("name", namess);
                    singlepackage.put("highlight", highlightss);
                    singlepackage.put("famous_for", most_famour_forss);
                    singlepackage.put("longitude", lon);
                    singlepackage.put("latitude", lat);
                    singlepackage.put("array", Arrays.asList(arr));

                    db.collection("Package")
                            .add(singlepackage)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();

                                    //saving image
                                    saveImage(documentReference.getId());

                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(EnterPackageActivity.this, "Package created successfully", Toast.LENGTH_SHORT).show();
                                    money.setText("");
                                    location.setText("");
                                    duration.setText("");
                                    description.setText("");
                                    name.setText("");
                                    highlights.setText("");
                                    most_famour_for.setText("");
                                    duration.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error adding document", e);
                                    Toast.makeText(EnterPackageActivity.this, "Failed to create package" + e, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });
                }
            }
        });
    }

    public void saveImage(String docId) {

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference reference = mStorageReference.child("Images" + "/" + docId);
        reference.putFile(mImageHolder).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d("EnterPackageActivity", "Image Set Successfully");
                StorageMetadata uri = taskSnapshot.getMetadata();
                String i = uri.getPath();
                Log.d("abc", i);


                //add download url in the package
                addDownloadUrl(docId, i);


//                Toast.makeText(EnterPackageActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(EnterPackageActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDownloadUrl(String docId, String path) {

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        mStorageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("abc", "-=- " + uri.toString());

                //update firestore document
                updateDocument(docId, uri.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("abc", "Error : " + e.getMessage());
            }
        });
    }

    private void updateDocument(String docId, String toString) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> map = new HashMap<>();
        map.put("imageUrl", toString);

        db.collection("Package")
                .document(docId).
                set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("EnterPackageActivity", "added image url in " + docId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EnterPackageActivity", "Error while updating package for image url " + e.getMessage()
                );
            }
        });

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
                Toast.makeText(this, "222 " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        setNavigation();
    }


    private void setNavigation() {
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.getMenu().getItem(1).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(this);
        final TextView name = mNavigationView.getHeaderView(0).findViewById(R.id.profileName);
        final TextView email = mNavigationView.getHeaderView(0).findViewById(R.id.email_tv);
        name.setText("Admin");
        email.setText("admin@gmail.com");

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("PROA", "onNavigationItemSelected: " + id);
        if (id == R.id.nav_home) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(EnterPackageActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_newTeacher) {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(ContactUs.this, EnterPackageActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_ContactUs) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(EnterPackageActivity.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

            SharedPreferences sharedPref = getSharedPreferences("Shared_Pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(EnterPackageActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return false;
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String address;
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("Address");
                    break;
                default:
                    address = null;
            }
//            Toast.makeText(EnterPackageActivity.this, address, Toast.LENGTH_SHORT).show();
        }
    }
}
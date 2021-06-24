package com.helpinghands.toursandtravels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddDailyEternary extends AppCompatActivity {
    EditText editText_DailyEternary;
    Button submit_btn;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_eternary);

        editText_DailyEternary = findViewById(R.id.editText_DailyEternary);
        submit_btn = findViewById(R.id.submit_btn);

        //dialog
        progressDialog = new ProgressDialog(AddDailyEternary.this);
        submit_btn.setOnClickListener(v -> {

            progressDialog.show();
            progressDialog.setMessage("Adding");
            progressDialog.setCanceledOnTouchOutside(false);

            String totalText = editText_DailyEternary.getText().toString();
            String[] arr = totalText.split(",");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String id = getIntent().getStringExtra("docId");

//                String[] lats = new String[arr.length];
//                String [] longs = new String[arr.length];

//                int i = 0;
//                for (String a : arr) {

//                    //adding geolocation
//                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                    double lat = 0;
//                    double lon = 0;
//                    String locations = "";
//                    try {
//                        List addressList = geocoder.getFromLocationName(a, 1);
//                        Address address = (Address) addressList.get(0);
//                        lon = address.getLongitude();
//                        lat = address.getLatitude();
//                        lats[i] = String.valueOf(lat);
//                        longs[i] = String.valueOf(lon);
//                        i++;
//                        Log.d("AddDailyEternary", a + " " + lon + " " + lat);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            Map<String, Object> singlepackage = new HashMap<>();
            singlepackage.put("array", Arrays.asList(arr));
//                singlepackage.put("arrayLatitude", Arrays.asList(lats));
//                singlepackage.put("arrayLongitude", Arrays.asList(longs));

            db.collection("Package")
                    .document(id)
                    .set(singlepackage, SetOptions.merge())
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();

                        Toast.makeText(AddDailyEternary.this, "Done", Toast.LENGTH_SHORT).show();

                    })
                    .addOnFailureListener(e -> Toast.makeText(AddDailyEternary.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
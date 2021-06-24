package com.helpinghands.toursandtravels;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditPackage extends AppCompatActivity {
    private static final String TAG = "EditPackage";

    //    AlertDialog.Builder builder;
//    AlertDialog progressDialog;
    Dialog dialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_package);
        Intent intent = getIntent();
        getWindow().getAttributes().windowAnimations = R.style.Fade;


        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Package");
        progressDialog = new ProgressDialog(EditPackage.this);

        //        progressDialog = getBuilder().create();
        //        progressDialog.setCancelable(false);


        final String id = intent.getStringExtra("ID");

//        Toast.makeText(this, "ID : " +id, Toast.LENGTH_SHORT).show();
//        Log.d("abc","IDN : "+ id);
        Button submitbutton = (Button) findViewById(R.id.edit_btn);
        final EditText money = (EditText) findViewById(R.id.editText_Price);
        final EditText location = (EditText) findViewById(R.id.editText_Location);
        final EditText duration = (EditText) findViewById(R.id.editText_duration);
        final EditText description = (EditText) findViewById(R.id.description);
        final EditText name = (EditText) findViewById(R.id.editText_Name);
        final EditText highlights = (EditText) findViewById(R.id.editText_Highlight);
        final EditText most_famour_for = (EditText) findViewById(R.id.editText_Famous);

//        Button erasebutton = (Button) findViewById(R.id.btn_Delete);
        Button edit_image_btn = findViewById(R.id.edit_image_btn);
        edit_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EditPackage.this, UploadImageActivity.class);
//                Log.d("abc","ID : " +id);
                intent1.putExtra("docId", id);
                startActivity(intent1);
            }
        });


        Button add_Daily_Eternary = findViewById(R.id.add_Daily_Eternary);
        add_Daily_Eternary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EditPackage.this, AddDailyEternary.class);
                intent2.putExtra("docId", id);
                startActivity(intent2);
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert id != null;
        DocumentReference documentReference = db.collection("Package").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        money.setText((CharSequence) document.get("money"));
                        location.setText((CharSequence) document.get("location"));
                        duration.setText((String) Objects.requireNonNull(document.get("duration")));
                        description.setText((String) Objects.requireNonNull(document.get("description")));
                        name.setText((String) Objects.requireNonNull(document.get("name")));
                        highlights.setText((String) Objects.requireNonNull(document.get("highlight")));
                        most_famour_for.setText((String) Objects.requireNonNull(document.get("famous_for")));
                        duration.setText((String) Objects.requireNonNull(document.get("duration")));

                    } else {
                        Toast.makeText(EditPackage.this, "Error in getting document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditPackage.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        erasebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                money.setText("");
//                location.setText("");
//                duration.setText("");
//                description.setText("");
//                name.setText("");
//                highlights.setText("");
//                most_famour_for.setText("");
//                duration.setText("");
//            }
//        });

        submitbutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
//                Log.d(TAG, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

                progressDialog.show();
                progressDialog.setMessage("Processing");

                progressDialog.setCanceledOnTouchOutside(false);

                String moneyss = String.valueOf(money.getText());
                String duratoinss = String.valueOf(duration.getText());
                String locationss = String.valueOf(location.getText());
                String descriptionss = String.valueOf(description.getText());
                String namess = String.valueOf(name.getText());
                String highlightss = String.valueOf(highlights.getText());
                String most_famour_forss = String.valueOf(most_famour_for.getText());

                if ((moneyss.equals("") || moneyss.isEmpty())) {
                    Toast.makeText(EditPackage.this, "Money can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((duratoinss.equals("") || duratoinss.isEmpty())) {
                    Toast.makeText(EditPackage.this, "Duration can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((locationss.equals("") || locationss.isEmpty())) {
                    Toast.makeText(EditPackage.this, "Location field can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((namess.equals("") || namess.isEmpty())) {
                    Toast.makeText(EditPackage.this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                } else if ((highlightss.equals("") || highlightss.isEmpty())) {
                    Toast.makeText(EditPackage.this, "Location field can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> singlepackage = new HashMap<>();
                    singlepackage.put("money", moneyss);
                    singlepackage.put("duration", duratoinss);
                    singlepackage.put("location", locationss);
                    singlepackage.put("description", descriptionss);
                    singlepackage.put("name", namess);
                    singlepackage.put("highlight", highlightss);
                    singlepackage.put("famous_for", most_famour_forss);

                    db.collection("Package")
                            .document(id)
                            .set(singlepackage, SetOptions.merge())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();


                                    Intent intent1 = new Intent(EditPackage.this, MainActivity.class);
                                    startActivity(intent1);
                                }
                            });
                }
            }
        });

    }
}
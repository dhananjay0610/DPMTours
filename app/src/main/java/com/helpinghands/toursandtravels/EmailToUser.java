package com.helpinghands.toursandtravels;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailToUser extends AppCompatActivity {

    String sEmail, sPassword;
    String emailContent;
    Button Save_btn;
    TextView email, User_name,User_message;
    EditText textOfEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_to_user);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        Save_btn = findViewById(R.id.send_btn);
        email = findViewById(R.id.User_email);
        User_name = findViewById(R.id.User_name);
        textOfEmail = findViewById(R.id.textOfEmail);
        User_message=findViewById(R.id.User_message);
        textOfEmail.setText("Hey " + User_name.getText() + ".\n" + " Your message is received. \n");
        String id = getIntent().getStringExtra("ID");
//        Toast.makeText(this, "ID : " + id, Toast.LENGTH_SHORT).show();
        //sender credentials
        sEmail = "dhananjaywaghade06@gmail.com";
        sPassword = "priy dhanu";


        //setting data from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert id != null;
        DocumentReference documentReference = db.collection("Contact").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
//                        money.setText((CharSequence) document.get("money"));
//                        location.setText((CharSequence) document.get("location"));
//                        duration.setText((String) Objects.requireNonNull(document.get("duration")));
//                        description.setText((String) Objects.requireNonNull(document.get("description")));
//                        name.setText((String) Objects.requireNonNull(document.get("name")));
//                        highlights.setText((String) Objects.requireNonNull(document.get("highlight")));
//                        most_famour_for.setText((String) Objects.requireNonNull(document.get("famous_for")));
//                        duration.setText((String) Objects.requireNonNull(document.get("duration")));

//                        Toast.makeText(EmailToUser.this, "exist" + document.getData(), Toast.LENGTH_SHORT).show();
                        User_name.setText((CharSequence) document.get("UserName"));
                        email.setText((CharSequence) document.get("Email"));
                        User_message.setText( document.get("description").toString());

                        Log.d("abc", String.valueOf(document.getData()));
                        textOfEmail.setText("Hey " + document.get("UserName") + " !.\n" + " Your message is received. \n");


                    } else {
                        Toast.makeText(EmailToUser.this, "Error in getting document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmailToUser.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

                //save button
        Save_btn.setOnClickListener(view -> savebutton());

    }

    private void savebutton() {


        //changing the boolean
        String id = getIntent().getStringExtra("ID");
        changeBoolean(id);

        //emailContent
//        emailContent = "Hello " + customer_Name.getText().toString() + ".\n" +
//                "Your Bill is ready\n" +
//                "Product : " + product_dropDown.getText().toString() + "\n" +
//                "Amount : " + amount.getText().toString() + "\n" +
//                "Price : " + billed_Amount.getText().toString() + "\n" +
//                "THANK YOU!!";

        emailContent = textOfEmail.getText().toString().trim();

        //properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        //session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail, sPassword);
            }
        });
        try {

            //mail content
            Message message = new MimeMessage(session);

            //sender email
            message.setFrom(new InternetAddress(sEmail));

            //receiver email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText().toString().trim()));

            //email subject
            message.setSubject("Solving Your Query");

            //email text
            message.setText(emailContent);

            //send email
            new SendMail().execute(message);


        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private void changeBoolean(String id) {



    }


    private class SendMail extends AsyncTask<Message, String, String> {

        //dialog box
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(EmailToUser.this, "Please wait", "Sending email", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {

                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.d("EmailToUser", e.getMessage() + "");
                Toast.makeText(EmailToUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equals("Success")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EmailToUser.this);
                builder.setCancelable(false);
                builder.setMessage("Email send successfully");
                builder.setPositiveButton("OK", (dialogInterface, i) -> progressDialog.dismiss());
                builder.show();
            } else {
                Toast.makeText(EmailToUser.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
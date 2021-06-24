package com.helpinghands.toursandtravels;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    Button login;
    EditText email, password;

    TextView forgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getAttributes().windowAnimations=R.style.Fade;

        initializeViews();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithCredentials();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PasswordResetDialog dialog = new PasswordResetDialog(MainActivity.this);
//                dialog.show(getSupportFragmentManager(), "Reset Password");
            }
        });
    }


    public void initializeViews() {
        password = (EditText) findViewById(R.id.password_et);
        forgetPass = (TextView) findViewById(R.id.forgetpassword_tv);
        email = findViewById(R.id.email_et);
        login = (Button) findViewById(R.id.login_btn);
    }

    public void loginWithCredentials() {

        String mEmail = email.getText().toString();
        String mPassword = password.getText().toString();
//        Toast.makeText(this, mEmail+" "+ mPassword, Toast.LENGTH_SHORT).show();

        if (mEmail.equals("") || mEmail.isEmpty()) {
            Toast.makeText(this, "Please enter EMAIL", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPassword.equals("") || mEmail.isEmpty()) {
            Toast.makeText(this, "Please enter PASSWORD", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (mEmail.equals("principal@gmail.com") && mPassword.equals("Principal@123")) {
        if (mEmail.equals("admin@gmail.com") && mPassword.equals("Admin@123")) {
            SharedPreferences sharedPref = getSharedPreferences("Shared_Pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
//                   editor.putString("FirstTime","Yes");
            editor.putBoolean("FirstTime", false);

            editor.apply();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please check credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
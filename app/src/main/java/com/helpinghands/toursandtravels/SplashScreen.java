package com.helpinghands.toursandtravels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {
LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().getAttributes().windowAnimations=R.style.Fade;

        lottieAnimationView=findViewById(R.id.lottie);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPref = getSharedPreferences("Shared_Pref", Context.MODE_PRIVATE);
                boolean firstTime = sharedPref.getBoolean("FirstTime", true);
                if (!firstTime) {
                    Intent myIntent = new Intent(com.helpinghands.toursandtravels.SplashScreen.this, MainActivity.class);
                    com.helpinghands.toursandtravels.SplashScreen.this.startActivity(myIntent);
                    com.helpinghands.toursandtravels.SplashScreen.this.finish();
                } else {
                    Intent myIntent = new Intent(com.helpinghands.toursandtravels.SplashScreen.this, Login.class);
                    com.helpinghands.toursandtravels.SplashScreen.this.startActivity(myIntent);
                    com.helpinghands.toursandtravels.SplashScreen.this.finish();
                }
            }
        }, 6200);
    }
}
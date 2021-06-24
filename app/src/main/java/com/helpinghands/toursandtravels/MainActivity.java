package com.helpinghands.toursandtravels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.api.Context;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirestoreAdapter adapter;
    final String TAG = "MainActivity";
    CollectionReference noteref;
    RecyclerView recyclerView;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getAttributes().windowAnimations=R.style.Fade;

        //for drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        noteref = firebaseFirestore.collection("Package");
        Query query = noteref;
        FirestoreRecyclerOptions<productsmodel> options = new FirestoreRecyclerOptions.Builder<productsmodel>()
                .setQuery(query, productsmodel.class)
                .build();
        adapter = new FirestoreAdapter(options,getApplicationContext());
        recyclerView = findViewById(R.id.firestorelist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setNavigation();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setNavigation() {
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.getMenu().getItem(0).setChecked(true);
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
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(NewTeacher.this, DashBoard.class);
//            startActivity(intent);
        } else if (id == R.id.nav_newTeacher) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(MainActivity.this, EnterPackageActivity.class);
            startActivity(intent);
        }
        else if(id ==R.id.nav_ContactUs)
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(MainActivity.this, ContactUs.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

            SharedPreferences sharedPref = getSharedPreferences("Shared_Pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return false;
    }
}


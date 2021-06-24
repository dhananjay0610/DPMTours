package com.helpinghands.toursandtravels;

import android.content.Context;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ContactUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FireStoreAdapterContactUs adapter;
    final String TAG = "ContactUs";
    CollectionReference noteref;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().getAttributes().windowAnimations=R.style.Fade;

        //for drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        recyclerView=findViewById(R.id.recyclerView);

       //recycler view
        noteref = firebaseFirestore.collection("Contact");
        Query query = noteref
                .whereEqualTo("Responded",false);
        FirestoreRecyclerOptions<productsModelForContactUs> options = new FirestoreRecyclerOptions.Builder<productsModelForContactUs>()
                .setQuery(query, productsModelForContactUs.class)
                .build();
        adapter = new FireStoreAdapterContactUs(options,getApplicationContext());
//        recyclerView  = findViewById(R.id.recyclerView);
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
        mNavigationView.getMenu().getItem(2).setChecked(true);
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
            Intent intent = new Intent(ContactUs.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_newTeacher) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(ContactUs.this, EnterPackageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ContactUs) {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(EnterPackageActivity.this, ContactUs.class);
//            startActivity(intent);
        } else if (id == R.id.nav_logout) {

            mDrawerLayout.closeDrawer(GravityCompat.START);
            SharedPreferences sharedPref = getSharedPreferences("Shared_Pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(ContactUs.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return false;
    }
}
package com.hamza.firestonev1.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.adapters.shiftAdapter;
import com.hamza.firestonev1.models.Shifts;

public class LocationActivity extends AppCompatActivity {

    EditText searchfield;
    ImageButton searchbutton;
    RecyclerView mResultlist;

    String personName;
    com.hamza.firestonev1.adapters.shiftAdapter shiftAdapter;
    private CollectionReference myCol = FirebaseFirestore.getInstance().collection("newShifts");
    private BottomNavigationView.OnNavigationItemSelectedListener mONListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Intent homeIntent = new Intent(getApplicationContext(), home.class);
                    startActivity(homeIntent);
                    finish();
                    return true;
                case R.id.nav_search:
                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(searchIntent);
                    finish();return true;
                case R.id.nav_post:
                    Intent postIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(postIntent);
                    finish();

                    return true;
                case R.id.nav_my:
                    Intent myIntent = new Intent(getApplicationContext(), myShiftsActivity.class);
                    myIntent.putExtra("username" , personName);
                    startActivity(myIntent);
                    return true;
                case R.id.nav_about:
                    Intent aboutintent = new Intent(getApplicationContext(), AboutAcitivity.class);
                    startActivity(aboutintent);
                    return true;
            }
            return false;
        }
    };

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        searchfield = findViewById(R.id.editLocationText);
        searchbutton = findViewById(R.id.locationButton);
        mResultlist = findViewById(R.id.locationResult);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Shifts by name");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(getApplicationContext(), view);
                String searchText = searchfield.getText().toString();
                searchFirebase(searchText);
            }
        });


    }

    private void searchFirebase(String searchText) {

        final Query mQuery = myCol.orderBy("location").startAt(searchText).endAt(searchText + "\uf8ff");


        FirestoreRecyclerOptions<Shifts> options = new FirestoreRecyclerOptions.Builder<Shifts>()
                .setQuery(mQuery, Shifts.class)
                .build();

        shiftAdapter = new shiftAdapter(options, this);
        mResultlist.setHasFixedSize(true);
        mResultlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mResultlist.setAdapter(shiftAdapter);
        shiftAdapter.startListening();


    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(backIntent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

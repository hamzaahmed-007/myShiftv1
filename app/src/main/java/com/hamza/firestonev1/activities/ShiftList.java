package com.hamza.firestonev1.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class ShiftList extends AppCompatActivity {

    Intent backIntent;
    String id;
    TextView textView, date_label;


    DatePickerDialog picker;
    RecyclerView mrecyclerView;

    shiftAdapter shiftAdapter;

    String personName;


    private CollectionReference myCol = FirebaseFirestore.getInstance().collection("newShifts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_list);

        Intent intent = getIntent();
        String date = intent.getExtras().getString("date");
        backIntent = new Intent(getApplicationContext(), CalenderActivity.class);


        date_label = findViewById(R.id.dateLabel);
        date_label.setText("Shifts Available on "+date);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Shifts Available");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);


        final Query mQuery = myCol.whereEqualTo("date", date);


        FirestoreRecyclerOptions<Shifts> options = new FirestoreRecyclerOptions.Builder<Shifts>()
                .setQuery(mQuery, Shifts.class)
                .build();

        shiftAdapter = new shiftAdapter(options, this);

        mrecyclerView = findViewById(R.id.rvShifts);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mrecyclerView.setAdapter(shiftAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        shiftAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (shiftAdapter != null) {
            shiftAdapter.stopListening();
        }
    }
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
                    Intent searchIntent = new Intent(getApplicationContext(), CalenderActivity.class);
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

    public void toastMethod(View view)
    {

        Toast toast = Toast.makeText(this, "You clicked the list" , Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    public void onBackPressed() {
        startActivity(backIntent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

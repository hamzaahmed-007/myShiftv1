package com.hamza.firestonev1.activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hamza.firestonev1.R;

import java.util.Calendar;

public class CalenderActivity extends AppCompatActivity {

    DatePicker picker;
    Button next_button;
    String dateText;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        next_button = findViewById(R.id.nextButton);
        picker = findViewById(R.id.datePicker);
        picker.setMinDate(System.currentTimeMillis() - 1000);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Shifts");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);



        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateText = picker.getDayOfMonth()+"/"+(picker.getMonth()+1)+"/"+picker.getYear();
                Intent intent = new Intent(getApplicationContext(), ShiftList.class);
                intent.putExtra("date" , dateText);
                startActivity(intent);

            }
        });



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

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(getApplicationContext(), home.class );
        startActivity(backIntent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

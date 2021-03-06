package com.hamza.firestonev1.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;

public class ShiftList extends AppCompatActivity {

    Intent backIntent;
    String id;
    TextView textView, date_label;
    EditText dateText;
    ImageButton datebutton;


    DatePickerDialog picker;
    RecyclerView mrecyclerView;

    shiftAdapter shiftAdapter;

    String personName;


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
        setContentView(R.layout.activity_shift_list);

        backIntent = new Intent(getApplicationContext(), SearchActivity.class);

        dateText = findViewById(R.id.editDateText);
        dateText.requestFocus();
        dateText.setInputType(InputType.TYPE_NULL);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                picker = new DatePickerDialog(ShiftList.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();


            }
        });

        datebutton = findViewById(R.id.buttonDateSearch);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = dateText.getText().toString();
                searchFirebase(searchText);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search by date");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);


    }


    @Override
    protected void onStop() {
        super.onStop();

        if (shiftAdapter != null) {
            shiftAdapter.stopListening();
        }
    }

    private void searchFirebase(String searchText) {
        final Query mQuery = myCol.orderBy("date").startAt(searchText).endAt(searchText + "\uf8ff");

        FirestoreRecyclerOptions<Shifts> options = new FirestoreRecyclerOptions.Builder<Shifts>()
                .setQuery(mQuery, Shifts.class)
                .build();

        shiftAdapter = new shiftAdapter(options, this);

        mrecyclerView = findViewById(R.id.rvShifts);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mrecyclerView.setAdapter(shiftAdapter);
        shiftAdapter.startListening();
    }

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

package com.hamza.firestonev1.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.models.Shifts;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.hamza.firestonev1.extra.idValue";

    EditText name, location, editID, dateText;
    DatePickerDialog picker;

    String username;
    Button sendButton;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;


    int shiftNum = 1;
    String docPath = "";
    String id = UUID.randomUUID().toString();

    Intent backIntent;


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DocumentReference myDoc  = FirebaseFirestore.getInstance().collection("sampleData").document(id);
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
                    finish();
                    return true;
                case R.id.nav_post:
                    Intent postIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(postIntent);
                    finish();

                    return true;
                case R.id.nav_my:
                    Intent myIntent = new Intent(getApplicationContext(), myShiftsActivity.class);
                    myIntent.putExtra("username", personName);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backIntent = new Intent(getApplicationContext(), home.class);
        sendButton = findViewById(R.id.send_button);
        //shiftView = findViewById(R.id.textView);
        dateText = findViewById(R.id.editDate);
        dateText.requestFocus();
        editID = findViewById(R.id.editID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post your Shift");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        username = mUser.getDisplayName();

        dateText.setInputType(InputType.TYPE_NULL);



        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                picker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateText.setText(dayOfMonth + "/" +(monthOfYear + 1 )  + "/" + year);

                    }
                }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();



            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

            }
        });

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

    public void saveData()
    {

        String docPath = "";

        shiftNum++;


        name = findViewById(R.id.editName);
        location = findViewById(R.id.editLocation);
        editID = findViewById(R.id.editID);




        String nameValue = name.getText().toString();
        String locationValue = location.getText().toString();
        String idValue  =editID.getText().toString();
        String dateValue = dateText.getText().toString();

        docPath = username + " " + idValue;

        Map<String, Object> dataToSave = new HashMap<String, Object>();


        if(nameValue.isEmpty() || locationValue.isEmpty() || idValue.isEmpty() || dateValue.isEmpty())
        {
            showMessage("Please fill in all Values!");

        }
        else
        {
            String shiftTitle = personEmail+idValue;

            Shifts shifts1 = new Shifts(personName, nameValue, locationValue, dateValue, shiftTitle, personEmail);

            myCol.document(shiftTitle).set(shifts1);
            showMessage("New Shift Posted!");
            Intent intent = new Intent(getApplicationContext(), home.class);
            startActivity(intent);

        }






    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

}

package com.hamza.firestonev1.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.models.pendingShifts;

public class ShiftConfirm extends AppCompatActivity {

    private static final String TAG = "this is the message";
    Intent backIntent;
    String id;
    TextView shiftName, shiftLocation, shiftDate;
    Button confirmButton, cancelButton;
    String nameText,locationText,dateText;
    private CollectionReference myCol = FirebaseFirestore.getInstance().collection("newShifts");
    private CollectionReference myPending = FirebaseFirestore.getInstance().collection("pendingShifts");
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_confirm);

        Intent intent = getIntent();
        backIntent = new Intent(getApplicationContext(), home.class);

        mAuth = FirebaseAuth.getInstance();
        mUser  = mAuth.getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm Application");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        //bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }

        //id = intent.getStringExtra(ShiftList.EXTRA_MESSAGE);
        id = intent.getExtras().getString("ShiftID");
        shiftName = findViewById(R.id.shiftview1);
        shiftLocation = findViewById(R.id.shiftview2);
        shiftDate = findViewById(R.id.shiftview3);
        confirmButton = findViewById(R.id.button_confirm);
        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), home.class);
                startActivity(backIntent);
                finish();
            }
        });

        getData();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmShift();
            }
        });
    }


    public void getData() {



        myCol.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {


                    nameText = documentSnapshot.getString("name");
                    locationText = documentSnapshot.getString("location");
                    dateText = documentSnapshot.getString("date");

                    shiftName.setText(nameText);
                    shiftLocation.setText(locationText);
                    shiftDate.setText(dateText);

                }
            }
        });
    }



    public void ConfirmShift()
    {
        addPendingShift();
        //Toast toast = Toast.makeText(ShiftConfirm.this, "You have applied for " + nameText, Toast.LENGTH_LONG);
        //toast.show();

        //String nameUser = mUser.getDisplayName();
        //myPending.document(nameUser).set()

        Intent intent = new Intent(this, home.class);
        // intent.putExtra(EXTRA_MESSAGE, idValue);
        startActivity(intent);





    }

    private void addPendingShift() {



        myCol.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    String username = mUser.getDisplayName();
                    nameText = documentSnapshot.getString("name");
                    locationText = documentSnapshot.getString("location");
                    dateText = documentSnapshot.getString("date");

                    pendingShifts pendingShifts = new pendingShifts(personEmail, nameText, locationText, dateText, personName, "Pending", id);

                    myPending.document(id).set(pendingShifts);
                    showMessage("Congratulations " + mUser.getDisplayName() + ", You have applied for " + nameText);
                }
            }
        });

    }


    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG ).show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}

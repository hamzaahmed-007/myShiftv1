package com.hamza.firestonev1.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.adapters.searchAdapter;
import com.hamza.firestonev1.adapters.searchPendingAdapter;
import com.hamza.firestonev1.models.Shifts;
import com.hamza.firestonev1.models.pendingShifts;

public class myShiftsActivity extends AppCompatActivity {


    private CollectionReference myCol = FirebaseFirestore.getInstance().collection("newShifts");
    private CollectionReference myPending = FirebaseFirestore.getInstance().collection("pendingShifts");
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    searchAdapter shiftAdapter;
    searchPendingAdapter shiftAdapter2;

    String username;
    TextView postedText;

    RecyclerView mRecyclerView1, mRecyclerView2;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shifts2);

        mAuth = FirebaseAuth.getInstance();
        mUser  = mAuth.getCurrentUser();

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Shifts");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }

        final Query mQuery1 = myCol.whereEqualTo("id",personName);
        FirestoreRecyclerOptions<Shifts> options = new FirestoreRecyclerOptions.Builder<Shifts>()
                .setQuery(mQuery1, Shifts.class)
                .build();

        shiftAdapter = new searchAdapter(options, this);
        mRecyclerView1 = findViewById(R.id.postedRv);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView1.setAdapter(shiftAdapter);





        final Query mQuery2 = myPending.whereEqualTo("username", personEmail);
        FirestoreRecyclerOptions<pendingShifts> options2 = new FirestoreRecyclerOptions.Builder<pendingShifts>()
                .setQuery(mQuery2, pendingShifts.class)
                .build();

        shiftAdapter2 = new searchPendingAdapter(options2, this);
        mRecyclerView2 = findViewById(R.id.appliedRV);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView2.setAdapter(shiftAdapter2);

    }


    @Override
    protected void onStart() {
        super.onStart();
        shiftAdapter.startListening();
        shiftAdapter2.startListening();
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

    @Override
    protected void onStop() {
        super.onStop();

        if (shiftAdapter != null) {
            shiftAdapter.stopListening();
        }
        if (shiftAdapter2 != null) {
            shiftAdapter2.stopListening();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

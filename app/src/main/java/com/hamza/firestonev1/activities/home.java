package com.hamza.firestonev1.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hamza.firestonev1.R;
import com.hamza.firestonev1.adapters.shiftAdapter;
import com.hamza.firestonev1.models.Shifts;
import com.hamza.firestonev1.models.users;

import java.util.ArrayList;


public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    RecyclerView recyclerView;
    myAdapter myAdapter;
    shiftAdapter shiftAdapter;
    final ArrayList<Shifts> myShifts = new ArrayList<>();
    boolean checker = false;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;
    GoogleSignInClient mGoogleSignInClient;



    private CollectionReference myCol;
    private CollectionReference myToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mONListener);
        myToken = FirebaseFirestore.getInstance().collection("userTokens");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        registertoken(token);

                        Log.d("TAG", token);

                    }
                });






        FirebaseApp.initializeApp(this);

        myCol = FirebaseFirestore.getInstance().collection("newShifts");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
           personName = acct.getDisplayName();
           personGivenName = acct.getGivenName();
           personFamilyName = acct.getFamilyName();
           personEmail = acct.getEmail();
           personId = acct.getId();
           personPhoto = acct.getPhotoUrl();

        }


        Query mQuery = myCol.orderBy("date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Shifts> options = new FirestoreRecyclerOptions.Builder<Shifts>()
                .setQuery(mQuery, Shifts.class)
                .build();

        shiftAdapter = new shiftAdapter(options, this);

        recyclerView = findViewById(R.id.postRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shiftAdapter);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateNav();
    }

    private void registertoken(String token) {
        users user1 = new users(personEmail, token);
        myToken.document(personEmail).set(user1);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            Intent aboutintent = new Intent(getApplicationContext(), AboutAcitivity.class);
            startActivity(aboutintent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            // Handle the camera action
        }else if (id == R.id.nav_myShifts) {
            Intent myIntent = new Intent(getApplicationContext(), myShiftsActivity.class);
            myIntent.putExtra("username" , personName);
            startActivity(myIntent);


        }
        else if (id == R.id.nav_send) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }  else if (id == R.id.nav_postshift) {

            getSupportActionBar().setTitle("Post a New Shift");
            Intent postIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(postIntent);
            finish();

        } else if (id == R.id.nav_viewshifts) {
            Intent viewIntent = new Intent(getApplicationContext(), CalenderActivity.class);
            startActivity(viewIntent);
            finish();



        } else if (id == R.id.nav_logout) {

            signOut();



        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        showMessage("You are now Signed Out");

        Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logoutIntent);
        finish();

    }


    public void updateNav()
    {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.navName);
        TextView navMail = headerView.findViewById(R.id.navMail);
        ImageView navImage = headerView.findViewById(R.id.navImage);
        navImage.setClipToOutline(true);
        navMail.setText(personEmail);
        navName.setText(personName);

        Glide.with(this).load(String.valueOf(personPhoto)).into(navImage);


    }







    private void showMessage(String s) {

    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}

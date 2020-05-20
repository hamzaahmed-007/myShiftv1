package com.hamza.firestonev1.activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hamza.firestonev1.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText userID, userName, userMail, userPas;
    private Button reg_button;
    private ProgressBar loadingBar;
    private TextView loginOpt;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginOpt = findViewById(R.id.loginText);
        userID = findViewById(R.id.regID);
        userName = findViewById(R.id.regName);
        userMail = findViewById(R.id.regMail);
        userPas = findViewById(R.id.regPass);
        reg_button = findViewById(R.id.regButton);
        loadingBar = findViewById(R.id.progressBar);

        loadingBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        loginOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginAct = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginAct);
                finish();
            }
        });

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_button.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);

                final String id = userID.getText().toString();
                final String name = userName.getText().toString();
                final String mail = userMail.getText().toString();
                final String password = userPas.getText().toString();

                if(id.isEmpty() || name.isEmpty() || mail.isEmpty() || password.isEmpty())
                {

                    showMessage("All Fields must be Filled in!");
                    reg_button.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);

                }else
                {

                    CreateNewUser(id,name,mail,password);

                }



            }
        });


    }

    private void CreateNewUser(final String id,final String name, String mail, String password)
    {

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            showMessage("New Profile Created!");
                            updateProfile(id, name, mAuth.getCurrentUser());

                        }else
                        {
                            showMessage("Error occured while creating this profile " + task.getException().getMessage());

                        }

                    }
                });

    }

    private void updateProfile(String id, String name, final FirebaseUser currentUser)
    {
        UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(updateProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    showMessage("Registration Complete");
                    updateUi();


                }
                else
                {
                    showMessage("Registration could not be Finished " + task.getException().getMessage());
                }
            }
        });



    }

    private void updateUi()
    {

        Intent intent = new Intent(getApplicationContext(), home.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();



    }
}

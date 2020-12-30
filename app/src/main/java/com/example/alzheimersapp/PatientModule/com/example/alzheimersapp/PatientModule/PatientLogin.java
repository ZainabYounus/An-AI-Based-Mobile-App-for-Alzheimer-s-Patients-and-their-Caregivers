package com.example.alzheimersapp.PatientModule;

import androidx.annotation.NonNull;

import com.example.alzheimersapp.LoginActivity;
import com.example.alzheimersapp.PatientModule.PatientDashboard;
import com.example.alzheimersapp.PatientModule.patientData;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.alzheimersapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientLogin extends AppCompatActivity{

    TextInputEditText patientEmail, patientPassword;
    Button patientLogin, pRegister;
    FirebaseAuth firebaseAuth;
    String pEmail;
    String pPswd;


    FirebaseUser user;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        pRegister = findViewById(R.id.patientRegister);
        patientEmail = findViewById(R.id.emailInput);
        patientPassword = findViewById(R.id.passwordInput);
        patientLogin = findViewById(R.id.pSignin);


        firebaseAuth = FirebaseAuth.getInstance();
        checkLoginStatus();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Logging in...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    public void patientRegister(View v){
        //Toast.makeText(this, "Hey I am working", Toast.LENGTH_SHORT).show();
        Intent patientRegistry = new Intent(this, PatientRegister.class);
        startActivity(patientRegistry);
    }

    public void patientLogin(View v){
        mProgress.show();
        //Getting entered email and password
        pEmail = patientEmail.getText().toString();
        pPswd = patientPassword.getText().toString();

        //Authentication
        if(TextUtils.isEmpty(pEmail)){
            patientEmail.setError("Please Enter the Patient's Email");
        }
        if(TextUtils.isEmpty(pPswd)){
            patientPassword.setError("Please Enter the Password");
        }

        if(pPswd.length() < 8){
            patientPassword.setError("Password length must be at least 8 characters");
        }

        else{

            firebaseAuth.signInWithEmailAndPassword(pEmail, pPswd)
                    .addOnCompleteListener(PatientLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                Intent goToDashboard = new Intent(PatientLogin.this, PatientDashboard.class);
                                goToDashboard.putExtra("ptEmail", pEmail);
                                startActivity(goToDashboard);
                            }
                            else {
                                mProgress.dismiss();
                                Toast.makeText(PatientLogin.this, "Login Failed! Make sure you have registered.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void checkLoginStatus(){
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Intent goToDashboard = new Intent(PatientLogin.this, PatientDashboard.class);
            goToDashboard.putExtra("ptEmail", user.getEmail().toString());
            startActivity(goToDashboard);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PatientLogin.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.example.alzheimersapp;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.CaregiverModule.CaregiverLogin;
import com.example.alzheimersapp.PatientModule.PatientLogin;


public class LoginActivity extends AppCompatActivity {

    Button pLogin, cLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pLogin = findViewById(R.id.patientLogin);
        cLogin = findViewById(R.id.caregiverLogin);
    }

    public void patientLogin(View view) {
        Intent goToPatientModule = new Intent(LoginActivity.this, PatientLogin.class);
        startActivity(goToPatientModule);
    }
    public void caregiverLogin(View view){
        Intent goToCaregiverModule = new Intent(LoginActivity.this, CaregiverLogin.class);
        startActivity(goToCaregiverModule);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

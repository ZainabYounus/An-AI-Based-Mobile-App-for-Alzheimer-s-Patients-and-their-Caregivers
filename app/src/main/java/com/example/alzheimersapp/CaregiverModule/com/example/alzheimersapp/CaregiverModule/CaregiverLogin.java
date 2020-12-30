package com.example.alzheimersapp.CaregiverModule;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alzheimersapp.LoginActivity;
import com.example.alzheimersapp.PatientModule.PatientDashboard;
import com.example.alzheimersapp.PatientModule.PatientLogin;
import com.example.alzheimersapp.PatientModule.PatientRegister;
import com.example.alzheimersapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CaregiverLogin extends AppCompatActivity {

    TextInputEditText caregiverEmail, caregiverPassword;
    Button caregiverLogin, cgRegister;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String cgEmail, cgPswd;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_login);

        cgRegister = findViewById(R.id.caregiverRegister);
        caregiverEmail = findViewById(R.id.emailInput);
       caregiverPassword = findViewById(R.id.passwordInput);
        caregiverLogin = findViewById(R.id.cgSignin);

        firebaseAuth = FirebaseAuth.getInstance();
        checkLoginStatus();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Logging in...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    public void caregiverRegister(View v){
        //Toast.makeText(this, "Hey I am working", Toast.LENGTH_SHORT).show();
        Intent caregiverRegistry = new Intent(CaregiverLogin.this, CaregiverRegister.class);
        startActivity(caregiverRegistry);
    }

    public void caregiverLogin(View v){
         mProgress.show();
        //Getting entered email and password
         cgEmail = caregiverEmail.getText().toString();
         cgPswd = caregiverPassword.getText().toString();

        //Authentication
        if(TextUtils.isEmpty(cgEmail)){
            caregiverEmail.setError("Please Enter the Caregiver's Email");
        }
        if(TextUtils.isEmpty(cgPswd)){
            caregiverPassword.setError("Please Enter the Password");
        }

        if(cgPswd.length() < 8){
            caregiverPassword.setError("Password length must be at least 8 characters");
        }

        else{
            firebaseAuth.signInWithEmailAndPassword(cgEmail, cgPswd)
                    .addOnCompleteListener(CaregiverLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()) {
                                Intent goToDashboard = new Intent(CaregiverLogin.this, CaregiverDashboard.class);
                                goToDashboard.putExtra("cgEmail", cgEmail);
                                //Toast.makeText(CaregiverLogin.this, cgEmail, Toast.LENGTH_SHORT).show();
                                startActivity(goToDashboard);
                            }
                            else {
                                mProgress.dismiss();
                                Toast.makeText(CaregiverLogin.this, "Login Failed! Make sure you have registered.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void checkLoginStatus(){
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Intent goToDashboard = new Intent(CaregiverLogin.this, CaregiverDashboard.class);
            goToDashboard.putExtra("cgEmail", user.getEmail().toString());
            startActivity(goToDashboard);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CaregiverLogin.this, LoginActivity.class));
        finish();
    }

}

package com.example.alzheimersapp.CaregiverModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alzheimersapp.PatientModule.PatientLogin;
import com.example.alzheimersapp.PatientModule.PatientRegister;
import com.example.alzheimersapp.PatientModule.patientData;
import com.example.alzheimersapp.R;
import com.example.alzheimersapp.SQLiteHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CaregiverRegister extends AppCompatActivity {

    Button registerCaregiver;
    TextInputEditText name, cEmail, password, contact, ptEmail, ptContact;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_register);

        //Registering Views
        registerCaregiver = findViewById(R.id.cgSignup);
        name = findViewById(R.id.nameInput);
        cEmail = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        contact = findViewById(R.id.contactInput);
        ptEmail = findViewById(R.id.patientEmailInput);
        ptContact = findViewById(R.id.pContactInput);

        //Initializing the firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Caregiver");
        firebaseAuth = FirebaseAuth.getInstance();

        //Initializing the SQLite database
        sqLiteHelper = new SQLiteHelper(this);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registerCaregiverToDB(View v) {
        final String cgName = name.getText().toString();
        final String cgEmail = cEmail.getText().toString();
        final String cgPswd = password.getText().toString();
//        final int cgContact = Integer.parseInt(contact.getText().toString());
        final String cgContact = contact.getText().toString();
        final String pEmail = ptEmail.getText().toString();
        final String pContact = ptContact.getText().toString();

        //Authentication Code Goes Here

        if(TextUtils.isEmpty(cgName)){
            name.setError("Please Enter the Name");
        }
        if(TextUtils.isEmpty(cgEmail)){
            cEmail.setError("Please Enter the Caregiver's Email");
        }
        if(TextUtils.isEmpty(cgPswd)){
            password.setError("Please Enter the Password");
        }
        if(TextUtils.isEmpty(cgContact)){
            contact.setError("Please Enter the Contact Number");
        }
        if(TextUtils.isEmpty(pEmail)){
            ptEmail.setError("Please Enter the Patient's Email");
        }
        if(cgPswd.length() < 8){
            password.setError("Password length must be at least 8 characters");
        }
        if(TextUtils.isEmpty(pContact)){
            ptContact.setError("Please Enter the Patient's Contact");
        }

        else{
            //Firebase Sign UP
            firebaseAuth.createUserWithEmailAndPassword(cgEmail, cgPswd)
                    .addOnCompleteListener(CaregiverRegister.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Sending Caregiver data to Firebase
                                caregiverData cgData = new caregiverData(cgName, cgEmail, cgPswd, cgContact, pEmail, pContact);
                                FirebaseDatabase.getInstance().getReference("Caregiver").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(cgData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //Sending data to SQLite DB
                                        sqLiteHelper.addCaregiver(new Caregiver(cgEmail, cgName, cgPswd, cgContact, pEmail, pContact));
                                        //Toast.makeText(CaregiverRegister.this, pEmail, Toast.LENGTH_SHORT).show();

                                        Toast.makeText(CaregiverRegister.this, "Caregiver Successfully Registered", Toast.LENGTH_SHORT).show();
                                        Intent cgLogin = new Intent(CaregiverRegister.this, CaregiverLogin.class);
                                        startActivity(cgLogin);
                                    }
                                });
                            } else {

                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, CaregiverLogin.class));
        finish();
    }
}

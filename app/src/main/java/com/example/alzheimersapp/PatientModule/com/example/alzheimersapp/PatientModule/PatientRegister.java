package com.example.alzheimersapp.PatientModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
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

public class PatientRegister extends AppCompatActivity {

    Button registerPatient;
    TextInputEditText name, ptEmail, password, contact, cgEmail, cgContact;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        //Registering Views
        registerPatient = findViewById(R.id.pSignup);
        name = findViewById(R.id.nameInput);
        ptEmail = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        contact = findViewById(R.id.contactInput);
        cgEmail = findViewById(R.id.caregiverEmailInput);
        cgContact = findViewById(R.id.cgContactInput);

        //Initializing the firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient");
        firebaseAuth = FirebaseAuth.getInstance();

        //Initializing SQLite Helper DB
        sqLiteHelper = new SQLiteHelper(PatientRegister.this);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registerPatientToDB(View v) {
        final String pName = name.getText().toString();
        final String pEmail = ptEmail.getText().toString();
        final String pPswd = password.getText().toString();
//        final int pContact = Integer.parseInt(contact.getText().toString());
        final String pContact = contact.getText().toString();
        final String cEmail = cgEmail.getText().toString();
        final String cContact = cgContact.getText().toString();

        //Authentication Code Goes Here

        if(TextUtils.isEmpty(pName)){
            name.setError("Please Enter the Name");
        }
        if(TextUtils.isEmpty(pEmail)){
            ptEmail.setError("Please Enter the Patient's Email");
        }
        if(TextUtils.isEmpty(pPswd)){
            password.setError("Please Enter the Password");
        }
        if(TextUtils.isEmpty(pContact)){
            contact.setError("Please Enter the Contact Number");
        }
        if(TextUtils.isEmpty(cEmail)){
            cgEmail.setError("Please Enter the Caregiver's Email");
        }
        if(pPswd.length() < 8){
            password.setError("Password length must be at least 8 characters");
        }
        if(TextUtils.isEmpty(cContact)){
            cgContact.setError("Please Enter the Caregiver's Contact Number");
        }

        else{
            //Firebase Sign UP
            firebaseAuth.createUserWithEmailAndPassword(pEmail, pPswd)
                    .addOnCompleteListener(PatientRegister.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                patientData pData = new patientData(pName, pEmail, pPswd, pContact, cEmail, cContact);
                                FirebaseDatabase.getInstance().getReference("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(pData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //Sending data to SQLite DB
                                        sqLiteHelper.addPatient(new Patient(pEmail, pName, pPswd,pContact, cEmail, cContact));
                                        //Toast.makeText(PatientRegister.this, pEmail, Toast.LENGTH_SHORT).show();

                                        Toast.makeText(PatientRegister.this, "Patient Successfully Registered", Toast.LENGTH_SHORT).show();
                                        Intent pLogin = new Intent(PatientRegister.this, PatientLogin.class);
                                        startActivity(pLogin);
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
        super.onBackPressed();
        startActivity(new Intent(this, PatientLogin.class));
        finish();
    }

}

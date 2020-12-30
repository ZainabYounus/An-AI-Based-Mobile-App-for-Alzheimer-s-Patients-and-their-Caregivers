package com.example.alzheimersapp.PatientModule;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alzheimersapp.CaregiverModule.AboutAlzCureCG;
import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.PatientModule.Chatbot.chatbot;
import com.example.alzheimersapp.PatientModule.FaceRecognition.RecognizeFace;
import com.example.alzheimersapp.PatientModule.GameModule.PlayGames;
import com.example.alzheimersapp.PatientModule.Painting.PaintingActivity;
import com.example.alzheimersapp.PatientModule.PillReminder.PillRemindersActivity;


import com.example.alzheimersapp.R;
import com.example.alzheimersapp.SQLiteHelper;
import com.example.alzheimersapp.sharelocation.ShareLocationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import weka.clusterers.forOPTICSAndDBScan.Databases.Database;

import android.graphics.Color;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;


public class PatientDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    GridLayout mainGrid;
    FloatingActionButton fab;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    String ptEmail;
    String cgContact;
    SQLiteHelper sqLiteHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        Bundle extras = getIntent().getExtras();
        ptEmail = extras.getString("ptEmail");
        //Toast.makeText(this, ptEmail, Toast.LENGTH_SHORT).show();

        mainGrid = findViewById(R.id.mainGrid);

        sqLiteHelper = new SQLiteHelper(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Patient");

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

        drawerLayout = findViewById(R.id.patient_dashboard);
        drawerToggle = new ActionBarDrawerToggle(PatientDashboard.this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.locationTracking:
                        Toast.makeText(PatientDashboard.this, "opening location sharing",Toast.LENGTH_SHORT).show();
                        goToLS();
                        break;

                    case R.id.pt_aboutApp:
                        AboutAlzCure();
                        break;

                    case R.id.pt_signOut:
                        patientSignOut();
                        break;


                    default:
                        return true;
                }

                return true;
            }
        });

        fab = findViewById(R.id.floatingActionButton);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PatientDashboard.this, new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE}, 1000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
//                        Toast.makeText(MainActivity.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
//                        Toast.makeText(MainActivity.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(finalI == 0){
                        goToFR();
                    }

                    else if(finalI == 1) {
                        goToCB();
                    }

                    else if(finalI == 2) {
                        goToPR();
                    }

                    else if(finalI == 3) {
                        goToPainting();
                    }

                    else if(finalI == 4) {
                        callCaregiver();
                    }

                    else if(finalI == 5) {
                        goToGames();
                    }
               //     else if (finalI==5){
               //     goToLS();
            //        }
                }
            });
       }
    }

    public void SpeakNow(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));

            for(int i=0; i<matches.size(); i++){
                if(matches.get(i).toString().contains("face") ||matches.get(i).toString().contains("face recognition") ||
                        matches.get(i).toString().contains("recognition") ||matches.get(i).toString().contains("recognize")){
                    goToFR();
                    break;
                }

                else if(matches.get(i).toString().contains("chat") ||matches.get(i).toString().contains("chatbot") ||
                        matches.get(i).toString().contains("bot") || matches.get(i).toString().contains("bot bot") ||
                        matches.get(i).toString().contains("chat bot")){
                    goToCB();
                    break;
                }

                else if(matches.get(i).toString().contains("pill reminder") ||matches.get(i).toString().contains("pill reminders") ||
                        matches.get(i).toString().contains("pill") || matches.get(i).toString().contains("reminder") ||
                        matches.get(i).toString().contains("reminders") || matches.get(i).toString().contains("pills")
                        || matches.get(i).toString().contains("medicine") ||matches.get(i).toString().contains("alert")
                        || matches.get(i).toString().contains("medication")){
                    goToPR();
                    break;
                }

                else if(matches.get(i).toString().contains("paint") ||matches.get(i).toString().contains("painting") ||
                        matches.get(i).toString().contains("draw") || matches.get(i).toString().contains("drawing") ||
                        matches.get(i).toString().contains("color") || matches.get(i).toString().contains("canvas")
                        || matches.get(i).toString().contains("art") ||matches.get(i).toString().contains("fun")
                        || matches.get(i).toString().contains("creative") || matches.get(i).toString().contains("creativity")){
                    goToPainting();
                    break;
                }

                else if(matches.get(i).toString().contains("call") ||matches.get(i).toString().contains("patient") ||
                        matches.get(i).toString().contains("dial") ||matches.get(i).toString().contains("number")
                        ||matches.get(i).toString().contains("contact")){
                    callCaregiver();
                    break;
                }

                else if(matches.get(i).toString().contains("play") ||matches.get(i).toString().contains("games") ||
                        matches.get(i).toString().contains("fun") || matches.get(i).toString().contains("puzzle") ||
                        matches.get(i).toString().contains("memory games") || matches.get(i).toString().contains("exercise")
                        || matches.get(i).toString().contains("sport") ||matches.get(i).toString().contains("gaming")
                        || matches.get(i).toString().contains("playing") || matches.get(i).toString().contains("enjoy")){
                    goToGames();
                    break;
                }

                else{
                    Toast.makeText(this, "No Action Identified. Please Say Again!", Toast.LENGTH_SHORT).show();
                    break;
                }


                }
            }

        }

    public void goToFR(){
        Intent goToFR = new Intent(PatientDashboard.this, RecognizeFace.class);
        startActivity(goToFR);
    }

    public void goToCB(){
        Intent goToCB = new Intent(PatientDashboard.this, chatbot.class);
        startActivity(goToCB);
    }

    public void goToPR(){
        Intent goToPR = new Intent(PatientDashboard.this, PillRemindersActivity.class);
        goToPR.putExtra("ptEmail", ptEmail);
        startActivity(goToPR);
    }

    public void goToLS(){
        Intent goToLS = new Intent(PatientDashboard.this, ShareLocationActivity.class);
       startActivity (goToLS);
    }

    public void goToPainting(){
        Intent goToPainting = new Intent(PatientDashboard.this, PaintingActivity.class);
        startActivity(goToPainting);
    }

    public void callCaregiver(){
        Query query = myRef.orderByChild("patientEmail").equalTo(ptEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    cgContact = postSnapshot.child("caregiverContact").getValue(String.class);
//                    Log("CG CONTACT", cgContact);
                    Log.d("CG CONTACT",cgContact);
                    Log.i("CG CONTACT", cgContact);
                    Toast.makeText(PatientDashboard.this, cgContact, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        Patient patient = sqLiteHelper.returnPatient(new Patient(ptEmail, null, null, null,null, null));
//        cgContact = patient.getCgContact();
//        Toast.makeText(this, cgContact, Toast.LENGTH_SHORT).show();

//        Creating intents for making a call
        Intent callCaregiver = new Intent(Intent.ACTION_CALL);
        callCaregiver.setData(Uri.parse("tel:" + cgContact));
        startActivity(callCaregiver);
    }

    public void goToGames(){
        Intent goToGames = new Intent(PatientDashboard.this, PlayGames.class);
        startActivity(goToGames);
    }

    public void AboutAlzCure(){
        startActivity(new Intent(PatientDashboard.this, AboutAlzCurePt.class));
    }

    public void patientSignOut(){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    Intent goBack = new Intent(PatientDashboard.this, PatientLogin.class);
                    startActivity(goBack);
//                                    finish();
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);

        //Call signOut()
        firebaseAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        confirmLeaving();
    }

    public void confirmLeaving(){
        //Sign out confirmation

        AlertDialog.Builder leavingDialog = new AlertDialog.Builder(this);
        leavingDialog.setMessage("Do you want to Log out?");

        leavingDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                patientSignOut();
            }
        });
        leavingDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        leavingDialog.show();
    }
}

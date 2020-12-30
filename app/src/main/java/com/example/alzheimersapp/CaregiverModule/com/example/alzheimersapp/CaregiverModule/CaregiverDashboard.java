package com.example.alzheimersapp.CaregiverModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.alzheimersapp.CaregiverModule.AlzheimerKnowledge.KnowAlzActivity;
import com.example.alzheimersapp.CaregiverModule.PillReminder.SetPillReminder;
import com.example.alzheimersapp.PatientModule.Painting.PaintingActivity;
import com.example.alzheimersapp.tracklocation.TrackLocationActivity;
import com.example.alzheimersapp.PatientModule.Chatbot.chatbot;
import com.example.alzheimersapp.PatientModule.FaceRecognition.RecognizeFace;
import com.example.alzheimersapp.PatientModule.PatientDashboard;
import com.example.alzheimersapp.R;
import com.example.alzheimersapp.SQLiteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class CaregiverDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ProgressDialog mProgress;

    GridLayout mainGrid;
    FloatingActionButton fab;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public String cgEmail;
    public String pContact;
    SQLiteHelper sqLiteHelper;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_dashboard);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Logging out...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Bundle extras = getIntent().getExtras();
        cgEmail = extras.getString("cgEmail");
        //Toast.makeText(this, cgEmail, Toast.LENGTH_SHORT).show();

        mainGrid = findViewById(R.id.mainGrid);
        sqLiteHelper = new SQLiteHelper(this);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

        drawerLayout = findViewById(R.id.caregiver_dashboard);
        drawerToggle = new ActionBarDrawerToggle(CaregiverDashboard.this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationMenu_CG);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.AboutApp:
                        AboutAlzCure();
                        break;

                    case R.id.SignOut:
                        caregiverSignOut();
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
            ActivityCompat.requestPermissions(CaregiverDashboard.this, new String[]{Manifest.permission.RECORD_AUDIO,
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
                        goToPillReminder();
                    }

                    else if(finalI == 1) {
                        goToLocationTracking();
                    }

                    else if(finalI == 2) {
                        callPatient();
                    }

                    else if(finalI == 3) {
                        AlzheimerKnowledge();
                    }

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
                if(matches.get(i).toString().contains("pill") ||matches.get(i).toString().contains("reminder") ||
                        matches.get(i).toString().contains("pill reminder") ||matches.get(i).toString().contains("alarm")
                        ||matches.get(i).toString().contains("medicine")){
                    goToPillReminder();
                    break;
                }

                else if(matches.get(i).toString().contains("location") ||matches.get(i).toString().contains("tracking") ||
                        matches.get(i).toString().contains("location tracking") ||matches.get(i).toString().contains("track")
                        ||matches.get(i).toString().contains("find")){
                    goToLocationTracking();
                    break;
                }

                else if(matches.get(i).toString().contains("call") ||matches.get(i).toString().contains("patient") ||
                        matches.get(i).toString().contains("dial") ||matches.get(i).toString().contains("number")
                        ||matches.get(i).toString().contains("contact")){
                    callPatient();
                    break;
                }

                else if(matches.get(i).toString().contains("alzheimer's knowledge") ||matches.get(i).toString().contains("knowledge") ||
                        matches.get(i).toString().contains("alzheimer's") ||matches.get(i).toString().contains("disease")
                        ||matches.get(i).toString().contains("information")){
                    AlzheimerKnowledge();
                    break;
                }

                else{
                    Toast.makeText(this, "No Action Identified. Please Say Again!", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }

    public void goToPillReminder(){
        Intent goToPR = new Intent(CaregiverDashboard.this, SetPillReminder.class);
        goToPR.putExtra("cgEmail", cgEmail);
        startActivity(goToPR);
    }

    public void callPatient(){
        Caregiver caregiver = sqLiteHelper.returnCaregiver(new Caregiver(cgEmail, null, null, null,null, null));
        pContact = caregiver.getPtContact();
        //Toast.makeText(this, pContact, Toast.LENGTH_SHORT).show();

        //Creating intents for making a call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + pContact));
        startActivity(callIntent);
    }
    public void goToLocationTracking(){
        Intent goToLT=new Intent(CaregiverDashboard.this, TrackLocationActivity.class);
        startActivity(goToLT);
    }

    public void AlzheimerKnowledge(){
        startActivity(new Intent(CaregiverDashboard.this, KnowAlzActivity.class));
    }

    public void AboutAlzCure(){
        startActivity(new Intent(CaregiverDashboard.this, AboutAlzCureCG.class));
    }

    public void caregiverSignOut(){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after sign out is complete
                    mProgress.dismiss();
                    Intent goBack = new Intent(CaregiverDashboard.this, CaregiverLogin.class);
                    startActivity(goBack);
//                                    finish();
                }
            }
        };
        mProgress.show();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);

        //Call signOut()
        firebaseAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        confirmLeaving();
    }

    public void confirmLeaving(){
        //Sign out confirmation

        AlertDialog.Builder leavingDialog = new AlertDialog.Builder(this);
        leavingDialog.setMessage("Do you want to Log out?");

        leavingDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                caregiverSignOut();
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

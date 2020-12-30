package com.example.alzheimersapp.PatientModule.PillReminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.PatientModule.Patient;
import com.example.alzheimersapp.PatientModule.PatientDashboard;
import com.example.alzheimersapp.R;
import com.example.alzheimersapp.SQLiteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PillRemindersActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton imgBtn;
    private FloatingActionButton add;
    private Dialog dialog;
    //private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private AdapterReminders adapter;
    private ArrayList<Reminders> temp = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRef;
    SQLiteHelper sqLiteHelper;
    String reminderText;
    Date reminderDate;
    String id;
    Reminders reminders;
    String ptEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminders);

        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        imgBtn = findViewById(R.id.load_reminders);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("reminders");
        sqLiteHelper = new SQLiteHelper(this);

        Bundle extras = getIntent().getExtras();
        ptEmail = extras.getString("ptEmail");
        //Toast.makeText(this, ptEmail, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PillRemindersActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        setItemsInRecyclerView();

    }

    public void FetchReminders(View view) {

        Query query = myRef.orderByChild("ptEmail").equalTo(ptEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                temp.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting reminders
                    reminders = postSnapshot.getValue(Reminders.class);
                    reminderText = postSnapshot.child("message").getValue(String.class);
                    reminderDate = new Date(postSnapshot.child("remindDate").child("time").getValue(Long.class));

                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    calendar.setTime(reminderDate);
                    calendar.set(Calendar.SECOND,0);
                    Intent intent = new Intent(PillRemindersActivity.this,NotifierAlarm.class);
                    intent.putExtra("Message",reminders.getMessage());
                    intent.putExtra("RemindDate",reminders.getRemindDate().toString());
                    intent.putExtra("id", reminders.getId());
                    PendingIntent intent1 = PendingIntent.getBroadcast(PillRemindersActivity.this,reminders.getId().hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent1);

                    //adding reminders to the list
                    temp.add(reminders);
                }

                setItemsInRecyclerView();

//                Toast.makeText(PillRemindersActivity.this,"Inserted Successfully",Toast.LENGTH_SHORT).show();
//                setItemsInRecyclerView();
//              AppDatabase.destroyInstance();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void setItemsInRecyclerView(){

        if(temp.size()>0) {
            //empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new AdapterReminders(temp);
        recyclerView.setAdapter(adapter);

    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, PatientDashboard.class));
//        finish();
//    }

}
package com.example.alzheimersapp.CaregiverModule.PillReminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alzheimersapp.CaregiverModule.Caregiver;
import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.CaregiverModule.CaregiverRegister;
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

public class SetPillReminder extends AppCompatActivity {


    private FloatingActionButton add;
    private Dialog dialog;
    //private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private AdapterReminders adapter;
    private ArrayList<Reminders> temp = new ArrayList<>();
    private TextView empty;

    FirebaseDatabase database;
    DatabaseReference myRef;
    SQLiteHelper sqLiteHelper;
    String reminderText;
    Date reminderDate;
    String id;
    Reminders reminders;
    public String cgEmail;
    public String ptEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pill_reminder);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Reminders");
        sqLiteHelper = new SQLiteHelper(this);

        //appDatabase = AppDatabase.geAppdatabase(MainPage.this);

        add = findViewById(R.id.floatingButton);
        empty = findViewById(R.id.empty);

        Bundle extras = getIntent().getExtras();
        cgEmail = extras.getString("cgEmail");
        //Toast.makeText(this, cgEmail, Toast.LENGTH_SHORT).show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetPillReminder.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        setItemsInRecyclerView();
    }

    public void addReminder(){

        dialog = new Dialog(SetPillReminder.this);
        dialog.setContentView(R.layout.floating_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select,add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.message);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SetPillReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(SetPillReminder.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0)
                                    textView.setText(newDate.getTime().toString());
                                else
                                    Toast.makeText(SetPillReminder.this,"Invalid time",Toast.LENGTH_SHORT).show();

                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();

                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Caregiver caregiver = sqLiteHelper.returnCaregiver(new Caregiver(cgEmail, null, null, null,null, null));
                if(caregiver == null){
                    Toast.makeText(SetPillReminder.this, "I am null", Toast.LENGTH_SHORT).show();
                }

                else{

                //Sending data to firebase
                myRef = FirebaseDatabase.getInstance().getReference("reminders");
                ptEmail = caregiver.getPtEmail();
                reminderText = message.getText().toString().trim();
                reminderDate = new Date(textView.getText().toString().trim());
                id = myRef.push().getKey();
                reminders = new Reminders(id, reminderText, reminderDate, cgEmail, ptEmail);
                myRef.child(id).setValue(reminders);

                // Read from the database
                Query query = myRef.orderByChild("cgEmail").equalTo(cgEmail);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        temp.clear();
                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            Reminders reminders = postSnapshot.getValue(Reminders.class);
                            //adding artist to the list
                            temp.add(reminders);
                        }

                        setItemsInRecyclerView();


//                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
//                        calendar.setTime(reminderDate);
//                        calendar.set(Calendar.SECOND,0);
//                        Intent intent = new Intent(SetPillReminder.this,NotifierAlarm.class);
//                        intent.putExtra("Message",reminders.getMessage());
//                        intent.putExtra("RemindDate",reminders.getRemindDate().toString());
//                        intent.putExtra("id",reminders.getId().toString());
//                        PendingIntent intent1 = PendingIntent.getBroadcast(SetPillReminder.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent1);

//                        Toast.makeText(SetPillReminder.this,"Inserted Successfully",Toast.LENGTH_SHORT).show();
                        setItemsInRecyclerView();
//                        AppDatabase.destroyInstance();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });}



            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void setItemsInRecyclerView(){

        if(temp.size()>0) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new AdapterReminders(temp);
        recyclerView.setAdapter(adapter);

    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, CaregiverDashboard.class));
//        finish();
//    }
}
package com.example.alzheimersapp.PatientModule.PillReminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.alzheimersapp.CaregiverModule.PillReminder.SetPillReminder;
import com.example.alzheimersapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import androidx.core.app.NotificationCompat;

public class NotifierAlarm extends BroadcastReceiver {

    FirebaseDatabase database;
    DatabaseReference ref;
    int i;

    @Override
    public void onReceive(Context context, Intent intent) {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("reminders");

        Reminders reminder = new Reminders();
        reminder.setMessage(intent.getStringExtra("Message"));
        reminder.setRemindDate(new Date(intent.getStringExtra("RemindDate")));
        reminder.setId(intent.getStringExtra("id"));
        int id = intent.getStringExtra("id").hashCode();


        //intent to the ringtone service
//        Intent toTheRingtone = new Intent(context, RingtonePlayingService.class);
//        toTheRingtone.putExtra("extra", "alarm on");
//        toTheRingtone.putExtra("id", id);
//        toTheRingtone.putExtra("msg", intent.getStringExtra("Message"));
//        context.startService(toTheRingtone);

        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context, PillRemindersActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(PillRemindersActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(id,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01","Pill Reminder", NotificationManager.IMPORTANCE_HIGH);
        }

        Notification notification = builder.setContentTitle("Reminder")
                .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
                .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(incrementCounter(), notification);


    }

    public int incrementCounter(){
        int i = (int) (Math.random() * (100 - 1 + 1) + 1);
        return i;
    }
}

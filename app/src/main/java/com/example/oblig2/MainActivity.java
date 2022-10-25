package com.example.oblig2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID = "MyChannel";

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new
                NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void stopPeriodicService(View v) {
        Intent i = new Intent(this, MySendService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.cancel(pintent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        BroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.service.MYSIGNAL");
        filter.addAction("com.example.service.MYSIGNAL");
        this.registerReceiver(myBroadcastReceiver, filter);
        Intent myIntent = new Intent(this, MySendService.class);
        this.startService(myIntent);
        Intent intent = new Intent();
        intent.setAction("com.example.service.MYSIGNAL");
        sendBroadcast(intent);

        Button btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, FriendsActivity.class);
            MainActivity.this.startActivity(newIntent);
        });

        Button btnAddAppointment = (Button) findViewById(R.id.btnAddAppointment);
        btnAddAppointment.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, AppointmentsActivity.class);
            MainActivity.this.startActivity(newIntent);
        });

    }
}
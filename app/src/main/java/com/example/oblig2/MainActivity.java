package com.example.oblig2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    public void checkPermissions() {
        int MY_PERMISSIONS_REQUEST_SEND_SMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (MY_PERMISSIONS_REQUEST_SEND_SMS != PackageManager.PERMISSION_GRANTED ||
                MY_PHONE_STATE_PERMISSION !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        String id = getString(R.string.channel_id);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new
                NotificationChannel(id, name, importance);
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
        checkPermissions();
        createNotificationChannel();
        final Resources res = getResources();
        long timeAt07_00 = 1000*60*60*7;
        getSharedPreferences("PREFERENCES", MODE_PRIVATE)
                .edit()
                .putString("StandardMessage", res.getString(R.string.standard_message))
                .putLong("TimeOfDay", timeAt07_00)
                .apply();
        BroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.service.MYSIGNAL");
        this.registerReceiver(myBroadcastReceiver, filter);


        Button btnAddContact = findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, ContactActivity.class);
            MainActivity.this.startActivity(newIntent);
        });

        Button btnAddAppointment = findViewById(R.id.btnAddAppointment);
        btnAddAppointment.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, AppointmentsActivity.class);
            MainActivity.this.startActivity(newIntent);
        });

        Switch swPeriodic = findViewById(R.id.swPeriodic);
        swPeriodic.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Intent intent = new Intent();
                intent.setAction("com.example.service.MYSIGNAL");
                sendBroadcast(intent);
            } else {
                stopPeriodicService(swPeriodic);
            }
        });

    }
}
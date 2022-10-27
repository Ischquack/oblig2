package com.example.oblig2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyPeriodicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** This service starts MySendService and defines when to do so */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent i = new Intent(this, MySendService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,
                0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Retrieves "TimeOfDay from SharedPreferences:"
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                getSharedPreferences("PREFERENCES", MODE_PRIVATE).getLong
                        ("TimeOfDay", 0),
                AlarmManager.INTERVAL_DAY, pendingIntent);  // Daily interval
        return super.onStartCommand(intent, flags, startId);
    }



}
package com.example.oblig2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.CarExtender;
import androidx.core.app.NotificationCompat.CarExtender.UnreadConversation;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import java.util.Calendar;

public class MyPeriodicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        java.util.Calendar cal = Calendar.getInstance();
        Intent i = new Intent(this, MySendService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm =(AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /*alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,  timeAt07_00,
                AlarmManager.INTERVAL_DAY, pendingIntent);*/
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                getSharedPreferences("PREFERENCES", MODE_PRIVATE).getLong("TimeOfDay", 0),
                1000*60, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }



}
package com.example.oblig2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Service sending notifications and messages regarding appointments
 */
public class MySendService extends Service {
    SQLiteDatabase db;
    DBHandler dbHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "In MySendService", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Log.d("test", "About to call findAppointments");
        List<Appointment> todaysAppointments = dbHelper.findAppointments(db);
        for (Appointment app : todaysAppointments) {
            Log.d("test", "Inside loop MySendService");
            Notification notification = new NotificationCompat.Builder(this, "MyChannel")
                    .setContentTitle(app.getTitle())
                    .setContentText(app.getMsg())
                    //.setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pIntent).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(app.getId(), notification);

        }
        return super.onStartCommand(intent, flags, startId);
    }
}
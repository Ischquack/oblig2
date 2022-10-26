package com.example.oblig2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Service sending notifications and messages regarding appointments
 */
public class MySendService extends Service {
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void SendSMS() {
        int MY_PERMISSIONS_REQUEST_SEND_SMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }*/
        Log.d("sms", Integer.toString(MY_PERMISSIONS_REQUEST_SEND_SMS));
        int MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        Log.d("sms", Integer.toString(MY_PHONE_STATE_PERMISSION));
        Log.d("sms", Integer.toString(PackageManager.PERMISSION_GRANTED));
        if (MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED&&
                MY_PHONE_STATE_PERMISSION ==
                        PackageManager.PERMISSION_GRANTED) {
            SmsManager smsMan = SmsManager.getDefault();
            //smsMan.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Har sendt sms", Toast.LENGTH_LONG).show();
        }
        /*else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }*/
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();
        SendSMS();
        //Toast.makeText(getApplicationContext(), "In MySendService", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Log.d("test", "About to call findAppointments");
        List<Appointment> todaysAppointments = dbHelper.findAppointments(db);
        for (Appointment app : todaysAppointments) {
            if (app.getMsg().isEmpty()) {
                Log.d("nomessage", "inside message is null");
                app.setMsg(getSharedPreferences("PREFERENCES", MODE_PRIVATE)
                        .getString("StandardMessage", ""));
                Log.d("nomessage", getSharedPreferences("PREFERENCES", MODE_PRIVATE)
                        .getString("StandardMessage", ""));
            }
            Log.d("test", "Inside loop MySendService");
            Log.d("test", app.getMsg());
            Notification notification = new NotificationCompat.Builder(this, "MyChannel")
                    .setContentTitle(app.getTitle())
                    .setContentText(app.getMsg())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pIntent).build();
            Log.d("test", "post notif");
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(app.getId(), notification);
            Log.d("test", "post notification flags");

        }
        return super.onStartCommand(intent, flags, startId);
    }
}
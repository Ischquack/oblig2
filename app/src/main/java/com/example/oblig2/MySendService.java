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
import android.telephony.SmsManager;
import java.util.List;

/**
 * Service sending notifications and messages regarding appointments
 */
public class MySendService extends Service {
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void sendSMS(String msg) {
            List<Contact> contacts = dbHelper.printContacts(db);
            for (Contact contact : contacts) {      // Sends sms to each contact from db
                SmsManager smsMan = SmsManager.getDefault();
                smsMan.sendTextMessage(contact.getTel(),
                        null, msg, null, null);
            }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Intent  that launches NotificationActivity
        Intent i = new Intent(this, NotificationActivity.class);

        List<Appointment> todaysAppointments = dbHelper.findAppointments(db); //Today´s appointments

        for (Appointment app : todaysAppointments) {
            if (app.getMsg().isEmpty()) {   // If no message was provided, get from SharedPref.
                app.setMsg(getSharedPreferences("PREFERENCES", MODE_PRIVATE)
                        .getString("StandardMessage", ""));
            }
            i.putExtra("EXTRA_TITLE", app.getTitle());
            i.putExtra("EXTRA_DATE", app.getDate());
            i.putExtra("EXTRA_TIME", app.getTime());
            i.putExtra("EXTRA_PLACE", app.getPlace());
            i.putExtra("EXTRA_MSG", app.getMsg());
            // Pending intent that contains the NotificationActivity intent i:
            PendingIntent pIntent = PendingIntent.getActivity(this, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            String sms = "Today " + app.getTime() + " o'clock at " + app.getPlace() + ": \n"
                    + app.getMsg();
            sendSMS(sms);

            // Create new notification
            Notification notification = new NotificationCompat.Builder(this,
                    "MyChannel")
                    .setContentTitle(app.getTitle())
                    .setContentText(app.getMsg())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // The notification implements pIntent so user can click and launch NotifAct.
                    .setContentIntent(pIntent).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(app.getId(), notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
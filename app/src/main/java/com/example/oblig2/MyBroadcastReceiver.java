package com.example.oblig2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public MyBroadcastReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "In MyBroadcastReceiver",
                Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, MyPeriodicService.class);
        context.startService(i);
    }
}
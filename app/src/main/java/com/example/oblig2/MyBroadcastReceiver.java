package com.example.oblig2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public MyBroadcastReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyPeriodicService.class);
        context.startService(i);
    }
}
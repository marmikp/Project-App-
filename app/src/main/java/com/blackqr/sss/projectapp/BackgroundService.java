package com.blackqr.sss.projectapp;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class BackgroundService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d("boot","okay");
        PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, Notification.class), PendingIntent.FLAG_UPDATE_CURRENT);
        if (isMyServiceNotRunning(Notification.class,context)) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, 200, 200, pi);
        }
    }

    private boolean isMyServiceNotRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already","not running");
                return false;
            }
        }
        Log.i("Service","running");
        return true;
    }
}
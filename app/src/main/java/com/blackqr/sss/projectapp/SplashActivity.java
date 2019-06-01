package com.blackqr.sss.projectapp;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        Notification n = new Notification();
        n.sb.setLength(0);
        n.countNoti = 0;

        setContentView(R.layout.activity_splash);

        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Log.d("boot","okay");
        PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, Notification.class), PendingIntent.FLAG_UPDATE_CURRENT);
        if (isMyServiceNotRunning(Notification.class,this)) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, 200, 200, pi);
        }



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
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


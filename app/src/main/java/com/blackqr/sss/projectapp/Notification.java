package com.blackqr.sss.projectapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification extends Service {

    private static final String TAG_BOOT_EXECUTE_SERVICE = "BOOT_BROADCAST_SERVICE";

    int count=0;
    public static int countNoti=0;
    SharedPreferences prefs;
    String rnomber;
    CheckNotificationTab cnt;
    DBconnection db = new DBconnection(this, "notification_db");
    public static StringBuilder sb = new StringBuilder();

    public Notification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG_BOOT_EXECUTE_SERVICE, "RunAfterBootService onCreate() method.");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        cnt = new CheckNotificationTab();
        cnt.execute();


        return super.onStartCommand(intent, flags, startId);
    }

    public class CheckNotificationTab extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            while (true){
                getNotification();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getNotification() {
        SharedPreferences prefs_ip = getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE);
        if (prefs_ip != null){
            String url = prefs_ip.getString("ip",null)+ "checkNotificationTab.php";
            StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("login",response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        int c = jsonObj.length();
                        while (c != 0){
                            addNotification(jsonObj.getJSONObject(String.valueOf(c)));
                            c--;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.toString());
                }


            }) {
                @Override
                protected Map<String, String> getParams() {
                    prefs = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
                    rnomber = prefs.getString("rnomber",null);
                    Map<String, String> params = new HashMap<String, String>();
                    if (rnomber != null) {
                        params.put("rno", rnomber);
                        params.put("id", String.valueOf(db.getMaxId()));
                    }
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(jsObjRequest);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void addNotification(JSONObject str) throws JSONException {

        db.insertData1(str);

        if (sb.length() == 0){
            sb.append(str.getString("msg"));
            countNoti++;
        }else{
            sb.append("," + str.getString("msg"));
            countNoti++;
        }
        final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.blackqr)
                .setContentText(String.valueOf(countNoti-1)+" Notifications received").setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(DEFAULT_VIBRATE_PATTERN);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();


        // Add your All messages here or use Loop to generate messages
        String msg[] = sb.toString().split(",");

        for(int i=0;i<msg.length;i++) {

            inboxStyle.addLine(msg[i]);
        }

        mBuilder.setStyle(inboxStyle);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Notification.this);

        stackBuilder.addNextIntentWithParentStack(new Intent(this,SplashActivity.class));



        PendingIntent pIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pIntent);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        try {
            r.play();
        }catch (Exception e){

        }

        NotificationManager mNotificationManager = (NotificationManager) Notification.this.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
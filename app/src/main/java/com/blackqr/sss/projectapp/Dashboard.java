package com.blackqr.sss.projectapp;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dashboard extends AppCompatActivity {

    private TextView mTextMessage;
    RetrieveNotification rn;
    ArrayList<String> listdata = new ArrayList<>();
    ArrayList<String> listdata1 = new ArrayList<>();
    RecyclerAdapter adapter = null;
    Button button;
    String data[];
    public int i = 0;
    DBconnection db = new DBconnection(this, "notification_db");
    Notification n = new Notification();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    n.sb.setLength(0);
                    notification fragment = null;
                    rn = new RetrieveNotification();
                    setTitle("Notifications");
                    RecyclerAdapter adapterTemp = new RecyclerAdapter(Dashboard.this,listdata);
                    fragment = new notification(adapterTemp);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.mylayout, fragment);
                    transaction.commit();
                    rn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    return true;
                case R.id.navigation_live:
                    try {
                        rn.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setTitle("Live Video");
                    Live fragment1 = null;
                    fragment1 = new Live();
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    transaction1.replace(R.id.mylayout, fragment1);
                    transaction1.commit();
                    return true;
                case R.id.navigation_profile:
                    try {
                        rn.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setTitle("Profile");
                    Profile fragment2 = null;
                    fragment2 = new Profile();
                    FragmentManager manager2 = getSupportFragmentManager();
                    FragmentTransaction transaction2 = manager2.beginTransaction();
                    transaction2.replace(R.id.mylayout, fragment2);
                    transaction2.commit();
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;

                case R.id.navigation_mode:
                    try {
                        rn.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setTitle("Mode Selection");
                    Fragment_mode fragment3 = null;
                    fragment3 = new Fragment_mode();
                    FragmentManager manager3 = getSupportFragmentManager();
                    FragmentTransaction transaction3 = manager3.beginTransaction();
                    transaction3.replace(R.id.mylayout, fragment3);
                    transaction3.commit();
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        listdata = db.getData();
        RecyclerAdapter adapterTemp = new RecyclerAdapter(this,listdata);

        notification fragment = new notification(adapterTemp);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mylayout, fragment);
        transaction.commit();

        Log.d("string--","2");
        Notification n = new Notification();
        n.countNoti = 1;


        try {
            rn = new RetrieveNotification();
            rn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.tableCreateStatements();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        int colorInt = getResources().getColor(R.color.colorAccent);
        ColorStateList csl = ColorStateList.valueOf(colorInt);
    }


    public class RetrieveNotification extends AsyncTask<String, String, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            try{
                dataMaintain();
            }catch (Exception e){

            }
            return null;
        }

        protected void dataMaintain() throws InterruptedException {

            listdata = db.getData();
            if (listdata!=null) {

                while (true) {
                    if(this.isCancelled()){
                        this.cancel(true);
                        break;
                    }

                    listdata1 = db.getData();
                    if (!listdata1.equals(listdata)) {
                        listdata = listdata1;
                        adapter = new RecyclerAdapter(Dashboard.this, listdata);
                        notification fragment = new notification(adapter);
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.mylayout, fragment);
                        transaction.commit();
                    }

                    Thread.sleep(1000);

                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        rn.cancel(true);
        super.onBackPressed();

    }

}

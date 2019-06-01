package com.blackqr.sss.projectapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Access_activity extends AppCompatActivity {

    ImageView image;
    Button decline_btn,accept_btn;
    private String id = null;
    String img;


    DBconnection db = new DBconnection(this, "notification_db");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_activity);
        image = findViewById(R.id.image_cli);
        decline_btn = findViewById(R.id.decline_btn);
        accept_btn = findViewById(R.id.accept_btn);
        String imgUrl = null;



        Dashboard dashboard = new Dashboard();
        try {
            dashboard.rn.cancel(true);
        }catch (Exception e){

        }
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getString("id");
            imgUrl = extras.getString("img");
            Log.d("string","img_name:"+imgUrl);
            if (id != null){
                GetImage getImage = new GetImage();

                try {
                    imgUrl = getImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(imgUrl).into(image);
            }
        }

        final Dialog dialog = new Dialog(this);


        dialog.setContentView(R.layout.dialog_layout);
        Button dialogButton = dialog.findViewById(R.id.submit_dialog);

        final String finalImgUrl = imgUrl;
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    try {
                        ImageView img = dialog.findViewById(R.id.image_dialog);
                        Picasso.get().load(finalImgUrl).into(img);
                    }catch (Exception e){
                        Toast.makeText(Access_activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("error--",e.toString());
                    }
                    dialog.show();

                } catch (Exception e){
                    Toast.makeText(Access_activity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportNoti reportNoti = new ReportNoti();
                boolean a = false;
                EditText tv = dialog.findViewById(R.id.username_dialog);
                String name = null;
                name = tv.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    tv.setError("Please Enter Person Name");
                }

                else{
                    CheckBox ch1 = dialog.findViewById(R.id.ch1_dialog);
                    String check=null;
                    if (ch1.isChecked()){
                        check = "0";
                    }else{
                        check = "1";
                    }
                    try {
                        a = reportNoti.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "accept", name,check,img).get();
                    } catch (Exception e) {
                        Toast.makeText(Access_activity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    if (a) {
                        Intent intent = new Intent(Access_activity.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

            }
        });

        decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportNoti reportNoti = new ReportNoti();
                try {
                    boolean a = reportNoti.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"decline").get();
                    if (a){
                        Intent intent = new Intent(Access_activity.this,Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(Access_activity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(Access_activity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public class GetImage extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {


            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
            String restoredText = prefs.toString();

            String imgUrl = null;
            String rnomber = prefs.getString("rnomber",null);
            SharedPreferences prefs_ip = getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE);

            try {
                URL url = new URL(prefs_ip.getString("ip",null)+"getImage.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                PrintStream ps1 = new PrintStream(con.getOutputStream());
                ps1.print("&rno=" + rnomber);
                ps1.print("&id="+id);
                StringBuilder sb1 = new StringBuilder();
                BufferedReader br1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String receiveLine1;
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    while ((receiveLine1 = br1.readLine()) != null) {
                        sb1.append(receiveLine1);
                    }
                }
                img = sb1.toString();


                imgUrl = prefs_ip.getString("ip",null)+"/images/" + sb1.toString();



            } catch (Exception e) {
                e.printStackTrace();
            }
            return imgUrl;
        }

    }





    public class ReportNoti extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);

            String rnomber = prefs.getString("rnomber",null);
            String btn = strings[0];
            try {
                SharedPreferences prefs_ip = getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE);
                URL url = new URL(prefs_ip.getString("ip",null)+"reportNoti.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                StringBuilder sb1 = new StringBuilder();
                String receiveLine1;
                if(btn == "accept"){
                    PrintStream ps1 = new PrintStream(con.getOutputStream());
                    ps1.print("&rno=" + rnomber);
                    ps1.print("&id=" + id);
                    ps1.print("&name=" + strings[1]);
                    ps1.print("&imgName="+img);
                    ps1.print("&train="+strings[2]);
                    ps1.print("&status=1");
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        while ((receiveLine1 = br1.readLine()) != null) {
                            sb1.append(receiveLine1);
                        }
                    }
                }

                else if(btn == "decline"){
                    Log.d("string","decline");
                    PrintStream ps1 = new PrintStream(con.getOutputStream());
                    ps1.print("&rno=" + rnomber);
                    ps1.print("&id=" + id);
                    ps1.print("&status=0");
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        while ((receiveLine1 = br1.readLine()) != null) {
                            sb1.append(receiveLine1);
                        }
                    }
                }
                String str = sb1.toString();
                String comp = "1";

                Log.d("error-", String.valueOf(sb1.length()));
                if (str.equals(comp)){
                    db.updateFlag(id);
                    return true;
                }
                else{
                    return false;
                }

            } catch (Exception e) {
                Log.d("exception--",e.toString());
            }
            return false;
        }
    }

}

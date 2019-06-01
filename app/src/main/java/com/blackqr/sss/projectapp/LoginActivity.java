package com.blackqr.sss.projectapp;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public String ip;
    String uname,rnumber;
    EditText username, password;
    String uname1,pass1;
    Button submit,signup, clr_ip;
    public DBconnection dBconnection;
    String rnomber;
    EditText ip_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        signup = findViewById(R.id.signup);
        clr_ip = findViewById(R.id.ip_clr);

        clr_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(getString(R.string.pref_ip), Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.ip_dialog);
        ip_edit = dialog.findViewById(R.id.ip_txt);
        Button dialogButton = dialog.findViewById(R.id.ip_submit_dialog);

        SharedPreferences prefs_ip = getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE);
        if (!prefs_ip.contains("ip")){
            dialog.show();
            Log.d("dialog","done");
        }

        Log.d("dialog","done");


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor1 = getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE).edit();
                ip = "http://"+ip_edit.getText().toString()+"/";
                Log.d("ip address",ip);
                editor1.putString("ip",ip);
                editor1.apply();
                dialog.dismiss();
            }
        });

        ip = prefs_ip.getString("ip",null);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        rnomber = prefs.getString("rnomber",null);
        uname = prefs.getString("name",null);
        loginRequest(rnomber);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest(null);
            }
        });

    }

    private void loginRequest(final String login){
        String url = ip+"login.php";
        Log.d("url",url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){


            @Override
            public void onResponse(String response) {
                try {
                    Log.d("login",response);
                    JSONObject jsonObj = new JSONObject(response);
                    Log.d("login",jsonObj.getString("response"));
                    if (jsonObj.getString("response").toString().equals("1")){
                        Log.d("login","true");
                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE).edit();
                        editor.putString("name",jsonObj.getString("uname").toString());
                        editor.putString("rnomber",jsonObj.getString("rnomber").toString());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Invalid Login!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("error",e.toString());
                    Toast.makeText(LoginActivity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }


        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                if (login == null) {
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("rnomber", "1");
                }else {
                    params.put("username", uname);
                    params.put("password", "");
                    params.put("rnomber", rnomber);
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(jsObjRequest);
        Toast.makeText(this, "run", Toast.LENGTH_SHORT).show();

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




    public void onBackPressed() {
        finish();
    }

}
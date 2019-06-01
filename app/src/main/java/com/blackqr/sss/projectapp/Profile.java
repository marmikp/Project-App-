package com.blackqr.sss.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


public class Profile extends Fragment {
    String rnomber,name,ip;
    SharedPreferences prefs;
    TextView name_tv,email_tv,priority_tv,phone_tv;
    ImageView image_profile;
    JSONObject jsonObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        prefs = v.getContext().getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        name_tv = v.findViewById(R.id.name_profile);
        email_tv = v.findViewById(R.id.email_profile);
        priority_tv = v.findViewById(R.id.priority_profile);
        phone_tv = v.findViewById(R.id.phone_profile);
        image_profile = v.findViewById(R.id.image_profile);
        SharedPreferences prefs_ip = v.getContext().getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE);
        ip = prefs_ip.getString("ip",null);
        rnomber = prefs.getString("rnomber",null);
        name = prefs.getString("name",null);



        String url = ip+"profile_info.php";
        Log.d("url",url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){


            @Override
            public void onResponse(String response) {
                try {

                    jsonObj = new JSONObject(response);
                    Log.d("jsonobj1",jsonObj.getString("name"));
                    name_tv.setText(jsonObj.getString("name"));
                    email_tv.setText(jsonObj.getString("email"));
                    priority_tv.setText(jsonObj.getString("priority"));
                    phone_tv.setText(jsonObj.getString("phone"));
                    try{
                        Picasso.get().load(ip+"profile_pic/"+jsonObj.getString("img").toString()).into(image_profile);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.blackqr).into(image_profile);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error Occured!!", Toast.LENGTH_SHORT).show();
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
                params.put("name", name);
                params.put("rno", rnomber);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsObjRequest);
        Toast.makeText(getContext(), "run", Toast.LENGTH_SHORT).show();







        return v;
    }

    public void setVariables(JSONObject jsonObj1) throws JSONException {

    }

    public void getInfo(){
        String url = ip+"profile_info.php";
        Log.d("url",url);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){


            @Override
            public void onResponse(String response) {
                try {
                    Log.d("jsonobj1",response);
                    jsonObj = new JSONObject(response);


                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error Occured!!", Toast.LENGTH_SHORT).show();
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
                    params.put("name", name);
                    params.put("rno", rnomber);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(jsObjRequest);
        Toast.makeText(getContext(), "run", Toast.LENGTH_SHORT).show();
    }



}

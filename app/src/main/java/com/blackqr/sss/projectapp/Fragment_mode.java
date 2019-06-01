package com.blackqr.sss.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_mode extends Fragment {
    RadioGroup rg;
    RadioButton low,normalR,high;
    SharedPreferences prefs;
    String rnomber;
    SharedPreferences prefs_ip;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mode, container, false);
        final View vi = v;
        rg = v.findViewById(R.id.mode_radio);
        low = v.findViewById(R.id.low_mode);
        normalR = v.findViewById(R.id.normal_mode);
        high = v.findViewById(R.id.high_mode);
        ChangeMode changeMode = new ChangeMode();
        prefs_ip = vi.getContext().getSharedPreferences(getString(R.string.pref_ip), MODE_PRIVATE);
        prefs = vi.getContext().getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        rnomber = prefs.getString("rnomber",null);
        try {
            String a = changeMode.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"get","1").get();
            if (a.matches("1")){
                Log.d("click","in low");
                low.setChecked(true);
            }else if(a.matches("2")){
                normalR.setChecked(true);
            }
            else if(a.matches("3")){
                Log.d("click","in high");
                high.setChecked(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("click","perform");
                prefs = vi.getContext().getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
                rnomber = prefs.getString("rnomber",null);
                Log.d("click",rnomber);
                switch (checkedId){
                    case R.id.low_mode:
                        try {
                            Log.d("click","low");
                            ChangeMode ch1 = new ChangeMode();
                            String a = ch1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"change","1").get();
                            if (a.equals("true")){
                                Toast toast = Toast.makeText(getContext(), "Mode Changed to Low", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.normal_mode:
                        try {
                            ChangeMode ch1 = new ChangeMode();
                            String a = ch1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"change","2").get();
                            if (a.equals("true")){
                                Toast toast = Toast.makeText(getContext(), "Mode Changed to Normal", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.high_mode:
                        try {
                            ChangeMode ch1 = new ChangeMode();
                            String a = ch1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"change","3").get();
                            if (a.equals("true")){
                                Toast toast = Toast.makeText(getContext(), "Mode Changed to High", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });
        return v;
    }

    class ChangeMode extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                Log.d("click","background");
                URL url = new URL(prefs_ip.getString("ip",null)+"ChangeMode.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                PrintStream ps1 = new PrintStream(con.getOutputStream());
                if (strings[0].equals("get")){
                    Log.d("click","get");
                    ps1.print("&rno="+rnomber);
                    ps1.print("&status=get");
                    Log.d("click",rnomber);
                }
                else {
                    ps1.print("&rno=" + rnomber);
                    ps1.print("&status=change");
                    ps1.print("&mode=" + strings[1]);
                }


                BufferedReader br1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Log.d("click","perform");
                String receiveLine1 = null;
                StringBuilder sb1 = new StringBuilder();
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("click","perform");

                    while ((receiveLine1 = br1.readLine()) != null) {
                            sb1.append(receiveLine1);
                            Log.d("click","Reading");

                    }
                }
                br1.close();
                Log.d("click",sb1.toString());

                if (sb1.toString().equals("true")){
                    return "true";
                }
                else if(sb1.toString().equals("invalid")){
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return "false";
                }else if (sb1.toString().equals("1") | sb1.toString().equals("2") | sb1.toString().equals("3")){
                    return sb1.toString();
                }

                con.disconnect();
                ps1.close();


            } catch (Exception e) {
                Log.d("click",e.toString());
            }


            return "false";
        }
    }

}

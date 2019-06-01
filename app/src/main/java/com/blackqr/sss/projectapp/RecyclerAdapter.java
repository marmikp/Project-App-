package com.blackqr.sss.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<String> android_versions;
    private Context context;
    SharedPreferences prefs_ip;


    public RecyclerAdapter(Context context,ArrayList<String> android_versions) {
        this.context = context;
        this.android_versions = android_versions;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_raw, viewGroup, false);
        prefs_ip = viewGroup.getContext().getSharedPreferences(viewGroup.getContext().getString(R.string.pref_ip), MODE_PRIVATE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

            String str[] = android_versions.get(i).split(",");
            String abc = android_versions.get(i).toString();
            Log.d("error---",abc);

            if (str.length > 3){

                viewHolder.tv_android.setText(str[0]);

                    viewHolder.action.setText(str[1]);

                    viewHolder.time1.setText(str[2] + "  " + str[3]);
                    String url = prefs_ip.getString("ip",null)+"/images/" + str[4];
                try{
                    Picasso.get().load(url).into(viewHolder.img_android);
                } catch (Exception e) {
                    Picasso.get().load(R.drawable.blackqr).into(viewHolder.img_android);
                }

            }

        viewHolder.fram_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (String) viewHolder.tv_android.getText();
                Intent i = new Intent(context,Access_activity.class);
                i.putExtra("id",viewHolder.tv_android.getText().toString());
                i.putExtra("img",viewHolder.img_android.getImageAlpha());
                context.startActivity(i);
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return android_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_android,action,time1;
        ImageView img_android;
        RelativeLayout fram_layout;
        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView)view.findViewById(R.id.id);
            img_android = (ImageView)view.findViewById(R.id.img_android);
            action = view.findViewById(R.id.action);
            time1 = view.findViewById(R.id.time1);
            fram_layout = (RelativeLayout) view.findViewById(R.id.fram_layout);

        }
        
    }
}
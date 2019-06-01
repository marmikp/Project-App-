package com.blackqr.sss.projectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBconnection extends SQLiteOpenHelper {

    public static final String id = "id";
    public static final String msg= "msg";
    public static final String date= "date";
    public static final String time= "time";
    public static final String img_name= "img_name";
    public static final String flag= "flag";

    public DBconnection(Context context,String name) {
        super(context, name, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }



    public void tableCreateStatements() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + "notification" + "("
                            + id + " INTEGER, "
                            + msg+ " VARCHAR(20), "
                            + date+ " VARCHAR(50), "
                            + time+ " VARCHAR(50), "
                            + img_name + " VARCHAR(50))"

            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxId(){
        SQLiteDatabase db = this.getReadableDatabase();
        int i;
        try {
            Cursor cursor = db.rawQuery("select * from notification ORDER BY `id` DESC", null);
            cursor.moveToNext();
            i = Integer.parseInt(cursor.getString(0));
        }catch (Exception e){
            i = 0;
        }
        return i;
    }


    public ArrayList<String> getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> listdata = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("select * from notification ORDER BY `id` DESC", null);
            while(cursor.moveToNext()) {
                String str = cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2)+
                        ","+cursor.getString(3)+","+cursor.getString(4);

                listdata.add(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listdata;
    }

    public void drop_table(){
        try {
            SQLiteDatabase sb = this.getWritableDatabase();
            sb.execSQL("DROP TABLE notification");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertData1(JSONObject str){

        SQLiteDatabase db = this.getWritableDatabase();

        String str1[] = new String[10];
        try {
            Log.d("login",str.getString("id"));
            str1[0] = str.getString("id");
            str1[1] = str.getString("msg");
            str1[2] = str.getString("date");
            str1[3] = str.getString("time");
            str1[4] = str.getString("img_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String query = "insert into notification values (" + str1[0] + ",'" + str1[1] + "','" + str1[2] + "','" + str1[3] + "','" + str1[4] + "')";
            Log.d("query",query);
            db.execSQL(query);
        }catch (Exception e){
            Log.d("exception",e.toString());
        }

    }

    public void insertData(ArrayList arrayList){
        SQLiteDatabase db = this.getWritableDatabase();
        int length = arrayList.size();
        for(int i = 0;i<length;i++){
            String str = arrayList.get(i).toString();
            String str1[] = str.split(",");
            try {
                String query = "insert into notification values (" + str1[0] + ",'" + str1[1] + "','" + str1[2] + "','" + str1[3] + "','" + str1[4] + "')";
                db.execSQL(query);
            }catch (Exception e){

            }
        }
    }

    public void truncateInsert(ArrayList arrayList, Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM notification");
        }catch (Exception e){
            e.printStackTrace();
        }
        int length = arrayList.size();
        for(int i = 0;i<length;i++){
            String str = arrayList.get(i).toString();
            String str1[] = str.split(",");
            try {
                String query = "insert into notification values (" + str1[0] + ",'" + str1[1] + "','" + str1[2] + "','" + str1[3] + "','" + str1[4] + "')";
                db.execSQL(query);
            }catch (Exception e){

            }
        }

    }

    public void updateFlag(String str){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("notification","id="+str,null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

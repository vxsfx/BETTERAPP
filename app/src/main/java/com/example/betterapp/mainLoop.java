package com.example.betterapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class mainLoop extends AppCompatActivity {
    //static vars
    public static itemClass[] items = {new itemClass("rum", 1000, 0, 10, 10)};

    public static final String file = "data.txt";

    public static double mult = 1;

    public static void addmult(){
        mult = mult + 0.1;
    }
    public static double score = 0;

    public static int totalprofit = 0;
    public static int totalweight = 0;

    public static int totalcrew = 0;
    public static int totalships = 0;  //if totalweight + weigth < total ships * ship capacity
    public static int crewcost = 10000;
    public static int shipcost = 200000;

    public static boolean open;

//    public fileSaver fs = new fileSaver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fs.load();
        open = true;
        runner runny = new runner();
        runny.start();
        load();
        //fs.save();
//        save();


        Intent intent = new Intent(mainLoop.this, mainScreen.class);
        startActivity(intent);

    }
    private final class runner extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                loop();

            }
        }
    }


    public void loop(){

                try {
                    Log.i("running", "runny");
                    Thread.sleep(1000);
                    score = score + (totalprofit * mult * totalcrew * totalships);
                    Log.i("score", String.valueOf(score));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        open = false;
        Log.i("destroy", "closing the app mainLoop");
        //save();

    }

    public void load(){
        String json = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();//arg br removed
            String lineText;

            while ((lineText = br.readLine()) != null) {
                sb.append(lineText);
            }

            json = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e(String.valueOf(e), "problem lodaing data");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("catch2", "second catch for load, IOexception");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("cathc3", "IO exception");
                }
            }
        }
        if (json != null) {
            try {
                JSONObject mainobj = new JSONObject(json);//full json//need to except

                //read crew, ships and totalprof first
                //unused here atm

                score = mainobj.getInt("score");
                mult = mainobj.getDouble("mult");
                JSONObject crewjson = mainobj.getJSONObject("crew");//rum json
                JSONObject shipsjson = mainobj.getJSONObject("ships");//rum json
                totalships = shipsjson.getInt("total");
                shipcost = shipsjson.getInt("cost");
                totalcrew = crewjson.getInt("total");
                crewcost = crewjson.getInt("cost");

                for (itemClass item : items) {
                    //item(args gathered)

                    JSONObject itemobj = mainobj.getJSONObject(item.name);
                    item.total = itemobj.getInt("total");
                    item.cost = itemobj.getInt("cost");
                    item.weight = itemobj.getInt("weight");
                    item.profit = itemobj.getInt("profit");
                    totalprofit = totalprofit + item.profit * item.total;
                    totalweight = totalweight + item.weight * item.total;
                }

            } catch (JSONException e) {
                Log.i(e.toString(), "required catch");
            }
        }
        //iterate json with array of itemClasses
    }

}

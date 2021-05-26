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
    public static itemClass[] items = {new itemClass("rum", 10, 10, 10, 10)};

    public static final String file = "data.txt";

    public static double mult = 1;

    public static void addmult(){
        mult = mult + 0.1;
    }
    public static double score = 10000;//test

    public static int totalprofit;
    public static int totalweight;

    public static int totalcrew = 0;
    public static int totalships = 0;  //if totalweight + weigth < total shipss * ship capacity
    public static int crewcost = 1000;
    public static int shipcost = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load();
        Log.i("saving", "test save file");
        //save();
        Intent intent = new Intent(mainLoop.this, mainScreen.class);
        startActivity(intent);
       // load();
/*
        while (true){
            Thread.sleep(1000);
            score = score + (totalprofit * mult);
        }*/
    }
    public void load(){        String json = null;
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
            ;
            json = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e(String.valueOf(e), "problem lodaing data");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (json != null) {
            try {
                JSONObject mainobj = new JSONObject(json);//full json//need to except

                //read crew, ships and totalprof first

                for (itemClass item : items) {
                    //item(args gathered)

                    JSONObject itemobj = mainobj.getJSONObject(item.name);
                    item.total = itemobj.getInt("total");
                    item.cost = itemobj.getInt("cost");
                    item.weight = itemobj.getInt("weight");
                    item.profit = itemobj.getInt("profit");
                }

            } catch (JSONException e) {
                Log.i(e.toString(), "required catch");
            }
            //unused here atm
            /*
            int score = mainobj.getInt("score");
            float mult = mainobj.getDouble("mult");
            JSONObject crewjson = mainobj.getJSONObject("crew");//rum json
            JSONObject shipsjson = mainobj.getJSONObject("ships");//rum json
            */
        }
        //iterate json with array of itemClasses
      }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }
    public void save(){
        //store data tojson string
        StringBuilder sb = new StringBuilder();
        String json;//???

        sb.append("{");

        //do ships + crew before
        for (itemClass item : items) {
            String name = item.name;
            String cost = String.valueOf(item.cost);
            String weight = String.valueOf(item.weight);
            String profit = String.valueOf(item.profit);
            String total = String.valueOf(item.total);

            //String itemjson = " \"${name}\":{ \"total\":${total},\"weight\": ${weight}, \"cost\": ${cost}, \"profit\":${profit} }";

            String itemjson = String.format(" \"%s$\":{ \"total\":%s,\"weight\": %s, \"cost\": %s, \"profit\":%s }", name, total, weight, cost, profit);
            Log.i("aaa", itemjson);
            sb.append(itemjson);
        }
        sb.append("}");
        json = sb.toString();
        Log.i("jsonsojso", json);

        FileOutputStream fos = null;

        try {
            fos = openFileOutput("data.txt", MODE_PRIVATE);
            fos.write(json.getBytes());
            Log.i("saved", "saved oooooooooooooooooooooooooooooooooooooo");
        } catch (FileNotFoundException e) {
            Log.i(e.toString(), "problem saving");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

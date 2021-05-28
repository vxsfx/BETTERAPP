package com.example.betterapp;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//does not work for no reason :(
//might be context/activty issue ??? \_0_/ ???
public class fileSaver extends AppCompatActivity {
    public void save(Context context){
        //store data tojson string
        Log.i("saving", "save");
        StringBuilder sb = new StringBuilder();
        String json;//???

        sb.append("{");
        String scorejson = String.format("\"score\":%s ,", mainLoop.score);
        String multjson = String.format("\"mult\":%s ,", mainLoop.mult);
        String crewjson = String.format("\"crew\":{\"total\":%s, \"cost\":%s},", mainLoop.totalcrew, mainLoop.crewcost);
        String shipjson = String.format("\"ships\":{\"total\":%s, \"cost\":%s},",mainLoop.totalships ,mainLoop.shipcost);
        sb.append(scorejson);
        sb.append(multjson);
        sb.append(crewjson);
        sb.append(shipjson);
        //do ships + crew before
        for (int index = 0; index < mainLoop.items.length;index++ ) {
            if (index > 0){
                sb.append(",");
            }
            itemClass item = mainLoop.items[index];

            String name = item.name;
            int cost = item.cost;
            int weight = item.weight;
            int profit = item.profit;
            int total = item.total;

            //String itemjson = " \"${name}\":{ \"total\":${total},\"weight\": ${weight}, \"cost\": ${cost}, \"profit\":${profit} }";

            String itemjson = String.format(" \"%s\":{ \"total\":%s,\"weight\": %s, \"cost\": %s, \"profit\":%s }", name, total, weight, cost, profit);
            Log.i("aaa", itemjson);
            sb.append(itemjson);
        }
        sb.append("}");
        json = sb.toString();
        Log.i("jsonsojso", json);
        //Log.i("saj", json.getBytes());
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("data.txt", context.MODE_PRIVATE);

            String test = "{\"score\":10000.0 ,\"mult\":1.0 ,\"crew\":{\"total\":0, \"cost\":1000},\"ships\":{\"total\":0, \"cost\":2000}, \"rum\":{ \"total\":10,\"weight\": 10, \"cost\": 10, \"profit\":10 }}";
            fos.write(test.getBytes());

            } catch (FileNotFoundException e) {
            Log.i(e.toString(), "problem saving");
        } catch (IOException a) {
            a.printStackTrace();
            Log.i(a.toString(), "problem saving");
            //e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.i(e.toString(), "problem saving");
                    //e.printStackTrace();
                }
            }
        }
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

                mainLoop.score = mainobj.getInt("score");
                mainLoop.mult = mainobj.getDouble("mult");
                JSONObject crewjson = mainobj.getJSONObject("crew");//rum json
                JSONObject shipsjson = mainobj.getJSONObject("ships");//rum json
                mainLoop.totalships = shipsjson.getInt("total");
                mainLoop.shipcost = shipsjson.getInt("cost");
                mainLoop.totalcrew = crewjson.getInt("total");
                mainLoop.crewcost = crewjson.getInt("cost");

                for (itemClass item : mainLoop.items) {
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


        }
        //iterate json with array of itemClasses
    }
}

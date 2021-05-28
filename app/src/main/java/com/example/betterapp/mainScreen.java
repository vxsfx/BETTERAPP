package com.example.betterapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class mainScreen extends AppCompatActivity implements swiper{



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        findviews();

        //save();
    }

    private final class scoretext extends Thread{
        @Override
        public void run() {
            while (mainLoop.open){
                txtscore.setText(String.valueOf(mainLoop.score));
            }
        }
    }

    //rotation code
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("changing", "orientation");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("land", "landscape orientation");
            setContentView(R.layout.mainscreen_hor);
            findviews();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("hor", "horizontal orientation");
            setContentView(R.layout.mainscreen);
            findviews();

        }
    }

//buttonx
    //public text\

    public TextView txtmult;
    public TextView txtscore;

    public itemButton rumbtn;
    public Button crewbtn;
    public TextView crewtotal;

    public Button shipbtn;
    public TextView shiptotal;
    public String multstring;

    public void findviews(){
        scoretext Scoretext = new scoretext();
        Scoretext.start();
        txtscore = findViewById(R.id.total);
        txtmult = findViewById(R.id.multiplier);
        multstring=String.valueOf(mainLoop.mult) + "X";
        txtmult.setText(multstring);


        Log.i("find view", "finding views");
        crewbtn = findViewById(R.id.buycrew);
        crewtotal = findViewById(R.id.totalCrew);
        crewtotal.setText(String.valueOf(mainLoop.totalcrew));
        crewbtn.setOnClickListener(v -> {
            if ( ( mainLoop.score >= mainLoop.crewcost &&
                     ( mainLoop.totalcrew / mainLoop.totalships  < 50) )//ships have 10->50 crew members
            || mainLoop.totalcrew == 0) {//first free
                mainLoop.totalcrew++;
                mainLoop.score = mainLoop.score - mainLoop.crewcost;
                crewtotal.setText(String.valueOf(mainLoop.totalcrew));
            }
            else{crewbtn.setBackgroundColor(Color.RED );
                try {
                    Thread.sleep(3000);
                    crewbtn.setBackgroundColor(Color.BLUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    crewbtn.setBackgroundColor(Color.BLUE);
                }
                }
        });
        shipbtn = findViewById(R.id.buyships);
        shiptotal = findViewById(R.id.totalShips);
        shiptotal.setText(String.valueOf(mainLoop.totalships));
        shipbtn.setOnClickListener(v ->{
            if( (mainLoop.score >= mainLoop.shipcost && ( mainLoop.totalcrew / (mainLoop.totalships + 1)  > 10) ) //ships have 10->50 crew members
            ||mainLoop.totalships == 0) {//first free
                mainLoop.totalships++;
                mainLoop.score = mainLoop.score - mainLoop.shipcost; //lol, - - was here
                shiptotal.setText(String.valueOf(mainLoop.totalships));
            }
            else{
                shipbtn.setBackgroundColor(Color.RED );
                try {
                    Thread.sleep(3000);
                    shipbtn.setBackgroundColor(Color.BLUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    shipbtn.setBackgroundColor(Color.BLUE);
                }
            }
        });

        itemButton rumbtn = new itemButton("buyrum", "totalRum", "costRum", "profitRum", "weightRum", "totalprofitRum", "totalweightRum", 0);



    }



    class itemButton{
        itemButton(String button, String total, String cost, String profit, String weight, String totprof, String toteright,   int index){
            Button itembtn = findViewById(getResources().getIdentifier(button, "id", getPackageName()));
            TextView tottxt = findViewById(getResources().getIdentifier(total, "id", getPackageName()));
            TextView costtxt = findViewById(getResources().getIdentifier(cost, "id", getPackageName()));
            TextView proftxt = findViewById(getResources().getIdentifier(profit, "id", getPackageName()));
            TextView weighttxt = findViewById(getResources().getIdentifier(weight, "id", getPackageName()));
            TextView totproftxt = findViewById(getResources().getIdentifier(totprof, "id", getPackageName()));
            TextView totweighttxt = findViewById(getResources().getIdentifier(toteright, "id", getPackageName()));


            tottxt.setText(String.valueOf(mainLoop.items[index].total));
            proftxt.setText("£" + mainLoop.items[index].profit);
            costtxt.setText("£" + mainLoop.items[index].cost);
            weighttxt.setText(String.valueOf(mainLoop.items[index].weight));

            totproftxt.setText("£" + (mainLoop.items[index].profit * mainLoop.items[index].total) + "/s");
            totweighttxt.setText(String.valueOf(mainLoop.items[index].weight * mainLoop.items[index].total));

            itembtn.setOnClickListener(v -> {
                if ((mainLoop.score >= mainLoop.items[index].cost && (mainLoop.totalweight + mainLoop.items[index].weight) < mainLoop.totalships * 100)
                        || (index == 0 && mainLoop.items[index].total == 0)) {//and totalweight + itemweigth < ships * WEIGHT_CAPACITY(1000)
                    mainLoop.items[index].total++;
                    mainLoop.score = mainLoop.score - mainLoop.items[index].cost;

                    mainLoop.totalprofit = mainLoop.totalprofit + mainLoop.items[index].profit;
                    mainLoop.totalweight = mainLoop.totalweight + mainLoop.items[index].weight;

                    tottxt.setText(String.valueOf(mainLoop.items[index].total));
                    proftxt.setText("£" + mainLoop.items[index].profit);
                    costtxt.setText("£" + mainLoop.items[index].cost);
                    weighttxt.setText(String.valueOf(mainLoop.items[index].weight));

                    totproftxt.setText("£" + (mainLoop.items[index].profit * mainLoop.items[index].total) + "/s");
                    totweighttxt.setText(String.valueOf(mainLoop.items[index].weight * mainLoop.items[index].total));
                } else {
                    itembtn.setBackgroundColor(Color.RED);
                    try {
                        Thread.sleep(3000);
                        itembtn.setBackgroundColor(Color.BLUE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        itembtn.setBackgroundColor(Color.BLUE);
                    }
                }
            });
        }
    }


//barcode code;

    public ArrayList<String> scannedlist;//save to file

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.i("debugo_1", "45");
        try {
            //if not scanned already
            String contents = result.getContents();
            Log.i("scanned ", contents);
            if (contents != null) {// && !scannedlist.contains(result)){
                Log.i("debugo_2", "48");
                //if (!scannedlist.contains(contents)) {
                    Log.i("deubg3 ", "not yet scanned");
                    scannedlist.add(contents);
                    mainLoop.addmult();


                    multstring = String.valueOf(mainLoop.mult) + "X";
                    txtmult.setText(multstring);
                //}


            } else {
                Log.i("you am here thing", "unusful msg");
            }
        }catch (Exception e){
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "error scanning", Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
                }
        }



    float x1, x2, y1, y2;
    long starttime, endtime;

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent){

        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = touchEvent.getX();
            y1 = touchEvent.getY();
            //start timer
            starttime = System.nanoTime();
            Log.i("start", String.valueOf(starttime));
        } else if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
            x2 = touchEvent.getX();
            y2 = touchEvent.getY();
            //stop timer
            endtime = System.nanoTime();
            Log.i("end", String.valueOf(endtime));
            long time = (endtime - starttime)/1000000;

            left_swipe(x1, x2, time);
            right_swipe(x1, x2, time);
        }
        return false;
    }

    @Override
    public void left_swipe(float x1, float x2, long time){
        Log.i("speed",String.valueOf((x1 - x2) / time));
        if( ( (x1 - x2) / time) > 3 ) {
            Log.i("scan","going to scan");
            IntentIntegrator intent = new IntentIntegrator(mainScreen.this);
            intent.setCaptureActivity(camScreen.class);

            intent.initiateScan();

        }


    }
    @Override
    public void right_swipe(float x1, float x2, long time){
        if ( ( (x1 - x2) / time) < -3) {
            Log.i("map", "going to map");
            Intent intent = new Intent(mainScreen.this, mapScreen.class);
            startActivity(intent);

            finish();
        }
    }
    //unused
    @Override
    public void up_swipe(float y1, float y2, long time){}

    //unused
    @Override
    public void down_swipe(float y1, float y2, long time){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainLoop.open = false;
        Log.i("destroy", "closing the app mainscreen");
        //mainLoop.save();
        save();
        //fs.save();
    }


    public void save(){
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
            fos = openFileOutput("data.txt", 0);

            String test = "{\"score\":10000.0 ,\"mult\":1.0 ,\"crew\":{\"total\":0, \"cost\":1000},\"ships\":{\"total\":0, \"cost\":2000}, \"rum\":{ \"total\":10,\"weight\": 10, \"cost\": 10, \"profit\":10 }}";
            fos.write(json.getBytes());

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
}

package com.example.betterapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class mainScreen extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainscreen);

        Button crewbtn = findViewById(R.id.buycrew);
        TextView crewtotal = findViewById(R.id.totalCrew);
        crewbtn.setOnClickListener(v -> {
            if (mainLoop.score >= mainLoop.crewcost) {
                mainLoop.totalcrew++;
                mainLoop.score = mainLoop.score - mainLoop.crewcost;
                crewtotal.setText(String.valueOf(mainLoop.totalcrew));
            }
        });
        Button shipbtn = findViewById(R.id.buyships);
        TextView shiptotal = findViewById(R.id.totalShips);
        shipbtn.setOnClickListener(v ->{
            if(mainLoop.score >= mainLoop.shipcost){}
                mainLoop.totalships++;
                mainLoop.score = mainLoop.score- - mainLoop.shipcost;
                shiptotal.setText(String.valueOf(mainLoop.totalships));
        });

        itemButton rumbtn = new itemButton("buyrum", "totalRum", 0);
    }

//rotation code
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.mainscreen_hor);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.mainscreen);

        }
    }

//buttons





    class itemButton{
        itemButton(String buttonview, String textview, int index){
            Button itembtn = findViewById(getResources().getIdentifier(buttonview, "id", getPackageName()));
            TextView itemtxt = findViewById(getResources().getIdentifier(textview, "id", getPackageName()));
            itembtn.setOnClickListener(v -> {
                if (mainLoop.score >= mainLoop.items[index].cost){
                    mainLoop.items[index].total++;
                    mainLoop.score = mainLoop.score - mainLoop.items[index].cost;
                    itemtxt.setText(String.valueOf(mainLoop.items[index].total));
                }
            });
        }
    }


    //swiper code
    float x1,y1, x2, y2;

    public boolean onTouchEvent(MotionEvent touchEvent) {
        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = touchEvent.getX();
            y1 = touchEvent.getY();
        } else if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
            x2 = touchEvent.getX();
            y2 = touchEvent.getY();
            //swipe right
            if (x1 < x2) {
                Log.i("open map", "map");
             //   Intent intent = new Intent(mainScreen.this, mapScreen.class);
               // startActivity(intent);

                //finish();
            }
            //swipe left
            if (x2 < x1){
                Log.i("start scna","scanning");
                IntentIntegrator intent = new IntentIntegrator(mainScreen.this);
                intent.setCaptureActivity(camScreen.class);
                intent.initiateScan();
            }
        }
        return false;
    }

//barcode code

    public ArrayList<IntentResult> scannedlist;//save to file

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    Log.i("debugo_1", "45");

    //if not scanned already
    if(result.getContents() != null && !scannedlist.contains(result)){
        Log.i("debugo_2", "48");
        scannedlist.add(result);
        mainLoop.addmult();



        }else{
        Log.i("you am here thing", "unusful msg");
    }

    }
}

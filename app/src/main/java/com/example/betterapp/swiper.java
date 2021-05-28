package com.example.betterapp;

import android.util.Log;
import android.view.MotionEvent;

import com.google.zxing.integration.android.IntentIntegrator;

public interface swiper {
    //float x1,y1, x2, y2;

    public void right_swipe(float x1, float x2, long time);//if speed good
    public void left_swipe(float x1, float x2, long time);
    public void up_swipe(float y1, float y2, long time);
    public void down_swipe(float y1, float y2, long time);

    public boolean onTouchEvent(MotionEvent touchEvent);

/*
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
            if (x2 < x1) {
                Log.i("start scna", "scanning");
                public void left_swipe(){}
                /*
                IntentIntegrator intent = new IntentIntegrator(mainScreen.this);
                intent.setCaptureActivity(camScreen.class);
                intent.initiateScan();
                /*
            }
        }
        return false;
    }
    */
}
